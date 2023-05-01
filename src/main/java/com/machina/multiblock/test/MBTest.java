package com.machina.multiblock.test;

import com.machina.multiblock.base.MultiblockBlock;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityType;

public class MBTest extends MultiblockBlock {

	public MBTest() {
		super(Properties.of(Material.METAL));
	}

	@Override
	public boolean isMaster() {
		return true;
	}

	@Override
	public TileEntityType<?> getTE() {
		return TileEntityInit.MULTIBLOCK.get();
	}

}
