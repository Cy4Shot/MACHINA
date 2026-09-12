package com.machina.block.entity.machine.fission_reactor;

import com.machina.api.block.entity.MultiblockPartBlockEntity;
import com.machina.api.cap.steam.ISteamHandler;
import com.machina.api.cap.steam.MachinaSteamStorage;
import com.machina.api.cap.steam.SteamBlockEntity;
import com.machina.api.util.block.BlockHelper;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class FissionReactorSteamPortBlockEntity extends MultiblockPartBlockEntity implements SteamBlockEntity {

	public FissionReactorSteamPortBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.FISSION_REACTOR_STEAM_PORT.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.FISSION_REACTOR;
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
		// Default implementation for interface - not used by capability system
		return getSteamStorage(null);
	}

	public MachinaSteamStorage getSteamStorage(Direction side) {
		if (!this.isCapabilitiesActive(MachinaCap.STEAM)) {
			return null;
		}
		return BlockHelper.getFromTe(level, getMaster(), FissionReactorControllerBlockEntity.class,
				be -> be.getSteamStorage(side));
	}
}
