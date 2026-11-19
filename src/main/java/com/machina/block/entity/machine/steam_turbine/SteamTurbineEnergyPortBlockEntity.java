package com.machina.block.entity.machine.steam_turbine;

import com.machina.api.block.entity.MultiblockPartBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class SteamTurbineEnergyPortBlockEntity extends MultiblockPartBlockEntity {

	public SteamTurbineEnergyPortBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.STEAM_TURBINE_ENERGY_PORT.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.STEAM_TURBINE;
	}

	@Override
	public boolean isPort(MachinaCap cap) {
		return cap.equals(MachinaCap.ENERGY);
	}

	@Override
	public int getMaxEnergy() {
		return 0;
	}

	@Override
	public boolean activeModel() {
		return false;
	}
}
