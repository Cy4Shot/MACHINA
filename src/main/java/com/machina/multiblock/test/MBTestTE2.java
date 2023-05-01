package com.machina.multiblock.test;

import com.machina.multiblock.base.MultiblockPartTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.text.MachinaRL;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public class MBTestTE2 extends MultiblockPartTileEntity {

	public MBTestTE2(TileEntityType<?> type) {
		super(type);
	}

	public MBTestTE2() {
		this(TileEntityInit.MULTIBLOCK2.get());
	}

	@Override
	public boolean isItemPort() {
		return false;
	}

	@Override
	public boolean isEnergyPort() {
		return false;
	}

	@Override
	public boolean isFluidPort() {
		return false;
	}

	@Override
	public ResourceLocation getMultiblock() {
		return new MachinaRL("test");
	}

}
