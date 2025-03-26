package com.machina.datagen.client.lang;

import com.machina.Machina;
import com.machina.api.cap.sided.ConnectionSide;
import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FruitInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TabInit;

import net.minecraft.data.PackOutput;

public class DatagenLangEnUs extends DatagenLang {

	public DatagenLangEnUs(PackOutput gen) {
		super(gen, "en_us", Machina.MOD_ID);
	}

	@Override
	protected void addTranslations() {
		// Creative Tabs
		add(TabInit.MACHINA_MACHINERY, "Machina: Machinery");
		add(TabInit.MACHINA_RESOURCES, "Machina: Resources");
		add(TabInit.MACHINA_WORLDGEN, "Machina: Worldgen");
		add(TabInit.MACHINA_MISCELLANEOUS, "Machina: Miscellaneous");

		// Items
		add(ItemInit.COPPER_NUGGET, "Copper Nugget");
		add(ItemInit.DIAMOND_NUGGET, "Diamond Nugget");

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
		add(ItemInit.DIAMOND_PLATE, "Diamond Plate");

		add(ItemInit.IRON_ROD, "Iron Rod");
		add(ItemInit.COPPER_ROD, "Copper Rod");
		add(ItemInit.GOLD_ROD, "Gold Rod");
		add(ItemInit.DIAMOND_ROD, "Diamond Rod");

		add(ItemInit.IRON_WIRE, "Iron Wire");
		add(ItemInit.COPPER_WIRE, "Copper Wire");
		add(ItemInit.GOLD_WIRE, "Gold Wire");
		add(ItemInit.DIAMOND_WIRE, "Diamond Wire");

		add(ItemInit.BLUEPRINT, "Blueprint");

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

		add(ItemInit.RAW_ALUMINUM, "Raw Aluminum");
		add(ItemInit.ALUMINUM_INGOT, "Aluminum Ingot");
		add(ItemInit.ALUMINUM_NUGGET, "Aluminum Nugget");
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
		add(BlockInit.ENERGY_CABLE, "Energy Cable");
		add(BlockInit.FLUID_PIPE, "Fluid Pipe");
		add(BlockInit.ITEM_CONDUIT, "Item Conduit");
		add(BlockInit.BASIC_MACHINE_CASE, "Basic Machine Case");
		add(BlockInit.BATTERY, "Battery");
		add(BlockInit.TANK, "Tank");
		add(BlockInit.CREATIVE_BATTERY, "Creative Battery");
		add(BlockInit.FURNACE_GENERATOR, "Furnace Generator");
		add(BlockInit.GRINDER, "Grinder");

		add(BlockInit.ALUMINUM_BLOCK, "Aluminum Block");
		add(BlockInit.ALUMINUM_ORE, "Aluminum Ore");

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
		add(FluidInit.MOLTEN_LEAD, "Molten Lead");
		add(FluidInit.MOLTEN_BISMUTH, "Molten Bismuth");
		add(FluidInit.LEAD_BISMUTH_EUTECTIC, "Lead Bismuth Eutectic");

		// Misc
		add(Mode.WHITELIST, "Whitelist");
		add(Mode.BLACKLIST, "Blacklist");

		add(ConnectionSide.INPUT, "Input");
		add(ConnectionSide.OUTPUT, "Output");
		add(ConnectionSide.NORMAL, "Connected");
		add(ConnectionSide.NONE, "Disconnected");

		// UI
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

		addUI("battery.missing", "Insert Capacitor");
		addUI("battery.input", "Input Energy");
		addUI("battery.output", "Output Energy");
		addUI("battery.capacitor", "Capacitor");

		addUI("furnace_generator.input", "Fuel");
		addUI("furnace_generator.progress", "Progress");
		addUI("furnace_generator.no_input", "Insert Fuel");
		addUI("furnace_generator.generating", "Generating");

		addUI("grinder.no_input", "No Recipe Found");
		addUI("grinder.no_power", "No Power");
		addUI("grinder.no_space", "No Output Space");
		addUI("grinder.progress", "Progress");
		addUI("grinder.usage", "Using");
		addUI("grinder.requires", "Requires");
		addUI("grinder.input", "Input");
		addUI("grinder.output", "Output");
	}
}