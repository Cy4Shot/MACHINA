package com.machina.block.tile.multiblock.pump;

import com.machina.block.tile.multiblock.MultiblockPartTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.MachinaRL;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public class PumpHeadTileEntity extends MultiblockPartTileEntity {

	public PumpHeadTileEntity() {
		this(TileEntityInit.PUMP_HEAD.get());
	}

	public PumpHeadTileEntity(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return new MachinaRL("pump");
	}

	@Override
	public boolean isPort() {
		return false;
	}
}
