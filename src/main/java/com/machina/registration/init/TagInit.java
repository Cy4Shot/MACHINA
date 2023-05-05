package com.machina.registration.init;

import static com.machina.Machina.MOD_ID;

import java.util.HashMap;
import java.util.Map;

import com.machina.planet.OreType;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public final class TagInit {

	public static final class Blocks {

		public static final ITag.INamedTag<Block> WRENCH_EFFECTIVE = mod("wrench_effective");
		public static final ITag.INamedTag<Block> CARVEABLE_BLOCKS = mod("carveable_blocks");

		public static final Map<OreType, ITag.INamedTag<Block>> ORE_TAGS = createOres();

		private static Map<OreType, ITag.INamedTag<Block>> createOres() {
			Map<OreType, ITag.INamedTag<Block>> map = new HashMap<>();
			for (OreType type : OreType.values()) {
				map.put(type, mod(type.name().toLowerCase() + "_ore"));
			}
			return map;
		}

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

	public static final class Items {
		public static final Map<OreType, ITag.INamedTag<Item>> ORE_TAGS = createOres();

		private static Map<OreType, ITag.INamedTag<Item>> createOres() {
			Map<OreType, ITag.INamedTag<Item>> map = new HashMap<>();
			for (OreType type : OreType.values()) {
				map.put(type, mod(type.name().toLowerCase() + "_ore"));
			}
			return map;
		}

		private static ITag.INamedTag<Item> mod(String path) {
			return ItemTags.bind(new ResourceLocation(MOD_ID, path).toString());
		}
	}
}