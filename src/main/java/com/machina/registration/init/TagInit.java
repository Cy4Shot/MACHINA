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

        private static TagKey<Block> create(String name) {
            return BlockTags.create(MachinaRL.create(name));
        }

//		private static TagKey<Block> common(String name) {
//			return BlockTags.create(new ResourceLocation("c", name));
//		}
    }

    public static class ItemTagInit {

        // Machina
        public static final TagKey<Item> CAPACITOR = create("capacitor");

        // Common: Base
        public static final TagKey<Item> PLATES = common("plates");
        public static final TagKey<Item> RODS = common("rods");
        public static final TagKey<Item> WIRES = common("wires");

        // Common: Ingots
        public static final TagKey<Item> INGOTS_ALUMINUM = common("ingots/aluminum");
        public static final TagKey<Item> INGOTS_LEAD = common("ingots/lead");
        public static final TagKey<Item> INGOTS_SILVER = common("ingots/silver");
        public static final TagKey<Item> INGOTS_STEEL = common("ingots/steel");
        public static final TagKey<Item> INGOTS_TIN = common("ingots/tin");
        public static final TagKey<Item> INGOTS_URANIUM = common("ingots/uranium");

        // Common: Nuggets
        public static final TagKey<Item> NUGGETS_COAL = common("nuggets/coal");
        public static final TagKey<Item> NUGGETS_COPPER = common("nuggets/copper");
        public static final TagKey<Item> NUGGETS_DIAMOND = common("nuggets/diamond");
        public static final TagKey<Item> NUGGETS_ALUMINUM = common("nuggets/aluminum");
        public static final TagKey<Item> NUGGETS_LEAD = common("nuggets/lead");
        public static final TagKey<Item> NUGGETS_SILVER = common("nuggets/silver");
        public static final TagKey<Item> NUGGETS_STEEL = common("nuggets/steel");
        public static final TagKey<Item> NUGGETS_TIN = common("nuggets/tin");
        public static final TagKey<Item> NUGGETS_URANIUM = common("nuggets/uranium");

        // Common: Dusts
        public static final TagKey<Item> DUSTS_COAL = common("dusts/coal");
        public static final TagKey<Item> DUSTS_IRON = common("dusts/iron");
        public static final TagKey<Item> DUSTS_COPPER = common("dusts/copper");
        public static final TagKey<Item> DUSTS_GOLD = common("dusts/gold");
        public static final TagKey<Item> DUSTS_DIAMOND = common("dusts/diamond");
        public static final TagKey<Item> DUSTS_LAPIS = common("dusts/lapis");
        public static final TagKey<Item> DUSTS_EMERALD = common("dusts/emerald");
        public static final TagKey<Item> DUSTS_QUARTZ = common("dusts/quartz");
        public static final TagKey<Item> DUSTS_ALUMINUM = common("dusts/aluminum");
        public static final TagKey<Item> DUSTS_LEAD = common("dusts/lead");
        public static final TagKey<Item> DUSTS_SILVER = common("dusts/silver");
        public static final TagKey<Item> DUSTS_STEEL = common("dusts/steel");
        public static final TagKey<Item> DUSTS_TIN = common("dusts/tin");
        public static final TagKey<Item> DUSTS_URANIUM = common("dusts/uranium");

        // Common: Ores
        public static final TagKey<Item> ORES_ALUMINUM = common("ores/aluminum");
        public static final TagKey<Item> ORES_LEAD = common("ores/lead");
        public static final TagKey<Item> ORES_SILVER = common("ores/silver");
        public static final TagKey<Item> ORES_STEEL = common("ores/steel");
        public static final TagKey<Item> ORES_TIN = common("ores/tin");
        public static final TagKey<Item> ORES_URANIUM = common("ores/uranium");

        // Common: Raw Materials
        public static final TagKey<Item> RAW_MATERIALS_ALUMINUM = common("raw_materials/aluminum");
        public static final TagKey<Item> RAW_MATERIALS_LEAD = common("raw_materials/lead");
        public static final TagKey<Item> RAW_MATERIALS_SILVER = common("raw_materials/silver");
        public static final TagKey<Item> RAW_MATERIALS_STEEL = common("raw_materials/steel");
        public static final TagKey<Item> RAW_MATERIALS_TIN = common("raw_materials/tin");
        public static final TagKey<Item> RAW_MATERIALS_URANIUM = common("raw_materials/uranium");

        // Common: Plate
        public static final TagKey<Item> PLATES_IRON = common("plates/iron");
        public static final TagKey<Item> PLATES_COPPER = common("plates/copper");
        public static final TagKey<Item> PLATES_GOLD = common("plates/gold");
        public static final TagKey<Item> PLATES_DIAMOND = common("plates/diamond");
        public static final TagKey<Item> PLATES_ALUMINUM = common("plates/aluminum");
        public static final TagKey<Item> PLATES_LEAD = common("plates/lead");
        public static final TagKey<Item> PLATES_SILVER = common("plates/silver");
        public static final TagKey<Item> PLATES_STEEL = common("plates/steel");
        public static final TagKey<Item> PLATES_TIN = common("plates/tin");
        public static final TagKey<Item> PLATES_URANIUM = common("plates/uranium");

        // Common: Rod
        public static final TagKey<Item> RODS_IRON = common("rods/iron");
        public static final TagKey<Item> RODS_COPPER = common("rods/copper");
        public static final TagKey<Item> RODS_GOLD = common("rods/gold");
        public static final TagKey<Item> RODS_DIAMOND = common("rods/diamond");
        public static final TagKey<Item> RODS_ALUMINUM = common("rods/aluminum");
        public static final TagKey<Item> RODS_LEAD = common("rods/lead");
        public static final TagKey<Item> RODS_SILVER = common("rods/silver");
        public static final TagKey<Item> RODS_STEEL = common("rods/steel");
        public static final TagKey<Item> RODS_TIN = common("rods/tin");
        public static final TagKey<Item> RODS_URANIUM = common("rods/uranium");

        // Common: Wire
        public static final TagKey<Item> WIRES_IRON = common("wires/iron");
        public static final TagKey<Item> WIRES_COPPER = common("wires/copper");
        public static final TagKey<Item> WIRES_GOLD = common("wires/gold");
        public static final TagKey<Item> WIRES_DIAMOND = common("wires/diamond");
        public static final TagKey<Item> WIRES_ALUMINUM = common("wires/aluminum");
        public static final TagKey<Item> WIRES_LEAD = common("wires/lead");
        public static final TagKey<Item> WIRES_SILVER = common("wires/silver");
        public static final TagKey<Item> WIRES_STEEL = common("wires/steel");
        public static final TagKey<Item> WIRES_TIN = common("wires/tin");
        public static final TagKey<Item> WIRES_URANIUM = common("wires/uranium");

        private static TagKey<Item> create(String name) {
            return ItemTags.create(MachinaRL.create(name));
        }

        private static TagKey<Item> common(String name) {
            return ItemTags.create(MachinaRL.create("c", name));
        }
    }
}