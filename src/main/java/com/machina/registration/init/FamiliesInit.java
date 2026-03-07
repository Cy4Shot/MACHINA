package com.machina.registration.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.machina.Machina;
import com.machina.registration.init.BlockInit.MachinaOre;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class FamiliesInit {

	public static final List<OreFamily> ORES = new ArrayList<>();
	public static final List<DirtFamily> DIRTS = new ArrayList<>();
	public static final List<WoodFamily> WOODS = new ArrayList<>();
	public static final List<StoneFamily> STONES = new ArrayList<>();
	public static final List<WoodFamily> WOODS_VANILLA = new ArrayList<>();

	static {
		// Minecraft Ores
		ORES.add(
				OreFamily.gemLike("coal", BlockInit.COAL_ORE, Blocks.COAL_BLOCK, Items.COAL, ItemInit.COAL_DUST.get()));
		ORES.add(OreFamily.gemLike("lapis", BlockInit.LAPIS_ORE, Blocks.LAPIS_BLOCK, Items.LAPIS_LAZULI,
				ItemInit.LAPIS_DUST.get()));
		ORES.add(OreFamily.dustLike("redstone", BlockInit.REDSTONE_ORE, Blocks.REDSTONE_BLOCK, Items.REDSTONE));
		ORES.add(OreFamily.gemLike("quartz", BlockInit.NETHER_QUARTZ_ORE, Blocks.QUARTZ_BLOCK, Items.QUARTZ,
				ItemInit.QUARTZ_DUST.get()));
		ORES.add(OreFamily.gemLike("emerald", BlockInit.EMERALD_ORE, Blocks.EMERALD_BLOCK, Items.EMERALD,
				ItemInit.EMERALD_DUST.get()));
		ORES.add(OreFamily.gemLike("diamond", BlockInit.DIAMOND_ORE, Blocks.DIAMOND_BLOCK, Items.DIAMOND,
				ItemInit.DIAMOND_DUST.get()));
		ORES.add(OreFamily.ingotLike("iron", BlockInit.IRON_ORE, Blocks.IRON_BLOCK, Items.IRON_NUGGET, Items.IRON_INGOT,
				ItemInit.IRON_DUST.get(), ItemInit.IRON_PLATE.get(), ItemInit.IRON_ROD.get(), ItemInit.IRON_WIRE.get(),
				Items.RAW_IRON, Blocks.RAW_IRON_BLOCK));
		ORES.add(OreFamily.ingotLike("gold", BlockInit.GOLD_ORE, Blocks.GOLD_BLOCK, Items.GOLD_NUGGET, Items.GOLD_INGOT,
				ItemInit.GOLD_DUST.get(), ItemInit.GOLD_PLATE.get(), ItemInit.GOLD_ROD.get(), ItemInit.GOLD_WIRE.get(),
				Items.RAW_GOLD, Blocks.RAW_GOLD_BLOCK));
		ORES.add(OreFamily.ingotLike("copper", BlockInit.COPPER_ORE, Blocks.COPPER_BLOCK, ItemInit.COPPER_NUGGET.get(),
				Items.COPPER_INGOT, ItemInit.COPPER_DUST.get(), ItemInit.COPPER_PLATE.get(), ItemInit.COPPER_ROD.get(),
				ItemInit.COPPER_WIRE.get(), Items.RAW_COPPER, Blocks.RAW_COPPER_BLOCK));

		// Machina Ores
		ORES.add(OreFamily.ingotLike("aluminum", BlockInit.ALUMINUM_ORE, BlockInit.ALUMINUM_BLOCK.get(),
				ItemInit.ALUMINUM_NUGGET.get(), ItemInit.ALUMINUM_INGOT.get(), ItemInit.ALUMINUM_DUST.get(),
				ItemInit.ALUMINUM_PLATE.get(), ItemInit.ALUMINUM_ROD.get(), ItemInit.ALUMINUM_WIRE.get(),
				ItemInit.RAW_ALUMINUM.get(), BlockInit.RAW_ALUMINUM_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("nickel", BlockInit.NICKEL_ORE, BlockInit.NICKEL_BLOCK.get(),
				ItemInit.NICKEL_NUGGET.get(), ItemInit.NICKEL_INGOT.get(), ItemInit.NICKEL_DUST.get(),
				ItemInit.NICKEL_PLATE.get(), ItemInit.NICKEL_ROD.get(), ItemInit.NICKEL_WIRE.get(),
				ItemInit.RAW_NICKEL.get(), BlockInit.RAW_NICKEL_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("lead", BlockInit.LEAD_ORE, BlockInit.LEAD_BLOCK.get(), ItemInit.LEAD_NUGGET.get(),
				ItemInit.LEAD_INGOT.get(), ItemInit.LEAD_DUST.get(), ItemInit.LEAD_PLATE.get(), ItemInit.LEAD_ROD.get(),
				ItemInit.LEAD_WIRE.get(), ItemInit.RAW_LEAD.get(), BlockInit.RAW_LEAD_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("boron", BlockInit.BORON_ORE, BlockInit.BORON_BLOCK.get(),
				ItemInit.BORON_NUGGET.get(), ItemInit.BORON_INGOT.get(), ItemInit.BORON_DUST.get(),
				ItemInit.BORON_PLATE.get(), ItemInit.BORON_ROD.get(), ItemInit.BORON_WIRE.get(),
				ItemInit.RAW_BORON.get(), BlockInit.RAW_BORON_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("palladium", BlockInit.PALLADIUM_ORE, BlockInit.PALLADIUM_BLOCK.get(),
				ItemInit.PALLADIUM_NUGGET.get(), ItemInit.PALLADIUM_INGOT.get(), ItemInit.PALLADIUM_DUST.get(),
				ItemInit.PALLADIUM_PLATE.get(), ItemInit.PALLADIUM_ROD.get(), ItemInit.PALLADIUM_WIRE.get(),
				ItemInit.RAW_PALLADIUM.get(), BlockInit.RAW_PALLADIUM_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("silver", BlockInit.SILVER_ORE, BlockInit.SILVER_BLOCK.get(),
				ItemInit.SILVER_NUGGET.get(), ItemInit.SILVER_INGOT.get(), ItemInit.SILVER_DUST.get(),
				ItemInit.SILVER_PLATE.get(), ItemInit.SILVER_ROD.get(), ItemInit.SILVER_WIRE.get(),
				ItemInit.RAW_SILVER.get(), BlockInit.RAW_SILVER_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("tin", BlockInit.TIN_ORE, BlockInit.TIN_BLOCK.get(), ItemInit.TIN_NUGGET.get(),
				ItemInit.TIN_INGOT.get(), ItemInit.TIN_DUST.get(), ItemInit.TIN_PLATE.get(), ItemInit.TIN_ROD.get(),
				ItemInit.TIN_WIRE.get(), ItemInit.RAW_TIN.get(), BlockInit.RAW_TIN_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("zinc", BlockInit.ZINC_ORE, BlockInit.ZINC_BLOCK.get(), ItemInit.ZINC_NUGGET.get(),
				ItemInit.ZINC_INGOT.get(), ItemInit.ZINC_DUST.get(), ItemInit.ZINC_PLATE.get(), ItemInit.ZINC_ROD.get(),
				ItemInit.ZINC_WIRE.get(), ItemInit.RAW_ZINC.get(), BlockInit.RAW_ZINC_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("low_grade_titanium", BlockInit.LOW_GRADE_TITANIUM_ORE,
				BlockInit.LOW_GRADE_TITANIUM_BLOCK.get(), ItemInit.LOW_GRADE_TITANIUM_NUGGET.get(),
				ItemInit.LOW_GRADE_TITANIUM_INGOT.get(), ItemInit.LOW_GRADE_TITANIUM_DUST.get(),
				ItemInit.LOW_GRADE_TITANIUM_PLATE.get(), ItemInit.LOW_GRADE_TITANIUM_ROD.get(),
				ItemInit.LOW_GRADE_TITANIUM_WIRE.get(), ItemInit.RAW_LOW_GRADE_TITANIUM.get(),
				BlockInit.RAW_LOW_GRADE_TITANIUM_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("magnetite", BlockInit.MAGNETITE_ORE, BlockInit.MAGNETITE_BLOCK.get(),
				ItemInit.MAGNETITE_NUGGET.get(), ItemInit.MAGNETITE_INGOT.get(), ItemInit.MAGNETITE_DUST.get(),
				ItemInit.MAGNETITE_PLATE.get(), ItemInit.MAGNETITE_ROD.get(), ItemInit.MAGNETITE_WIRE.get(),
				ItemInit.RAW_MAGNETITE.get(), BlockInit.RAW_MAGNETITE_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("gypsum", BlockInit.GYPSUM_ORE, BlockInit.GYPSUM_BLOCK.get(),
				ItemInit.GYPSUM_NUGGET.get(), ItemInit.GYPSUM_INGOT.get(), ItemInit.GYPSUM_DUST.get(),
				ItemInit.GYPSUM_PLATE.get(), ItemInit.GYPSUM_ROD.get(), ItemInit.GYPSUM_WIRE.get(),
				ItemInit.RAW_GYPSUM.get(), BlockInit.RAW_GYPSUM_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("ilmenite", BlockInit.ILMENITE_ORE, BlockInit.ILMENITE_BLOCK.get(),
				ItemInit.ILMENITE_NUGGET.get(), ItemInit.ILMENITE_INGOT.get(), ItemInit.ILMENITE_DUST.get(),
				ItemInit.ILMENITE_PLATE.get(), ItemInit.ILMENITE_ROD.get(), ItemInit.ILMENITE_WIRE.get(),
				ItemInit.RAW_ILMENITE.get(), BlockInit.RAW_ILMENITE_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("platinum", BlockInit.PLATINUM_ORE, BlockInit.PLATINUM_BLOCK.get(),
				ItemInit.PLATINUM_NUGGET.get(), ItemInit.PLATINUM_INGOT.get(), ItemInit.PLATINUM_DUST.get(),
				ItemInit.PLATINUM_PLATE.get(), ItemInit.PLATINUM_ROD.get(), ItemInit.PLATINUM_WIRE.get(),
				ItemInit.RAW_PLATINUM.get(), BlockInit.RAW_PLATINUM_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("iridium", BlockInit.IRIDIUM_ORE, BlockInit.IRIDIUM_BLOCK.get(),
				ItemInit.IRIDIUM_NUGGET.get(), ItemInit.IRIDIUM_INGOT.get(), ItemInit.IRIDIUM_DUST.get(),
				ItemInit.IRIDIUM_PLATE.get(), ItemInit.IRIDIUM_ROD.get(), ItemInit.IRIDIUM_WIRE.get(),
				ItemInit.RAW_IRIDIUM.get(), BlockInit.RAW_IRIDIUM_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("osmium", BlockInit.OSMIUM_ORE, BlockInit.OSMIUM_BLOCK.get(),
				ItemInit.OSMIUM_NUGGET.get(), ItemInit.OSMIUM_INGOT.get(), ItemInit.OSMIUM_DUST.get(),
				ItemInit.OSMIUM_PLATE.get(), ItemInit.OSMIUM_ROD.get(), ItemInit.OSMIUM_WIRE.get(),
				ItemInit.RAW_OSMIUM.get(), BlockInit.RAW_OSMIUM_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("cobalt", BlockInit.COBALT_ORE, BlockInit.COBALT_BLOCK.get(),
				ItemInit.COBALT_NUGGET.get(), ItemInit.COBALT_INGOT.get(), ItemInit.COBALT_DUST.get(),
				ItemInit.COBALT_PLATE.get(), ItemInit.COBALT_ROD.get(), ItemInit.COBALT_WIRE.get(),
				ItemInit.RAW_COBALT.get(), BlockInit.RAW_COBALT_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("uraninite", BlockInit.URANINITE_ORE, BlockInit.URANINITE_BLOCK.get(),
				ItemInit.URANINITE_NUGGET.get(), ItemInit.URANINITE_INGOT.get(), ItemInit.URANINITE_DUST.get(),
				ItemInit.URANINITE_PLATE.get(), ItemInit.URANINITE_ROD.get(), ItemInit.URANINITE_WIRE.get(),
				ItemInit.RAW_URANINITE.get(), BlockInit.RAW_URANINITE_BLOCK.get()));
		ORES.add(OreFamily.ingotLike("thorium", BlockInit.THORIUM_ORE, BlockInit.THORIUM_BLOCK.get(),
				ItemInit.THORIUM_NUGGET.get(), ItemInit.THORIUM_INGOT.get(), ItemInit.THORIUM_DUST.get(),
				ItemInit.THORIUM_PLATE.get(), ItemInit.THORIUM_ROD.get(), ItemInit.THORIUM_WIRE.get(),
				ItemInit.RAW_THORIUM.get(), BlockInit.RAW_THORIUM_BLOCK.get()));
		ORES.add(OreFamily.alloyLike("steel", BlockInit.STEEL_BLOCK.get(), ItemInit.STEEL_NUGGET.get(),
				ItemInit.STEEL_INGOT.get(), ItemInit.STEEL_DUST.get(), ItemInit.STEEL_PLATE.get(),
				ItemInit.STEEL_ROD.get(), ItemInit.STEEL_WIRE.get()));
		ORES.add(OreFamily.alloyLike("constantan", BlockInit.CONSTANTAN_BLOCK.get(), ItemInit.CONSTANTAN_NUGGET.get(),
				ItemInit.CONSTANTAN_INGOT.get(), ItemInit.CONSTANTAN_DUST.get(), ItemInit.CONSTANTAN_PLATE.get(),
				ItemInit.CONSTANTAN_ROD.get(), ItemInit.CONSTANTAN_WIRE.get()));
		ORES.add(OreFamily.resLike("fluorite", BlockInit.FLUORITE_ORE, ItemInit.FLUORITE.get(),
				ItemInit.FLUORITE_DUST.get()));
		ORES.add(OreFamily.resLike("niter", BlockInit.SALTPETER_ORE, ItemInit.NITER.get(), ItemInit.NITER_DUST.get()));
		ORES.add(OreFamily.resLike("sulfur", BlockInit.PYRITE_ORE, ItemInit.SULFUR.get(), ItemInit.SULFUR_DUST.get()));
		ORES.add(OreFamily.resLike("bismuth", BlockInit.BISMUTH_ORE, ItemInit.BISMUTH.get(),
				ItemInit.BISMUTH_DUST.get()));
		ORES.add(OreFamily.oreLike("perchlorate", BlockInit.PERCHLORATE_ORE, ItemInit.PERCHLORATE.get()));

		// Dirts
		DIRTS.add(new DirtFamily("tropical", BlockInit.TROPICAL_DIRT.get(), BlockInit.TROPICAL_DIRT_STAIRS.get(),
				BlockInit.TROPICAL_DIRT_SLAB.get(), Optional.of(BlockInit.TROPICAL_GRASS_BLOCK.get())));
		DIRTS.add(new DirtFamily("forest", BlockInit.FOREST_DIRT.get(), BlockInit.FOREST_DIRT_STAIRS.get(),
				BlockInit.FOREST_DIRT_SLAB.get(), Optional.of(BlockInit.FOREST_GRASS_BLOCK.get())));
		DIRTS.add(new DirtFamily("coniferous", BlockInit.CONIFEROUS_DIRT.get(), BlockInit.CONIFEROUS_DIRT_STAIRS.get(),
				BlockInit.CONIFEROUS_DIRT_SLAB.get(), Optional.of(BlockInit.CONIFEROUS_GRASS_BLOCK.get())));
		DIRTS.add(new DirtFamily("windswept", BlockInit.WINDSWEPT_DIRT.get(), BlockInit.WINDSWEPT_DIRT_STAIRS.get(),
				BlockInit.WINDSWEPT_DIRT_SLAB.get(), Optional.of(BlockInit.WINDSWEPT_GRASS_BLOCK.get())));
		DIRTS.add(new DirtFamily("mycelial", BlockInit.MYCELIAL_DIRT.get(), BlockInit.MYCELIAL_DIRT_STAIRS.get(),
				BlockInit.MYCELIAL_DIRT_SLAB.get(), Optional.of(BlockInit.MYCELIAL_GRASS_BLOCK.get())));
		DIRTS.add(new DirtFamily("peat", BlockInit.PEAT.get(), BlockInit.PEAT_STAIRS.get(), BlockInit.PEAT_SLAB.get()));
		DIRTS.add(new DirtFamily("silt", BlockInit.SILT.get(), BlockInit.SILT_STAIRS.get(), BlockInit.SILT_SLAB.get()));

		// Woods
		WOODS.add(new WoodFamily("tropical", BlockInit.TROPICAL_LOG.get(), BlockInit.TROPICAL_WOOD.get(),
				BlockInit.STRIPPED_TROPICAL_LOG.get(), BlockInit.STRIPPED_TROPICAL_WOOD.get(),
				BlockInit.TROPICAL_PLANKS.get(), BlockInit.TROPICAL_STAIRS.get(), BlockInit.TROPICAL_SLAB.get(),
				BlockInit.TROPICAL_FENCE.get(), BlockInit.TROPICAL_FENCE_GATE.get(), BlockInit.TROPICAL_DOOR.get(),
				BlockInit.TROPICAL_TRAPDOOR.get(), BlockInit.TROPICAL_PRESSURE_PLATE.get(),
				BlockInit.TROPICAL_BUTTON.get(), ItemInit.TROPICAL_SIGN.get(), ItemInit.TROPICAL_HANGING_SIGN.get(),
				BlockInit.TROPICAL_SIGN.get(), BlockInit.TROPICAL_WALL_SIGN.get(),
				BlockInit.TROPICAL_HANGING_SIGN.get(), BlockInit.TROPICAL_WALL_HANGING_SIGN.get(),
				new Block[] { BlockInit.TROPICAL_LEAVES.get() }));
		WOODS.add(new WoodFamily("dead_tropical", BlockInit.DEAD_TROPICAL_LOG.get(), BlockInit.DEAD_TROPICAL_WOOD.get(),
				BlockInit.STRIPPED_DEAD_TROPICAL_LOG.get(), BlockInit.STRIPPED_DEAD_TROPICAL_WOOD.get(),
				BlockInit.DEAD_TROPICAL_PLANKS.get(), BlockInit.DEAD_TROPICAL_STAIRS.get(),
				BlockInit.DEAD_TROPICAL_SLAB.get(), BlockInit.DEAD_TROPICAL_FENCE.get(),
				BlockInit.DEAD_TROPICAL_FENCE_GATE.get(), BlockInit.DEAD_TROPICAL_DOOR.get(),
				BlockInit.DEAD_TROPICAL_TRAPDOOR.get(), BlockInit.DEAD_TROPICAL_PRESSURE_PLATE.get(),
				BlockInit.DEAD_TROPICAL_BUTTON.get(), ItemInit.DEAD_TROPICAL_SIGN.get(),
				ItemInit.DEAD_TROPICAL_HANGING_SIGN.get(), BlockInit.DEAD_TROPICAL_SIGN.get(),
				BlockInit.DEAD_TROPICAL_WALL_SIGN.get(), BlockInit.DEAD_TROPICAL_HANGING_SIGN.get(),
				BlockInit.DEAD_TROPICAL_WALL_HANGING_SIGN.get(), new Block[] { BlockInit.DEAD_TROPICAL_LEAVES.get() }));
		WOODS.add(new WoodFamily("pine", BlockInit.PINE_LOG.get(), BlockInit.PINE_WOOD.get(),
				BlockInit.STRIPPED_PINE_LOG.get(), BlockInit.STRIPPED_PINE_WOOD.get(), BlockInit.PINE_PLANKS.get(),
				BlockInit.PINE_STAIRS.get(), BlockInit.PINE_SLAB.get(), BlockInit.PINE_FENCE.get(),
				BlockInit.PINE_FENCE_GATE.get(), BlockInit.PINE_DOOR.get(), BlockInit.PINE_TRAPDOOR.get(),
				BlockInit.PINE_PRESSURE_PLATE.get(), BlockInit.PINE_BUTTON.get(), ItemInit.PINE_SIGN.get(),
				ItemInit.PINE_HANGING_SIGN.get(), BlockInit.PINE_SIGN.get(), BlockInit.PINE_WALL_SIGN.get(),
				BlockInit.PINE_HANGING_SIGN.get(), BlockInit.PINE_WALL_HANGING_SIGN.get(),
				new Block[] { BlockInit.PINE_LEAVES.get() }));
		WOODS.add(new WoodFamily("coniferous", BlockInit.CONIFEROUS_LOG.get(), BlockInit.CONIFEROUS_WOOD.get(),
				BlockInit.STRIPPED_CONIFEROUS_LOG.get(), BlockInit.STRIPPED_CONIFEROUS_WOOD.get(),
				BlockInit.CONIFEROUS_PLANKS.get(), BlockInit.CONIFEROUS_STAIRS.get(), BlockInit.CONIFEROUS_SLAB.get(),
				BlockInit.CONIFEROUS_FENCE.get(), BlockInit.CONIFEROUS_FENCE_GATE.get(),
				BlockInit.CONIFEROUS_DOOR.get(), BlockInit.CONIFEROUS_TRAPDOOR.get(),
				BlockInit.CONIFEROUS_PRESSURE_PLATE.get(), BlockInit.CONIFEROUS_BUTTON.get(),
				ItemInit.CONIFEROUS_SIGN.get(), ItemInit.CONIFEROUS_HANGING_SIGN.get(), BlockInit.CONIFEROUS_SIGN.get(),
				BlockInit.CONIFEROUS_WALL_SIGN.get(), BlockInit.CONIFEROUS_HANGING_SIGN.get(),
				BlockInit.CONIFEROUS_WALL_HANGING_SIGN.get(),
				new Block[] { BlockInit.GREEN_CONIFEROUS_LEAVES.get(), BlockInit.YELLOW_CONIFEROUS_LEAVES.get(),
						BlockInit.ORANGE_CONIFEROUS_LEAVES.get(), BlockInit.RED_CONIFEROUS_LEAVES.get() }));
		WOODS.add(new WoodFamily("cycad", BlockInit.CYCAD_LOG.get(), BlockInit.CYCAD_WOOD.get(),
				BlockInit.STRIPPED_CYCAD_LOG.get(), BlockInit.STRIPPED_CYCAD_WOOD.get(), BlockInit.CYCAD_PLANKS.get(),
				BlockInit.CYCAD_STAIRS.get(), BlockInit.CYCAD_SLAB.get(), BlockInit.CYCAD_FENCE.get(),
				BlockInit.CYCAD_FENCE_GATE.get(), BlockInit.CYCAD_DOOR.get(), BlockInit.CYCAD_TRAPDOOR.get(),
				BlockInit.CYCAD_PRESSURE_PLATE.get(), BlockInit.CYCAD_BUTTON.get(), ItemInit.CYCAD_SIGN.get(),
				ItemInit.CYCAD_HANGING_SIGN.get(), BlockInit.CYCAD_SIGN.get(), BlockInit.CYCAD_WALL_SIGN.get(),
				BlockInit.CYCAD_HANGING_SIGN.get(), BlockInit.CYCAD_WALL_HANGING_SIGN.get(),
				new Block[] { BlockInit.CYCAD_LEAVES.get() }));

		// Stones
		STONES.add(new StoneFamily("anthracite", BlockInit.ANTHRACITE.get(), BlockInit.ANTHRACITE_SLAB.get(),
				BlockInit.ANTHRACITE_STAIRS.get(), BlockInit.ANTHRACITE_WALL.get(),
				BlockInit.ANTHRACITE_PRESSURE_PLATE.get(), BlockInit.ANTHRACITE_BUTTON.get(),
				BlockInit.ANTHRACITE_PEBBLES.get()));
		STONES.add(new StoneFamily("feldspar", BlockInit.FELDSPAR.get(), BlockInit.FELDSPAR_SLAB.get(),
				BlockInit.FELDSPAR_STAIRS.get(), BlockInit.FELDSPAR_WALL.get(), BlockInit.FELDSPAR_PRESSURE_PLATE.get(),
				BlockInit.FELDSPAR_BUTTON.get(), BlockInit.FELDSPAR_PEBBLES.get()));
		STONES.add(new StoneFamily("gray_soapstone", BlockInit.GRAY_SOAPSTONE.get(),
				BlockInit.GRAY_SOAPSTONE_SLAB.get(), BlockInit.GRAY_SOAPSTONE_STAIRS.get(),
				BlockInit.GRAY_SOAPSTONE_WALL.get(), BlockInit.GRAY_SOAPSTONE_PRESSURE_PLATE.get(),
				BlockInit.GRAY_SOAPSTONE_BUTTON.get(), BlockInit.GRAY_SOAPSTONE_PEBBLES.get()));
		STONES.add(new StoneFamily("green_soapstone", BlockInit.GREEN_SOAPSTONE.get(),
				BlockInit.GREEN_SOAPSTONE_SLAB.get(), BlockInit.GREEN_SOAPSTONE_STAIRS.get(),
				BlockInit.GREEN_SOAPSTONE_WALL.get(), BlockInit.GREEN_SOAPSTONE_PRESSURE_PLATE.get(),
				BlockInit.GREEN_SOAPSTONE_BUTTON.get(), BlockInit.GREEN_SOAPSTONE_PEBBLES.get()));
		STONES.add(new StoneFamily("white_soapstone", BlockInit.WHITE_SOAPSTONE.get(),
				BlockInit.WHITE_SOAPSTONE_SLAB.get(), BlockInit.WHITE_SOAPSTONE_STAIRS.get(),
				BlockInit.WHITE_SOAPSTONE_WALL.get(), BlockInit.WHITE_SOAPSTONE_PRESSURE_PLATE.get(),
				BlockInit.WHITE_SOAPSTONE_BUTTON.get(), BlockInit.WHITE_SOAPSTONE_PEBBLES.get()));
		STONES.add(new StoneFamily("shale", BlockInit.SHALE.get(), BlockInit.SHALE_SLAB.get(),
				BlockInit.SHALE_STAIRS.get(), BlockInit.SHALE_WALL.get(), BlockInit.SHALE_PRESSURE_PLATE.get(),
				BlockInit.SHALE_BUTTON.get(), BlockInit.SHALE_PEBBLES.get()));
		STONES.add(new StoneFamily("tectonite", BlockInit.TECTONITE.get(), BlockInit.TECTONITE_SLAB.get(),
				BlockInit.TECTONITE_STAIRS.get(), BlockInit.TECTONITE_WALL.get(),
				BlockInit.TECTONITE_PRESSURE_PLATE.get(), BlockInit.TECTONITE_BUTTON.get(),
				BlockInit.TECTONITE_PEBBLES.get()));
		STONES.add(new StoneFamily("marble", BlockInit.MARBLE.get(), BlockInit.MARBLE_SLAB.get(),
				BlockInit.MARBLE_STAIRS.get(), BlockInit.MARBLE_WALL.get(), BlockInit.MARBLE_PRESSURE_PLATE.get(),
				BlockInit.MARBLE_BUTTON.get(), BlockInit.MARBLE_PEBBLES.get()));
		STONES.add(new StoneFamily("chalk", BlockInit.CHALK.get(), BlockInit.CHALK_SLAB.get(),
				BlockInit.CHALK_STAIRS.get(), BlockInit.CHALK_WALL.get(), BlockInit.CHALK_PRESSURE_PLATE.get(),
				BlockInit.CHALK_BUTTON.get(), BlockInit.CHALK_PEBBLES.get()));
		STONES.add(new StoneFamily("limestone", BlockInit.LIMESTONE.get(), BlockInit.LIMESTONE_SLAB.get(),
				BlockInit.LIMESTONE_STAIRS.get(), BlockInit.LIMESTONE_WALL.get(),
				BlockInit.LIMESTONE_PRESSURE_PLATE.get(), BlockInit.LIMESTONE_BUTTON.get(),
				BlockInit.LIMESTONE_PEBBLES.get()));
		STONES.add(new StoneFamily("migmatite", BlockInit.MIGMATITE.get(), BlockInit.MIGMATITE_SLAB.get(),
				BlockInit.MIGMATITE_STAIRS.get(), BlockInit.MIGMATITE_WALL.get(),
				BlockInit.MIGMATITE_PRESSURE_PLATE.get(), BlockInit.MIGMATITE_BUTTON.get(),
				BlockInit.MIGMATITE_PEBBLES.get()));
		STONES.add(new StoneFamily("gneiss", BlockInit.GNEISS.get(), BlockInit.GNEISS_SLAB.get(),
				BlockInit.GNEISS_STAIRS.get(), BlockInit.GNEISS_WALL.get(), BlockInit.GNEISS_PRESSURE_PLATE.get(),
				BlockInit.GNEISS_BUTTON.get(), BlockInit.GNEISS_PEBBLES.get()));

		// Woods Vanilla
		WOODS_VANILLA.add(new WoodFamily("oak", Blocks.OAK_LOG, Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_LOG,
				Blocks.STRIPPED_OAK_WOOD, Blocks.OAK_PLANKS, Blocks.OAK_STAIRS, Blocks.OAK_SLAB, Blocks.OAK_FENCE,
				Blocks.OAK_FENCE_GATE, Blocks.OAK_DOOR, Blocks.OAK_TRAPDOOR, Blocks.OAK_PRESSURE_PLATE,
				Blocks.OAK_BUTTON, Items.OAK_SIGN, Items.OAK_HANGING_SIGN, Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN,
				Blocks.OAK_HANGING_SIGN, Blocks.OAK_WALL_HANGING_SIGN, new Block[] { Blocks.OAK_LEAVES }));
		WOODS_VANILLA.add(new WoodFamily("spruce", Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_LOG,
				Blocks.STRIPPED_SPRUCE_WOOD, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_SLAB,
				Blocks.SPRUCE_FENCE, Blocks.SPRUCE_FENCE_GATE, Blocks.SPRUCE_DOOR, Blocks.SPRUCE_TRAPDOOR,
				Blocks.SPRUCE_PRESSURE_PLATE, Blocks.SPRUCE_BUTTON, Items.SPRUCE_SIGN, Items.SPRUCE_HANGING_SIGN,
				Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN, Blocks.SPRUCE_HANGING_SIGN,
				Blocks.SPRUCE_WALL_HANGING_SIGN, new Block[] { Blocks.SPRUCE_LEAVES }));
		WOODS_VANILLA.add(new WoodFamily("birch", Blocks.BIRCH_LOG, Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_LOG,
				Blocks.STRIPPED_BIRCH_WOOD, Blocks.BIRCH_PLANKS, Blocks.BIRCH_STAIRS, Blocks.BIRCH_SLAB,
				Blocks.BIRCH_FENCE, Blocks.BIRCH_FENCE_GATE, Blocks.BIRCH_DOOR, Blocks.BIRCH_TRAPDOOR,
				Blocks.BIRCH_PRESSURE_PLATE, Blocks.BIRCH_BUTTON, Items.BIRCH_SIGN, Items.BIRCH_HANGING_SIGN,
				Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN, Blocks.BIRCH_HANGING_SIGN, Blocks.BIRCH_WALL_HANGING_SIGN,
				new Block[] { Blocks.BIRCH_LEAVES }));
		WOODS_VANILLA.add(new WoodFamily("jungle", Blocks.JUNGLE_LOG, Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_LOG,
				Blocks.STRIPPED_JUNGLE_WOOD, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_SLAB,
				Blocks.JUNGLE_FENCE, Blocks.JUNGLE_FENCE_GATE, Blocks.JUNGLE_DOOR, Blocks.JUNGLE_TRAPDOOR,
				Blocks.JUNGLE_PRESSURE_PLATE, Blocks.JUNGLE_BUTTON, Items.JUNGLE_SIGN, Items.JUNGLE_HANGING_SIGN,
				Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN, Blocks.JUNGLE_HANGING_SIGN,
				Blocks.JUNGLE_WALL_HANGING_SIGN, new Block[] { Blocks.JUNGLE_LEAVES }));
		WOODS_VANILLA.add(new WoodFamily("acacia", Blocks.ACACIA_LOG, Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_LOG,
				Blocks.STRIPPED_ACACIA_WOOD, Blocks.ACACIA_PLANKS, Blocks.ACACIA_STAIRS, Blocks.ACACIA_SLAB,
				Blocks.ACACIA_FENCE, Blocks.ACACIA_FENCE_GATE, Blocks.ACACIA_DOOR, Blocks.ACACIA_TRAPDOOR,
				Blocks.ACACIA_PRESSURE_PLATE, Blocks.ACACIA_BUTTON, Items.ACACIA_SIGN, Items.ACACIA_HANGING_SIGN,
				Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.ACACIA_HANGING_SIGN,
				Blocks.ACACIA_WALL_HANGING_SIGN, new Block[] { Blocks.ACACIA_LEAVES }));
		WOODS_VANILLA.add(new WoodFamily("dark_oak", Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_WOOD,
				Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.DARK_OAK_PLANKS,
				Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_FENCE_GATE,
				Blocks.DARK_OAK_DOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.DARK_OAK_PRESSURE_PLATE, Blocks.DARK_OAK_BUTTON,
				Items.DARK_OAK_SIGN, Items.DARK_OAK_HANGING_SIGN, Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN,
				Blocks.DARK_OAK_HANGING_SIGN, Blocks.DARK_OAK_WALL_HANGING_SIGN,
				new Block[] { Blocks.DARK_OAK_LEAVES }));
		WOODS_VANILLA.add(new WoodFamily("mangrove", Blocks.MANGROVE_LOG, Blocks.MANGROVE_WOOD,
				Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_WOOD, Blocks.MANGROVE_PLANKS,
				Blocks.MANGROVE_STAIRS, Blocks.MANGROVE_SLAB, Blocks.MANGROVE_FENCE, Blocks.MANGROVE_FENCE_GATE,
				Blocks.MANGROVE_DOOR, Blocks.MANGROVE_TRAPDOOR, Blocks.MANGROVE_PRESSURE_PLATE, Blocks.MANGROVE_BUTTON,
				Items.MANGROVE_SIGN, Items.MANGROVE_HANGING_SIGN, Blocks.MANGROVE_SIGN, Blocks.MANGROVE_WALL_SIGN,
				Blocks.MANGROVE_HANGING_SIGN, Blocks.MANGROVE_WALL_HANGING_SIGN,
				new Block[] { Blocks.MANGROVE_LEAVES }));
		WOODS_VANILLA.add(new WoodFamily("cherry", Blocks.CHERRY_LOG, Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_LOG,
				Blocks.STRIPPED_CHERRY_WOOD, Blocks.CHERRY_PLANKS, Blocks.CHERRY_STAIRS, Blocks.CHERRY_SLAB,
				Blocks.CHERRY_FENCE, Blocks.CHERRY_FENCE_GATE, Blocks.CHERRY_DOOR, Blocks.CHERRY_TRAPDOOR,
				Blocks.CHERRY_PRESSURE_PLATE, Blocks.CHERRY_BUTTON, Items.CHERRY_SIGN, Items.CHERRY_HANGING_SIGN,
				Blocks.CHERRY_SIGN, Blocks.CHERRY_WALL_SIGN, Blocks.CHERRY_HANGING_SIGN,
				Blocks.CHERRY_WALL_HANGING_SIGN, new Block[] { Blocks.CHERRY_LEAVES }));
		WOODS_VANILLA.add(new WoodFamily("crimson", Blocks.CRIMSON_STEM, Blocks.CRIMSON_HYPHAE,
				Blocks.STRIPPED_CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.CRIMSON_PLANKS,
				Blocks.CRIMSON_STAIRS, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_FENCE, Blocks.CRIMSON_FENCE_GATE,
				Blocks.CRIMSON_DOOR, Blocks.CRIMSON_TRAPDOOR, Blocks.CRIMSON_PRESSURE_PLATE, Blocks.CRIMSON_BUTTON,
				Items.CRIMSON_SIGN, Items.CRIMSON_HANGING_SIGN, Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN,
				Blocks.CRIMSON_HANGING_SIGN, Blocks.CRIMSON_WALL_HANGING_SIGN, new Block[] {}));
		WOODS_VANILLA.add(new WoodFamily("warped", Blocks.WARPED_STEM, Blocks.WARPED_HYPHAE,
				Blocks.STRIPPED_WARPED_STEM, Blocks.STRIPPED_WARPED_HYPHAE, Blocks.WARPED_PLANKS, Blocks.WARPED_STAIRS,
				Blocks.WARPED_SLAB, Blocks.WARPED_FENCE, Blocks.WARPED_FENCE_GATE, Blocks.WARPED_DOOR,
				Blocks.WARPED_TRAPDOOR, Blocks.WARPED_PRESSURE_PLATE, Blocks.WARPED_BUTTON, Items.WARPED_SIGN,
				Items.WARPED_HANGING_SIGN, Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN, Blocks.WARPED_HANGING_SIGN,
				Blocks.WARPED_WALL_HANGING_SIGN, new Block[] {}));
	}

	public interface ItemLikeFamily {
		List<ItemLike> tab();
	}

	public record OreFamily(String name, Optional<MachinaOre> ore, Optional<Block> block, Optional<Item> nugget,
			Optional<Item> ingot, Optional<Item> dust, Optional<Item> plate, Optional<Item> rod, Optional<Item> wire,
			Optional<Item> raw, Optional<Block> rawBlock) implements ItemLikeFamily {

		public static OreFamily resLike(String name, MachinaOre ore, Item gem, Item dust) {
			return new OreFamily(name, Optional.of(ore), Optional.empty(), Optional.empty(), Optional.of(gem),
					Optional.of(dust), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
					Optional.empty());
		}

		public static OreFamily gemLike(String name, MachinaOre ore, Block block, Item ingot, Item dust) {
			return new OreFamily(name, Optional.of(ore), Optional.of(block), Optional.empty(), Optional.of(ingot),
					Optional.of(dust), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
					Optional.empty());
		}

		public static OreFamily ingotLike(String name, MachinaOre ore, Block block, Item nugget, Item ingot, Item dust,
				Item plate, Item rod, Item wire, Item raw, Block rawBlock) {
			return new OreFamily(name, Optional.of(ore), Optional.of(block), Optional.of(nugget), Optional.of(ingot),
					Optional.of(dust), Optional.of(plate), Optional.of(rod), Optional.of(wire), Optional.of(raw),
					Optional.of(rawBlock));
		}

		public static OreFamily alloyLike(String name, Block block, Item nugget, Item ingot, Item dust, Item plate,
				Item rod, Item wire) {
			return new OreFamily(name, Optional.empty(), Optional.of(block), Optional.of(nugget), Optional.of(ingot),
					Optional.of(dust), Optional.of(plate), Optional.of(rod), Optional.of(wire), Optional.empty(),
					Optional.empty());
		}

		public static OreFamily dustLike(String name, MachinaOre ore, Block block, Item ingot) {
			return new OreFamily(name, Optional.of(ore), Optional.of(block), Optional.empty(), Optional.of(ingot),
					Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
					Optional.empty());
		}

		public static OreFamily oreLike(String name, MachinaOre ore, Item ingot) {
			return new OreFamily(name, Optional.of(ore), Optional.empty(), Optional.empty(), Optional.of(ingot),
					Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
					Optional.empty());
		}

		private static boolean isOurItem(ItemLike item) {
			return BuiltInRegistries.ITEM.getKey(item.asItem()).getNamespace().equals(Machina.MOD_ID);
		}

		public Optional<Block> getBlock() {
			return block.filter(OreFamily::isOurItem);
		}

		public Optional<Item> getNugget() {
			return nugget.filter(OreFamily::isOurItem);
		}

		public Optional<Item> getIngot() {
			return ingot.filter(OreFamily::isOurItem);
		}

		public Optional<Item> getDust() {
			return dust.filter(OreFamily::isOurItem);
		}

		public Optional<Item> getRaw() {
			return raw.filter(OreFamily::isOurItem);
		}

		public Optional<Block> getRawBlock() {
			return rawBlock.filter(OreFamily::isOurItem);
		}

		@Override
		public List<ItemLike> tab() {
			List<ItemLike> builder = new ArrayList<>();

			Consumer<ItemLike> add = i -> {
				if (BuiltInRegistries.ITEM.getKey(i.asItem()).getNamespace().equals(Machina.MOD_ID)) {
					builder.add(i);
				}
			};

			ingot.ifPresent(add);
			nugget.ifPresent(add);
			block.ifPresent(add);
			dust.ifPresent(add);
			plate.ifPresent(add);
			rod.ifPresent(add);
			wire.ifPresent(add);
			raw.ifPresent(add);
			rawBlock.ifPresent(add);
			return builder;
		}

		public List<ItemLike> oreTab() {
			return ore.map(ore -> ore.map().values().stream().map(x -> (ItemLike) x.get()).collect(Collectors.toList()))
					.orElse(List.of());
		}
	}

	public record DirtFamily(String name, Block dirt, Block stairs, Block slab, Optional<Block> grass)
			implements ItemLikeFamily {

		public DirtFamily(String name, Block dirt, Block stairs, Block slab) {
			this(name, dirt, stairs, slab, Optional.empty());
		}

		@Override
		public List<ItemLike> tab() {
			return grass.<List<ItemLike>>map(block -> List.of(block, dirt, stairs, slab))
					.orElseGet(() -> List.of(dirt, stairs, slab));
		}
	}

	public record WoodFamily(String name, Block log, Block wood, Block stripped_log, Block stripped_wood, Block planks,
			Block stairs, Block slab, Block fence, Block fencegate, Block door, Block trapdoor, Block pressure_plate,
			Block button, Item sign, Item hangingsign, Block signblock, Block wallsignblock, Block hangingsignblock,
			Block hangingwallsignblock, Block[] leaves) implements ItemLikeFamily {

		@Override
		public List<ItemLike> tab() {
			return Stream.concat(Stream.<ItemLike>of(log, wood, stripped_log, stripped_wood, planks, stairs, slab,
					fence, fencegate, door, trapdoor, pressure_plate, button, sign, hangingsign), Arrays.stream(leaves))
					.toList();
		}
	}

	public record StoneFamily(String name, Block base, Block slab, Block stairs, Block wall, Block pressure_plate,
			Block button, Block pebbles) implements ItemLikeFamily {

		@Override
		public List<ItemLike> tab() {
			return List.of(base, stairs, slab, wall, pressure_plate, button, pebbles);
		}
	}
}
