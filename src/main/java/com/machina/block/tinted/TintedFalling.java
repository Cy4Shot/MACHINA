package com.machina.block.tinted;

import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class TintedFalling extends FallingBlock implements ITinted {
	
	int tintIndex;

	public TintedFalling(Properties p_i48401_1_l, int tintIndex) {
		super(p_i48401_1_l);
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