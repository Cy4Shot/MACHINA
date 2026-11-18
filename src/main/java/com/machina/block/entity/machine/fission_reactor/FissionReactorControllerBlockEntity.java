package com.machina.block.entity.machine.fission_reactor;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.entity.MultiblockMasterBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.cap.steam.ISteamHandler;
import com.machina.api.cap.steam.SteamBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.DataMapsInit;
import com.machina.registration.init.MultiblockInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

public class FissionReactorControllerBlockEntity extends MultiblockMasterBlockEntity implements SteamBlockEntity {

	public record FissionFuel(int burnTime, int steamPerTick, ItemStack wasteProduct, float optimalReactivity,
			float reactivityTolerance, int startupEnergy) {
		public static final Codec<FissionFuel> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(Codec.INT.fieldOf("burnTime").forGetter(FissionFuel::burnTime),
						Codec.INT.fieldOf("steamPerTick").forGetter(FissionFuel::steamPerTick),
						ItemStack.CODEC.fieldOf("wasteProduct").forGetter(FissionFuel::wasteProduct),
						Codec.FLOAT.optionalFieldOf("optimalReactivity", 1.0f).forGetter(FissionFuel::optimalReactivity),
						Codec.FLOAT.optionalFieldOf("reactivityTolerance", 0.003f).forGetter(FissionFuel::reactivityTolerance),
						Codec.INT.optionalFieldOf("startupEnergy", 100000).forGetter(FissionFuel::startupEnergy))
				.apply(instance, FissionFuel::new));

		public float getMinReactivity() {
			return optimalReactivity - reactivityTolerance;
		}

		public float getMaxReactivity() {
			return optimalReactivity + reactivityTolerance;
		}
	}

	public enum ReactorState {
		IDLE, STARTING, RUNNING, SCRAMMED, MELTDOWN_WARNING
	}

	public enum MeltdownCause {
		NONE, REACTIVITY_HIGH, NO_WATER
	}

	private static final int STEAM_PER_WATER = 50;
	private static final float STEAM_RAMP_UP_FACTOR = 0.05f;
	private static final float STEAM_RAMP_DOWN_FACTOR = 0.03f;
	private static final float MIN_REACTIVITY = 0.994f;
	private static final float MAX_REACTIVITY = 1.006f;
	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_SLOT = 1;
	private static final int WATER_TANK = 0;
	private static final int STARTUP_TICKS = 100;
	private static final int MELTDOWN_COUNTDOWN = 200;

	private ItemStack activeFuel = ItemStack.EMPTY;
	private int burnTimeRemaining = 0;
	private int totalBurnTime = 0;
	private int baseSteamRate = 0;
	private int maxSteamRate = 0;
	private int currentSteamRate = 0;
	private float reactivity = 1.0f;
	private ReactorState state = ReactorState.IDLE;
	private int startupProgress = 0;
	private int meltdownTimer = 0;
	private MeltdownCause meltdownCause = MeltdownCause.NONE;
	private float fuelOptimalReactivity = 1.0f;
	private float fuelReactivityTolerance = 0.003f;

	public FissionReactorControllerBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.FISSION_REACTOR_CONTROLLER.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.FISSION_REACTOR;
	}

	@Override
	public void createStorages() {
		itemStorage(Side.INPUTS);
		itemStorage(Side.OUTPUTS);
		fluidStorage(100_000, stack -> stack.getFluid().equals(Fluids.WATER), Side.INPUTS);
		energyStorage(Side.INPUTS);
		steamStorage(100_000, Side.OUTPUTS);
	}

	@Override
	public int getMaxSteam() {
		return 100_000;
	}

	@Override
	protected int extractSteam(int maxExtract, boolean simulate) {
		return Math.min(maxExtract, currentSteamRate);
	}

	@Override
	protected int receiveSteam(int maxReceive, boolean simulate) {
		return 0;
	}

	@Override
	public int getMaxEnergy() {
		return 1_000_000;
	}

	@Override
	public boolean activeModel() {
		return false;
	}

	@Override
	public void update(@javax.annotation.Nullable BlockPos changed) {
		super.update(changed);
		if (this.formed) {
			calculateReactivity();
		}
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, Provider registries) {
		if (!activeFuel.isEmpty()) {
			tag.put("activeFuel", activeFuel.save(registries));
		}
		tag.putInt("burnTimeRemaining", burnTimeRemaining);
		tag.putInt("totalBurnTime", totalBurnTime);
		tag.putInt("baseSteamRate", baseSteamRate);
		tag.putInt("maxSteamRate", maxSteamRate);
		tag.putInt("currentSteamRate", currentSteamRate);
		tag.putFloat("reactivity", reactivity);
		tag.putString("state", state.name());
		tag.putInt("startupProgress", startupProgress);
		tag.putInt("meltdownTimer", meltdownTimer);
		tag.putFloat("fuelOptimalReactivity", fuelOptimalReactivity);
		tag.putFloat("fuelReactivityTolerance", fuelReactivityTolerance);
		super.saveAdditional(tag, registries);
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag tag, Provider registries) {
		if (tag.contains("activeFuel")) {
			activeFuel = ItemStack.parseOptional(registries, tag.getCompound("activeFuel"));
		} else {
			activeFuel = ItemStack.EMPTY;
		}
		burnTimeRemaining = tag.getInt("burnTimeRemaining");
		totalBurnTime = tag.getInt("totalBurnTime");
		baseSteamRate = tag.getInt("baseSteamRate");
		maxSteamRate = tag.getInt("maxSteamRate");
		currentSteamRate = tag.getInt("currentSteamRate");
		reactivity = tag.getFloat("reactivity");
		if (tag.contains("state")) {
			try {
				state = ReactorState.valueOf(tag.getString("state"));
			} catch (IllegalArgumentException e) {
				state = ReactorState.IDLE;
			}
		}
		startupProgress = tag.getInt("startupProgress");
		meltdownTimer = tag.getInt("meltdownTimer");
		fuelOptimalReactivity = tag.contains("fuelOptimalReactivity") ? tag.getFloat("fuelOptimalReactivity") : 1.0f;
		fuelReactivityTolerance = tag.contains("fuelReactivityTolerance") ? tag.getFloat("fuelReactivityTolerance") : 0.003f;
		super.loadAdditional(tag, registries);
	}

	@Override
	public void tick() {
		if (this.level == null || this.level.isClientSide())
			return;

		if (!this.formed) {
			if (state == ReactorState.RUNNING || state == ReactorState.MELTDOWN_WARNING) {
				triggerMeltdown();
			}
			resetReactor();
			return;
		}

		calculateReactivity();

		switch (state) {
		case IDLE:
			handleIdleState();
			break;
		case STARTING:
			handleStartingState();
			break;
		case RUNNING:
			handleRunningState();
			break;
		case SCRAMMED:
			handleScrammedState();
			break;
		case MELTDOWN_WARNING:
			handleMeltdownWarningState();
			break;
		}

		super.tick();
	}

	private void handleIdleState() {
		if (currentSteamRate > 0) {
			rampDownSteam();
		}
	}

	private void handleStartingState() {
		if (activeFuel.isEmpty()) {
			state = ReactorState.IDLE;
			startupProgress = 0;
			this.setChanged();
			return;
		}
		startupProgress++;
		if (startupProgress >= STARTUP_TICKS) {
			state = ReactorState.RUNNING;
			startupProgress = 0;
			this.setChanged();
		}
	}

	private void handleRunningState() {
		if (activeFuel.isEmpty() || burnTimeRemaining <= 0) {
			if (!tryLoadFuel()) {
				state = ReactorState.IDLE;
				rampDownSteam();
				this.setChanged();
				return;
			}
		}

		boolean hasWater = checkAndConsumeWater();
		boolean reactivityOk = isReactivityInRange();

		if (!reactivityOk && reactivity > getMaxAllowedReactivity()) {
			state = ReactorState.MELTDOWN_WARNING;
			meltdownCause = MeltdownCause.REACTIVITY_HIGH;
			meltdownTimer = MELTDOWN_COUNTDOWN;
			this.setChanged();
			return;
		}

		if (!hasWater) {
			state = ReactorState.MELTDOWN_WARNING;
			meltdownCause = MeltdownCause.NO_WATER;
			meltdownTimer = MELTDOWN_COUNTDOWN;
			this.setChanged();
			return;
		}

		if (!reactivityOk && reactivity < getMinAllowedReactivity()) {
			burnTimeRemaining = Math.max(0, burnTimeRemaining - 10);
		}

		if (reactivityOk) {
			if (currentSteamRate < maxSteamRate) {
				int remaining = maxSteamRate - currentSteamRate;
				int increase = Math.max(1, (int) (remaining * STEAM_RAMP_UP_FACTOR));
				currentSteamRate = Math.min(maxSteamRate, currentSteamRate + increase);
			}
		} else {
			rampDownSteam();
		}

		burnTimeRemaining--;
		if (burnTimeRemaining <= 0) {
			outputWaste();
			activeFuel = ItemStack.EMPTY;
		}

		this.setChanged();
	}

	private void handleScrammedState() {
		rampDownSteam();
		if (currentSteamRate <= 0) {
			state = ReactorState.IDLE;
			this.setChanged();
		}
	}

	private void handleMeltdownWarningState() {
		boolean reactivityOk = isReactivityInRange();
		boolean hasWater = hasEnoughWater();

		if (reactivityOk && hasWater) {
			state = ReactorState.RUNNING;
			meltdownTimer = 0;
			meltdownCause = MeltdownCause.NONE;
			this.setChanged();
			return;
		}

		meltdownTimer--;
		if (meltdownTimer <= 0) {
			triggerMeltdown();
		} else {
			this.setChanged();
		}
	}

	private boolean hasEnoughWater() {
		int waterNeeded = Math.max(1, maxSteamRate / STEAM_PER_WATER);
		FluidStack water = getTank(WATER_TANK).getFluid();
		return water.getAmount() >= waterNeeded;
	}

	private void triggerMeltdown() {
		if (level instanceof ServerLevel serverLevel) {
			BlockPos center = worldPosition;
			serverLevel.explode(null, center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5,
					8.0f, true, ExplosionInteraction.BLOCK);
		}
		resetReactor();
	}

	private void resetReactor() {
		state = ReactorState.IDLE;
		startupProgress = 0;
		meltdownTimer = 0;
		meltdownCause = MeltdownCause.NONE;
		currentSteamRate = 0;
		maxSteamRate = 0;
		baseSteamRate = 0;
		burnTimeRemaining = 0;
		totalBurnTime = 0;
		activeFuel = ItemStack.EMPTY;
		reactivity = 1.0f;
		this.setChanged();
	}

	private boolean checkAndConsumeWater() {
		int waterNeeded = Math.max(1, maxSteamRate / STEAM_PER_WATER);
		FluidStack water = getTank(WATER_TANK).getFluid();
		if (water.getAmount() >= waterNeeded) {
			drain(WATER_TANK, waterNeeded, FluidAction.EXECUTE);
			return true;
		}
		return false;
	}

	private boolean isReactivityInRange() {
		return reactivity >= getMinAllowedReactivity() && reactivity <= getMaxAllowedReactivity();
	}

	private float getMinAllowedReactivity() {
		return fuelOptimalReactivity - fuelReactivityTolerance;
	}

	private float getMaxAllowedReactivity() {
		return fuelOptimalReactivity + fuelReactivityTolerance;
	}

	private void rampDownSteam() {
		if (currentSteamRate > 0) {
			int decrease = Math.max(1, (int) (currentSteamRate * STEAM_RAMP_DOWN_FACTOR));
			currentSteamRate = Math.max(0, currentSteamRate - decrease);
			this.setChanged();
		}
	}

	private boolean tryLoadFuel() {
		ItemStack inputStack = getItem(INPUT_SLOT);
		if (inputStack.isEmpty()) {
			return false;
		}

		FissionFuel fuelData = inputStack.getItemHolder().getData(DataMapsInit.FISSION_FUEL);
		if (fuelData == null) {
			return false;
		}

		if (!activeFuel.isEmpty() && !ItemStack.isSameItem(activeFuel, inputStack)) {
			return false;
		}

		activeFuel = inputStack.copy();
		activeFuel.setCount(1);
		inputStack.shrink(1);

		burnTimeRemaining = fuelData.burnTime();
		totalBurnTime = fuelData.burnTime();
		baseSteamRate = fuelData.steamPerTick();
		fuelOptimalReactivity = fuelData.optimalReactivity();
		fuelReactivityTolerance = fuelData.reactivityTolerance();
		updateMaxSteamRate();

		this.setChanged();
		return true;
	}

	private void outputWaste() {
		if (activeFuel.isEmpty()) {
			return;
		}

		FissionFuel fuelData = activeFuel.getItemHolder().getData(DataMapsInit.FISSION_FUEL);
		if (fuelData == null || fuelData.wasteProduct().isEmpty()) {
			return;
		}

		ItemStack outputStack = getItem(OUTPUT_SLOT);
		ItemStack waste = fuelData.wasteProduct().copy();

		if (outputStack.isEmpty()) {
			setItem(OUTPUT_SLOT, waste);
		} else if (ItemStack.isSameItemSameComponents(outputStack, waste)) {
			int spaceLeft = outputStack.getMaxStackSize() - outputStack.getCount();
			int toAdd = Math.min(spaceLeft, waste.getCount());
			outputStack.grow(toAdd);
		}
	}

	@Override
	public boolean isLit() {
		return state == ReactorState.RUNNING || state == ReactorState.MELTDOWN_WARNING;
	}

	@Override
	public ISteamHandler getSteamCap() {
		return null;
	}

	public boolean canStart() {
		if (state != ReactorState.IDLE) return false;
		if (!formed) return false;

		ItemStack inputStack = getItem(INPUT_SLOT);
		if (inputStack.isEmpty()) return false;

		FissionFuel fuelData = inputStack.getItemHolder().getData(DataMapsInit.FISSION_FUEL);
		if (fuelData == null) return false;

		if (getEnergy() < fuelData.startupEnergy()) return false;

		return true;
	}

	public void startReactor() {
		if (!canStart()) return;

		ItemStack inputStack = getItem(INPUT_SLOT);
		FissionFuel fuelData = inputStack.getItemHolder().getData(DataMapsInit.FISSION_FUEL);
		if (fuelData == null) return;

		consumeEnergy(fuelData.startupEnergy());
		state = ReactorState.STARTING;
		startupProgress = 0;

		activeFuel = inputStack.copy();
		activeFuel.setCount(1);
		inputStack.shrink(1);

		burnTimeRemaining = fuelData.burnTime();
		totalBurnTime = fuelData.burnTime();
		baseSteamRate = fuelData.steamPerTick();
		fuelOptimalReactivity = fuelData.optimalReactivity();
		fuelReactivityTolerance = fuelData.reactivityTolerance();
		updateMaxSteamRate();

		this.setChanged();
	}

	public void scram() {
		if (state == ReactorState.RUNNING || state == ReactorState.STARTING || state == ReactorState.MELTDOWN_WARNING) {
			state = ReactorState.SCRAMMED;
			meltdownTimer = 0;
			this.setChanged();
		}
	}

	public int getBurnTimeRemaining() {
		return burnTimeRemaining;
	}

	public int getTotalBurnTime() {
		return totalBurnTime;
	}

	public float getBurnProgress() {
		if (totalBurnTime == 0) {
			return 0;
		}
		return 1.0f - ((float) burnTimeRemaining / (float) totalBurnTime);
	}

	public int getCurrentSteamRate() {
		return currentSteamRate;
	}

	public int getMaxSteamRate() {
		return maxSteamRate;
	}

	public float getSteamRatePercent() {
		if (maxSteamRate == 0) {
			return 0;
		}
		return (float) currentSteamRate / (float) maxSteamRate;
	}

	public float getReactivity() {
		return reactivity;
	}

	public ReactorState getReactorState() {
		return state;
	}

	public int getStartupProgress() {
		return startupProgress;
	}

	public int getStartupTotal() {
		return STARTUP_TICKS;
	}

	public int getMeltdownTimer() {
		return meltdownTimer;
	}

	public int getMeltdownTotal() {
		return MELTDOWN_COUNTDOWN;
	}

	public MeltdownCause getMeltdownCause() {
		return meltdownCause;
	}

	public float getFuelOptimalReactivity() {
		if (state == ReactorState.IDLE || state == ReactorState.SCRAMMED) {
			ItemStack inputStack = getItem(INPUT_SLOT);
			if (!inputStack.isEmpty()) {
				FissionFuel fuelData = inputStack.getItemHolder().getData(DataMapsInit.FISSION_FUEL);
				if (fuelData != null) {
					return fuelData.optimalReactivity();
				}
			}
		}
		return fuelOptimalReactivity;
	}

	public float getFuelReactivityTolerance() {
		if (state == ReactorState.IDLE || state == ReactorState.SCRAMMED) {
			ItemStack inputStack = getItem(INPUT_SLOT);
			if (!inputStack.isEmpty()) {
				FissionFuel fuelData = inputStack.getItemHolder().getData(DataMapsInit.FISSION_FUEL);
				if (fuelData != null) {
					return fuelData.reactivityTolerance();
				}
			}
		}
		return fuelReactivityTolerance;
	}

	public ItemStack getActiveFuel() {
		return activeFuel;
	}

	public int getStartupEnergyRequired() {
		ItemStack inputStack = getItem(INPUT_SLOT);
		if (inputStack.isEmpty()) return 0;
		FissionFuel fuelData = inputStack.getItemHolder().getData(DataMapsInit.FISSION_FUEL);
		return fuelData != null ? fuelData.startupEnergy() : 0;
	}

	private void calculateReactivity() {
		if (!formed || parts.isEmpty()) {
			reactivity = 1.0f;
			updateMaxSteamRate();
			return;
		}

		int totalAssemblies = 0;
		int totalInsertion = 0;

		for (BlockPos pos : parts) {
			if (level.getBlockEntity(pos) instanceof FissionFuelRodAssemblyBlockEntity assembly) {
				totalAssemblies++;
				totalInsertion += assembly.getInsertStage();
			}
		}

		if (totalAssemblies == 0) {
			reactivity = 1.0f;
			updateMaxSteamRate();
			return;
		}

		float avgInsertion = (float) totalInsertion / totalAssemblies;
		reactivity = MAX_REACTIVITY - (avgInsertion / 4.0f) * (MAX_REACTIVITY - MIN_REACTIVITY);
		updateMaxSteamRate();
	}

	private void updateMaxSteamRate() {
		if (baseSteamRate == 0) {
			maxSteamRate = 0;
			return;
		}

		if (reactivity < MIN_REACTIVITY || reactivity > MAX_REACTIVITY) {
			maxSteamRate = 0;
		} else {
			maxSteamRate = baseSteamRate;
		}
	}

	public void onReactivityChanged() {
		calculateReactivity();
		this.setChanged();
	}
}
