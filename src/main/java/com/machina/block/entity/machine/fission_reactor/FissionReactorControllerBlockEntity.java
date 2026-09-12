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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

public class FissionReactorControllerBlockEntity extends MultiblockMasterBlockEntity implements SteamBlockEntity {

	public record FissionFuel(int burnTime, int steamPerTick, ItemStack wasteProduct) {
		public static final Codec<FissionFuel> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(Codec.INT.fieldOf("burnTime").forGetter(FissionFuel::burnTime),
						Codec.INT.fieldOf("steamPerTick").forGetter(FissionFuel::steamPerTick),
						ItemStack.CODEC.fieldOf("wasteProduct").forGetter(FissionFuel::wasteProduct))
				.apply(instance, FissionFuel::new));
	}

	private static final int STEAM_PER_WATER = 50; // 1 mB water produces 50 mB/t steam
	private static final float STEAM_RAMP_UP_FACTOR = 0.05f; // Exponential ramp up rate (5% of remaining per tick)
	private static final float STEAM_RAMP_DOWN_FACTOR = 0.03f; // Exponential ramp down rate (3% of current per tick)
	private static final int BASE_STEAM_RATE = 1000; // Base steam production rate before multipliers
	private static final float MIN_REACTIVITY = 0.994f; // Below this, reactor is subcritical
	private static final float MAX_REACTIVITY = 1.006f; // Above this, reactor goes critical
	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_SLOT = 1;
	private static final int WATER_TANK = 0;

	private ItemStack activeFuel = ItemStack.EMPTY;
	private int burnTimeRemaining = 0;
	private int baseSteamRate = 0; // Base steam rate from fuel (before reactivity)
	private int maxSteamRate = 0; // Target steam rate (after reactivity modifier)
	private int currentSteamRate = 0; // Actual current steam rate (ramps up/down)
	private float reactivity = 1.0f; // Current reactivity level

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
		fluidStorage(100_000, stack -> stack.getFluid().equals(Fluids.WATER), Side.INPUTS); // TODO: config
	}

	@Override
	public int getMaxEnergy() {
		return 0; // Reactor doesn't store energy
	}

	@Override
	public boolean activeModel() {
		return false;
	}

	@Override
	public void update(@javax.annotation.Nullable BlockPos changed) {
		super.update(changed);
		// When multiblock forms, calculate initial reactivity
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
		tag.putInt("baseSteamRate", baseSteamRate);
		tag.putInt("maxSteamRate", maxSteamRate);
		tag.putInt("currentSteamRate", currentSteamRate);
		tag.putFloat("reactivity", reactivity);
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
		baseSteamRate = tag.getInt("baseSteamRate");
		maxSteamRate = tag.getInt("maxSteamRate");
		currentSteamRate = tag.getInt("currentSteamRate");
		reactivity = tag.getFloat("reactivity");
		super.loadAdditional(tag, registries);
	}

	@Override
	public void tick() {
		if (this.level != null && this.level.isClientSide())
			return;

		if (!this.formed) {
			reactivity = 1.0f;
			return;
		}

		// Update reactivity based on moderator rod insertion
		calculateReactivity();

		// Handle fuel burning
		if (burnTimeRemaining > 0) {
			// Check if we have enough water for current rate
			// 1 mB water produces STEAM_PER_WATER mB/t of steam
			int waterNeeded = Math.max(1, maxSteamRate / STEAM_PER_WATER);
			FluidStack water = getTank(WATER_TANK).getFluid();

			if (water.getAmount() >= waterNeeded) {
				// Consume water
				drain(WATER_TANK, waterNeeded, FluidAction.EXECUTE);

				// Ramp up steam rate exponentially
				if (currentSteamRate < maxSteamRate) {
					int remaining = maxSteamRate - currentSteamRate;
					int increase = Math.max(1, (int) (remaining * STEAM_RAMP_UP_FACTOR));
					currentSteamRate = Math.min(maxSteamRate, currentSteamRate + increase);
				}

				burnTimeRemaining--;

				if (burnTimeRemaining <= 0) {
					// Fuel depleted, output waste product
					outputWaste();
					activeFuel = ItemStack.EMPTY;
					maxSteamRate = 0;
				}

				this.setChanged();
			} else {
				// Not enough water, ramp down
				rampDownSteam();
			}
		} else {
			// No fuel burning, try to load new fuel or ramp down
			if (currentSteamRate > 0) {
				rampDownSteam();
			}
			loadFuel();
		}

		super.tick();
	}

	private void rampDownSteam() {
		if (currentSteamRate > 0) {
			int decrease = Math.max(1, (int) (currentSteamRate * STEAM_RAMP_DOWN_FACTOR));
			currentSteamRate = Math.max(0, currentSteamRate - decrease);
			this.setChanged();
		}
	}

	private void loadFuel() {
		ItemStack inputStack = getItem(INPUT_SLOT);
		if (inputStack.isEmpty()) {
			return;
		}

		FissionFuel fuelData = inputStack.getItemHolder().getData(DataMapsInit.FISSION_FUEL);
		if (fuelData == null) {
			return;
		}

		// Consume one fuel item
		activeFuel = inputStack.copy();
		activeFuel.setCount(1);
		inputStack.shrink(1);

		// Set burn parameters
		burnTimeRemaining = fuelData.burnTime();
		baseSteamRate = fuelData.steamPerTick();
		// maxSteamRate will be calculated based on reactivity
		updateMaxSteamRate();
		// currentSteamRate stays as-is to allow smooth ramping

		this.setChanged();
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
		return currentSteamRate > 0;
	}

	@Override
	public ISteamHandler getSteamCap() {
		// Steam is a rate, not stored - this interface may be repurposed later for
		// turbines
		return null;
	}

	public int getBurnTimeRemaining() {
		return burnTimeRemaining;
	}

	public int getTotalBurnTime() {
		if (activeFuel.isEmpty()) {
			return 0;
		}
		FissionFuel fuelData = activeFuel.getItemHolder().getData(DataMapsInit.FISSION_FUEL);
		return fuelData != null ? fuelData.burnTime() : 0;
	}

	public float getBurnProgress() {
		int total = getTotalBurnTime();
		if (total == 0) {
			return 0;
		}
		return 1.0f - ((float) burnTimeRemaining / (float) total);
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

	private void calculateReactivity() {
		if (!formed || parts.isEmpty()) {
			reactivity = 1.0f;
			updateMaxSteamRate();
			return;
		}

		// Count fuel rod assemblies and their insertion levels
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

		// Calculate average insertion (0 to 4)
		float avgInsertion = (float) totalInsertion / totalAssemblies;

		// Map insertion to reactivity
		// 0 insertion (rods out) = 1.006 (supercritical)
		// 2 insertion (middle) = 1.000 (critical)
		// 4 insertion (rods in) = 0.994 (subcritical)
		reactivity = MAX_REACTIVITY - (avgInsertion / 4.0f) * (MAX_REACTIVITY - MIN_REACTIVITY);

		// Update max steam rate based on new reactivity
		updateMaxSteamRate();
	}

	private void updateMaxSteamRate() {
		if (baseSteamRate == 0) {
			maxSteamRate = 0;
			return;
		}

		// Reactivity affects steam production
		// Below MIN_REACTIVITY (subcritical): no production
		// Between MIN and MAX: full production (within stable range)
		// Above MAX_REACTIVITY (supercritical): reactor shuts down

		if (reactivity < MIN_REACTIVITY || reactivity > MAX_REACTIVITY) {
			// Subcritical or supercritical - no production
			maxSteamRate = 0;
		} else {
			// Within stable range - full production
			maxSteamRate = baseSteamRate;
		}
	}

	/**
	 * Called by fuel rod assemblies when their insertion stage changes
	 */
	public void onReactivityChanged() {
		calculateReactivity();
		this.setChanged();
	}
}
