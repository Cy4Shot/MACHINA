package com.machina.block.tile.multiblock.haber;

import com.machina.block.tile.multiblock.MultiblockPartTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.text.MachinaRL;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public class HaberPortTileEntity extends MultiblockPartTileEntity {

	public HaberPortTileEntity() {
		this(TileEntityInit.HABER_PORT.get());
	}

	public HaberPortTileEntity(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return new MachinaRL("haber");
	}

	@Override
	public boolean isPort() {
		return true;
	}
}
