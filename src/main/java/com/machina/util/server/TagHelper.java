package com.machina.util.server;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class TagHelper {
	public static ITag.INamedTag<Item> getForgeItemTag(String tag) {
		return ItemTags.createOptional(new ResourceLocation("forge", tag));
	}

	public static ITag.INamedTag<Block> getForgeBlockTag(String tag) {
		return BlockTags.createOptional(new ResourceLocation("forge", tag));
	}
}
