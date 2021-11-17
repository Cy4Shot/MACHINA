package com.cy4.machina.init;

import static com.cy4.machina.Machina.MOD_ID;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public final class TagInit {

	public static final class Blocks {
		
		public static final ITag.INamedTag<Block> WRENCH_EFFECTIVE = mod("wrench_effective");

		private static ITag.INamedTag<Block> mod(String path) {
			return BlockTags.bind(new ResourceLocation(MOD_ID, path).toString());
		}
	}
	
}
