package com.machina.block.multiblock.haber;

import com.machina.block.multiblock.MultiblockBlock;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntityType;

public class HaberControllerBlock extends MultiblockBlock {

	public HaberControllerBlock() {
		super(Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY).harvestLevel(2).strength(6f)
				.sound(SoundType.METAL));
	}

	@Override
	public TileEntityType<?> getTE() {
		return TileEntityInit.HABER_CONTROLLER.get();
	}

	@Override
	public boolean isMaster() {
		return true;
	}
}