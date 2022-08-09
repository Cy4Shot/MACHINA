package com.machina.block.tinted;

import java.util.function.Supplier;

import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class TintedStairs extends StairsBlock implements ITinted {

	public TintedStairs(BlockState pBaseState, Properties pProperties) {
		super(pBaseState, pProperties);
	}

	public TintedStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityInit.TINTED.get().create();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
}
