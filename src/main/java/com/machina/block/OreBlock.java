package com.machina.block;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;

public class OreBlock extends Block {

	String type;
	Supplier<RegistryObject<? extends Block>> bg;

	public OreBlock(Properties prop, String type, Supplier<RegistryObject<? extends Block>> bg) {
		super(prop);
		this.type = type;
		this.bg = bg;
	}

	public String getType() {
		return type;
	}

	public Block getBg() {
		return bg.get().get();
	}
}
