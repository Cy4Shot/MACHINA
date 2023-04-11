package com.machina.block.ore;

import com.machina.block.tinted.TintedBlock;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public class OreBlock extends TintedBlock {

	OreType type;
	RegistryObject<? extends Block> bg;

	public OreBlock(Properties prop, OreType type, RegistryObject<? extends Block> bg) {
		super(prop, 0);
		this.type = type;
		this.bg = bg;
	}

	public OreType getType() {
		return type;
	}

	public Block getBg() {
		return bg.get();
	}

	public ResourceLocation getBgTexturePath() {
		ResourceLocation loc = getBg().getRegistryName();
		return new ResourceLocation(loc.getNamespace(), "block/" + loc.getPath());
	}

	public ResourceLocation getFgTexturePath() {
		return getType().getTexturePath();
	}
}
