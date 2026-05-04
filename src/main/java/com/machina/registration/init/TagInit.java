package com.machina.registration.init;

import com.machina.api.util.MachinaRL;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagInit {

	public static class BlockTagInit {

		public static final TagKey<Block> PLANET_CARVABLE = create("planet_carvable");

		public static final TagKey<Block> GEOTHERMAL_GENERATOR_CTM = create("geothermal_generator_ctm");
		public static final TagKey<Block> FISSION_REACTOR_CTM = create("fission_reactor_ctm");
		public static final TagKey<Block> FISSION_REACTOR_GLASS_CTM = create("fission_reactor_glass_ctm");
		public static final TagKey<Block> FISSION_FUEL_ROD_CTM = create("fission_fuel_rod_ctm");

		private static TagKey<Block> create(String name) {
			return BlockTags.create(MachinaRL.create(name));
		}
	}

	public static class ItemTagInit {

		// Machina
		public static final TagKey<Item> CAPACITOR = create("capacitor");

		private static TagKey<Item> create(String name) {
			return ItemTags.create(MachinaRL.create(name));
		}
	}
}