package com.machina.block.entity;

import com.machina.api.block.entity.BaseBlockEntity;
import com.machina.block.SulfurGeyserBlock;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SulfurGeyserBlockEntity extends BaseBlockEntity {

	public SulfurGeyserBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.SULFUR_GEYSER.get(), pos, state);
	}

	@Override
	public boolean activeModel() {
		return false;
	}

	public void particleTick(Level level, BlockPos pos, BlockState state) {
		RandomSource randomsource = level.random;
		if (randomsource.nextFloat() < 0.11F) {
			for (int i = 0; i < randomsource.nextInt(2) + 2; i++) {
				SulfurGeyserBlock.makeParticles(level, pos);
			}
		}
	}
}