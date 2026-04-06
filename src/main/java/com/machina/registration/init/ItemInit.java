package com.machina.registration.init;

import java.util.function.Function;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.item.ChemicalItem;
import com.machina.api.item.RocketItem;
import com.machina.block.MachinaHangingSignBlock;
import com.machina.block.MachinaHangingWallSignBlock;
import com.machina.block.MachinaSignBlock;
import com.machina.block.MachinaWallSignBlock;
import com.machina.config.CommonConfig;
import com.machina.item.CapacitorItem;
import com.machina.item.MouldItem;
import com.machina.item.MouldItem.Mould;
import com.machina.item.WindCompassItem;
import com.machina.item.filter.AdvancedItemFilterItem;
import com.machina.item.filter.FluidFilterItem;
import com.machina.item.filter.ItemFilterItem;

import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SignItem;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Machina.MOD_ID);

	//@formatter:off
	public static final DeferredItem<Item> ROCKET = basic("rocket", RocketItem::new);
	
	public static final DeferredItem<CapacitorItem> BASIC_CAPACITOR = capacitor("basic_capacitor", () -> CommonConfig.basicCapacitorSize);
	public static final DeferredItem<CapacitorItem> ADVANCED_CAPACITOR = capacitor("advanced_capacitor", () -> CommonConfig.advancedCapacitorSize);
	public static final DeferredItem<CapacitorItem> SUPREME_CAPACITOR = capacitor("supreme_capacitor", () -> CommonConfig.supremeCapacitorSize);
	
	public static final DeferredItem<MouldItem> MOULD_BASE = mould("mould_base", Mould.BASE);
	public static final DeferredItem<MouldItem> MOULD_PLATE = mould("mould_plate", Mould.PLATE);
	public static final DeferredItem<MouldItem> MOULD_ROD = mould("mould_rod", Mould.ROD);
	public static final DeferredItem<MouldItem> MOULD_WIRE = mould("mould_wire", Mould.WIRE);
	
	public static final DeferredItem<ItemFilterItem> ITEM_FILTER = basic("item_filter", ItemFilterItem::new);
	public static final DeferredItem<AdvancedItemFilterItem> ADVANCED_ITEM_FILTER = basic("advanced_item_filter", AdvancedItemFilterItem::new);
	public static final DeferredItem<FluidFilterItem> FLUID_FILTER = basic("fluid_filter", FluidFilterItem::new);
	public static final DeferredItem<WindCompassItem> WIND_COMPASS = basic("wind_compass", WindCompassItem::new);
	
	public static final DeferredItem<ChemicalItem> RAW_ALUMINUM = chemical("raw_aluminum", "Al");
	public static final DeferredItem<ChemicalItem> RAW_NICKEL = chemical("raw_nickel", "Ni");
	public static final DeferredItem<ChemicalItem> RAW_LEAD = chemical("raw_lead", "Pb");
	public static final DeferredItem<ChemicalItem> RAW_BORON = chemical("raw_boron", "B");
	public static final DeferredItem<ChemicalItem> RAW_PALLADIUM = chemical("raw_palladium", "Pd");
	public static final DeferredItem<ChemicalItem> RAW_SILVER = chemical("raw_silver", "Ag");
	public static final DeferredItem<ChemicalItem> RAW_TIN = chemical("raw_tin", "Sn");
	public static final DeferredItem<ChemicalItem> RAW_ZINC = chemical("raw_zinc", "Zn");
	public static final DeferredItem<ChemicalItem> RAW_LOW_GRADE_TITANIUM = chemical("raw_low_grade_titanium", "Ti");
	public static final DeferredItem<ChemicalItem> RAW_MAGNETITE = chemical("raw_magnetite", "Fe3O4");
	public static final DeferredItem<ChemicalItem> RAW_GYPSUM = chemical("raw_gypsum", "CaSO4");
	public static final DeferredItem<ChemicalItem> RAW_ILMENITE = chemical("raw_ilmenite", "FeTiO3");
	public static final DeferredItem<ChemicalItem> RAW_PLATINUM = chemical("raw_platinum", "Pt");
	public static final DeferredItem<ChemicalItem> RAW_IRIDIUM = chemical("raw_iridium", "Ir");
	public static final DeferredItem<ChemicalItem> RAW_OSMIUM = chemical("raw_osmium", "Os");
	public static final DeferredItem<ChemicalItem> RAW_COBALT = chemical("raw_cobalt", "Co");
	public static final DeferredItem<ChemicalItem> RAW_URANINITE = chemical("raw_uraninite", "UO2");
	public static final DeferredItem<ChemicalItem> RAW_THORIUM = chemical("raw_thorium", "Th");
	
	public static final DeferredItem<ChemicalItem> ALUMINUM_INGOT = chemical("aluminum_ingot", "Al");
	public static final DeferredItem<ChemicalItem> NICKEL_INGOT = chemical("nickel_ingot", "Ni");
	public static final DeferredItem<ChemicalItem> LEAD_INGOT = chemical("lead_ingot", "Pb");
	public static final DeferredItem<ChemicalItem> BORON_INGOT = chemical("boron_ingot", "B");
	public static final DeferredItem<ChemicalItem> PALLADIUM_INGOT = chemical("palladium_ingot", "Pd");
	public static final DeferredItem<ChemicalItem> SILVER_INGOT = chemical("silver_ingot", "Ag");
	public static final DeferredItem<ChemicalItem> TIN_INGOT = chemical("tin_ingot", "Sn");
	public static final DeferredItem<ChemicalItem> ZINC_INGOT = chemical("zinc_ingot", "Zn");
	public static final DeferredItem<ChemicalItem> LOW_GRADE_TITANIUM_INGOT = chemical("low_grade_titanium_ingot", "Ti");
	public static final DeferredItem<ChemicalItem> MAGNETITE_INGOT = chemical("magnetite_ingot", "Fe3O4");
	public static final DeferredItem<ChemicalItem> GYPSUM_INGOT = chemical("gypsum_ingot", "CaSO4");
	public static final DeferredItem<ChemicalItem> ILMENITE_INGOT = chemical("ilmenite_ingot", "FeTiO3");
	public static final DeferredItem<ChemicalItem> PLATINUM_INGOT = chemical("platinum_ingot", "Pt");
	public static final DeferredItem<ChemicalItem> IRIDIUM_INGOT = chemical("iridium_ingot", "Ir");
	public static final DeferredItem<ChemicalItem> OSMIUM_INGOT = chemical("osmium_ingot", "Os");
	public static final DeferredItem<ChemicalItem> COBALT_INGOT = chemical("cobalt_ingot", "Co");
	public static final DeferredItem<ChemicalItem> URANINITE_INGOT = chemical("uraninite_ingot", "UO2");
	public static final DeferredItem<ChemicalItem> THORIUM_INGOT = chemical("thorium_ingot", "Th");
	public static final DeferredItem<ChemicalItem> STEEL_INGOT = chemical("steel_ingot", "Fe+C");
	public static final DeferredItem<ChemicalItem> CONSTANTAN_INGOT = chemical("constantan_ingot", "Cu+Ni");
	
	public static final DeferredItem<ChemicalItem> ALUMINUM_NUGGET = chemical("aluminum_nugget", "Al");
	public static final DeferredItem<ChemicalItem> NICKEL_NUGGET = chemical("nickel_nugget", "Ni");
	public static final DeferredItem<ChemicalItem> LEAD_NUGGET = chemical("lead_nugget", "Pb");
	public static final DeferredItem<ChemicalItem> BORON_NUGGET = chemical("boron_nugget", "B");
	public static final DeferredItem<ChemicalItem> PALLADIUM_NUGGET = chemical("palladium_nugget", "Pd");
	public static final DeferredItem<ChemicalItem> SILVER_NUGGET = chemical("silver_nugget", "Ag");
	public static final DeferredItem<ChemicalItem> STEEL_NUGGET = chemical("steel_nugget", "Fe+C");
	public static final DeferredItem<ChemicalItem> CONSTANTAN_NUGGET = chemical("constantan_nugget", "Cu+Ni");
	public static final DeferredItem<ChemicalItem> TIN_NUGGET = chemical("tin_nugget", "Sn");
	public static final DeferredItem<ChemicalItem> ZINC_NUGGET = chemical("zinc_nugget", "Zn");
	public static final DeferredItem<ChemicalItem> LOW_GRADE_TITANIUM_NUGGET = chemical("low_grade_titanium_nugget", "Ti");
	public static final DeferredItem<ChemicalItem> MAGNETITE_NUGGET = chemical("magnetite_nugget", "Fe3O4");
	public static final DeferredItem<ChemicalItem> GYPSUM_NUGGET = chemical("gypsum_nugget", "CaSO4");
	public static final DeferredItem<ChemicalItem> ILMENITE_NUGGET = chemical("ilmenite_nugget", "FeTiO3");
	public static final DeferredItem<ChemicalItem> PLATINUM_NUGGET = chemical("platinum_nugget", "Pt");
	public static final DeferredItem<ChemicalItem> IRIDIUM_NUGGET = chemical("iridium_nugget", "Ir");
	public static final DeferredItem<ChemicalItem> OSMIUM_NUGGET = chemical("osmium_nugget", "Os");
	public static final DeferredItem<ChemicalItem> COBALT_NUGGET = chemical("cobalt_nugget", "Co");
	public static final DeferredItem<ChemicalItem> URANINITE_NUGGET = chemical("uraninite_nugget", "UO2");
	public static final DeferredItem<ChemicalItem> THORIUM_NUGGET = chemical("thorium_nugget", "Th");
	
	public static final DeferredItem<Item> COAL_CHUNK = basic("coal_chunk");
	public static final DeferredItem<Item> COPPER_NUGGET = basic("copper_nugget");
	
	public static final DeferredItem<ChemicalItem> BISMUTH = chemical("bismuth", "Bi");
	public static final DeferredItem<ChemicalItem> PERCHLORATE = chemical("perchlorate", "KClO4");
	
	public static final DeferredItem<Item> COPPER_COIL = basic("copper_coil");
	public static final DeferredItem<Item> TRANSISTOR = basic("transistor");
	public static final DeferredItem<Item> LOGIC_UNIT = basic("logic_unit");
	public static final DeferredItem<Item> PROCESSOR_CORE = basic("processor_core");
	public static final DeferredItem<Item> PROCESSOR = basic("processor");
	public static final DeferredItem<Item> RAW_SILICON_BLEND = basic("raw_silicon_blend");
	
	public static final DeferredItem<ChemicalItem> COAL_DUST = chemical("coal_dust", "C");
	public static final DeferredItem<ChemicalItem> IRON_DUST = chemical("iron_dust", "Fe");
	public static final DeferredItem<ChemicalItem> COPPER_DUST = chemical("copper_dust", "Cu");
	public static final DeferredItem<ChemicalItem> GOLD_DUST = chemical("gold_dust", "Au");
	public static final DeferredItem<ChemicalItem> DIAMOND_DUST = chemical("diamond_dust", "C");
	public static final DeferredItem<ChemicalItem> LAPIS_DUST = chemical("lapis_dust", "NaAl6Si6O24S2");
	public static final DeferredItem<ChemicalItem> EMERALD_DUST = chemical("emerald_dust", "Be3Al2(SiO3)6");
	public static final DeferredItem<ChemicalItem> QUARTZ_DUST = chemical("quartz_dust", "SiO2");
	public static final DeferredItem<ChemicalItem> ALUMINUM_DUST = chemical("aluminum_dust", "Al");
	public static final DeferredItem<ChemicalItem> NICKEL_DUST = chemical("nickel_dust", "Ni");
	public static final DeferredItem<ChemicalItem> LEAD_DUST = chemical("lead_dust", "Pb");
	public static final DeferredItem<ChemicalItem> BORON_DUST = chemical("boron_dust", "B");
	public static final DeferredItem<ChemicalItem> PALLADIUM_DUST = chemical("palladium_dust", "Pd");
	public static final DeferredItem<ChemicalItem> SILVER_DUST = chemical("silver_dust", "Ag");
	public static final DeferredItem<ChemicalItem> STEEL_DUST = chemical("steel_dust", "Fe+C");
	public static final DeferredItem<ChemicalItem> CONSTANTAN_DUST = chemical("constantan_dust", "Cu+Ni");
	public static final DeferredItem<ChemicalItem> FLUORITE_DUST = chemical("fluorite_dust", "CaF2");
	public static final DeferredItem<ChemicalItem> SULFUR_DUST = chemical("sulfur_dust", "SO3");
	public static final DeferredItem<ChemicalItem> NITER_DUST = chemical("niter_dust", "KNO3");
	public static final DeferredItem<ChemicalItem> BISMUTH_DUST = chemical("bismuth_dust", "Bi");
	public static final DeferredItem<ChemicalItem> TIN_DUST = chemical("tin_dust", "Sn");
	public static final DeferredItem<ChemicalItem> ZINC_DUST = chemical("zinc_dust", "Zn");
	public static final DeferredItem<ChemicalItem> LOW_GRADE_TITANIUM_DUST = chemical("low_grade_titanium_dust", "Ti");
	public static final DeferredItem<ChemicalItem> MAGNETITE_DUST = chemical("magnetite_dust", "Fe3O4");
	public static final DeferredItem<ChemicalItem> GYPSUM_DUST = chemical("gypsum_dust", "CaSO4");
	public static final DeferredItem<ChemicalItem> ILMENITE_DUST = chemical("ilmenite_dust", "FeTiO3");
	public static final DeferredItem<ChemicalItem> PLATINUM_DUST = chemical("platinum_dust", "Pt");
	public static final DeferredItem<ChemicalItem> IRIDIUM_DUST = chemical("iridium_dust", "Ir");
	public static final DeferredItem<ChemicalItem> OSMIUM_DUST = chemical("osmium_dust", "Os");
	public static final DeferredItem<ChemicalItem> COBALT_DUST = chemical("cobalt_dust", "Co");
	public static final DeferredItem<ChemicalItem> URANINITE_DUST = chemical("uraninite_dust", "UO2");
	public static final DeferredItem<ChemicalItem> THORIUM_DUST = chemical("thorium_dust", "Th");
	
	public static final DeferredItem<ChemicalItem> IRON_PLATE = chemical("iron_plate", "Fe");
	public static final DeferredItem<ChemicalItem> COPPER_PLATE = chemical("copper_plate", "Cu");
	public static final DeferredItem<ChemicalItem> GOLD_PLATE = chemical("gold_plate", "Au");
	public static final DeferredItem<ChemicalItem> ALUMINUM_PLATE = chemical("aluminum_plate", "Al");
	public static final DeferredItem<ChemicalItem> NICKEL_PLATE = chemical("nickel_plate", "Ni");
	public static final DeferredItem<ChemicalItem> LEAD_PLATE = chemical("lead_plate", "Pb");
	public static final DeferredItem<ChemicalItem> BORON_PLATE = chemical("boron_plate", "B");
	public static final DeferredItem<ChemicalItem> PALLADIUM_PLATE = chemical("palladium_plate", "Pd");
	public static final DeferredItem<ChemicalItem> SILVER_PLATE = chemical("silver_plate", "Ag");
	public static final DeferredItem<ChemicalItem> STEEL_PLATE = chemical("steel_plate", "Fe+C");
	public static final DeferredItem<ChemicalItem> CONSTANTAN_PLATE = chemical("constantan_plate", "Cu+Ni");
	public static final DeferredItem<ChemicalItem> TIN_PLATE = chemical("tin_plate", "Sn");
	public static final DeferredItem<ChemicalItem> ZINC_PLATE = chemical("zinc_plate", "Zn");
	public static final DeferredItem<ChemicalItem> LOW_GRADE_TITANIUM_PLATE = chemical("low_grade_titanium_plate", "Ti");
	public static final DeferredItem<ChemicalItem> MAGNETITE_PLATE = chemical("magnetite_plate", "Fe3O4");
	public static final DeferredItem<ChemicalItem> GYPSUM_PLATE = chemical("gypsum_plate", "CaSO4");
	public static final DeferredItem<ChemicalItem> ILMENITE_PLATE = chemical("ilmenite_plate", "FeTiO3");
	public static final DeferredItem<ChemicalItem> PLATINUM_PLATE = chemical("platinum_plate", "Pt");
	public static final DeferredItem<ChemicalItem> IRIDIUM_PLATE = chemical("iridium_plate", "Ir");
	public static final DeferredItem<ChemicalItem> OSMIUM_PLATE = chemical("osmium_plate", "Os");
	public static final DeferredItem<ChemicalItem> COBALT_PLATE = chemical("cobalt_plate", "Co");
	public static final DeferredItem<ChemicalItem> URANINITE_PLATE = chemical("uraninite_plate", "UO2");
	public static final DeferredItem<ChemicalItem> THORIUM_PLATE = chemical("thorium_plate", "Th");
	
	public static final DeferredItem<ChemicalItem> IRON_ROD = chemical("iron_rod", "Fe");
	public static final DeferredItem<ChemicalItem> COPPER_ROD = chemical("copper_rod", "Cu");
	public static final DeferredItem<ChemicalItem> GOLD_ROD = chemical("gold_rod", "Au");
	public static final DeferredItem<ChemicalItem> ALUMINUM_ROD = chemical("aluminum_rod", "Al");
	public static final DeferredItem<ChemicalItem> NICKEL_ROD = chemical("nickel_rod", "Ni");
	public static final DeferredItem<ChemicalItem> LEAD_ROD = chemical("lead_rod", "Pb");
	public static final DeferredItem<ChemicalItem> BORON_ROD = chemical("boron_rod", "B");
	public static final DeferredItem<ChemicalItem> PALLADIUM_ROD = chemical("palladium_rod", "Pd");
	public static final DeferredItem<ChemicalItem> SILVER_ROD = chemical("silver_rod", "Ag");
	public static final DeferredItem<ChemicalItem> STEEL_ROD = chemical("steel_rod", "Fe+C");
	public static final DeferredItem<ChemicalItem> CONSTANTAN_ROD = chemical("constantan_rod", "Cu+Ni");
	public static final DeferredItem<ChemicalItem> TIN_ROD = chemical("tin_rod", "Sn");
	public static final DeferredItem<ChemicalItem> ZINC_ROD = chemical("zinc_rod", "Zn");
	public static final DeferredItem<ChemicalItem> LOW_GRADE_TITANIUM_ROD = chemical("low_grade_titanium_rod", "Ti");
	public static final DeferredItem<ChemicalItem> MAGNETITE_ROD = chemical("magnetite_rod", "Fe3O4");
	public static final DeferredItem<ChemicalItem> GYPSUM_ROD = chemical("gypsum_rod", "CaSO4");
	public static final DeferredItem<ChemicalItem> ILMENITE_ROD = chemical("ilmenite_rod", "FeTiO3");
	public static final DeferredItem<ChemicalItem> PLATINUM_ROD = chemical("platinum_rod", "Pt");
	public static final DeferredItem<ChemicalItem> IRIDIUM_ROD = chemical("iridium_rod", "Ir");
	public static final DeferredItem<ChemicalItem> OSMIUM_ROD = chemical("osmium_rod", "Os");
	public static final DeferredItem<ChemicalItem> COBALT_ROD = chemical("cobalt_rod", "Co");
	public static final DeferredItem<ChemicalItem> URANINITE_ROD = chemical("uraninite_rod", "UO2");
	public static final DeferredItem<ChemicalItem> THORIUM_ROD = chemical("thorium_rod", "Th");
	
	public static final DeferredItem<ChemicalItem> IRON_WIRE = chemical("iron_wire", "Fe");
	public static final DeferredItem<ChemicalItem> COPPER_WIRE = chemical("copper_wire", "Cu");
	public static final DeferredItem<ChemicalItem> GOLD_WIRE = chemical("gold_wire", "Au");
	public static final DeferredItem<ChemicalItem> ALUMINUM_WIRE = chemical("aluminum_wire", "Al");
	public static final DeferredItem<ChemicalItem> NICKEL_WIRE = chemical("nickel_wire", "Ni");
	public static final DeferredItem<ChemicalItem> LEAD_WIRE = chemical("lead_wire", "Pb");
	public static final DeferredItem<ChemicalItem> BORON_WIRE = chemical("boron_wire", "B");
	public static final DeferredItem<ChemicalItem> PALLADIUM_WIRE = chemical("palladium_wire", "Pd");
	public static final DeferredItem<ChemicalItem> SILVER_WIRE = chemical("silver_wire", "Ag");
	public static final DeferredItem<ChemicalItem> STEEL_WIRE = chemical("steel_wire", "Fe+C");
	public static final DeferredItem<ChemicalItem> CONSTANTAN_WIRE = chemical("constantan_wire", "Cu+Ni");
	public static final DeferredItem<ChemicalItem> TIN_WIRE = chemical("tin_wire", "Sn");
	public static final DeferredItem<ChemicalItem> ZINC_WIRE = chemical("zinc_wire", "Zn");
	public static final DeferredItem<ChemicalItem> LOW_GRADE_TITANIUM_WIRE = chemical("low_grade_titanium_wire", "Ti");
	public static final DeferredItem<ChemicalItem> MAGNETITE_WIRE = chemical("magnetite_wire", "Fe3O4");
	public static final DeferredItem<ChemicalItem> GYPSUM_WIRE = chemical("gypsum_wire", "CaSO4");
	public static final DeferredItem<ChemicalItem> ILMENITE_WIRE = chemical("ilmenite_wire", "FeTiO3");
	public static final DeferredItem<ChemicalItem> PLATINUM_WIRE = chemical("platinum_wire", "Pt");
	public static final DeferredItem<ChemicalItem> IRIDIUM_WIRE = chemical("iridium_wire", "Ir");
	public static final DeferredItem<ChemicalItem> OSMIUM_WIRE = chemical("osmium_wire", "Os");
	public static final DeferredItem<ChemicalItem> COBALT_WIRE = chemical("cobalt_wire", "Co");
	public static final DeferredItem<ChemicalItem> URANINITE_WIRE = chemical("uraninite_wire", "UO2");
	public static final DeferredItem<ChemicalItem> THORIUM_WIRE = chemical("thorium_wire", "Th");
	
	public static final DeferredItem<ChemicalItem> SILICON = chemical("silicon", "Si");
	public static final DeferredItem<ChemicalItem> SILICON_BOLUS = chemical("silicon_bolus", "Si");
	public static final DeferredItem<ChemicalItem> HIGH_PURITY_SILICON = chemical("high_purity_silicon", "Si");
	public static final DeferredItem<ChemicalItem> AMMONIUM_NITRATE = chemical("ammonium_nitrate", "NH4NO3");
	public static final DeferredItem<ChemicalItem> LDPE = chemical("ldpe", "ldpe", "(CH2CH2)");
	public static final DeferredItem<ChemicalItem> HDPE = chemical("hdpe", "hdpe","(CH2CH2)");
	public static final DeferredItem<ChemicalItem> UHMWPE = chemical("uhmwpe", "uhmwpe", "(CH2CH2)");
	public static final DeferredItem<ChemicalItem> SODIUM_HYDROXIDE = chemical("sodium_hydroxide", "NaOH");
	public static final DeferredItem<ChemicalItem> SODIUM_CARBONATE = chemical("sodium_carbonate", "Na2CO3");
	public static final DeferredItem<ChemicalItem> POTASSIUM_BISULFATE = chemical("potassium_bisulfate", "KHSO4");
	public static final DeferredItem<ChemicalItem> CALCIUM_SULPHATE = chemical("calcium_sulphate", "CaSO4");
	public static final DeferredItem<ChemicalItem> PALLADIUM_CHLORIDE = chemical("palladium_chloride", "PdCl2");
	public static final DeferredItem<ChemicalItem> PALLADIUM_ON_CARBON = chemical("palladium_on_carbon", "Pd/C");
	public static final DeferredItem<ChemicalItem> HEXAMINE = chemical("hexamine", "(CH2)6N4");
	public static final DeferredItem<ChemicalItem> NITRONIUM_TETRAFLUOROBORATE = chemical("nitronium_tetrafluoroborate", "NO2BF4");
	
	public static final DeferredItem<SignItem> TROPICAL_SIGN = sign("tropical_sign", BlockInit.TROPICAL_SIGN, BlockInit.TROPICAL_WALL_SIGN);
	public static final DeferredItem<HangingSignItem> TROPICAL_HANGING_SIGN = hanging_sign("tropical_hanging_sign", BlockInit.TROPICAL_HANGING_SIGN, BlockInit.TROPICAL_WALL_HANGING_SIGN);
	public static final DeferredItem<SignItem> DEAD_TROPICAL_SIGN = sign("dead_tropical_sign", BlockInit.DEAD_TROPICAL_SIGN, BlockInit.DEAD_TROPICAL_WALL_SIGN);
	public static final DeferredItem<HangingSignItem> DEAD_TROPICAL_HANGING_SIGN = hanging_sign("dead_tropical_hanging_sign", BlockInit.DEAD_TROPICAL_HANGING_SIGN, BlockInit.DEAD_TROPICAL_WALL_HANGING_SIGN);
	public static final DeferredItem<SignItem> PINE_SIGN = sign("pine_sign", BlockInit.PINE_SIGN, BlockInit.PINE_WALL_SIGN);
	public static final DeferredItem<HangingSignItem> PINE_HANGING_SIGN = hanging_sign("pine_hanging_sign", BlockInit.PINE_HANGING_SIGN, BlockInit.PINE_WALL_HANGING_SIGN);
	public static final DeferredItem<SignItem> CONIFEROUS_SIGN = sign("coniferous_sign", BlockInit.CONIFEROUS_SIGN, BlockInit.CONIFEROUS_WALL_SIGN);
	public static final DeferredItem<HangingSignItem> CONIFEROUS_HANGING_SIGN = hanging_sign("coniferous_hanging_sign", BlockInit.CONIFEROUS_HANGING_SIGN, BlockInit.CONIFEROUS_WALL_HANGING_SIGN);
	public static final DeferredItem<SignItem> CYCAD_SIGN = sign("cycad_sign", BlockInit.CYCAD_SIGN, BlockInit.CYCAD_WALL_SIGN);
	public static final DeferredItem<HangingSignItem> CYCAD_HANGING_SIGN = hanging_sign("cycad_hanging_sign", BlockInit.CYCAD_HANGING_SIGN, BlockInit.CYCAD_WALL_HANGING_SIGN);
	//@formatter:on

	public static DeferredItem<Item> basic(String name) {
		return register(name, ItemBuilder::basicItem);
	}

	public static DeferredItem<MouldItem> mould(String name, Mould mould) {
		return register(name, () -> ItemBuilder.basicItem(p -> new MouldItem(p, mould)));
	}

	public static DeferredItem<CapacitorItem> capacitor(String name, Supplier<ModConfigSpec.IntValue> cap) {
		return register(name, () -> ItemBuilder.basicItem(p -> new CapacitorItem(p, cap)));
	}

	public static DeferredItem<ChemicalItem> chemical(String name, String chemical) {
		return basic(name, p -> new ChemicalItem(p, chemical));
	}

	public static DeferredItem<ChemicalItem> chemical(String name, String tooltip, String chemical) {
		return basic(name, p -> new ChemicalItem(p, tooltip, chemical));
	}

	public static DeferredItem<SignItem> sign(String name, DeferredBlock<MachinaSignBlock> standing,
			DeferredBlock<MachinaWallSignBlock> wall) {
		return register(name, () -> new SignItem((new Item.Properties()).stacksTo(16), standing.get(), wall.get()));
	}

	public static DeferredItem<HangingSignItem> hanging_sign(String name,
			DeferredBlock<MachinaHangingSignBlock> standing, DeferredBlock<MachinaHangingWallSignBlock> wall) {
		return register(name,
				() -> new HangingSignItem(standing.get(), wall.get(), (new Item.Properties()).stacksTo(16)));
	}

	public static DeferredItem<Item> props(String name, Function<Item.Properties, Item.Properties> propsProcessor) {
		return register(name, () -> ItemBuilder.props(propsProcessor));
	}

	public static <T extends Item> DeferredItem<T> basic(String name, Function<Item.Properties, T> factory) {
		return register(name, () -> ItemBuilder.basicItem(factory));
	}

	public static <T extends Item> DeferredItem<T> register(String name, Supplier<T> item) {
		return (DeferredItem<T>) ITEMS.register(name, item);
	}

    public record ItemBuilder<T extends Item>(Function<Properties, T> factory) {

        public static Item basicItem() {
            return new ItemBuilder<>(Item::new).build();
        }

        public static Item props(Function<Properties, Properties> propsProcessor) {
            return new ItemBuilder<>(p -> new Item(propsProcessor.apply(p))).build();
        }

        public static <T extends Item> T basicItem(Function<Properties, T> factory) {
            return new ItemBuilder<>(factory).build();
        }

        public static <T extends Item> ItemBuilder<T> create(Function<Properties, T> factory) {
            return new ItemBuilder<>(factory);
        }

        public T build() {
            return factory.apply(getProperties());
        }

        public Properties getProperties() {
            return new Properties();
        }
    }

}
