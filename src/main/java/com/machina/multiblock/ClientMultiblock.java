package com.machina.multiblock;

import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.lighting.WorldLightManager;

public class ClientMultiblock implements IBlockDisplayReader {

	public Multiblock mb;

	public ClientMultiblock(Multiblock mb) {
		this.mb = mb;
	}

	@Override
	public TileEntity getBlockEntity(BlockPos pPos) {
		return null;
	}

	@Override
	public BlockState getBlockState(BlockPos pPos) {
		return mb.getRenderAtPos(pPos);
	}

	@Override
	public FluidState getFluidState(BlockPos pPos) {
		return Fluids.EMPTY.defaultFluidState();
	}

	@Override
	public float getShade(Direction pDirection, boolean pIsShade) {
		return 1f;
	}

	@Override
	public WorldLightManager getLightEngine() {
		return null;
	}

	@Override
	public int getBlockTint(BlockPos pos, ColorResolver color) {
		return color.getColor(BiomeRegistry.PLAINS, pos.getX(), pos.getZ());
	}

	@Override
	public int getBrightness(LightType pLightType, BlockPos pBlockPos) {
		return 15;
	}

	@Override
	public int getRawBrightness(BlockPos pBlockPos, int pAmount) {
		return 15 - pAmount;
	}

	public ClientMultiblockRestricted restrict(Predicate<BlockPos> has) {
		return new ClientMultiblockRestricted(mb, has);
	}

	public class ClientMultiblockRestricted extends ClientMultiblock {

		private final Predicate<BlockPos> has;

		public ClientMultiblockRestricted(Multiblock mb, Predicate<BlockPos> has) {
			super(mb);
			this.has = has;
		}

		@Override
		public BlockState getBlockState(BlockPos pPos) {
			if (!has.test(pPos)) {
				return Blocks.AIR.defaultBlockState();
			}
			return super.getBlockState(pPos);
		}
	}
}