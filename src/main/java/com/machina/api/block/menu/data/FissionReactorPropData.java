package com.machina.api.block.menu.data;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.machina.block.entity.machine.fission_reactor.FissionReactorControllerBlockEntity;

import net.minecraft.world.inventory.ContainerData;

public class FissionReactorPropData implements ContainerData {

	public static final FissionReactorPropData of(
			Supplier<FissionReactorControllerBlockEntity> data,
			Consumer<Integer> state,
			Consumer<Integer> burnTimeRemaining,
			Consumer<Integer> totalBurnTime,
			Consumer<Integer> currentSteamRate,
			Consumer<Integer> maxSteamRate,
			Consumer<Integer> reactivityScaled,
			Consumer<Integer> startupProgress,
			Consumer<Integer> meltdownTimer,
			Consumer<Integer> optimalReactivityScaled,
			Consumer<Integer> reactivityToleranceScaled,
			Consumer<Integer> waterAmount,
			Consumer<Integer> waterCapacity,
			Consumer<Integer> meltdownCause) {
		return new FissionReactorPropData(data, state, burnTimeRemaining, totalBurnTime,
				currentSteamRate, maxSteamRate, reactivityScaled, startupProgress, meltdownTimer,
				optimalReactivityScaled, reactivityToleranceScaled, waterAmount, waterCapacity, meltdownCause);
	}

	private final Supplier<FissionReactorControllerBlockEntity> data;
	private final Consumer<Integer> state;
	private final Consumer<Integer> burnTimeRemaining;
	private final Consumer<Integer> totalBurnTime;
	private final Consumer<Integer> currentSteamRate;
	private final Consumer<Integer> maxSteamRate;
	private final Consumer<Integer> reactivityScaled;
	private final Consumer<Integer> startupProgress;
	private final Consumer<Integer> meltdownTimer;
	private final Consumer<Integer> optimalReactivityScaled;
	private final Consumer<Integer> reactivityToleranceScaled;
	private final Consumer<Integer> waterAmount;
	private final Consumer<Integer> waterCapacity;
	private final Consumer<Integer> meltdownCause;

	private FissionReactorPropData(
			Supplier<FissionReactorControllerBlockEntity> data,
			Consumer<Integer> state,
			Consumer<Integer> burnTimeRemaining,
			Consumer<Integer> totalBurnTime,
			Consumer<Integer> currentSteamRate,
			Consumer<Integer> maxSteamRate,
			Consumer<Integer> reactivityScaled,
			Consumer<Integer> startupProgress,
			Consumer<Integer> meltdownTimer,
			Consumer<Integer> optimalReactivityScaled,
			Consumer<Integer> reactivityToleranceScaled,
			Consumer<Integer> waterAmount,
			Consumer<Integer> waterCapacity,
			Consumer<Integer> meltdownCause) {
		this.data = data;
		this.state = state;
		this.burnTimeRemaining = burnTimeRemaining;
		this.totalBurnTime = totalBurnTime;
		this.currentSteamRate = currentSteamRate;
		this.maxSteamRate = maxSteamRate;
		this.reactivityScaled = reactivityScaled;
		this.startupProgress = startupProgress;
		this.meltdownTimer = meltdownTimer;
		this.optimalReactivityScaled = optimalReactivityScaled;
		this.reactivityToleranceScaled = reactivityToleranceScaled;
		this.waterAmount = waterAmount;
		this.waterCapacity = waterCapacity;
		this.meltdownCause = meltdownCause;
	}

	private FissionReactorControllerBlockEntity be() {
		return data.get();
	}

	@Override
	public int get(int index) {
		FissionReactorControllerBlockEntity be = be();
		if (be == null) return 0;
		return switch (index) {
			case 0 -> be.getReactorState().ordinal();
			case 1 -> be.getBurnTimeRemaining();
			case 2 -> be.getTotalBurnTime();
			case 3 -> be.getCurrentSteamRate();
			case 4 -> be.getMaxSteamRate();
			case 5 -> (int) (be.getReactivity() * 100000);
			case 6 -> be.getStartupProgress();
			case 7 -> be.getMeltdownTimer();
			case 8 -> (int) (be.getFuelOptimalReactivity() * 100000);
			case 9 -> (int) (be.getFuelReactivityTolerance() * 100000);
			case 10 -> be.getFluidMB(0);
			case 11 -> be.getTankCapacity(0);
			case 12 -> be.getMeltdownCause().ordinal();
			default -> throw new IllegalArgumentException("Illegal FissionReactorPropData index: " + index);
		};
	}

	@Override
	public void set(int index, int value) {
		switch (index) {
			case 0 -> state.accept(value);
			case 1 -> burnTimeRemaining.accept(value);
			case 2 -> totalBurnTime.accept(value);
			case 3 -> currentSteamRate.accept(value);
			case 4 -> maxSteamRate.accept(value);
			case 5 -> reactivityScaled.accept(value);
			case 6 -> startupProgress.accept(value);
			case 7 -> meltdownTimer.accept(value);
			case 8 -> optimalReactivityScaled.accept(value);
			case 9 -> reactivityToleranceScaled.accept(value);
			case 10 -> waterAmount.accept(value);
			case 11 -> waterCapacity.accept(value);
			case 12 -> meltdownCause.accept(value);
			default -> throw new IllegalArgumentException("Illegal FissionReactorPropData index: " + index);
		}
	}

	@Override
	public int getCount() {
		return 13;
	}
}
