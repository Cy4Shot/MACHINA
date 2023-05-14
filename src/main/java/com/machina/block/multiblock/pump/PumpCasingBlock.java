package com.machina.block.multiblock.pump;

import com.machina.block.multiblock.MultiblockBlock;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;

public class PumpCasingBlock extends MultiblockBlock {

	public PumpCasingBlock() {
		super(Properties.copy(Blocks.IRON_BLOCK));
	}

	@Override
	public TileEntityType<?> getTE() {
		return TileEntityInit.PUMP_CASING.get();
	}

	@Override
	public boolean isMaster() {
		return false;
	}
}