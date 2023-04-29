package com.machina.item;

import com.machina.client.ister.OreBlockItemRenderer;

import net.minecraft.block.Block;

public class OreBlockItem extends TintedItem {

	public OreBlockItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties.setISTER(() -> () -> new OreBlockItemRenderer()));
	}
}
