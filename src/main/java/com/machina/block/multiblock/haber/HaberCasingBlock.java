package com.machina.block.multiblock.haber;

import com.machina.block.multiblock.MultiblockBlock;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;

public class HaberCasingBlock extends MultiblockBlock {

	public HaberCasingBlock() {
		super(Properties.copy(Blocks.IRON_BLOCK));
	}

	@Override
	public TileEntityType<?> getTE() {
		return TileEntityInit.HABER_CASING.get();
	}

	@Override
	public boolean isMaster() {
		return false;
	}
}