package com.cy4.machina.block;

import com.cy4.machina.init.BlockInit;
import com.cy4.machina.init.TileEntityTypesInit;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class PumpBlock extends Block {

	public PumpBlock() {
		super(AbstractBlock.Properties.copy(BlockInit.TANK));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypesInit.PUMP_TILE_ENTITY_TYPE.create();
	}

}