package com.cy4.machina.init;

import static com.cy4.machina.Machina.MOD_ID;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public final class TagInit {

	public static final class Blocks {
		
		public static final ITag.INamedTag<Block> WRENCH_EFFECTIVE = mod("wrench_effective");

		private static ITag.INamedTag<Block> mod(String path) {
			return BlockTags.bind(new ResourceLocation(MOD_ID, path).toString());
		}
	}
	
	public static final class Fluids {
		
		public static final ITag.INamedTag<Fluid> NOT_ACTUALLY_WATER = minecraft("not_actually_water");
		
		private static ITag.INamedTag<Fluid> minecraft(String path) {
			return FluidTags.bind(new ResourceLocation(path).toString());
		}
	}
	
}
