package com.machina.api.multiblock;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LightChunk;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.lighting.ChunkSkyLightSources;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class ClientMultiblock implements BlockAndTintGetter, LightChunk {

	public final Multiblock mb;

	public ClientMultiblock(Multiblock mb) {
		this.mb = mb;
	}

	@Override
	public BlockEntity getBlockEntity(@NotNull BlockPos pos) {
		return null;
	}

	@Override
	public @NotNull BlockState getBlockState(@NotNull BlockPos pos) {
		return mb.getRenderAtPos(pos);
	}

	@Override
	public @NotNull FluidState getFluidState(@NotNull BlockPos pPos) {
		return Fluids.EMPTY.defaultFluidState();
	}

	public ClientMultiblockRestricted restrict(Predicate<BlockPos> has) {
		return new ClientMultiblockRestricted(mb, has);
	}

	public static class ClientMultiblockRestricted extends ClientMultiblock {

		private final Predicate<BlockPos> has;

		public ClientMultiblockRestricted(Multiblock mb, Predicate<BlockPos> has) {
			super(mb);
			this.has = has;
		}

		@Override
		public @NotNull BlockState getBlockState(@NotNull BlockPos pPos) {
			if (!has.test(pPos)) {
				return Blocks.AIR.defaultBlockState();
			}
			return super.getBlockState(pPos);
		}
	}

	@Override
	public int getHeight() {
		return 256;
	}

	@Override
	public int getMinBuildHeight() {
		return -128;
	}

	@Override
	public float getShade(@NotNull Direction p_45522_, boolean p_45523_) {
		return 0;
	}

	@Override
	public @NotNull LevelLightEngine getLightEngine() {
		return new LevelLightEngine(new LightChunkGetter() {
			@Override
			public @NotNull BlockGetter getLevel() {
				return ClientMultiblock.this;
			}

			@Override
			public LightChunk getChunkForLighting(int x, int y) {
				return ClientMultiblock.this;
			}
		}, false, false);
	}

	@Override
	public int getBlockTint(@NotNull BlockPos p_45520_, @NotNull ColorResolver p_45521_) {
		return 0;
	}

	@Override
	public void findBlockLightSources(@NotNull BiConsumer<BlockPos, BlockState> p_285040_) {
	}

	@Override
	public @NotNull ChunkSkyLightSources getSkyLightSources() {
		return new ChunkSkyLightSources(this);
	}
}