package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaMachineMenu;
import com.machina.api.block.menu.data.FissionReactorPropData;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.api.block.menu.slot.ResultSlot;
import com.machina.api.client.screen.ProgressBar;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.fission_reactor.FissionReactorControllerBlockEntity;
import com.machina.block.entity.machine.fission_reactor.FissionReactorControllerBlockEntity.MeltdownCause;
import com.machina.block.entity.machine.fission_reactor.FissionReactorControllerBlockEntity.ReactorState;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.DataMapsInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class FissionReactorMenu extends MachinaMachineMenu<FissionReactorControllerBlockEntity> {

	private int state = 0;
	private int burnTimeRemaining = 0;
	private int totalBurnTime = 0;
	private int currentSteamRate = 0;
	private int maxSteamRate = 0;
	private int reactivityScaled = 100000;
	private int startupProgress = 0;
	private int meltdownTimer = 0;
	private int optimalReactivityScaled = 100000;
	private int reactivityToleranceScaled = 300;
	private int waterAmount = 0;
	private int waterCapacity = 1;
	private int meltdownCause = 0;

	public FissionReactorMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, client(buf), new ItemStackHandler(2));
	}

	public FissionReactorMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.FISSION_REACTOR.get(), id, level);

		this.addSlot(new AcceptSlot(container, 0, 17, -62,
				stack -> stack.getItemHolder().getData(DataMapsInit.FISSION_FUEL) != null));
		this.addSlot(new ResultSlot(container, 1, 17, 20));

		invSlots(inv, 0);

		this.addDataSlots(FissionReactorPropData.of(
				this::getBlockEntity,
				x -> this.state = x,
				x -> this.burnTimeRemaining = x,
				x -> this.totalBurnTime = x,
				x -> this.currentSteamRate = x,
				x -> this.maxSteamRate = x,
				x -> this.reactivityScaled = x,
				x -> this.startupProgress = x,
				x -> this.meltdownTimer = x,
				x -> this.optimalReactivityScaled = x,
				x -> this.reactivityToleranceScaled = x,
				x -> this.waterAmount = x,
				x -> this.waterCapacity = x,
				x -> this.meltdownCause = x));
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.FISSION_REACTOR_CONTROLLER.get();
	}

	public ReactorState getReactorState() {
		ReactorState[] states = ReactorState.values();
		return state >= 0 && state < states.length ? states[state] : ReactorState.IDLE;
	}

	public int getBurnTimeRemaining() {
		return burnTimeRemaining;
	}

	public int getTotalBurnTime() {
		return totalBurnTime;
	}

	public float getBurnProgress() {
		if (totalBurnTime == 0) return 0;
		return 1.0f - ((float) burnTimeRemaining / (float) totalBurnTime);
	}

	public int getCurrentSteamRate() {
		return currentSteamRate;
	}

	public int getMaxSteamRate() {
		return maxSteamRate;
	}

	public float getReactivity() {
		return reactivityScaled / 100000.0f;
	}

	public int getStartupProgress() {
		return startupProgress;
	}

	public float getStartupPercent() {
		return startupProgress / 100.0f;
	}

	public int getMeltdownTimer() {
		return meltdownTimer;
	}

	public MeltdownCause getMeltdownCause() {
		MeltdownCause[] causes = MeltdownCause.values();
		return meltdownCause >= 0 && meltdownCause < causes.length ? causes[meltdownCause] : MeltdownCause.NONE;
	}

	public float getMeltdownPercent() {
		return meltdownTimer > 0 ? meltdownTimer / 200.0f : 0;
	}

	public float getOptimalReactivity() {
		return optimalReactivityScaled / 100000.0f;
	}

	public float getReactivityTolerance() {
		return reactivityToleranceScaled / 100000.0f;
	}

	public float getMinReactivity() {
		return getOptimalReactivity() - getReactivityTolerance();
	}

	public float getMaxReactivity() {
		return getOptimalReactivity() + getReactivityTolerance();
	}

	public int getWaterAmount() {
		return waterAmount;
	}

	public int getWaterCapacity() {
		return waterCapacity;
	}

	public float getWaterPercent() {
		return waterCapacity > 0 ? (float) waterAmount / waterCapacity : 0;
	}

	public ProgressBar<Float> getReactivityProgress() {
		return new ProgressBar<>(this::getReactivity, () -> 1.006f, () -> 0.994f,
				r -> String.format("%.4f", r));
	}

	public ProgressBar<Float> getBurnBar() {
		return new ProgressBar<>(() -> getBurnProgress(), () -> 1.0f, p -> StringUtils.formatPercent(p));
	}

	public ProgressBar<Float> getSteamRateBar() {
		return new ProgressBar<>(() -> (float) getCurrentSteamRate(), () -> (float) Math.max(1, getMaxSteamRate()),
				rate -> String.format("%.0f mB/t", rate));
	}

	public ProgressBar<Float> getWaterBar() {
		return new ProgressBar<>(() -> (float) getWaterAmount(), () -> (float) Math.max(1, getWaterCapacity()),
				a -> StringUtils.formatFluid((int) (float) a));
	}

	public boolean canStart() {
		if (getReactorState() != ReactorState.IDLE) return false;

		ItemStack fuelStack = this.getSlot(0).getItem();
		if (fuelStack.isEmpty()) return false;

		FissionReactorControllerBlockEntity.FissionFuel fuelData = fuelStack.getItemHolder().getData(DataMapsInit.FISSION_FUEL);
		if (fuelData == null) return false;

		FissionReactorControllerBlockEntity entity = getBlockEntity();
		if (entity == null || !entity.formed) return false;
		if (entity.getEnergy() < fuelData.startupEnergy()) return false;

		return true;
	}

	public boolean canScram() {
		ReactorState state = getReactorState();
		return state == ReactorState.RUNNING || state == ReactorState.STARTING || state == ReactorState.MELTDOWN_WARNING;
	}
}
