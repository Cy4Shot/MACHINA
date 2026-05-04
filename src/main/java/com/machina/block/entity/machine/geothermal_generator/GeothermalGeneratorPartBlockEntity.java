package com.machina.block.entity.machine.geothermal_generator;

import com.machina.api.block.entity.MultiblockPartBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class GeothermalGeneratorPartBlockEntity extends MultiblockPartBlockEntity {

	public GeothermalGeneratorPartBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.GEOTHERMAL_GENERATOR_PART.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.GEOTHERMAL_GENERATOR;
	}

	@Override
	public boolean isPort(MachinaCap cap) {
		return false;
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
