package com.machina.multiblock.test;

import com.machina.multiblock.base.MultiblockMasterTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.text.MachinaRL;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public class MBTestTE extends MultiblockMasterTileEntity {

	public MBTestTE(TileEntityType<?> type) {
		super(type);
	}

	public MBTestTE() {
		this(TileEntityInit.MULTIBLOCK.get());
	}

	@Override
	public ResourceLocation getMultiblock() {
		return new MachinaRL("test");
	}

	@Override
	public void createStorages() {
	}

}
