package com.machina.block.tile.multiblock.haber;

import com.machina.block.tile.multiblock.MultiblockPartTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.text.MachinaRL;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public class HaberCasingTileEntity extends MultiblockPartTileEntity {

	public HaberCasingTileEntity() {
		this(TileEntityInit.HABER_CASING.get());
	}

	public HaberCasingTileEntity(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return new MachinaRL("haber");
	}

	@Override
	public boolean isPort() {
		return false;
	}
}
