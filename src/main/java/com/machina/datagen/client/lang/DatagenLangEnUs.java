package com.machina.datagen.client.lang;

import com.machina.Machina;
import com.machina.api.cap.sided.ConnectionSide;
import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.EntityTypeInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FruitInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.PlanetTraitInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RocketPartInit;
import com.machina.registration.init.TabInit;

import net.minecraft.data.PackOutput;

public class DatagenLangEnUs extends DatagenLang {

	public DatagenLangEnUs(PackOutput gen) {
		super(gen, "en_us", Machina.MOD_ID);

		this.music_disc = "Music Disc";
		this.bucket = "Bucket";
	}

	@Override
	protected void addTranslations() {
		// Creative Tabs
		addTab(TabInit.MACHINA_MACHINERY, "Machina: Machinery");
		addTab(TabInit.MACHINA_RESOURCES, "Machina: Resources");
		addTab(TabInit.MACHINA_WORLDGEN, "Machina: Worldgen");
		addTab(TabInit.MACHINA_MISCELLANEOUS, "Machina: Miscellaneous");
		addTab(TabInit.MACHINA_ROCKETRY, "Machina: Rocketry");

		// Items
		add(ItemInit.ROCKET, "Rocket");

		add(ItemInit.COPPER_NUGGET, "Copper Nugget");

		add(ItemInit.COAL_DUST, "Coal Dust");
		add(ItemInit.IRON_DUST, "Iron Dust");
		add(ItemInit.COPPER_DUST, "Copper Dust");
		add(ItemInit.GOLD_DUST, "Gold Dust");
		add(ItemInit.DIAMOND_DUST, "Diamond Dust");
		add(ItemInit.LAPIS_DUST, "Lapis Dust");
		add(ItemInit.EMERALD_DUST, "Emerald Dust");
		add(ItemInit.QUARTZ_DUST, "Quartz Dust");

		add(ItemInit.IRON_PLATE, "Iron Plate");
		add(ItemInit.COPPER_PLATE, "Copper Plate");
		add(ItemInit.GOLD_PLATE, "Gold Plate");

		add(ItemInit.IRON_ROD, "Iron Rod");
		add(ItemInit.COPPER_ROD, "Copper Rod");
		add(ItemInit.GOLD_ROD, "Gold Rod");

		add(ItemInit.IRON_WIRE, "Iron Wire");
		add(ItemInit.COPPER_WIRE, "Copper Wire");
		add(ItemInit.GOLD_WIRE, "Gold Wire");

		add(ItemInit.ALUMINUM_NUGGET, "Aluminum Nugget");
		add(ItemInit.ALUMINUM_INGOT, "Aluminum Ingot");
		add(ItemInit.ALUMINUM_DUST, "Aluminum Dust");
		add(ItemInit.ALUMINUM_PLATE, "Aluminum Plate");
		add(ItemInit.ALUMINUM_ROD, "Aluminum Rod");
		add(ItemInit.ALUMINUM_WIRE, "Aluminum Wire");
		add(ItemInit.RAW_ALUMINUM, "Raw Aluminum");

		add(ItemInit.NICKEL_NUGGET, "Nickel Nugget");
		add(ItemInit.NICKEL_INGOT, "Nickel Ingot");
		add(ItemInit.NICKEL_DUST, "Nickel Dust");
		add(ItemInit.NICKEL_PLATE, "Nickel Plate");
		add(ItemInit.NICKEL_ROD, "Nickel Rod");
		add(ItemInit.NICKEL_WIRE, "Nickel Wire");
		add(ItemInit.RAW_NICKEL, "Raw Nickel");

		add(ItemInit.LEAD_NUGGET, "Lead Nugget");
		add(ItemInit.LEAD_INGOT, "Lead Ingot");
		add(ItemInit.LEAD_DUST, "Lead Dust");
		add(ItemInit.LEAD_PLATE, "Lead Plate");
		add(ItemInit.LEAD_ROD, "Lead Rod");
		add(ItemInit.LEAD_WIRE, "Lead Wire");
		add(ItemInit.RAW_LEAD, "Raw Lead");

		add(ItemInit.BORON_NUGGET, "Boron Nugget");
		add(ItemInit.BORON_INGOT, "Boron Ingot");
		add(ItemInit.BORON_DUST, "Boron Dust");
		add(ItemInit.BORON_PLATE, "Boron Plate");
		add(ItemInit.BORON_ROD, "Boron Rod");
		add(ItemInit.BORON_WIRE, "Boron Wire");
		add(ItemInit.RAW_BORON, "Raw Boron");

		add(ItemInit.PALLADIUM_NUGGET, "Palladium Nugget");
		add(ItemInit.PALLADIUM_INGOT, "Palladium Ingot");
		add(ItemInit.PALLADIUM_DUST, "Palladium Dust");
		add(ItemInit.PALLADIUM_PLATE, "Palladium Plate");
		add(ItemInit.PALLADIUM_ROD, "Palladium Rod");
		add(ItemInit.PALLADIUM_WIRE, "Palladium Wire");
		add(ItemInit.RAW_PALLADIUM, "Raw Palladium");

		add(ItemInit.SILVER_NUGGET, "Silver Nugget");
		add(ItemInit.SILVER_INGOT, "Silver Ingot");
		add(ItemInit.SILVER_DUST, "Silver Dust");
		add(ItemInit.SILVER_PLATE, "Silver Plate");
		add(ItemInit.SILVER_ROD, "Silver Rod");
		add(ItemInit.SILVER_WIRE, "Silver Wire");
		add(ItemInit.RAW_SILVER, "Raw Silver");

		add(ItemInit.STEEL_NUGGET, "Steel Nugget");
		add(ItemInit.STEEL_INGOT, "Steel Ingot");
		add(ItemInit.STEEL_DUST, "Steel Dust");
		add(ItemInit.STEEL_PLATE, "Steel Plate");
		add(ItemInit.STEEL_ROD, "Steel Rod");
		add(ItemInit.STEEL_WIRE, "Steel Wire");

		add(ItemInit.CONSTANTAN_NUGGET, "Constantan Nugget");
		add(ItemInit.CONSTANTAN_INGOT, "Constantan Ingot");
		add(ItemInit.CONSTANTAN_DUST, "Constantan Dust");
		add(ItemInit.CONSTANTAN_PLATE, "Constantan Plate");
		add(ItemInit.CONSTANTAN_ROD, "Constantan Rod");
		add(ItemInit.CONSTANTAN_WIRE, "Constantan Wire");

		add(ItemInit.TIN_NUGGET, "Tin Nugget");
		add(ItemInit.TIN_INGOT, "Tin Ingot");
		add(ItemInit.TIN_DUST, "Tin Dust");
		add(ItemInit.TIN_PLATE, "Tin Plate");
		add(ItemInit.TIN_ROD, "Tin Rod");
		add(ItemInit.TIN_WIRE, "Tin Wire");

		add(ItemInit.ZINC_NUGGET, "Zinc Nugget");
		add(ItemInit.ZINC_INGOT, "Zinc Ingot");
		add(ItemInit.ZINC_DUST, "Zinc Dust");
		add(ItemInit.ZINC_PLATE, "Zinc Plate");
		add(ItemInit.ZINC_ROD, "Zinc Rod");
		add(ItemInit.ZINC_WIRE, "Zinc Wire");

		add(ItemInit.LOW_GRADE_TITANIUM_NUGGET, "Low-Grade Titanium Nugget");
		add(ItemInit.LOW_GRADE_TITANIUM_INGOT, "Low-Grade Titanium Ingot");
		add(ItemInit.LOW_GRADE_TITANIUM_DUST, "Low-Grade Titanium Dust");
		add(ItemInit.LOW_GRADE_TITANIUM_PLATE, "Low-Grade Titanium Plate");
		add(ItemInit.LOW_GRADE_TITANIUM_ROD, "Low-Grade Titanium Rod");
		add(ItemInit.LOW_GRADE_TITANIUM_WIRE, "Low-Grade Titanium Wire");

		add(ItemInit.MAGNETITE_NUGGET, "Magnetite Nugget");
		add(ItemInit.MAGNETITE_INGOT, "Magnetite Ingot");
		add(ItemInit.MAGNETITE_DUST, "Magnetite Dust");
		add(ItemInit.MAGNETITE_PLATE, "Magnetite Plate");
		add(ItemInit.MAGNETITE_ROD, "Magnetite Rod");
		add(ItemInit.MAGNETITE_WIRE, "Magnetite Wire");

		add(ItemInit.GYPSUM_NUGGET, "Gypsum Nugget");
		add(ItemInit.GYPSUM_INGOT, "Gypsum Ingot");
		add(ItemInit.GYPSUM_DUST, "Gypsum Dust");
		add(ItemInit.GYPSUM_PLATE, "Gypsum Plate");
		add(ItemInit.GYPSUM_ROD, "Gypsum Rod");
		add(ItemInit.GYPSUM_WIRE, "Gypsum Wire");

		add(ItemInit.ILMENITE_NUGGET, "Ilmenite Nugget");
		add(ItemInit.ILMENITE_INGOT, "Ilmenite Ingot");
		add(ItemInit.ILMENITE_DUST, "Ilmenite Dust");
		add(ItemInit.ILMENITE_PLATE, "Ilmenite Plate");
		add(ItemInit.ILMENITE_ROD, "Ilmenite Rod");
		add(ItemInit.ILMENITE_WIRE, "Ilmenite Wire");

		add(ItemInit.PLATINUM_NUGGET, "Platinum Nugget");
		add(ItemInit.PLATINUM_INGOT, "Platinum Ingot");
		add(ItemInit.PLATINUM_DUST, "Platinum Dust");
		add(ItemInit.PLATINUM_PLATE, "Platinum Plate");
		add(ItemInit.PLATINUM_ROD, "Platinum Rod");
		add(ItemInit.PLATINUM_WIRE, "Platinum Wire");

		add(ItemInit.IRIDIUM_NUGGET, "Iridium Nugget");
		add(ItemInit.IRIDIUM_INGOT, "Iridium Ingot");
		add(ItemInit.IRIDIUM_DUST, "Iridium Dust");
		add(ItemInit.IRIDIUM_PLATE, "Iridium Plate");
		add(ItemInit.IRIDIUM_ROD, "Iridium Rod");
		add(ItemInit.IRIDIUM_WIRE, "Iridium Wire");

		add(ItemInit.OSMIUM_NUGGET, "Osmium Nugget");
		add(ItemInit.OSMIUM_INGOT, "Osmium Ingot");
		add(ItemInit.OSMIUM_DUST, "Osmium Dust");
		add(ItemInit.OSMIUM_PLATE, "Osmium Plate");
		add(ItemInit.OSMIUM_ROD, "Osmium Rod");
		add(ItemInit.OSMIUM_WIRE, "Osmium Wire");

		add(ItemInit.COBALT_NUGGET, "Cobalt Nugget");
		add(ItemInit.COBALT_INGOT, "Cobalt Ingot");
		add(ItemInit.COBALT_DUST, "Cobalt Dust");
		add(ItemInit.COBALT_PLATE, "Cobalt Plate");
		add(ItemInit.COBALT_ROD, "Cobalt Rod");
		add(ItemInit.COBALT_WIRE, "Cobalt Wire");

		add(ItemInit.URANINITE_NUGGET, "Uraninite Nugget");
		add(ItemInit.URANINITE_INGOT, "Uraninite Ingot");
		add(ItemInit.URANINITE_DUST, "Uraninite Dust");
		add(ItemInit.URANINITE_PLATE, "Uraninite Plate");
		add(ItemInit.URANINITE_ROD, "Uraninite Rod");
		add(ItemInit.URANINITE_WIRE, "Uraninite Wire");

		add(ItemInit.THORIUM_NUGGET, "Thorium Nugget");
		add(ItemInit.THORIUM_INGOT, "Thorium Ingot");
		add(ItemInit.THORIUM_DUST, "Thorium Dust");
		add(ItemInit.THORIUM_PLATE, "Thorium Plate");
		add(ItemInit.THORIUM_ROD, "Thorium Rod");
		add(ItemInit.THORIUM_WIRE, "Thorium Wire");

		add(BlockInit.FLUORITE, "Fluorite");
		add(BlockInit.SULFUR, "Sulfur");
		add(BlockInit.NITER, "Niter");
		add(ItemInit.BISMUTH, "Bismuth");
		add(ItemInit.PERCHLORATE, "Perchlorate");

		add(ItemInit.FLUORITE_DUST, "Fluorite Dust");
		add(ItemInit.SULFUR_DUST, "Sulfur Dust");
		add(ItemInit.NITER_DUST, "Niter Dust");
		add(ItemInit.BISMUTH_DUST, "Bismuth Dust");

		add(ItemInit.BASIC_CAPACITOR, "Basic Capacitor");
		add(ItemInit.ADVANCED_CAPACITOR, "Advanced Capacitor");
		add(ItemInit.SUPREME_CAPACITOR, "Supreme Capacitor");

		add(ItemInit.MOULD_BASE, "Mould Base");
		add(ItemInit.MOULD_PLATE, "Plate Mould");
		add(ItemInit.MOULD_ROD, "Rod Mould");
		add(ItemInit.MOULD_WIRE, "Wire Mould");

		add(ItemInit.ITEM_FILTER, "Item Filter");
		add(ItemInit.ADVANCED_ITEM_FILTER, "Advanced Item Filter");
		add(ItemInit.FLUID_FILTER, "Fluid Filter");

		add(ItemInit.METEO_PROBE, "Meteo Probe");

		add(ItemInit.COPPER_COIL, "Copper Coil");
		add(ItemInit.PROCESSOR, "Processor");
		add(ItemInit.RAW_SILICON_BLEND, "Raw Silicon Blend");
		add(ItemInit.SILICON, "Silicon");
		add(ItemInit.SILICON_BOLUS, "Silicon Bolus");
		add(ItemInit.HIGH_PURITY_SILICON, "High Purity Silicon");
		add(ItemInit.TRANSISTOR, "Transistor");
		add(ItemInit.AMMONIUM_NITRATE, "Ammonium Nitrate");
		add(ItemInit.LDPE, "LDPE");
		add(ItemInit.HDPE, "HDPE");
		add(ItemInit.UHMWPE, "UHMWPE");
		add(ItemInit.SODIUM_HYDROXIDE, "Sodium Hydroxide");
		add(ItemInit.SODIUM_CARBONATE, "Sodium Carbonate");
		add(ItemInit.CALCIUM_SULPHATE, "Calcium Sulphate");
		add(ItemInit.PALLADIUM_CHLORIDE, "Palladium Chloride");
		add(ItemInit.PALLADIUM_ON_CARBON, "Palladium on Carbon");
		add(ItemInit.HEXAMINE, "Hexamine");
		add(ItemInit.NITRONIUM_TETRAFLUOROBORATE, "Nitronium Tetrafluoroborate");
		add(ItemInit.LOGIC_UNIT, "Logic Unit");
		add(ItemInit.PROCESSOR_CORE, "Processor Core");
		add(ItemInit.COAL_CHUNK, "Coal Chunk");

		add(FruitInit.TAMA_SPORE, "Tama Spore");
		add(FruitInit.STRAPPLE, "Strapple");
		add(FruitInit.ARGO_BERRY, "Argo Berry");
		add(FruitInit.Y2_NANA, "Y2-Nana");
		add(FruitInit.AVA_FRUIT, "Ava Fruit");
		add(FruitInit.GRELP_BERRY, "Grelp Berry");
		add(FruitInit.SPARR_BALL, "Sparr Ball");
		add(FruitInit.ERBI_POD, "Erbi Pod");

		// Blocks
		add(BlockInit.BASIC_CASING, "Basic Casing");
		add(BlockInit.LIGHTWEIGHT_CASING, "Lightweight Casing");

		add(BlockInit.ENERGY_CABLE, "Energy Cable");
		add(BlockInit.FLUID_PIPE, "Fluid Pipe");
		add(BlockInit.ITEM_CONDUIT, "Item Conduit");
		add(BlockInit.MULTIBLOCK_HOUSING, "Multiblock Housing");
		add(BlockInit.BATTERY, "Battery");
		add(BlockInit.TANK, "Tank");
		add(BlockInit.CREATIVE_BATTERY, "Creative Battery");
		add(BlockInit.ELECTRIC_SMELTER, "Electric Smelter");
		add(BlockInit.FURNACE_GENERATOR, "Furnace Generator");
		add(BlockInit.CHEMICAL_GENERATOR, "Chemical Generator");
		add(BlockInit.GRINDER, "Grinder");
		add(BlockInit.COMPRESSOR, "Compressor");
		add(BlockInit.SAWMILL, "Sawmill");
		add(BlockInit.MELTER, "Melter");
		add(BlockInit.SOLIDIFIER, "Solidifier");
		add(BlockInit.REACTION_CHAMBER, "Reaction Chamber");
		add(BlockInit.COMPOSTER_VAT, "Composter Vat");
		add(BlockInit.ELECTROLYZER, "Electrolyzer");
		add(BlockInit.ELECTRIC_PUMP, "Electric Pump");
		add(BlockInit.ATMOSPHERIC_SEPARATOR, "Atmospheric Separator");
		add(BlockInit.ROCKET_PART_BENCH, "Rocket Part Bench");
		add(BlockInit.ROCKET_ASSEMBLY_STATION, "Rocket Assembly Station");
		add(BlockInit.ROCKET_REFUELING_STATION, "Rocket Refueling Station");

		add(BlockInit.RAW_ALUMINUM_BLOCK, "Raw Aluminum Block");
		add(BlockInit.RAW_NICKEL_BLOCK, "Raw Nickel Block");
		add(BlockInit.RAW_LEAD_BLOCK, "Raw Lead Block");
		add(BlockInit.RAW_BORON_BLOCK, "Raw Boron Block");
		add(BlockInit.RAW_PALLADIUM_BLOCK, "Raw Palladium Block");
		add(BlockInit.RAW_SILVER_BLOCK, "Raw Silver Block");
		add(BlockInit.RAW_TIN_BLOCK, "Raw Tin Block");
		add(BlockInit.RAW_ZINC_BLOCK, "Raw Zinc Block");
		add(BlockInit.RAW_LOW_GRADE_TITANIUM_BLOCK, "Raw Low-Grade Titanium Block");
		add(BlockInit.RAW_MAGNETITE_BLOCK, "Raw Magnetite Block");
		add(BlockInit.RAW_GYPSUM_BLOCK, "Raw Gypsum Block");
		add(BlockInit.RAW_ILMENITE_BLOCK, "Raw Ilmenite Block");
		add(BlockInit.RAW_PLATINUM_BLOCK, "Raw Platinum Block");
		add(BlockInit.RAW_IRIDIUM_BLOCK, "Raw Iridium Block");
		add(BlockInit.RAW_OSMIUM_BLOCK, "Raw Osmium Block");
		add(BlockInit.RAW_COBALT_BLOCK, "Raw Cobalt Block");
		add(BlockInit.RAW_URANINITE_BLOCK, "Raw Uraninite Block");
		add(BlockInit.RAW_THORIUM_BLOCK, "Raw Thorium Block");

		add(BlockInit.ALUMINUM_BLOCK, "Aluminum Block");
		add(BlockInit.NICKEL_BLOCK, "Nickel Block");
		add(BlockInit.LEAD_BLOCK, "Lead Block");
		add(BlockInit.BORON_BLOCK, "Boron Block");
		add(BlockInit.PALLADIUM_BLOCK, "Palladium Block");
		add(BlockInit.SILVER_BLOCK, "Silver Block");
		add(BlockInit.STEEL_BLOCK, "Steel Block");
		add(BlockInit.CONSTANTAN_BLOCK, "Constantan Block");
		add(BlockInit.TIN_BLOCK, "Tin Block");
		add(BlockInit.ZINC_BLOCK, "Zinc Block");
		add(BlockInit.LOW_GRADE_TITANIUM_BLOCK, "Low-Grade Titanium Block");
		add(BlockInit.MAGNETITE_BLOCK, "Magnetite Block");
		add(BlockInit.GYPSUM_BLOCK, "Gypsum Block");
		add(BlockInit.ILMENITE_BLOCK, "Ilmenite Block");
		add(BlockInit.PLATINUM_BLOCK, "Platinum Block");
		add(BlockInit.IRIDIUM_BLOCK, "Iridium Block");
		add(BlockInit.OSMIUM_BLOCK, "Osmium Block");
		add(BlockInit.COBALT_BLOCK, "Cobalt Block");
		add(BlockInit.URANINITE_BLOCK, "Uraninite Block");
		add(BlockInit.THORIUM_BLOCK, "Thorium Block");

		add(BlockInit.COAL_ORE, "Coal Ore");
		add(BlockInit.IRON_ORE, "Iron Ore");
		add(BlockInit.COPPER_ORE, "Copper Ore");
		add(BlockInit.GOLD_ORE, "Gold Ore");
		add(BlockInit.REDSTONE_ORE, "Redstone Ore");
		add(BlockInit.NETHER_QUARTZ_ORE, "Quartz Ore");
		add(BlockInit.EMERALD_ORE, "Emerald Ore");
		add(BlockInit.LAPIS_ORE, "Lapis Ore");
		add(BlockInit.DIAMOND_ORE, "Diamond Ore");
		add(BlockInit.ALUMINUM_ORE, "Aluminum Ore");
		add(BlockInit.NICKEL_ORE, "Nickel Ore");
		add(BlockInit.LEAD_ORE, "Lead Ore");
		add(BlockInit.BORON_ORE, "Boron Ore");
		add(BlockInit.PALLADIUM_ORE, "Palladium Ore");
		add(BlockInit.SILVER_ORE, "Silver Ore");
		add(BlockInit.FLUORITE_ORE, "Fluorite Ore");
		add(BlockInit.SALTPETER_ORE, "Saltpeter Ore");
		add(BlockInit.PYRITE_ORE, "Pyrite Ore");
		add(BlockInit.BISMUTH_ORE, "Bismuth Ore");
		add(BlockInit.TIN_ORE, "Tin Ore");
		add(BlockInit.ZINC_ORE, "Zinc Ore");
		add(BlockInit.LOW_GRADE_TITANIUM_ORE, "Low-Grade Titanium Ore");
		add(BlockInit.MAGNETITE_ORE, "Magnetite Ore");
		add(BlockInit.GYPSUM_ORE, "Gypsum Ore");
		add(BlockInit.PERCHLORATE_ORE, "Perchlorate Ore");
		add(BlockInit.ILMENITE_ORE, "Ilmenite Ore");
		add(BlockInit.PLATINUM_ORE, "Platinum Ore");
		add(BlockInit.IRIDIUM_ORE, "Iridium Ore");
		add(BlockInit.OSMIUM_ORE, "Osmium Ore");
		add(BlockInit.COBALT_ORE, "Cobalt Ore");
		add(BlockInit.URANINITE_ORE, "Uraninite Ore");
		add(BlockInit.THORIUM_ORE, "Thorium Ore");

		add(BlockInit.ANTHRACITE, "Anthracite");
		add(BlockInit.ANTHRACITE_SLAB, "Anthracite Slab");
		add(BlockInit.ANTHRACITE_STAIRS, "Anthracite Stairs");
		add(BlockInit.ANTHRACITE_WALL, "Anthracite Wall");
		add(BlockInit.ANTHRACITE_BUTTON, "Anthracite Button");
		add(BlockInit.ANTHRACITE_PRESSURE_PLATE, "Anthracite Pressure Plate");
		add(BlockInit.ANTHRACITE_PEBBLES, "Anthracite Pebbles");

		add(BlockInit.FELDSPAR, "Feldspar");
		add(BlockInit.FELDSPAR_SLAB, "Feldspar Slab");
		add(BlockInit.FELDSPAR_STAIRS, "Feldspar Stairs");
		add(BlockInit.FELDSPAR_WALL, "Feldspar Wall");
		add(BlockInit.FELDSPAR_BUTTON, "Feldspar Button");
		add(BlockInit.FELDSPAR_PRESSURE_PLATE, "Feldspar Pressure Plate");
		add(BlockInit.FELDSPAR_PEBBLES, "Feldspar Pebbles");

		add(BlockInit.GRAY_SOAPSTONE, "Gray Soapstone");
		add(BlockInit.GRAY_SOAPSTONE_SLAB, "Gray Soapstone Slab");
		add(BlockInit.GRAY_SOAPSTONE_STAIRS, "Gray Soapstone Stairs");
		add(BlockInit.GRAY_SOAPSTONE_WALL, "Gray Soapstone Wall");
		add(BlockInit.GRAY_SOAPSTONE_BUTTON, "Gray Soapstone Button");
		add(BlockInit.GRAY_SOAPSTONE_PRESSURE_PLATE, "Gray Soapstone Pressure Plate");
		add(BlockInit.GRAY_SOAPSTONE_PEBBLES, "Gray Soapstone Pebbles");

		add(BlockInit.GREEN_SOAPSTONE, "Green Soapstone");
		add(BlockInit.GREEN_SOAPSTONE_SLAB, "Green Soapstone Slab");
		add(BlockInit.GREEN_SOAPSTONE_STAIRS, "Green Soapstone Stairs");
		add(BlockInit.GREEN_SOAPSTONE_WALL, "Green Soapstone Wall");
		add(BlockInit.GREEN_SOAPSTONE_BUTTON, "Green Soapstone Button");
		add(BlockInit.GREEN_SOAPSTONE_PRESSURE_PLATE, "Green Soapstone Pressure Plate");
		add(BlockInit.GREEN_SOAPSTONE_PEBBLES, "Green Soapstone Pebbles");

		add(BlockInit.WHITE_SOAPSTONE, "White Soapstone");
		add(BlockInit.WHITE_SOAPSTONE_SLAB, "White Soapstone Slab");
		add(BlockInit.WHITE_SOAPSTONE_STAIRS, "White Soapstone Stairs");
		add(BlockInit.WHITE_SOAPSTONE_WALL, "White Soapstone Wall");
		add(BlockInit.WHITE_SOAPSTONE_BUTTON, "White Soapstone Button");
		add(BlockInit.WHITE_SOAPSTONE_PRESSURE_PLATE, "White Soapstone Pressure Plate");
		add(BlockInit.WHITE_SOAPSTONE_PEBBLES, "White Soapstone Pebbles");

		add(BlockInit.WISPY_SOAPSTONE, "Wispy Soapstone");
		add(BlockInit.WISPY_SOAPSTONE_SLAB, "Wispy Soapstone Slab");
		add(BlockInit.WISPY_SOAPSTONE_STAIRS, "Wispy Soapstone Stairs");
		add(BlockInit.WISPY_SOAPSTONE_WALL, "Wispy Soapstone Wall");
		add(BlockInit.WISPY_SOAPSTONE_BUTTON, "Wispy Soapstone Button");
		add(BlockInit.WISPY_SOAPSTONE_PRESSURE_PLATE, "Wispy Soapstone Pressure Plate");
		add(BlockInit.WISPY_SOAPSTONE_PEBBLES, "Wispy Soapstone Pebbles");

		add(BlockInit.MOONSTONE, "Moonstone");
		add(BlockInit.MOONSTONE_SLAB, "Moonstone Slab");
		add(BlockInit.MOONSTONE_STAIRS, "Moonstone Stairs");
		add(BlockInit.MOONSTONE_WALL, "Moonstone Wall");
		add(BlockInit.MOONSTONE_BUTTON, "Moonstone Button");
		add(BlockInit.MOONSTONE_PRESSURE_PLATE, "Moonstone Pressure Plate");
		add(BlockInit.MOONSTONE_PEBBLES, "Moonstone Pebbles");

		add(BlockInit.SHALE, "Shale");
		add(BlockInit.SHALE_SLAB, "Shale Slab");
		add(BlockInit.SHALE_STAIRS, "Shale Stairs");
		add(BlockInit.SHALE_WALL, "Shale Wall");
		add(BlockInit.SHALE_BUTTON, "Shale Button");
		add(BlockInit.SHALE_PRESSURE_PLATE, "Shale Pressure Plate");
		add(BlockInit.SHALE_PEBBLES, "Shale Pebbles");

		add(BlockInit.TECTONITE, "Tectonite");
		add(BlockInit.TECTONITE_SLAB, "Tectonite Slab");
		add(BlockInit.TECTONITE_STAIRS, "Tectonite Stairs");
		add(BlockInit.TECTONITE_WALL, "Tectonite Wall");
		add(BlockInit.TECTONITE_BUTTON, "Tectonite Button");
		add(BlockInit.TECTONITE_PRESSURE_PLATE, "Tectonite Pressure Plate");
		add(BlockInit.TECTONITE_PEBBLES, "Tectonite Pebbles");

		add(BlockInit.MARBLE, "Marble");
		add(BlockInit.MARBLE_SLAB, "Marble Slab");
		add(BlockInit.MARBLE_STAIRS, "Marble Stairs");
		add(BlockInit.MARBLE_WALL, "Marble Wall");
		add(BlockInit.MARBLE_BUTTON, "Marble Button");
		add(BlockInit.MARBLE_PRESSURE_PLATE, "Marble Pressure Plate");
		add(BlockInit.MARBLE_PEBBLES, "Marble Pebbles");

		add(BlockInit.CHALK, "Chalk");
		add(BlockInit.CHALK_SLAB, "Chalk Slab");
		add(BlockInit.CHALK_STAIRS, "Chalk Stairs");
		add(BlockInit.CHALK_WALL, "Chalk Wall");
		add(BlockInit.CHALK_BUTTON, "Chalk Button");
		add(BlockInit.CHALK_PRESSURE_PLATE, "Chalk Pressure Plate");
		add(BlockInit.CHALK_PEBBLES, "Chalk Pebbles");

		add(BlockInit.LIMESTONE, "Limestone");
		add(BlockInit.LIMESTONE_SLAB, "Limestone Slab");
		add(BlockInit.LIMESTONE_STAIRS, "Limestone Stairs");
		add(BlockInit.LIMESTONE_WALL, "Limestone Wall");
		add(BlockInit.LIMESTONE_BUTTON, "Limestone Button");
		add(BlockInit.LIMESTONE_PRESSURE_PLATE, "Limestone Pressure Plate");
		add(BlockInit.LIMESTONE_PEBBLES, "Limestone Pebbles");

		add(BlockInit.MIGMATITE, "Migmatite");
		add(BlockInit.MIGMATITE_SLAB, "Migmatite Slab");
		add(BlockInit.MIGMATITE_STAIRS, "Migmatite Stairs");
		add(BlockInit.MIGMATITE_WALL, "Migmatite Wall");
		add(BlockInit.MIGMATITE_BUTTON, "Migmatite Button");
		add(BlockInit.MIGMATITE_PRESSURE_PLATE, "Migmatite Pressure Plate");
		add(BlockInit.MIGMATITE_PEBBLES, "Migmatite Pebbles");

		add(BlockInit.GNEISS, "Gneiss");
		add(BlockInit.GNEISS_SLAB, "Gneiss Slab");
		add(BlockInit.GNEISS_STAIRS, "Gneiss Stairs");
		add(BlockInit.GNEISS_WALL, "Gneiss Wall");
		add(BlockInit.GNEISS_BUTTON, "Gneiss Button");
		add(BlockInit.GNEISS_PRESSURE_PLATE, "Gneiss Pressure Plate");
		add(BlockInit.GNEISS_PEBBLES, "Gneiss Pebbles");

		add(BlockInit.BASALT, "Basalt");
		add(BlockInit.BASALT_SLAB, "Basalt Slab");
		add(BlockInit.BASALT_STAIRS, "Basalt Stairs");
		add(BlockInit.BASALT_WALL, "Basalt Wall");
		add(BlockInit.BASALT_BUTTON, "Basalt Button");
		add(BlockInit.BASALT_PRESSURE_PLATE, "Basalt Pressure Plate");
		add(BlockInit.BASALT_PEBBLES, "Basalt Pebbles");

		add(BlockInit.BASALT_SPRINGSTONE, "Basalt Springstone");
		add(BlockInit.BASALT_SPRINGSTONE_SLAB, "Basalt Springstone Slab");
		add(BlockInit.BASALT_SPRINGSTONE_STAIRS, "Basalt Springstone Stairs");
		add(BlockInit.BASALT_SPRINGSTONE_WALL, "Basalt Springstone Wall");
		add(BlockInit.BASALT_SPRINGSTONE_BUTTON, "Basalt Springstone Button");
		add(BlockInit.BASALT_SPRINGSTONE_PRESSURE_PLATE, "Basalt Springstone Pressure Plate");
		add(BlockInit.BASALT_SPRINGSTONE_PEBBLES, "Basalt Springstone Pebbles");

		add(BlockInit.TROPICAL_GRASS_BLOCK, "Tropical Grass Block");
		add(BlockInit.FOREST_GRASS_BLOCK, "Forest Grass Block");
		add(BlockInit.CONIFEROUS_GRASS_BLOCK, "Coniferous Grass Block");
		add(BlockInit.WINDSWEPT_GRASS_BLOCK, "Windswept Grass Block");
		add(BlockInit.MYCELIAL_GRASS_BLOCK, "Mycelial Grass Block");

		add(BlockInit.TROPICAL_DIRT, "Tropical Dirt");
		add(BlockInit.TROPICAL_DIRT_STAIRS, "Tropical Dirt Stairs");
		add(BlockInit.TROPICAL_DIRT_SLAB, "Tropical Dirt Slab");
		add(BlockInit.FOREST_DIRT, "Forest Dirt");
		add(BlockInit.FOREST_DIRT_STAIRS, "Forest Dirt Stairs");
		add(BlockInit.FOREST_DIRT_SLAB, "Forest Dirt Slab");
		add(BlockInit.CONIFEROUS_DIRT, "Coniferous Dirt");
		add(BlockInit.CONIFEROUS_DIRT_STAIRS, "Coniferous Dirt Stairs");
		add(BlockInit.CONIFEROUS_DIRT_SLAB, "Coniferous Dirt Slab");
		add(BlockInit.WINDSWEPT_DIRT, "Windswept Dirt");
		add(BlockInit.WINDSWEPT_DIRT_STAIRS, "Windswept Dirt Stairs");
		add(BlockInit.WINDSWEPT_DIRT_SLAB, "Windswept Dirt Slab");
		add(BlockInit.MYCELIAL_DIRT, "Mycelial Dirt");
		add(BlockInit.MYCELIAL_DIRT_STAIRS, "Mycelial Dirt Stairs");
		add(BlockInit.MYCELIAL_DIRT_SLAB, "Mycelial Dirt Slab");
		add(BlockInit.PEAT, "Peat");
		add(BlockInit.PEAT_STAIRS, "Peat Stairs");
		add(BlockInit.PEAT_SLAB, "Peat Slab");
		add(BlockInit.SILT, "Silt");
		add(BlockInit.SILT_STAIRS, "Silt Stairs");
		add(BlockInit.SILT_SLAB, "Silt Slab");

		add(BlockInit.TROPICAL_SAND, "Tropical Sand");
		add(BlockInit.MOONSAND, "Moonsand");
		add(BlockInit.FERROUS_SAND, "Ferrous Sand");
		add(BlockInit.ASH, "Ash");

		add(BlockInit.SULFUR_GEYSER, "Sulfur Geyser");

		add(BlockInit.TROPICAL_BUTTON, "Tropical Button");
		add(BlockInit.TROPICAL_DOOR, "Tropical Door");
		add(BlockInit.TROPICAL_FENCE, "Tropical Fence");
		add(BlockInit.TROPICAL_FENCE_GATE, "Tropical Fence Gate");
		add(BlockInit.TROPICAL_HANGING_SIGN, "Tropical Hanging Sign");
		add(BlockInit.TROPICAL_LOG, "Tropical Log");
		add(BlockInit.TROPICAL_PLANKS, "Tropical Planks");
		add(BlockInit.TROPICAL_LEAVES, "Tropical Leaves");
		add(BlockInit.TROPICAL_PRESSURE_PLATE, "Tropical Pressure Plate");
		add(BlockInit.TROPICAL_SIGN, "Tropical Sign");
		add(BlockInit.TROPICAL_SLAB, "Tropical Slab");
		add(BlockInit.TROPICAL_STAIRS, "Tropical Stairs");
		add(BlockInit.TROPICAL_TRAPDOOR, "Tropical Trapdoor");
		add(BlockInit.TROPICAL_WOOD, "Tropical Wood");
		add(BlockInit.STRIPPED_TROPICAL_LOG, "Stripped Tropical Log");
		add(BlockInit.STRIPPED_TROPICAL_WOOD, "Stripped Tropical Wood");

		add(BlockInit.DEAD_TROPICAL_BUTTON, "Dead Tropical Button");
		add(BlockInit.DEAD_TROPICAL_DOOR, "Dead Tropical Door");
		add(BlockInit.DEAD_TROPICAL_FENCE, "Dead Tropical Fence");
		add(BlockInit.DEAD_TROPICAL_FENCE_GATE, "Dead Tropical Fence Gate");
		add(BlockInit.DEAD_TROPICAL_HANGING_SIGN, "Dead Tropical Hanging Sign");
		add(BlockInit.DEAD_TROPICAL_LOG, "Dead Tropical Log");
		add(BlockInit.DEAD_TROPICAL_PLANKS, "Dead Tropical Planks");
		add(BlockInit.DEAD_TROPICAL_LEAVES, "Dead Tropical Leaves");
		add(BlockInit.DEAD_TROPICAL_PRESSURE_PLATE, "Dead Tropical Pressure Plate");
		add(BlockInit.DEAD_TROPICAL_SIGN, "Dead Tropical Sign");
		add(BlockInit.DEAD_TROPICAL_SLAB, "Dead Tropical Slab");
		add(BlockInit.DEAD_TROPICAL_STAIRS, "Dead Tropical Stairs");
		add(BlockInit.DEAD_TROPICAL_TRAPDOOR, "Dead Tropical Trapdoor");
		add(BlockInit.DEAD_TROPICAL_WOOD, "Dead Tropical Wood");
		add(BlockInit.STRIPPED_DEAD_TROPICAL_LOG, "Stripped Tropical Log");
		add(BlockInit.STRIPPED_DEAD_TROPICAL_WOOD, "Stripped Tropical Wood");

		add(BlockInit.PINE_BUTTON, "Pine Button");
		add(BlockInit.PINE_DOOR, "Pine Door");
		add(BlockInit.PINE_FENCE, "Pine Fence");
		add(BlockInit.PINE_FENCE_GATE, "Pine Fence Gate");
		add(BlockInit.PINE_HANGING_SIGN, "Pine Hanging Sign");
		add(BlockInit.PINE_LOG, "Pine Log");
		add(BlockInit.PINE_PLANKS, "Pine Planks");
		add(BlockInit.PINE_LEAVES, "Pine Leaves");
		add(BlockInit.PINE_PRESSURE_PLATE, "Pine Pressure Plate");
		add(BlockInit.PINE_SIGN, "Pine Sign");
		add(BlockInit.PINE_SLAB, "Pine Slab");
		add(BlockInit.PINE_STAIRS, "Pine Stairs");
		add(BlockInit.PINE_TRAPDOOR, "Pine Trapdoor");
		add(BlockInit.PINE_WOOD, "Pine Wood");
		add(BlockInit.STRIPPED_PINE_LOG, "Stripped Pine Log");
		add(BlockInit.STRIPPED_PINE_WOOD, "Stripped Pine Wood");

		add(BlockInit.CONIFEROUS_BUTTON, "Coniferous Button");
		add(BlockInit.CONIFEROUS_DOOR, "Coniferous Door");
		add(BlockInit.CONIFEROUS_FENCE, "Coniferous Fence");
		add(BlockInit.CONIFEROUS_FENCE_GATE, "Coniferous Fence Gate");
		add(BlockInit.CONIFEROUS_HANGING_SIGN, "Coniferous Hanging Sign");
		add(BlockInit.CONIFEROUS_LOG, "Coniferous Log");
		add(BlockInit.CONIFEROUS_PLANKS, "Coniferous Planks");
		add(BlockInit.GREEN_CONIFEROUS_LEAVES, "Green Coniferous Leaves");
		add(BlockInit.YELLOW_CONIFEROUS_LEAVES, "Yellow Coniferous Leaves");
		add(BlockInit.ORANGE_CONIFEROUS_LEAVES, "Orange Coniferous Leaves");
		add(BlockInit.RED_CONIFEROUS_LEAVES, "Red Coniferous Leaves");
		add(BlockInit.CONIFEROUS_PRESSURE_PLATE, "Coniferous Pressure Plate");
		add(BlockInit.CONIFEROUS_SIGN, "Coniferous Sign");
		add(BlockInit.CONIFEROUS_SLAB, "Coniferous Slab");
		add(BlockInit.CONIFEROUS_STAIRS, "Coniferous Stairs");
		add(BlockInit.CONIFEROUS_TRAPDOOR, "Coniferous Trapdoor");
		add(BlockInit.CONIFEROUS_WOOD, "Coniferous Wood");
		add(BlockInit.STRIPPED_CONIFEROUS_LOG, "Stripped Coniferous Log");
		add(BlockInit.STRIPPED_CONIFEROUS_WOOD, "Stripped Coniferous Wood");

		add(BlockInit.CYCAD_BUTTON, "Cycad Button");
		add(BlockInit.CYCAD_DOOR, "Cycad Door");
		add(BlockInit.CYCAD_FENCE, "Cycad Fence");
		add(BlockInit.CYCAD_FENCE_GATE, "Cycad Fence Gate");
		add(BlockInit.CYCAD_HANGING_SIGN, "Cycad Hanging Sign");
		add(BlockInit.CYCAD_LOG, "Cycad Log");
		add(BlockInit.CYCAD_PLANKS, "Cycad Planks");
		add(BlockInit.CYCAD_LEAVES, "Cycad Leaves");
		add(BlockInit.CYCAD_PRESSURE_PLATE, "Cycad Pressure Plate");
		add(BlockInit.CYCAD_SIGN, "Cycad Sign");
		add(BlockInit.CYCAD_SLAB, "Cycad Slab");
		add(BlockInit.CYCAD_STAIRS, "Cycad Stairs");
		add(BlockInit.CYCAD_TRAPDOOR, "Cycad Trapdoor");
		add(BlockInit.CYCAD_WOOD, "Cycad Wood");
		add(BlockInit.STRIPPED_CYCAD_LOG, "Stripped Cycad Log");
		add(BlockInit.STRIPPED_CYCAD_WOOD, "Stripped Cycad Wood");

		add(BlockInit.BROWN_MUSHROOM_STALK, "Brown Mushroom Stalk");
		add(BlockInit.GREEN_MUSHROOM_STALK, "Green Mushroom Stalk");
		add(BlockInit.PURPLE_MUSHROOM_STALK, "Purple Mushroom Stalk");
		add(BlockInit.YELLOW_MUSHROOM_STALK, "Yellow Mushroom Stalk");
		add(BlockInit.BROWN_MUSHROOM_CAP, "Brown Mushroom Cap");
		add(BlockInit.RED_MUSHROOM_CAP, "Red Mushroom Cap");
		add(BlockInit.PURPLE_MUSHROOM_CAP, "Purple Mushroom Cap");
		add(BlockInit.YELLOW_MUSHROOM_CAP, "Yellow Mushroom Cap");
		add(BlockInit.BROWN_MUSHROOM_GILLS, "Brown Mushroom Gills");
		add(BlockInit.RED_MUSHROOM_GILLS, "Red Mushroom Gills");
		add(BlockInit.PURPLE_MUSHROOM_GILLS, "Purple Mushroom Gills");
		add(BlockInit.YELLOW_MUSHROOM_GILLS, "Yellow Mushroom Gills");
		add(BlockInit.SPECKLED_BROWN_MUSHROOM_CAP, "Speckled Brown Mushroom Cap");
		add(BlockInit.SPECKLED_RED_MUSHROOM_CAP, "Speckled Red Mushroom Cap");
		add(BlockInit.SPECKLED_PURPLE_MUSHROOM_CAP, "Speckled Purple Mushroom Cap");
		add(BlockInit.SPECKLED_YELLOW_MUSHROOM_CAP, "Speckled Yellow Mushroom Cap");
		add(BlockInit.IMBUED_BROWN_MUSHROOM_CAP, "Imbued Brown Mushroom Cap");
		add(BlockInit.IMBUED_RED_MUSHROOM_CAP, "Imbued Red Mushroom Cap");
		add(BlockInit.IMBUED_PURPLE_MUSHROOM_CAP, "Imbued Purple Mushroom Cap");
		add(BlockInit.IMBUED_YELLOW_MUSHROOM_CAP, "Imbued Yellow Mushroom Cap");

		add(BlockInit.TROPICAL_GRASS, "Tropical Grass");
		add(BlockInit.TWISTED_GRASS, "Twisted Grass");
		add(BlockInit.CONIFEROUS_GRASS, "Coniferous Grass");
		add(BlockInit.SHORT_CONIFEROUS_GRASS, "Short Coniferous Grass");
		add(BlockInit.WINDSWEPT_GRASS, "Windswept Grass");
		add(BlockInit.MYCELIAL_GRASS, "Mycelial Grass");
		add(BlockInit.FERROUS_GRASS, "Ferrous Grass");
		add(BlockInit.MOONGRASS, "Moongrass");

		add(BlockInit.PURPLE_PETALS, "Purple Petals");
		add(BlockInit.RED_PETALS, "Red Petals");
		add(BlockInit.ORANGE_PETALS, "Orange Petals");
		add(BlockInit.YELLOW_PETALS, "Yellow Petals");
		add(BlockInit.GREEN_PETALS, "Green Petals");
		add(BlockInit.TURQUOISE_PETALS, "Turquoise Petals");
		add(BlockInit.BLUE_PETALS, "Blue Petals");

		add(BlockInit.ORPHEUM, "Orpheum");
		add(BlockInit.DRAGON_PEONY, "Dragon Peony");

		add(BlockInit.CLOVER, "Clover");
		add(BlockInit.SPINDLEGRASS, "Spindlegrass");
		add(BlockInit.SPINDLESPROUT, "Spindlesprout");
		add(BlockInit.SPRUCE_CUP, "Spruce Cup");
		add(BlockInit.SMALL_FERN, "Small Fern");
		add(BlockInit.DEAD_SMALL_FERN, "Dead Small Fern");
		add(BlockInit.NEEDLEGRASS, "Needlegrass");
		add(BlockInit.NEEDLETHATCH, "Needlethatch");

		add(BlockInit.POTTED_SPRUCE_CUP, "Potted Spruce Cup");
		add(BlockInit.POTTED_DRAGON_PEONY, "Potted Dragon Peony");
		add(BlockInit.POTTED_SMALL_FERN, "Potted Small Fern");
		add(BlockInit.POTTED_DEAD_SMALL_FERN, "Potted Dead Small Fern");
		add(BlockInit.POTTED_NEEDLEGRASS, "Potted Needlegrass");

		add(BlockInit.PURPLE_GROUNDLILY, "Purple Groundlily");
		add(BlockInit.PINK_GROUNDLILY, "Pink Groundlily");
		add(BlockInit.RED_GROUNDLILY, "Red Groundlily");
		add(BlockInit.ORANGE_GROUNDLILY, "Orange Groundlily");
		add(BlockInit.YELLOW_GROUNDLILY, "Yellow Groundlily");
		add(BlockInit.GREEN_GROUNDLILY, "Green Groundlily");
		add(BlockInit.TURQUOISE_GROUNDLILY, "Turquoise Groundlily");
		add(BlockInit.BLUE_GROUNDLILY, "Blue Groundlily");
		add(BlockInit.PURPLE_WATERLILY, "Purple Waterlily");
		add(BlockInit.PINK_WATERLILY, "Pink Waterlily");
		add(BlockInit.RED_WATERLILY, "Red Waterlily");
		add(BlockInit.ORANGE_WATERLILY, "Orange Waterlily");
		add(BlockInit.YELLOW_WATERLILY, "Yellow Waterlily");
		add(BlockInit.GREEN_WATERLILY, "Green Waterlily");
		add(BlockInit.TURQUOISE_WATERLILY, "Turquoise Waterlily");
		add(BlockInit.BLUE_WATERLILY, "Blue Waterlily");

		add(BlockInit.PURPLE_GLOWSHROOM, "Purple Glowshroom");
		add(BlockInit.POTTED_PURPLE_GLOWSHROOM, "Potted Purple Glowshroom");
		add(BlockInit.PINK_GLOWSHROOM, "Pink Glowshroom");
		add(BlockInit.POTTED_PINK_GLOWSHROOM, "Potted Pink Glowshroom");
		add(BlockInit.RED_GLOWSHROOM, "Red Glowshroom");
		add(BlockInit.POTTED_RED_GLOWSHROOM, "Potted Red Glowshroom");
		add(BlockInit.ORANGE_GLOWSHROOM, "Orange Glowshroom");
		add(BlockInit.POTTED_ORANGE_GLOWSHROOM, "Potted Orange Glowshroom");
		add(BlockInit.YELLOW_GLOWSHROOM, "Yellow Glowshroom");
		add(BlockInit.POTTED_YELLOW_GLOWSHROOM, "Potted Yellow Glowshroom");
		add(BlockInit.GREEN_GLOWSHROOM, "Green Glowshroom");
		add(BlockInit.POTTED_GREEN_GLOWSHROOM, "Potted Green Glowshroom");
		add(BlockInit.TURQUOISE_GLOWSHROOM, "Turquoise Glowshroom");
		add(BlockInit.POTTED_TURQUOISE_GLOWSHROOM, "Potted Turquoise Glowshroom");
		add(BlockInit.BLUE_GLOWSHROOM, "Blue Glowshroom");
		add(BlockInit.POTTED_BLUE_GLOWSHROOM, "Potted Blue Glowshroom");

		// Tooltips
		addTooltip("ldpe", "Low Density Polyethylene");
		addTooltip("hdpe", "High Density Polyethylene");
		addTooltip("uhmwpe", "Ultra High Molecular Weight Polyethylene");
		addTooltip("fluid_filter.empty", "Empty");
		addTooltip("item_filter.empty", "Empty");
		addTooltip("item_filter.configured", "Configured");

		// Fluids
		add(FluidInit.OXYGEN, "Oxygen");
		add(FluidInit.NITROGEN, "Nitrogen");
		add(FluidInit.AMMONIA, "Ammonia");
		add(FluidInit.CARBON_DIOXIDE, "Carbon Dioxide");
		add(FluidInit.CARBON_DISULPHIDE, "Carbon Disulphide");
		add(FluidInit.HYDROGEN, "Hydrogen");
		add(FluidInit.METHANE, "Methane");
		add(FluidInit.ETHANE, "Ethane");
		add(FluidInit.ETHYLENE, "Ethylene");
		add(FluidInit.CHLORINE, "Chlorine");
		add(FluidInit.BORON_TRIFLUORIDE, "Boron Trifluoride");
		add(FluidInit.FORMALDEHYDE, "Formaldehyde");
		add(FluidInit.NITROGEN_DIOXIDE, "Nitrogen Dioxide");
		add(FluidInit.SULPHUR_DIOXIDE, "Sulphur Dioxide");
		add(FluidInit.HYDROGEN_BROMIDE, "Hydrogen Bromide");
		add(FluidInit.HYDROGEN_SULPHIDE, "Hydrogen Sulphide");
		add(FluidInit.CARBON_MONOXIDE, "Carbon Monoxide");
		add(FluidInit.ARGON, "Argon");

		add(FluidInit.MOLTEN_IRON, "Molten Iron");
		add(FluidInit.MOLTEN_GOLD, "Molten Gold");
		add(FluidInit.MOLTEN_COPPER, "Molten Copper");
		add(FluidInit.MOLTEN_ALUMINUM, "Molten Aluminum");
		add(FluidInit.MOLTEN_NICKEL, "Molten Nickel");
		add(FluidInit.MOLTEN_LEAD, "Molten Lead");
		add(FluidInit.MOLTEN_BORON, "Molten Boron");
		add(FluidInit.MOLTEN_PALLADIUM, "Molten Palladium");
		add(FluidInit.MOLTEN_SILVER, "Molten Silver");
		add(FluidInit.MOLTEN_STEEL, "Molten Steel");
		add(FluidInit.MOLTEN_CONSTANTAN, "Molten Constantan");
		add(FluidInit.MOLTEN_BISMUTH, "Molten Bismuth");
		add(FluidInit.MOLTEN_TIN, "Molten Tin");
		add(FluidInit.MOLTEN_ZINC, "Molten Zinc");
		add(FluidInit.MOLTEN_LOW_GRADE_TITANIUM, "Molten Low-Grade Titanium");
		add(FluidInit.MOLTEN_MAGNETITE, "Molten Magnetite");
		add(FluidInit.MOLTEN_GYPSUM, "Molten Gypsum");
		add(FluidInit.MOLTEN_ILMENITE, "Molten Ilmenite");
		add(FluidInit.MOLTEN_PLATINUM, "Molten Platinum");
		add(FluidInit.MOLTEN_IRIDIUM, "Molten Iridium");
		add(FluidInit.MOLTEN_OSMIUM, "Molten Osmium");
		add(FluidInit.MOLTEN_COBALT, "Molten Cobalt");
		add(FluidInit.MOLTEN_URANINITE, "Molten Uraninite");
		add(FluidInit.MOLTEN_THORIUM, "Molten Thorium");

		add(FluidInit.ACETIC_ACID, "Acetic Acid");
		add(FluidInit.BRINE, "Brine");
		add(FluidInit.HYDROCHLORIC_ACID, "Hydrochloric Acid");
		add(FluidInit.SULPHURIC_ACID, "Sulphuric Acid");
		add(FluidInit.BROMINE, "Bromine");
		add(FluidInit.BENZENE, "Benzene");
		add(FluidInit.TOLUENE, "Toluene");
		add(FluidInit.METHANOL, "Methanol");
		add(FluidInit.ETHANOL, "Ethanol");
		add(FluidInit.HYDROGEN_FLUORIDE, "Hydrogen Fluoride");
		add(FluidInit.HELIUM, "Helium");
		add(FluidInit.ACETALDEHYDE, "Acetaldehyde");
		add(FluidInit.BENZYL_CHLORIDE, "Benzyl Chloride");
		add(FluidInit.NITRIC_ACID, "Nitric Acid");
		add(FluidInit.BROMOBENZENE, "Bromobenzene");
		add(FluidInit.GLYOXAL, "Glyoxal");
		add(FluidInit.BENZYLAMINE, "Benzylamine");
		add(FluidInit.HNIW, "HNIW (CL-20)");
		add(FluidInit.HEXOGEN, "Hexogen (RDX)");
		add(FluidInit.NITROMETHANE, "Nitromethane");
		add(FluidInit.SULPHUR_TRIOXIDE, "Sulphur Trioxide");
		add(FluidInit.LEAD_BISMUTH_EUTECTIC, "Lead Bismuth Eutectic");

		// Recipes
		add(RecipeInit.COMPRESSOR, "Compressing");
		add(RecipeInit.COMPOSTER_VAT, "Composting");
		add(RecipeInit.GRINDER, "Grinding");
		add(RecipeInit.MELTER, "Melting");
		add(RecipeInit.SOLIDIFIER, "Solidifying");
		add(RecipeInit.REACTION_CHAMBER, "Reacting");
		add(RecipeInit.SAWMILL, "Sawing");
		add(RecipeInit.ELECTROLYZER, "Electrolyzing");
		add(RecipeInit.ROCKET_PART_BENCH, "Rocket Part Bench");

		// Rocket Parts
		addPart(RocketPartInit.SIMPLE_CHASSIS, "Simple Chassis");
		addPart(RocketPartInit.ADVANCED_CHASSIS, "Advanced Chassis");

		addPart(RocketPartInit.SIMPLE_FUEL_TANK, "Simple Fuel Tank");
		addPart(RocketPartInit.PRESSURIZED_FUEL_TANK, "Pressurized Fuel Tank");

		addPart(RocketPartInit.SIMPLE_LIFE_SUPPORT, "Simple Life Support");
		addPart(RocketPartInit.REINFORCED_LIFE_SUPPORT, "Reinforced Life Support");

		addPart(RocketPartInit.SIMPLE_SHIELD, "Simple Shield");
		addPart(RocketPartInit.BREAKER_SHIELD, "Breaker Shield");

		addPart(RocketPartInit.SIMPLE_THRUSTER, "Simple Thruster");
		addPart(RocketPartInit.TRI_TALL_THRUSTER, "Tri Tall Thruster");

		// Entities
		add(EntityTypeInit.ROCKET, "Rocket");

		// Misc
		add(Mode.WHITELIST, "Whitelist");
		add(Mode.BLACKLIST, "Blacklist");

		add(ConnectionSide.INPUT, "Input");
		add(ConnectionSide.OUTPUT, "Output");
		add(ConnectionSide.NORMAL, "Connected");
		add(ConnectionSide.NONE, "Disconnected");

		add(RocketPartType.CHASSIS, "Chassis");
		add(RocketPartType.FUEL_TANK, "Fuel Tank");
		add(RocketPartType.LIFE_SUPPORT, "Life Support");
		add(RocketPartType.SHIELD, "Shielding");
		add(RocketPartType.THRUSTER, "Thruster");

		addMisc("none_fluid", "None");
		addMisc("yes", "Yes");
		addMisc("no", "No");

		// UI
		addUI("none", "");

		addUI("jei.input", "Input");
		addUI("jei.output", "Output");

		addUI("config.energy", "Energy Config");
		addUI("config.item", "Item Config");
		addUI("config.fluid", "Fluid Config");

		addUI("dir.north", "North");
		addUI("dir.south", "South");
		addUI("dir.east", "East");
		addUI("dir.west", "West");
		addUI("dir.up", "Up");
		addUI("dir.down", "Down");

		addUI("side.input", "Input");
		addUI("side.output", "Output");
		addUI("side.none", "None");

		addUI("fluid_pipe.filter", "Filter");
		addUI("fluid_pipe.insert", "Insert a Fluid");
		addUI("item_conduit.filter", "Filter");
		addUI("item_conduit.insert", "Insert an Item");

		addUI("fluid_filter.insert", "Insert a Fluid");
		addUI("fluid_filter.for", " for");
		addUI("item_filter.insert", "Insert a Item");
		addUI("item_filter.for", " for");
		addUI("item_filter.for_colon", " for:");

		addUI("tank.input", "Input Fluid");
		addUI("tank.output", "Output Fluid");
		addUI("tank.empty", "Tank Empty");

		addUI("rocket_refueling_station.input", "Input Fluid");
		addUI("rocket_refueling_station.output", "Output Fluid");
		addUI("rocket_refueling_station.no_rockets", "No Rockets In Range");
		addUI("rocket_refueling_station.scan_hint", "Place this station within 5 blocks of a rocket");
		addUI("rocket_refueling_station.mode_fuel", "Mode: Fuel");
		addUI("rocket_refueling_station.mode_coolant", "Mode: Coolant");
		addUI("rocket_refueling_station.switch_to_fuel", "Switch To Fuel");
		addUI("rocket_refueling_station.switch_to_coolant", "Switch To Coolant");

		addUI("battery.missing", "Insert Capacitor");
		addUI("battery.input", "Input Energy");
		addUI("battery.output", "Output Energy");
		addUI("battery.capacitor", "Capacitor");

		addUI("furnace_generator.input", "Fuel");
		addUI("furnace_generator.progress", "Progress");
		addUI("furnace_generator.no_input", "Insert Fuel");
		addUI("furnace_generator.generating", "Generating");

		addUI("chemical_generator.progress", "Burning Gas");
		addUI("chemical_generator.no_space", "No Output Space");
		addUI("chemical_generator.no_input", "Insert Fuel");
		addUI("chemical_generator.generating", "Generating");

		addUI("electric_smelter.no_input", "No Recipe Found");
		addUI("electric_smelter.no_power", "No Power");
		addUI("electric_smelter.no_space", "No Output Space");
		addUI("electric_smelter.progress", "Progress");
		addUI("electric_smelter.usage", "Using");
		addUI("electric_smelter.requires", "Requires");
		addUI("electric_smelter.input", "Input");
		addUI("electric_smelter.output", "Output");

		addUI("grinder.no_input", "No Recipe Found");
		addUI("grinder.no_power", "No Power");
		addUI("grinder.no_space", "No Output Space");
		addUI("grinder.progress", "Progress");
		addUI("grinder.usage", "Using");
		addUI("grinder.requires", "Requires");
		addUI("grinder.input", "Input");
		addUI("grinder.output", "Output");

		addUI("sawmill.no_input", "No Recipe Found");
		addUI("sawmill.no_power", "No Power");
		addUI("sawmill.no_space", "No Output Space");
		addUI("sawmill.progress", "Progress");
		addUI("sawmill.usage", "Using");
		addUI("sawmill.requires", "Requires");
		addUI("sawmill.input", "Input");
		addUI("sawmill.output", "Output");

		addUI("compressor.no_input", "No Recipe Found");
		addUI("compressor.no_power", "No Power");
		addUI("compressor.no_space", "No Output Space");
		addUI("compressor.progress", "Progress");
		addUI("compressor.usage", "Using");
		addUI("compressor.requires", "Requires");
		addUI("compressor.input", "Input");
		addUI("compressor.output", "Output");
		addUI("compressor.mould", "Insert a Mould");

		addUI("reaction_chamber.no_input", "No Recipe Found");
		addUI("reaction_chamber.no_power", "No Power");
		addUI("reaction_chamber.no_space", "No Output Space");
		addUI("reaction_chamber.progress", "Next Item");
		addUI("reaction_chamber.usage", "Using");
		addUI("reaction_chamber.requires", "Requires");

		addUI("melter.no_input", "No Recipe Found");
		addUI("melter.no_power", "No Power");
		addUI("melter.no_space", "No Output Space");
		addUI("melter.progress", "Progress");
		addUI("melter.usage", "Using");
		addUI("melter.requires", "Requires");
		addUI("melter.input", "Input");
		addUI("melter.output", "Output");

		addUI("solidifier.no_input", "No Recipe Found");
		addUI("solidifier.no_power", "No Power");
		addUI("solidifier.no_space", "No Output Space");
		addUI("solidifier.progress", "Progress");
		addUI("solidifier.usage", "Using");
		addUI("solidifier.requires", "Requires");
		addUI("solidifier.input", "Input");
		addUI("solidifier.output", "Output");

		addUI("composter_vat.no_input", "Awaiting Compostable Item");
		addUI("composter_vat.no_power", "No Power");
		addUI("composter_vat.no_space", "No Output Space");
		addUI("composter_vat.progress", "Progress");
		addUI("composter_vat.usage", "Using");
		addUI("composter_vat.requires", "Requires");
		addUI("composter_vat.input", "Input");

		addUI("electrolyzer.no_input", "No Recipe Found");
		addUI("electrolyzer.no_power", "No Power");
		addUI("electrolyzer.no_space", "No Output Space");
		addUI("electrolyzer.progress", "Next Item");
		addUI("electrolyzer.usage", "Using");
		addUI("electrolyzer.requires", "Requires");

		addUI("electric_pump.scanning", "Scanning for fluids below");
		addUI("electric_pump.no_power", "No Power");
		addUI("electric_pump.full", "Tank Buffer Full");

		addUI("atmospheric_separator.no_power", "No Power");
		addUI("atmospheric_separator.separating", "Separating Atmosphere");

		addUI("rocket_part_bench.mass", "Mass");
		addUI("rocket_part_bench.fuel_type", "Fuel");
		addUI("rocket_part_bench.coolant_type", "Coolant");
		addUI("rocket_part_bench.fuel_capacity", "Fuel Cap");
		addUI("rocket_part_bench.coolant_capacity", "Coolant Cap");
		addUI("rocket_part_bench.efficiency", "Efficiency");
		addUI("rocket_part_bench.storage", "Storage Slots");
		addUI("rocket_part_bench.max_pressure", "Max Pressure");
		addUI("rocket_part_bench.craft", "Craft");
		addUI("rocket_part_bench.requires", "Requires");
		addUI("rocket_part_bench.unavailable", "(Unavailable)");
		addUI("rocket_part_bench.progress", "Crafting");
		addUI("rocket_part_bench.no_power", "No Power");

		addUI("rocket_assembly_station.mass", "Mass");
		addUI("rocket_assembly_station.fuel_type", "Fuel");
		addUI("rocket_assembly_station.coolant_type", "Coolant");
		addUI("rocket_assembly_station.fuel_capacity", "Fuel Cap");
		addUI("rocket_assembly_station.coolant_capacity", "Coolant Cap");
		addUI("rocket_assembly_station.fuel_efficiency", "Fuel Eff");
		addUI("rocket_assembly_station.coolant_efficiency", "Coolant Eff");
		addUI("rocket_assembly_station.storage", "Storage Slots");
		addUI("rocket_assembly_station.max_pressure", "Max Pressure");
		addUI("rocket_assembly_station.craft", "Craft");
		addUI("rocket_assembly_station.unavailable", "(Unavailable)");
		addUI("rocket_assembly_station.progress", "Crafting");
		addUI("rocket_assembly_station.no_power", "No Power");
		addUI("rocket_assembly_station.chassis", "Chassis");
		addUI("rocket_assembly_station.fuel_tank", "Fuel Tank");
		addUI("rocket_assembly_station.life_support", "Life Support");
		addUI("rocket_assembly_station.shield", "Shields");
		addUI("rocket_assembly_station.thruster", "Thrusters");

		addUI("rocket.tab.info", "Info");
		addUI("rocket.info.mass", "Mass");
		addUI("rocket.info.fuel_type", "Fuel");
		addUI("rocket.info.coolant_type", "Coolant");
		addUI("rocket.info.fuel_capacity", "Fuel Cap");
		addUI("rocket.info.coolant_capacity", "Coolant Cap");
		addUI("rocket.info.fuel_efficiency", "Fuel Eff");
		addUI("rocket.info.coolant_efficiency", "Coolant Eff");
		addUI("rocket.info.storage", "Storage Slots");
		addUI("rocket.info.max_pressure", "Max Pressure");

		addUI("rocket.tab.fueling", "Fueling");
		addUI("rocket.fueling.too_far", "Destination Too Far");
		addUI("rocket.fueling.ready", "READY FOR LAUNCH");
		addUI("rocket.fueling.insufficient", "INSUFFICIENT FUEL");
		addUI("rocket.fueling.fuel_stored", "Fuel Stored");
		addUI("rocket.fueling.fuel_capacity", "Fuel Capacity");
		addUI("rocket.fueling.fuel_required", "Fuel Required");
		addUI("rocket.fueling.cool_stored", "Coolant Stored");
		addUI("rocket.fueling.cool_capacity", "Coolant Capacity");
		addUI("rocket.fueling.cool_required", "Coolant Required");

		addUI("rocket.tab.storage", "Storage");
		addUI("rocket.storage.soon", "Cargo Storage");

		addUI("rocket.tab.destination", "Destination");
		addUI("rocket.destination.invalid", "NO DESTINATION SELECTED");
		addUI("rocket.destination.hint", "Select a planet in the Starmap tab");
		addUI("rocket.destination.clear", "Clear Destination");
		addUI("rocket.destination.destination", "Destination");
		addUI("rocket.destination.distance", "Distance");
		addUI("rocket.destination.fuel", "Fuel Cost");
		addUI("rocket.destination.coolant", "Coolant Cost");
		addUI("rocket.destination.max_temperature", "Max Temperature");
		addUI("rocket.destination.max_pressure", "Max Pressure");
		addUI("rocket.destination.launch", "LAUNCH");
		addUI("rocket.destination.out_of_range", "Destination out of range");
		addUI("rocket.destination.insufficient_fuel", "Insufficient fuel or coolant");

		addUI("rocket.tab.starmap", "Starmap");
		addUI("rocket.starmap.pan", "Pan");
		addUI("rocket.starmap.rotate", "Rotate");
		addUI("rocket.starmap.zoom", "Zoom");
		addUI("rocket.starmap.planet_type", "Planet Type");
		addUI("rocket.starmap.day_length", "Day Length");
		addUI("rocket.starmap.temperature", "Temperature");
		addUI("rocket.starmap.gravity", "Gravity");
		addUI("rocket.starmap.breathable_atmosphere", "Breathable Atmosphere");
		addUI("rocket.starmap.gas_giant", "Gas Giant");
		addUI("rocket.starmap.surface_fluid", "Surface Fluid");

		addUI("meteo_probe", "Meteo Probe");
		addUI("meteo_probe.no_data", "No planetary weather data available.");
		addUI("meteo_probe.tab.summary", "Summary");
		addUI("meteo_probe.tab.time", "Time");
		addUI("meteo_probe.tab.wind", "Wind");
		addUI("meteo_probe.tab.temperature", "Temp");

		addUI("meteo_probe.summary.time", "Time");
		addUI("meteo_probe.summary.daynight", "Day/Night");
		addUI("meteo_probe.summary.season", "Season");
		addUI("meteo_probe.summary.wind", "Wind");
		addUI("meteo_probe.summary.temperature", "Temperature");
		addUI("meteo_probe.summary.today_range", "Today Range");
		addUI("meteo_probe.summary.year_range", "Year Range");

		addUI("meteo_probe.time.clock", "Clock");
		addUI("meteo_probe.time.phase", "Phase");
		addUI("meteo_probe.time.day", "Day");
		addUI("meteo_probe.time.night", "Night");
		addUI("meteo_probe.time.cycle", "Day/Night Cycle");
		addUI("meteo_probe.time.noon", "Noon");
		addUI("meteo_probe.time.sunset", "Sunset");
		addUI("meteo_probe.time.midnight", "Midnight");
		addUI("meteo_probe.time.season", "Season");

		addUI("meteo_probe.wind", "Wind");
		addUI("meteo_probe.wind.direction", "Direction");
		addUI("meteo_probe.wind.intensity", "Intensity");

		addUI("meteo_probe.weather.current", "Current");

		addUI("meteo_probe.weather.unknown.name", "Unknown");

		add("weather_event.machina.clear", "Clear");
		add("weather_event.machina.rain", "Rain");
		add("weather_event.machina.dust_storm", "Dust Storm");
		add("weather_event.machina.aurora", "Aurora");

		addUI("meteo_probe.temp.current", "Current");
		addUI("meteo_probe.temp.day_range", "Day Range");
		addUI("meteo_probe.temp.year_range", "Year Range");
		addUI("meteo_probe.temp.today", "Current Day");
		addUI("meteo_probe.temp.year", "Full Year");

		// Planets
		addTrait(PlanetTraitInit.ALWAYS_AURORA, "Permanent Aurora",
				"This planet has an aurora permanently in the night sky.");
		addTrait(PlanetTraitInit.ALWAYS_RAINING, "Permanent Rain",
				"There is permanent rainfall on this planet's surface.");
		addTrait(PlanetTraitInit.ALWAYS_DUST_STORM, "Permanent Dust Storm",
				"The is a permanent dust storm on this planet's surface.");

		addTrait(PlanetTraitInit.RICH_COAL, "Coal Rich", "This planet is abundant in coal deposits.");
		addTrait(PlanetTraitInit.RICH_IRON, "Iron Rich", "This planet is abundant in iron deposits.");
		addTrait(PlanetTraitInit.RICH_COPPER, "Copper Rich", "This planet is abundant in copper deposits.");
		addTrait(PlanetTraitInit.RICH_GOLD, "Gold Rich", "This planet is abundant in gold deposits.");
		addTrait(PlanetTraitInit.RICH_REDSTONE, "Redstone Rich", "This planet is abundant in redstone deposits.");
		addTrait(PlanetTraitInit.RICH_QUARTZ, "Quartz Rich", "This planet is abundant in quartz deposits.");
		addTrait(PlanetTraitInit.RICH_EMERALD, "Emerald Rich", "This planet is abundant in emerald deposits.");
		addTrait(PlanetTraitInit.RICH_LAPIS, "Lapis Rich", "This planet is abundant in lapis deposits.");
		addTrait(PlanetTraitInit.RICH_DIAMOND, "Diamond Rich", "This planet is abundant in diamond deposits.");
		addTrait(PlanetTraitInit.RICH_ALUMINUM, "Aluminum Rich", "This planet is abundant in aluminum deposits.");
		addTrait(PlanetTraitInit.RICH_NICKEL, "Nickel Rich", "This planet is abundant in nickel deposits.");
		addTrait(PlanetTraitInit.RICH_TIN, "Tin Rich", "This planet is abundant in tin deposits.");
		addTrait(PlanetTraitInit.RICH_ZINC, "Zinc Rich", "This planet is abundant in zinc deposits.");
		addTrait(PlanetTraitInit.RICH_LOW_GRADE_TITANIUM, "Titanium Rich",
				"This planet is abundant in titanium deposits.");
		addTrait(PlanetTraitInit.RICH_LEAD, "Lead Rich", "This planet is abundant in lead deposits.");
		addTrait(PlanetTraitInit.RICH_BORON, "Boron Rich", "This planet is abundant in boron deposits.");
		addTrait(PlanetTraitInit.RICH_PALLADIUM, "Palladium Rich", "This planet is abundant in palladium deposits.");
		addTrait(PlanetTraitInit.RICH_SILVER, "Silver Rich", "This planet is abundant in silver deposits.");
		addTrait(PlanetTraitInit.RICH_FLUORITE, "Fluorite Rich", "This planet is abundant in fluorite deposits.");
		addTrait(PlanetTraitInit.RICH_SALTPETER, "Niter Rich", "This planet is abundant in niter deposits.");
		addTrait(PlanetTraitInit.RICH_PYRITE, "Sulfur Rich", "This planet is abundant in sulfur deposits.");
		addTrait(PlanetTraitInit.RICH_BISMUTH, "Bismuth Rich", "This planet is abundant in bismuth deposits.");
		addTrait(PlanetTraitInit.RICH_MAGNETITE, "Magnetite Rich", "This planet is abundant in magnetite deposits.");
		addTrait(PlanetTraitInit.RICH_GYPSUM, "Gypsum Rich", "This planet is abundant in gypsum deposits.");
		addTrait(PlanetTraitInit.RICH_PERCHLORATE, "Perchlorate Rich",
				"This planet is abundant in perchlorate deposits.");
		addTrait(PlanetTraitInit.RICH_ILMENITE, "Ilmenite Rich", "This planet is abundant in ilmenite deposits.");
		addTrait(PlanetTraitInit.RICH_PLATINUM, "Platinum Rich", "This planet is abundant in platinum deposits.");
		addTrait(PlanetTraitInit.RICH_IRIDIUM, "Iridium Rich", "This planet is abundant in iridium deposits.");
		addTrait(PlanetTraitInit.RICH_OSMIUM, "Osmium Rich", "This planet is abundant in osmium deposits.");
		addTrait(PlanetTraitInit.RICH_COBALT, "Cobalt Rich", "This planet is abundant in cobalt deposits.");
		addTrait(PlanetTraitInit.RICH_URANINITE, "Uraninite Rich", "This planet is abundant in uraninite deposits.");
		addTrait(PlanetTraitInit.RICH_THORIUM, "Thorium Rich", "This planet is abundant in thorium deposits.");

		addMisc("planet_type.earthlike", "Earthlike");
		addMisc("planet_type.martian", "Martian");

		add("season.earthlike.spring", "Spring");
		add("season.earthlike.summer", "Summer");
		add("season.earthlike.autumn", "Autumn");
		add("season.earthlike.winter", "Winter");

		add("season.martian.spring", "Thaw");
		add("season.martian.summer", "Scorch");
		add("season.martian.autumn", "Haze");
		add("season.martian.winter", "Ash");
	}
}
