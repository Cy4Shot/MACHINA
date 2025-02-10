package com.machina.api.util.block;

import com.machina.registration.init.BlockStateProviderInit;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import org.jetbrains.annotations.NotNull;

public class HorizontalFacingBlockProvider extends BlockStateProvider {
	public static final Codec<HorizontalFacingBlockProvider> CODEC = BlockState.CODEC.fieldOf("state")
			.xmap(BlockBehaviour.BlockStateBase::getBlock, Block::defaultBlockState)
			.xmap(HorizontalFacingBlockProvider::new, instance -> instance.block).codec();
	private final Block block;

	public HorizontalFacingBlockProvider(Block block) {
		this.block = block;
	}

	public HorizontalFacingBlockProvider(BlockState state) {
		this(state.getBlock());
	}

	protected @NotNull BlockStateProviderType<?> type() {
		return BlockStateProviderInit.HORIZONTAL_FACING_BLOCK_PROVIDER.get();
	}

	public @NotNull BlockState getState(@NotNull RandomSource state, @NotNull BlockPos pos) {
		Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(state);
		return this.block.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, dir);
	}
}
