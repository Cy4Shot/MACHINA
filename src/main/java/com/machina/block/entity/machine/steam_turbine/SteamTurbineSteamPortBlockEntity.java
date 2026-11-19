package com.machina.block.entity.machine.steam_turbine;

import com.machina.api.block.entity.MultiblockPartBlockEntity;
import com.machina.api.cap.steam.ISteamHandler;
import com.machina.api.cap.steam.SteamBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class SteamTurbineSteamPortBlockEntity extends MultiblockPartBlockEntity implements SteamBlockEntity {

	private int steamReceivedThisTick = 0;
	private long lastTickTime = 0;

	public SteamTurbineSteamPortBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.STEAM_TURBINE_STEAM_PORT.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.STEAM_TURBINE;
	}

	@Override
	public boolean isPort(MachinaCap cap) {
		return cap == MachinaCap.STEAM;
	}

	@Override
	public int getMaxEnergy() {
		return 0;
	}

	@Override
	public boolean activeModel() {
		return false;
	}

	@Override
	public ISteamHandler getSteamCap() {
		return getSteamHandler();
	}

	public ISteamHandler getSteamHandler() {
		if (!this.isCapabilitiesActive(MachinaCap.STEAM)) {
			return null;
		}
		return new ISteamHandler() {
			@Override
			public int receiveSteam(int toReceive, boolean simulate) {
				if (!simulate) {
					addSteamReceived(toReceive);
				}
				return toReceive;
			}

			@Override
			public int extractSteam(int toExtract, boolean simulate) {
				return 0;
			}

			@Override
			public boolean canExtract() {
				return false;
			}

			@Override
			public boolean canReceive() {
				return true;
			}
		};
	}

	public void addSteamReceived(int amount) {
		if (level != null) {
			long currentTime = level.getGameTime();
			if (currentTime != lastTickTime) {
				lastTickTime = currentTime;
				steamReceivedThisTick = 0;
			}
			steamReceivedThisTick += amount;
		}
	}

	public int getAndResetSteamReceived() {
		int steam = steamReceivedThisTick;
		steamReceivedThisTick = 0;
		return steam;
	}

	public int getSteamReceivedThisTick() {
		return steamReceivedThisTick;
	}
}
