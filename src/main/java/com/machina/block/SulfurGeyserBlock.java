package com.machina.block;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.machina.block.entity.SulfurGeyserBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SulfurGeyserBlock extends Block implements EntityBlock {

	public SulfurGeyserBlock(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return Objects.requireNonNull(BlockEntityInit.SULFUR_GEYSER.get().create(pos, state));
	}

	@Override
	protected MapCodec<SulfurGeyserBlock> codec() {
		return simpleCodec(SulfurGeyserBlock::new);
	}

	public static void makeParticles(Level level, BlockPos pos) {
		RandomSource randomsource = level.getRandom();
		SimpleParticleType simpleparticletype = ParticleTypes.CAMPFIRE_COSY_SMOKE;
		level.addAlwaysVisibleParticle(simpleparticletype, true,
				(double) pos.getX() + 0.5
						+ randomsource.nextDouble() / 3.0 * (double) (randomsource.nextBoolean() ? 1 : -1),
				(double) pos.getY() + randomsource.nextDouble() + randomsource.nextDouble(),
				(double) pos.getZ() + 0.5
						+ randomsource.nextDouble() / 3.0 * (double) (randomsource.nextBoolean() ? 1 : -1),
				0.0, 0.07, 0.0);
		level.addParticle(ParticleTypes.SMOKE,
				(double) pos.getX() + 0.5
						+ randomsource.nextDouble() / 4.0 * (double) (randomsource.nextBoolean() ? 1 : -1),
				(double) pos.getY() + 0.4,
				(double) pos.getZ() + 0.5
						+ randomsource.nextDouble() / 4.0 * (double) (randomsource.nextBoolean() ? 1 : -1),
				0.0, 0.005, 0.0);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state,
			@NotNull BlockEntityType<T> type) {
		if (level.isClientSide && type == BlockEntityInit.SULFUR_GEYSER.get()) {
			return (level1, pos, state1, be) -> ((SulfurGeyserBlockEntity) be).particleTick(level1, pos, state1);
		}
		return null;
	}
}
