package com.machina.block.tile.multiblock.pump;

import com.machina.block.tile.multiblock.MultiblockPartTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.MachinaRL;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public class PumpPortTileEntity extends MultiblockPartTileEntity {

	public PumpPortTileEntity() {
		this(TileEntityInit.PUMP_PORT.get());
	}

	public PumpPortTileEntity(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return new MachinaRL("pump");
	}

	@Override
	public boolean isPort() {
		return true;
	}
}
