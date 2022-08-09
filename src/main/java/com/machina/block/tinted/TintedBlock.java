package com.machina.block.tinted;

import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class TintedBlock extends Block implements ITinted {

	int tintIndex;

	public TintedBlock(Properties p_i48440_1_, int tintIndex) {
		super(p_i48440_1_);
		this.tintIndex = tintIndex;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityInit.TINTED.get().create();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public int getTintIndex() {
		return tintIndex;
	}

	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player,
			boolean willHarvest, FluidState fluid) {
		if (!player.isCreative())
			drop(world, pos, this);
		return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
	}

	@Override
	public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
		if (canDropFromExplosion(state, world, pos, explosion))
			drop(world, pos, this);
		super.onBlockExploded(state, world, pos, explosion);
	}
}