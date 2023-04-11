package com.machina.item;

import com.machina.client.item.OreBlockItemRenderer;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class OreBlockItem extends BlockItem {

	public OreBlockItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties.setISTER(() -> () -> new OreBlockItemRenderer()));
	}
}
