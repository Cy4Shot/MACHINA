package com.machina.block.tinted;

import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

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
}