package com.machina.registration.init;

import com.machina.api.util.MachinaRL;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagInit {

	public static class BlockTagInit {

		public static final TagKey<Block> PLANET_CARVABLE = create();

		private static TagKey<Block> create() {
			return TagKey.create(Registries.BLOCK, new MachinaRL("planet_carvable"));
		}
	}

	public static class ItemTagInit {
		public static final TagKey<Item> CAPACITOR = create();

		private static TagKey<Item> create() {
			return TagKey.create(Registries.ITEM, new MachinaRL("capacitor"));
		}
	}
}