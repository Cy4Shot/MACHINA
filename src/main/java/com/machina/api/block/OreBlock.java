package com.machina.api.block;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;

public class OreBlock extends Block {

	final Block base;

	public OreBlock(Block base, Properties properties) {
		super(properties);
		this.base = base;
	}

	@Override
	public MutableComponent getName() {
		return base.getName().append(" ").append(super.getName());
	}
}
