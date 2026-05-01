package com.machina.registration.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.block.LitOreBlock;
import com.machina.api.block.OreBlock;
import com.machina.api.item.ChemicalBlockItem;
import com.machina.api.util.MachinaRL;
import com.machina.block.CrystalBlock;
import com.machina.block.SulfurGeyserBlock;
import com.machina.block.TallFakeGrassBlock;
import com.machina.block.MachinaHangingSignBlock;
import com.machina.block.MachinaHangingWallSignBlock;
import com.machina.block.MachinaSignBlock;
import com.machina.block.MachinaWallSignBlock;
import com.machina.block.MachinaWaterlilyBlock;
import com.machina.block.PebbleBlock;
import com.machina.block.SmallFlowerBlock;
import com.machina.block.connector.EnergyCableBlock;
import com.machina.block.connector.FluidPipeBlock;
import com.machina.block.connector.ItemConduitBlock;
import com.machina.block.machine.AtmosphericSeparatorBlock;
import com.machina.block.machine.BatteryBlock;
import com.machina.block.machine.ChemicalGeneratorBlock;
import com.machina.block.machine.ComposterVatBlock;
import com.machina.block.machine.CompressorBlock;
import com.machina.block.machine.CreativeBatteryBlock;
import com.machina.block.machine.ElectricPumpBlock;
import com.machina.block.machine.ElectricSmelterBlock;
import com.machina.block.machine.ElectrolyzerBlock;
import com.machina.block.machine.FurnaceGeneratorBlock;
import com.machina.block.machine.GrinderBlock;
import com.machina.block.machine.MelterBlock;
import com.machina.block.machine.MultiblockHousingBlock;
import com.machina.block.machine.ReactionChamberBlock;
import com.machina.block.machine.RocketAssemblyStationBlock;
import com.machina.block.machine.RocketRefuelingStationBlock;
import com.machina.block.machine.RocketPartBenchBlock;
import com.machina.block.machine.SawmillBlock;
import com.machina.block.machine.SolidifierBlock;
import com.machina.block.machine.TankBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockInit {
	public static final List<DeferredBlock<? extends Block>> SIGNS = new ArrayList<>();
	public static final List<DeferredBlock<? extends Block>> HANGING_SIGNS = new ArrayList<>();
	public static final Map<ResourceLocation, MachinaOre> ORES = new HashMap<>();

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(Machina.MOD_ID);

	//@formatter:off
	public static final DeferredBlock<Block> BASIC_CASING = cutout("basic_casing", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> LIGHTWEIGHT_CASING = cutout("lightweight_casing", Blocks.IRON_BLOCK);

	public static final DeferredBlock<ItemConduitBlock> ITEM_CONDUIT = register("item_conduit", ItemConduitBlock::new);
	public static final DeferredBlock<EnergyCableBlock> ENERGY_CABLE = register("energy_cable", EnergyCableBlock::new);
	public static final DeferredBlock<FluidPipeBlock> FLUID_PIPE = register("fluid_pipe", FluidPipeBlock::new);
	public static final DeferredBlock<BatteryBlock> BATTERY = register("battery", Blocks.IRON_BLOCK, BatteryBlock::new);
	public static final DeferredBlock<TankBlock> TANK = register("tank", Blocks.IRON_BLOCK, TankBlock::new);
	public static final DeferredBlock<CreativeBatteryBlock> CREATIVE_BATTERY = register("creative_battery", Blocks.IRON_BLOCK, CreativeBatteryBlock::new);
	public static final DeferredBlock<MultiblockHousingBlock> MULTIBLOCK_HOUSING = register("multiblock_housing", Blocks.IRON_BLOCK, MultiblockHousingBlock::new);
	public static final DeferredBlock<FurnaceGeneratorBlock> FURNACE_GENERATOR = register("furnace_generator", Blocks.IRON_BLOCK, FurnaceGeneratorBlock::new);
	public static final DeferredBlock<ChemicalGeneratorBlock> CHEMICAL_GENERATOR = register("chemical_generator", Blocks.IRON_BLOCK, ChemicalGeneratorBlock::new);
	public static final DeferredBlock<ElectricSmelterBlock> ELECTRIC_SMELTER = register("electric_smelter", Blocks.IRON_BLOCK, ElectricSmelterBlock::new);
	public static final DeferredBlock<GrinderBlock> GRINDER = register("grinder", Blocks.IRON_BLOCK, GrinderBlock::new);
	public static final DeferredBlock<CompressorBlock> COMPRESSOR = register("compressor", Blocks.IRON_BLOCK, CompressorBlock::new);
	public static final DeferredBlock<MelterBlock> MELTER = register("melter", Blocks.IRON_BLOCK, MelterBlock::new);
	public static final DeferredBlock<SolidifierBlock> SOLIDIFIER = register("solidifier", Blocks.IRON_BLOCK, SolidifierBlock::new);
	public static final DeferredBlock<ReactionChamberBlock> REACTION_CHAMBER = register("reaction_chamber", Blocks.IRON_BLOCK, ReactionChamberBlock::new);
	public static final DeferredBlock<ComposterVatBlock> COMPOSTER_VAT = register("composter_vat", Blocks.IRON_BLOCK, ComposterVatBlock::new);
	public static final DeferredBlock<SawmillBlock> SAWMILL = register("sawmill", Blocks.IRON_BLOCK, SawmillBlock::new);
	public static final DeferredBlock<ElectrolyzerBlock> ELECTROLYZER = register("electrolyzer", Blocks.IRON_BLOCK, ElectrolyzerBlock::new);
	public static final DeferredBlock<ElectricPumpBlock> ELECTRIC_PUMP = register("electric_pump", Blocks.IRON_BLOCK, ElectricPumpBlock::new);
	public static final DeferredBlock<AtmosphericSeparatorBlock> ATMOSPHERIC_SEPARATOR = register("atmospheric_separator", Blocks.IRON_BLOCK, AtmosphericSeparatorBlock::new);
	public static final DeferredBlock<RocketPartBenchBlock> ROCKET_PART_BENCH = register("rocket_part_bench", Blocks.IRON_BLOCK, RocketPartBenchBlock::new);
	public static final DeferredBlock<RocketAssemblyStationBlock> ROCKET_ASSEMBLY_STATION = register("rocket_assembly_station", Blocks.IRON_BLOCK, RocketAssemblyStationBlock::new);
	public static final DeferredBlock<RocketRefuelingStationBlock> ROCKET_REFUELING_STATION = register("rocket_refueling_station", Blocks.IRON_BLOCK, RocketRefuelingStationBlock::new);

	public static final DeferredBlock<Block> RAW_ALUMINUM_BLOCK = block("raw_aluminum_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_NICKEL_BLOCK = block("raw_nickel_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_LEAD_BLOCK = block("raw_lead_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_BORON_BLOCK = block("raw_boron_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_PALLADIUM_BLOCK = block("raw_palladium_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_SILVER_BLOCK = block("raw_silver_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_TIN_BLOCK = block("raw_tin_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_ZINC_BLOCK = block("raw_zinc_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_LOW_GRADE_TITANIUM_BLOCK = block("raw_low_grade_titanium_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_MAGNETITE_BLOCK = block("raw_magnetite_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_GYPSUM_BLOCK = block("raw_gypsum_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_ILMENITE_BLOCK = block("raw_ilmenite_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_PLATINUM_BLOCK = block("raw_platinum_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_IRIDIUM_BLOCK = block("raw_iridium_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_OSMIUM_BLOCK = block("raw_osmium_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_COBALT_BLOCK = block("raw_cobalt_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_URANINITE_BLOCK = block("raw_uraninite_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> RAW_THORIUM_BLOCK = block("raw_thorium_block", Blocks.IRON_BLOCK);
	
	public static final DeferredBlock<Block> ALUMINUM_BLOCK = block("aluminum_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> NICKEL_BLOCK = block("nickel_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> LEAD_BLOCK = block("lead_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> BORON_BLOCK = block("boron_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> PALLADIUM_BLOCK = block("palladium_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> SILVER_BLOCK = block("silver_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> TIN_BLOCK = block("tin_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> ZINC_BLOCK = block("zinc_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> LOW_GRADE_TITANIUM_BLOCK = block("low_grade_titanium_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> MAGNETITE_BLOCK = block("magnetite_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> GYPSUM_BLOCK = block("gypsum_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> ILMENITE_BLOCK = block("ilmenite_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> PLATINUM_BLOCK = block("platinum_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> IRIDIUM_BLOCK = block("iridium_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> OSMIUM_BLOCK = block("osmium_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> COBALT_BLOCK = block("cobalt_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> URANINITE_BLOCK = block("uraninite_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> THORIUM_BLOCK = block("thorium_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> STEEL_BLOCK = block("steel_block", Blocks.IRON_BLOCK);
	public static final DeferredBlock<Block> CONSTANTAN_BLOCK = block("constantan_block", Blocks.IRON_BLOCK);
	
	public static final DeferredBlock<CrystalBlock> FLUORITE = chemical("fluorite", Blocks.AMETHYST_CLUSTER, "CaF2");
	public static final DeferredBlock<CrystalBlock> SULFUR = chemical("sulfur", Blocks.AMETHYST_CLUSTER, "SO3");
	public static final DeferredBlock<CrystalBlock> NITER = chemical("niter", Blocks.AMETHYST_CLUSTER, "KNO3");

	public static final DeferredBlock<SulfurGeyserBlock> SULFUR_GEYSER = register("sulfur_geyser", Blocks.OBSIDIAN, SulfurGeyserBlock::new);

	public static final DeferredBlock<Block> ANTHRACITE = block("anthracite", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> ANTHRACITE_SLAB = slab("anthracite_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> ANTHRACITE_STAIRS = stairs("anthracite_stairs", ANTHRACITE, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> ANTHRACITE_WALL = wall("anthracite_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> ANTHRACITE_PRESSURE_PLATE = stone_pressure_plate("anthracite_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> ANTHRACITE_BUTTON = stone_button("anthracite_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> ANTHRACITE_PEBBLES = pebbles("anthracite_pebbles");
	
	public static final DeferredBlock<Block> FELDSPAR = block("feldspar", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> FELDSPAR_SLAB = slab("feldspar_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> FELDSPAR_STAIRS = stairs("feldspar_stairs", FELDSPAR, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> FELDSPAR_WALL = wall("feldspar_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> FELDSPAR_PRESSURE_PLATE = stone_pressure_plate("feldspar_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> FELDSPAR_BUTTON = stone_button("feldspar_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> FELDSPAR_PEBBLES = pebbles("feldspar_pebbles");
	
	public static final DeferredBlock<Block> GRAY_SOAPSTONE = block("gray_soapstone", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> GRAY_SOAPSTONE_SLAB = slab("gray_soapstone_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> GRAY_SOAPSTONE_STAIRS = stairs("gray_soapstone_stairs", GRAY_SOAPSTONE, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> GRAY_SOAPSTONE_WALL = wall("gray_soapstone_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> GRAY_SOAPSTONE_PRESSURE_PLATE = stone_pressure_plate("gray_soapstone_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> GRAY_SOAPSTONE_BUTTON = stone_button("gray_soapstone_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> GRAY_SOAPSTONE_PEBBLES = pebbles("gray_soapstone_pebbles");
	
	public static final DeferredBlock<Block> GREEN_SOAPSTONE = block("green_soapstone", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> GREEN_SOAPSTONE_SLAB = slab("green_soapstone_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> GREEN_SOAPSTONE_STAIRS = stairs("green_soapstone_stairs", GREEN_SOAPSTONE, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> GREEN_SOAPSTONE_WALL = wall("green_soapstone_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> GREEN_SOAPSTONE_PRESSURE_PLATE = stone_pressure_plate("green_soapstone_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> GREEN_SOAPSTONE_BUTTON = stone_button("green_soapstone_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> GREEN_SOAPSTONE_PEBBLES = pebbles("green_soapstone_pebbles");
	
	public static final DeferredBlock<Block> WHITE_SOAPSTONE = block("white_soapstone", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> WHITE_SOAPSTONE_SLAB = slab("white_soapstone_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> WHITE_SOAPSTONE_STAIRS = stairs("white_soapstone_stairs", WHITE_SOAPSTONE, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> WHITE_SOAPSTONE_WALL = wall("white_soapstone_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> WHITE_SOAPSTONE_PRESSURE_PLATE = stone_pressure_plate("white_soapstone_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> WHITE_SOAPSTONE_BUTTON = stone_button("white_soapstone_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> WHITE_SOAPSTONE_PEBBLES = pebbles("white_soapstone_pebbles");
	
	public static final DeferredBlock<Block> WISPY_SOAPSTONE = block("wispy_soapstone", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> WISPY_SOAPSTONE_SLAB = slab("wispy_soapstone_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> WISPY_SOAPSTONE_STAIRS = stairs("wispy_soapstone_stairs", WISPY_SOAPSTONE, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> WISPY_SOAPSTONE_WALL = wall("wispy_soapstone_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> WISPY_SOAPSTONE_PRESSURE_PLATE = stone_pressure_plate("wispy_soapstone_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> WISPY_SOAPSTONE_BUTTON = stone_button("wispy_soapstone_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> WISPY_SOAPSTONE_PEBBLES = pebbles("wispy_soapstone_pebbles");
	
	public static final DeferredBlock<Block> MOONSTONE = block("moonstone", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> MOONSTONE_SLAB = slab("moonstone_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> MOONSTONE_STAIRS = stairs("moonstone_stairs", MOONSTONE, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> MOONSTONE_WALL = wall("moonstone_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> MOONSTONE_PRESSURE_PLATE = stone_pressure_plate("moonstone_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> MOONSTONE_BUTTON = stone_button("moonstone_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> MOONSTONE_PEBBLES = pebbles("moonstone_pebbles");
	
	public static final DeferredBlock<Block> SHALE = block("shale", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> SHALE_SLAB = slab("shale_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> SHALE_STAIRS = stairs("shale_stairs", SHALE, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> SHALE_WALL = wall("shale_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> SHALE_PRESSURE_PLATE = stone_pressure_plate("shale_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> SHALE_BUTTON = stone_button("shale_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> SHALE_PEBBLES = pebbles("shale_pebbles");
	
	public static final DeferredBlock<Block> TECTONITE = block("tectonite", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> TECTONITE_SLAB = slab("tectonite_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> TECTONITE_STAIRS = stairs("tectonite_stairs", TECTONITE, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> TECTONITE_WALL = wall("tectonite_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> TECTONITE_PRESSURE_PLATE = stone_pressure_plate("tectonite_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> TECTONITE_BUTTON = stone_button("tectonite_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> TECTONITE_PEBBLES = pebbles("tectonite_pebbles");
	
	public static final DeferredBlock<Block> MARBLE = block("marble", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> MARBLE_SLAB = slab("marble_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> MARBLE_STAIRS = stairs("marble_stairs", MARBLE, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> MARBLE_WALL = wall("marble_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> MARBLE_PRESSURE_PLATE = stone_pressure_plate("marble_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> MARBLE_BUTTON = stone_button("marble_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> MARBLE_PEBBLES = pebbles("marble_pebbles");
	
	public static final DeferredBlock<Block> CHALK = block("chalk", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> CHALK_SLAB = slab("chalk_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> CHALK_STAIRS = stairs("chalk_stairs", CHALK, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> CHALK_WALL = wall("chalk_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> CHALK_PRESSURE_PLATE = stone_pressure_plate("chalk_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> CHALK_BUTTON = stone_button("chalk_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> CHALK_PEBBLES = pebbles("chalk_pebbles");
	
	public static final DeferredBlock<Block> LIMESTONE = block("limestone", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> LIMESTONE_SLAB = slab("limestone_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> LIMESTONE_STAIRS = stairs("limestone_stairs", LIMESTONE, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> LIMESTONE_WALL = wall("limestone_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> LIMESTONE_PRESSURE_PLATE = stone_pressure_plate("limestone_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> LIMESTONE_BUTTON = stone_button("limestone_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> LIMESTONE_PEBBLES = pebbles("limestone_pebbles");
	
	public static final DeferredBlock<Block> MIGMATITE = block("migmatite", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> MIGMATITE_SLAB = slab("migmatite_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> MIGMATITE_STAIRS = stairs("migmatite_stairs", MIGMATITE, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> MIGMATITE_WALL = wall("migmatite_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> MIGMATITE_PRESSURE_PLATE = stone_pressure_plate("migmatite_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> MIGMATITE_BUTTON = stone_button("migmatite_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> MIGMATITE_PEBBLES = pebbles("migmatite_pebbles");
	
	public static final DeferredBlock<Block> GNEISS = block("gneiss", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> GNEISS_SLAB = slab("gneiss_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> GNEISS_STAIRS = stairs("gneiss_stairs", GNEISS, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> GNEISS_WALL = wall("gneiss_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> GNEISS_PRESSURE_PLATE = stone_pressure_plate("gneiss_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> GNEISS_BUTTON = stone_button("gneiss_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> GNEISS_PEBBLES = pebbles("gneiss_pebbles");
	
	public static final DeferredBlock<Block> BASALT = block("basalt", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> BASALT_SLAB = slab("basalt_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> BASALT_STAIRS = stairs("basalt_stairs", BASALT, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> BASALT_WALL = wall("basalt_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> BASALT_PRESSURE_PLATE = stone_pressure_plate("basalt_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> BASALT_BUTTON = stone_button("basalt_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> BASALT_PEBBLES = pebbles("basalt_pebbles");
	
	public static final DeferredBlock<Block> BASALT_SPRINGSTONE = block("basalt_springstone", Blocks.ANDESITE);
	public static final DeferredBlock<SlabBlock> BASALT_SPRINGSTONE_SLAB = slab("basalt_springstone_slab", Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<StairBlock> BASALT_SPRINGSTONE_STAIRS = stairs("basalt_springstone_stairs", BASALT_SPRINGSTONE, Blocks.ANDESITE_SLAB);
	public static final DeferredBlock<WallBlock> BASALT_SPRINGSTONE_WALL = wall("basalt_springstone_wall", Blocks.ANDESITE_WALL);
	public static final DeferredBlock<PressurePlateBlock> BASALT_SPRINGSTONE_PRESSURE_PLATE = stone_pressure_plate("basalt_springstone_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final DeferredBlock<ButtonBlock> BASALT_SPRINGSTONE_BUTTON = stone_button("basalt_springstone_button", Blocks.STONE_BUTTON);
	public static final DeferredBlock<PebbleBlock> BASALT_SPRINGSTONE_PEBBLES = pebbles("basalt_springstone_pebbles");
	
	public static final DeferredBlock<Block> TROPICAL_GRASS_BLOCK = block("tropical_grass_block", Blocks.GRASS_BLOCK);
	public static final DeferredBlock<Block> FOREST_GRASS_BLOCK = block("forest_grass_block", Blocks.GRASS_BLOCK);
	public static final DeferredBlock<Block> CONIFEROUS_GRASS_BLOCK = block("coniferous_grass_block", Blocks.GRASS_BLOCK);
	public static final DeferredBlock<Block> WINDSWEPT_GRASS_BLOCK = block("windswept_grass_block", Blocks.GRASS_BLOCK);
	public static final DeferredBlock<Block> MYCELIAL_GRASS_BLOCK = block("mycelial_grass_block", Blocks.GRASS_BLOCK);
	
	public static final DeferredBlock<Block> TROPICAL_DIRT = block("tropical_dirt", Blocks.DIRT);
	public static final DeferredBlock<StairBlock> TROPICAL_DIRT_STAIRS = stairs("tropical_dirt_stairs", TROPICAL_DIRT, Blocks.DIRT);
	public static final DeferredBlock<SlabBlock> TROPICAL_DIRT_SLAB = slab("tropical_dirt_slab", Blocks.DIRT);
	
	public static final DeferredBlock<Block> FOREST_DIRT = block("forest_dirt", Blocks.DIRT);
	public static final DeferredBlock<StairBlock> FOREST_DIRT_STAIRS = stairs("forest_dirt_stairs", FOREST_DIRT, Blocks.DIRT);
	public static final DeferredBlock<SlabBlock> FOREST_DIRT_SLAB = slab("forest_dirt_slab", Blocks.DIRT);
	
	public static final DeferredBlock<Block> CONIFEROUS_DIRT = block("coniferous_dirt", Blocks.DIRT);
	public static final DeferredBlock<StairBlock> CONIFEROUS_DIRT_STAIRS = stairs("coniferous_dirt_stairs", CONIFEROUS_DIRT, Blocks.DIRT);
	public static final DeferredBlock<SlabBlock> CONIFEROUS_DIRT_SLAB = slab("coniferous_dirt_slab", Blocks.DIRT);
	
	public static final DeferredBlock<Block> WINDSWEPT_DIRT = block("windswept_dirt", Blocks.DIRT);
	public static final DeferredBlock<StairBlock> WINDSWEPT_DIRT_STAIRS = stairs("windswept_dirt_stairs", WINDSWEPT_DIRT, Blocks.DIRT);
	public static final DeferredBlock<SlabBlock> WINDSWEPT_DIRT_SLAB = slab("windswept_dirt_slab", Blocks.DIRT);
	
	public static final DeferredBlock<Block> MYCELIAL_DIRT = block("mycelial_dirt", Blocks.DIRT);
	public static final DeferredBlock<StairBlock> MYCELIAL_DIRT_STAIRS = stairs("mycelial_dirt_stairs", MYCELIAL_DIRT, Blocks.DIRT);
	public static final DeferredBlock<SlabBlock> MYCELIAL_DIRT_SLAB = slab("mycelial_dirt_slab", Blocks.DIRT);
	
	public static final DeferredBlock<Block> SILT = block("silt", Blocks.MUD);
	public static final DeferredBlock<StairBlock> SILT_STAIRS = stairs("silt_stairs", SILT, Blocks.MUD);
	public static final DeferredBlock<SlabBlock> SILT_SLAB = slab("silt_slab", Blocks.MUD);
	
	public static final DeferredBlock<Block> PEAT = block("peat", Blocks.MUD);
	public static final DeferredBlock<StairBlock> PEAT_STAIRS = stairs("peat_stairs", PEAT, Blocks.MUD);
	public static final DeferredBlock<SlabBlock> PEAT_SLAB = slab("peat_slab", Blocks.MUD);

    public static final DeferredBlock<FallingBlock> TROPICAL_SAND = falling("tropical_sand", 0xCD9351, Blocks.SAND);
    public static final DeferredBlock<FallingBlock> MOONSAND = falling("moonsand", 0x97B2B4, Blocks.SAND);
    public static final DeferredBlock<FallingBlock> FERROUS_SAND = falling("ferrous_sand", 0xbe4d2e, Blocks.SAND);
    public static final DeferredBlock<FallingBlock> ASH = falling("ash", 0x36373a, Blocks.SAND);
    public static final DeferredBlock<FallingBlock> POLLUTED_SAND = falling("polluted_sand", 0x4a4523, Blocks.SAND);
    public static final DeferredBlock<FallingBlock> TOXIC_SAND = falling("toxic_sand", 0x397a38, Blocks.SAND);

	public static final WoodType TROPICAL = registerWoodType("tropical");
	public static final WoodType DEAD_TROPICAL = registerWoodType("dead_tropical");
	public static final WoodType PINE = registerWoodType("pine");
	public static final WoodType CONIFEROUS = registerWoodType("coniferous");
	public static final WoodType CYCAD = registerWoodType("cycad");

	public static final DeferredBlock<RotatedPillarBlock> TROPICAL_LOG = log("tropical_log", Blocks.OAK_LOG);
	public static final DeferredBlock<RotatedPillarBlock> TROPICAL_WOOD = log("tropical_wood", Blocks.OAK_WOOD);
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_TROPICAL_LOG = log("stripped_tropical_log", Blocks.STRIPPED_OAK_LOG);
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_TROPICAL_WOOD = log("stripped_tropical_wood", Blocks.STRIPPED_OAK_WOOD);
	public static final DeferredBlock<Block> TROPICAL_PLANKS = block("tropical_planks", Blocks.OAK_PLANKS);
	public static final DeferredBlock<LeavesBlock> TROPICAL_LEAVES = leaves("tropical_leaves", Blocks.OAK_LEAVES);
	public static final DeferredBlock<SlabBlock> TROPICAL_SLAB = slab("tropical_slab", Blocks.OAK_SLAB);
	public static final DeferredBlock<StairBlock> TROPICAL_STAIRS = stairs("tropical_stairs", TROPICAL_PLANKS, Blocks.OAK_STAIRS);
	public static final DeferredBlock<MachinaSignBlock> TROPICAL_SIGN = sign("tropical_sign", Blocks.OAK_SIGN, TROPICAL);
	public static final DeferredBlock<MachinaWallSignBlock> TROPICAL_WALL_SIGN = wall_sign("tropical_wall_sign", Blocks.OAK_WALL_SIGN, TROPICAL);
	public static final DeferredBlock<MachinaHangingSignBlock> TROPICAL_HANGING_SIGN = hanging_sign("tropical_hanging_sign", Blocks.OAK_HANGING_SIGN, TROPICAL);
	public static final DeferredBlock<MachinaHangingWallSignBlock> TROPICAL_WALL_HANGING_SIGN = wall_hanging_sign("tropical_wall_hanging_sign", Blocks.OAK_WALL_HANGING_SIGN, TROPICAL);
	public static final DeferredBlock<ButtonBlock> TROPICAL_BUTTON = wood_button("tropical_button", Blocks.OAK_BUTTON, TROPICAL);
	public static final DeferredBlock<DoorBlock> TROPICAL_DOOR = wood_door("tropical_door", Blocks.OAK_DOOR, TROPICAL);
	public static final DeferredBlock<TrapDoorBlock> TROPICAL_TRAPDOOR = wood_trapdoor("tropical_trapdoor", Blocks.OAK_TRAPDOOR, TROPICAL);
	public static final DeferredBlock<FenceBlock> TROPICAL_FENCE = fence("tropical_fence", Blocks.OAK_FENCE);
	public static final DeferredBlock<FenceGateBlock> TROPICAL_FENCE_GATE = fence_gate("tropical_fence_gate", Blocks.OAK_FENCE_GATE, TROPICAL);
	public static final DeferredBlock<PressurePlateBlock> TROPICAL_PRESSURE_PLATE = wood_pressure_plate("tropical_pressure_plate", Blocks.OAK_PRESSURE_PLATE, TROPICAL);
	
	public static final DeferredBlock<RotatedPillarBlock> DEAD_TROPICAL_LOG = log("dead_tropical_log", Blocks.OAK_LOG);
	public static final DeferredBlock<RotatedPillarBlock> DEAD_TROPICAL_WOOD = log("dead_tropical_wood", Blocks.OAK_WOOD);
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_DEAD_TROPICAL_LOG = log("stripped_dead_tropical_log", Blocks.STRIPPED_OAK_LOG);
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_DEAD_TROPICAL_WOOD = log("stripped_dead_tropical_wood", Blocks.STRIPPED_OAK_WOOD);
	public static final DeferredBlock<Block> DEAD_TROPICAL_PLANKS = block("dead_tropical_planks", Blocks.OAK_PLANKS);
	public static final DeferredBlock<LeavesBlock> DEAD_TROPICAL_LEAVES = leaves("dead_tropical_leaves", Blocks.OAK_LEAVES);
	public static final DeferredBlock<SlabBlock> DEAD_TROPICAL_SLAB = slab("dead_tropical_slab", Blocks.OAK_SLAB);
	public static final DeferredBlock<StairBlock> DEAD_TROPICAL_STAIRS = stairs("dead_tropical_stairs", DEAD_TROPICAL_PLANKS, Blocks.OAK_STAIRS);
	public static final DeferredBlock<MachinaSignBlock> DEAD_TROPICAL_SIGN = sign("dead_tropical_sign", Blocks.OAK_SIGN, DEAD_TROPICAL);
	public static final DeferredBlock<MachinaWallSignBlock> DEAD_TROPICAL_WALL_SIGN = wall_sign("dead_tropical_wall_sign", Blocks.OAK_WALL_SIGN, DEAD_TROPICAL);
	public static final DeferredBlock<MachinaHangingSignBlock> DEAD_TROPICAL_HANGING_SIGN = hanging_sign("dead_tropical_hanging_sign", Blocks.OAK_HANGING_SIGN, DEAD_TROPICAL);
	public static final DeferredBlock<MachinaHangingWallSignBlock> DEAD_TROPICAL_WALL_HANGING_SIGN = wall_hanging_sign("dead_tropical_wall_hanging_sign", Blocks.OAK_WALL_HANGING_SIGN, DEAD_TROPICAL);
	public static final DeferredBlock<ButtonBlock> DEAD_TROPICAL_BUTTON = wood_button("dead_tropical_button", Blocks.OAK_BUTTON, DEAD_TROPICAL);
	public static final DeferredBlock<DoorBlock> DEAD_TROPICAL_DOOR = wood_door("dead_tropical_door", Blocks.OAK_DOOR, DEAD_TROPICAL);
	public static final DeferredBlock<TrapDoorBlock> DEAD_TROPICAL_TRAPDOOR = wood_trapdoor("dead_tropical_trapdoor", Blocks.OAK_TRAPDOOR, DEAD_TROPICAL);
	public static final DeferredBlock<FenceBlock> DEAD_TROPICAL_FENCE = fence("dead_tropical_fence", Blocks.OAK_FENCE);
	public static final DeferredBlock<FenceGateBlock> DEAD_TROPICAL_FENCE_GATE = fence_gate("dead_tropical_fence_gate", Blocks.OAK_FENCE_GATE, DEAD_TROPICAL);
	public static final DeferredBlock<PressurePlateBlock> DEAD_TROPICAL_PRESSURE_PLATE = wood_pressure_plate("dead_tropical_pressure_plate", Blocks.OAK_PRESSURE_PLATE, TROPICAL);
	
	public static final DeferredBlock<RotatedPillarBlock> PINE_LOG = log("pine_log", Blocks.OAK_LOG);
	public static final DeferredBlock<RotatedPillarBlock> PINE_WOOD = log("pine_wood", Blocks.OAK_WOOD);
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_PINE_LOG = log("stripped_pine_log", Blocks.STRIPPED_OAK_LOG);
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_PINE_WOOD = log("stripped_pine_wood", Blocks.STRIPPED_OAK_WOOD);
	public static final DeferredBlock<Block> PINE_PLANKS = block("pine_planks", Blocks.OAK_PLANKS);
	public static final DeferredBlock<LeavesBlock> PINE_LEAVES = leaves("pine_leaves", Blocks.OAK_LEAVES);
	public static final DeferredBlock<SlabBlock> PINE_SLAB = slab("pine_slab", Blocks.OAK_SLAB);
	public static final DeferredBlock<StairBlock> PINE_STAIRS = stairs("pine_stairs", PINE_PLANKS, Blocks.OAK_STAIRS);
	public static final DeferredBlock<MachinaSignBlock> PINE_SIGN = sign("pine_sign", Blocks.OAK_SIGN, PINE);
	public static final DeferredBlock<MachinaWallSignBlock> PINE_WALL_SIGN = wall_sign("pine_wall_sign", Blocks.OAK_WALL_SIGN, PINE);
	public static final DeferredBlock<MachinaHangingSignBlock> PINE_HANGING_SIGN = hanging_sign("pine_hanging_sign", Blocks.OAK_HANGING_SIGN, PINE);
	public static final DeferredBlock<MachinaHangingWallSignBlock> PINE_WALL_HANGING_SIGN = wall_hanging_sign("pine_wall_hanging_sign", Blocks.OAK_WALL_HANGING_SIGN, PINE);
	public static final DeferredBlock<ButtonBlock> PINE_BUTTON = wood_button("pine_button", Blocks.OAK_BUTTON, PINE);
	public static final DeferredBlock<DoorBlock> PINE_DOOR = wood_door("pine_door", Blocks.OAK_DOOR, PINE);
	public static final DeferredBlock<TrapDoorBlock> PINE_TRAPDOOR = wood_trapdoor("pine_trapdoor", Blocks.OAK_TRAPDOOR, PINE);
	public static final DeferredBlock<FenceBlock> PINE_FENCE = fence("pine_fence", Blocks.OAK_FENCE);
	public static final DeferredBlock<FenceGateBlock> PINE_FENCE_GATE = fence_gate("pine_fence_gate", Blocks.OAK_FENCE_GATE, PINE);
	public static final DeferredBlock<PressurePlateBlock> PINE_PRESSURE_PLATE = wood_pressure_plate("pine_pressure_plate", Blocks.OAK_PRESSURE_PLATE, PINE);
	
	public static final DeferredBlock<RotatedPillarBlock> CONIFEROUS_LOG = log("coniferous_log", Blocks.OAK_LOG);
	public static final DeferredBlock<RotatedPillarBlock> CONIFEROUS_WOOD = log("coniferous_wood", Blocks.OAK_WOOD);
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_CONIFEROUS_LOG = log("stripped_coniferous_log", Blocks.STRIPPED_OAK_LOG);
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_CONIFEROUS_WOOD = log("stripped_coniferous_wood", Blocks.STRIPPED_OAK_WOOD);
	public static final DeferredBlock<Block> CONIFEROUS_PLANKS = block("coniferous_planks", Blocks.OAK_PLANKS);
	public static final DeferredBlock<LeavesBlock> GREEN_CONIFEROUS_LEAVES = leaves("green_coniferous_leaves", Blocks.OAK_LEAVES);
	public static final DeferredBlock<LeavesBlock> YELLOW_CONIFEROUS_LEAVES = leaves("yellow_coniferous_leaves", Blocks.OAK_LEAVES);
	public static final DeferredBlock<LeavesBlock> ORANGE_CONIFEROUS_LEAVES = leaves("orange_coniferous_leaves", Blocks.OAK_LEAVES);
	public static final DeferredBlock<LeavesBlock> RED_CONIFEROUS_LEAVES = leaves("red_coniferous_leaves", Blocks.OAK_LEAVES);
	public static final DeferredBlock<SlabBlock> CONIFEROUS_SLAB = slab("coniferous_slab", Blocks.OAK_SLAB);
	public static final DeferredBlock<StairBlock> CONIFEROUS_STAIRS = stairs("coniferous_stairs", CONIFEROUS_PLANKS, Blocks.OAK_STAIRS);
	public static final DeferredBlock<MachinaSignBlock> CONIFEROUS_SIGN = sign("coniferous_sign", Blocks.OAK_SIGN, CONIFEROUS);
	public static final DeferredBlock<MachinaWallSignBlock> CONIFEROUS_WALL_SIGN = wall_sign("coniferous_wall_sign", Blocks.OAK_WALL_SIGN, CONIFEROUS);
	public static final DeferredBlock<MachinaHangingSignBlock> CONIFEROUS_HANGING_SIGN = hanging_sign("coniferous_hanging_sign", Blocks.OAK_HANGING_SIGN, CONIFEROUS);
	public static final DeferredBlock<MachinaHangingWallSignBlock> CONIFEROUS_WALL_HANGING_SIGN = wall_hanging_sign("coniferous_wall_hanging_sign", Blocks.OAK_WALL_HANGING_SIGN, CONIFEROUS);
	public static final DeferredBlock<ButtonBlock> CONIFEROUS_BUTTON = wood_button("coniferous_button", Blocks.OAK_BUTTON, CONIFEROUS);
	public static final DeferredBlock<DoorBlock> CONIFEROUS_DOOR = wood_door("coniferous_door", Blocks.OAK_DOOR, CONIFEROUS);
	public static final DeferredBlock<TrapDoorBlock> CONIFEROUS_TRAPDOOR = wood_trapdoor("coniferous_trapdoor", Blocks.OAK_TRAPDOOR, CONIFEROUS);
	public static final DeferredBlock<FenceBlock> CONIFEROUS_FENCE = fence("coniferous_fence", Blocks.OAK_FENCE);
	public static final DeferredBlock<FenceGateBlock> CONIFEROUS_FENCE_GATE = fence_gate("coniferous_fence_gate", Blocks.OAK_FENCE_GATE, CONIFEROUS);
	public static final DeferredBlock<PressurePlateBlock> CONIFEROUS_PRESSURE_PLATE = wood_pressure_plate("coniferous_pressure_plate", Blocks.OAK_PRESSURE_PLATE, CONIFEROUS);
	
	public static final DeferredBlock<RotatedPillarBlock> CYCAD_LOG = log("cycad_log", Blocks.OAK_LOG);
	public static final DeferredBlock<RotatedPillarBlock> CYCAD_WOOD = log("cycad_wood", Blocks.OAK_WOOD);
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_CYCAD_LOG = log("stripped_cycad_log", Blocks.STRIPPED_OAK_LOG);
	public static final DeferredBlock<RotatedPillarBlock> STRIPPED_CYCAD_WOOD = log("stripped_cycad_wood", Blocks.STRIPPED_OAK_WOOD);
	public static final DeferredBlock<Block> CYCAD_PLANKS = block("cycad_planks", Blocks.OAK_PLANKS);
	public static final DeferredBlock<LeavesBlock> CYCAD_LEAVES = leaves("cycad_leaves", Blocks.OAK_LEAVES);
	public static final DeferredBlock<SlabBlock> CYCAD_SLAB = slab("cycad_slab", Blocks.OAK_SLAB);
	public static final DeferredBlock<StairBlock> CYCAD_STAIRS = stairs("cycad_stairs", CYCAD_PLANKS, Blocks.OAK_STAIRS);
	public static final DeferredBlock<MachinaSignBlock> CYCAD_SIGN = sign("cycad_sign", Blocks.OAK_SIGN, CYCAD);
	public static final DeferredBlock<MachinaWallSignBlock> CYCAD_WALL_SIGN = wall_sign("cycad_wall_sign", Blocks.OAK_WALL_SIGN, CYCAD);
	public static final DeferredBlock<MachinaHangingSignBlock> CYCAD_HANGING_SIGN = hanging_sign("cycad_hanging_sign", Blocks.OAK_HANGING_SIGN, CYCAD);
	public static final DeferredBlock<MachinaHangingWallSignBlock> CYCAD_WALL_HANGING_SIGN = wall_hanging_sign("cycad_wall_hanging_sign", Blocks.OAK_WALL_HANGING_SIGN, CYCAD);
	public static final DeferredBlock<ButtonBlock> CYCAD_BUTTON = wood_button("cycad_button", Blocks.OAK_BUTTON, CYCAD);
	public static final DeferredBlock<DoorBlock> CYCAD_DOOR = wood_door("cycad_door", Blocks.OAK_DOOR, CYCAD);
	public static final DeferredBlock<TrapDoorBlock> CYCAD_TRAPDOOR = wood_trapdoor("cycad_trapdoor", Blocks.OAK_TRAPDOOR, CYCAD);
	public static final DeferredBlock<FenceBlock> CYCAD_FENCE = fence("cycad_fence", Blocks.OAK_FENCE);
	public static final DeferredBlock<FenceGateBlock> CYCAD_FENCE_GATE = fence_gate("cycad_fence_gate", Blocks.OAK_FENCE_GATE, CYCAD);
	public static final DeferredBlock<PressurePlateBlock> CYCAD_PRESSURE_PLATE = wood_pressure_plate("cycad_pressure_plate", Blocks.OAK_PRESSURE_PLATE, CYCAD);
	
	public static final DeferredBlock<Block> BROWN_MUSHROOM_STALK = block("brown_mushroom_stalk", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> GREEN_MUSHROOM_STALK = block("green_mushroom_stalk", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> PURPLE_MUSHROOM_STALK = block("purple_mushroom_stalk", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> YELLOW_MUSHROOM_STALK = block("yellow_mushroom_stalk", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> BROWN_MUSHROOM_CAP = block("brown_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> RED_MUSHROOM_CAP = block("red_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> PURPLE_MUSHROOM_CAP = block("purple_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> YELLOW_MUSHROOM_CAP = block("yellow_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> BROWN_MUSHROOM_GILLS = block("brown_mushroom_gills", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> RED_MUSHROOM_GILLS = block("red_mushroom_gills", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> PURPLE_MUSHROOM_GILLS = block("purple_mushroom_gills", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> YELLOW_MUSHROOM_GILLS = block("yellow_mushroom_gills", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> SPECKLED_BROWN_MUSHROOM_CAP = block("speckled_brown_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> SPECKLED_RED_MUSHROOM_CAP = block("speckled_red_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> SPECKLED_PURPLE_MUSHROOM_CAP = block("speckled_purple_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> SPECKLED_YELLOW_MUSHROOM_CAP = block("speckled_yellow_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> IMBUED_BROWN_MUSHROOM_CAP = block("imbued_brown_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> IMBUED_RED_MUSHROOM_CAP = block("imbued_red_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> IMBUED_PURPLE_MUSHROOM_CAP = block("imbued_purple_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final DeferredBlock<Block> IMBUED_YELLOW_MUSHROOM_CAP = block("imbued_yellow_mushroom_cap", Blocks.MUSHROOM_STEM);

	public static final DeferredBlock<BushBlock> TROPICAL_GRASS = register("tropical_grass", Blocks.SHORT_GRASS, TallGrassBlock::new);
	public static final DeferredBlock<BushBlock> TWISTED_GRASS = register("twisted_grass", Blocks.SHORT_GRASS, TallGrassBlock::new);
	public static final DeferredBlock<BushBlock> CONIFEROUS_GRASS = register("coniferous_grass", Blocks.SHORT_GRASS, TallGrassBlock::new);
	public static final DeferredBlock<BushBlock> SHORT_CONIFEROUS_GRASS = register("short_coniferous_grass", Blocks.SHORT_GRASS, TallGrassBlock::new);
	public static final DeferredBlock<BushBlock> WINDSWEPT_GRASS = register("windswept_grass", Blocks.SHORT_GRASS, TallGrassBlock::new);
	public static final DeferredBlock<BushBlock> MYCELIAL_GRASS = register("mycelial_grass", Blocks.SHORT_GRASS, TallGrassBlock::new);
	public static final DeferredBlock<BushBlock> FERROUS_GRASS = register("ferrous_grass", Blocks.SHORT_GRASS, TallFakeGrassBlock::new);
	public static final DeferredBlock<BushBlock> MOONGRASS = register("moongrass", Blocks.SHORT_GRASS, TallFakeGrassBlock::new);
	
	public static final DeferredBlock<SmallFlowerBlock> CLOVER = register("clover", Blocks.PINK_PETALS, SmallFlowerBlock::new);
	public static final DeferredBlock<SmallFlowerBlock> SPINDLESPROUT = register("spindlesprout", Blocks.FERN, SmallFlowerBlock::new);
	public static final DeferredBlock<SmallFlowerBlock> SMALL_FERN = register("small_fern", Blocks.FERN, SmallFlowerBlock::new);
	public static final DeferredBlock<SmallFlowerBlock> DEAD_SMALL_FERN = register("dead_small_fern", Blocks.FERN, SmallFlowerBlock::new);
	public static final DeferredBlock<SmallFlowerBlock> NEEDLEGRASS = register("needlegrass", Blocks.FERN, SmallFlowerBlock::new);
	public static final DeferredBlock<TallFlowerBlock> NEEDLETHATCH = tall_flower("needlethatch", Blocks.SUNFLOWER);
	public static final DeferredBlock<TallFlowerBlock> SPINDLEGRASS = tall_flower("spindlegrass", Blocks.SUNFLOWER);

	public static final DeferredBlock<PinkPetalsBlock> PURPLE_PETALS = register("purple_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	public static final DeferredBlock<PinkPetalsBlock> RED_PETALS = register("red_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	public static final DeferredBlock<PinkPetalsBlock> ORANGE_PETALS = register("orange_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	public static final DeferredBlock<PinkPetalsBlock> YELLOW_PETALS = register("yellow_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	public static final DeferredBlock<PinkPetalsBlock> GREEN_PETALS = register("green_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	public static final DeferredBlock<PinkPetalsBlock> TURQUOISE_PETALS = register("turquoise_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	public static final DeferredBlock<PinkPetalsBlock> BLUE_PETALS = register("blue_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	
	public static final DeferredBlock<TallFlowerBlock> ORPHEUM = tall_flower("orpheum", Blocks.PEONY);

	public static final DeferredBlock<FlowerBlock> DRAGON_PEONY = flower("dragon_peony", MobEffects.LEVITATION, 5, Blocks.DANDELION);
	public static final DeferredBlock<FlowerBlock> SPRUCE_CUP = flower("spruce_cup", MobEffects.UNLUCK, 50, Blocks.BROWN_MUSHROOM);
	
	public static final DeferredBlock<FlowerPotBlock> POTTED_DRAGON_PEONY = flower_pot("potted_dragon_peony", DRAGON_PEONY);
	public static final DeferredBlock<FlowerPotBlock> POTTED_SPRUCE_CUP = flower_pot("potted_spruce_cup", SPRUCE_CUP);
	public static final DeferredBlock<FlowerPotBlock> POTTED_SPINDLESPROUT = flower_pot("potted_spindlesprout", SPINDLESPROUT);
	public static final DeferredBlock<FlowerPotBlock> POTTED_SMALL_FERN = flower_pot("potted_small_fern", SMALL_FERN);
	public static final DeferredBlock<FlowerPotBlock> POTTED_DEAD_SMALL_FERN = flower_pot("potted_dead_small_fern", DEAD_SMALL_FERN);
	public static final DeferredBlock<FlowerPotBlock> POTTED_NEEDLEGRASS = flower_pot("potted_needlegrass", NEEDLEGRASS);
	
	public static final DeferredBlock<SmallFlowerBlock> PURPLE_GROUNDLILY = groundlily("purple_groundlily");
	public static final DeferredBlock<SmallFlowerBlock> PINK_GROUNDLILY = groundlily("pink_groundlily");
	public static final DeferredBlock<SmallFlowerBlock> RED_GROUNDLILY = groundlily("red_groundlily");
	public static final DeferredBlock<SmallFlowerBlock> ORANGE_GROUNDLILY = groundlily("orange_groundlily");
	public static final DeferredBlock<SmallFlowerBlock> YELLOW_GROUNDLILY = groundlily("yellow_groundlily");
	public static final DeferredBlock<SmallFlowerBlock> GREEN_GROUNDLILY = groundlily("green_groundlily");
	public static final DeferredBlock<SmallFlowerBlock> TURQUOISE_GROUNDLILY = groundlily("turquoise_groundlily");
	public static final DeferredBlock<SmallFlowerBlock> BLUE_GROUNDLILY = groundlily("blue_groundlily");
	
	public static final DeferredBlock<MachinaWaterlilyBlock> PURPLE_WATERLILY = waterlily("purple_waterlily");
	public static final DeferredBlock<MachinaWaterlilyBlock> PINK_WATERLILY = waterlily("pink_waterlily");
	public static final DeferredBlock<MachinaWaterlilyBlock> RED_WATERLILY = waterlily("red_waterlily");
	public static final DeferredBlock<MachinaWaterlilyBlock> ORANGE_WATERLILY = waterlily("orange_waterlily");
	public static final DeferredBlock<MachinaWaterlilyBlock> YELLOW_WATERLILY = waterlily("yellow_waterlily");
	public static final DeferredBlock<MachinaWaterlilyBlock> GREEN_WATERLILY = waterlily("green_waterlily");
	public static final DeferredBlock<MachinaWaterlilyBlock> TURQUOISE_WATERLILY = waterlily("turquoise_waterlily");
	public static final DeferredBlock<MachinaWaterlilyBlock> BLUE_WATERLILY = waterlily("blue_waterlily");

	public static final DeferredBlock<FlowerBlock> PURPLE_GLOWSHROOM = flower("purple_glowshroom", MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(6));
	public static final DeferredBlock<FlowerBlock> PINK_GLOWSHROOM = flower("pink_glowshroom", MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final DeferredBlock<FlowerBlock> RED_GLOWSHROOM = flower("red_glowshroom", MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final DeferredBlock<FlowerBlock> ORANGE_GLOWSHROOM = flower("orange_glowshroom", MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final DeferredBlock<FlowerBlock> YELLOW_GLOWSHROOM = flower("yellow_glowshroom", MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final DeferredBlock<FlowerBlock> GREEN_GLOWSHROOM = flower("green_glowshroom", MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final DeferredBlock<FlowerBlock> TURQUOISE_GLOWSHROOM = flower("turquoise_glowshroom", MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final DeferredBlock<FlowerBlock> BLUE_GLOWSHROOM = flower("blue_glowshroom", MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final DeferredBlock<FlowerPotBlock> POTTED_PURPLE_GLOWSHROOM = flower_pot("potted_purple_glowshroom", PURPLE_GLOWSHROOM, light(7));
	public static final DeferredBlock<FlowerPotBlock> POTTED_PINK_GLOWSHROOM = flower_pot("potted_pink_glowshroom", PINK_GLOWSHROOM, light(7));
	public static final DeferredBlock<FlowerPotBlock> POTTED_RED_GLOWSHROOM = flower_pot("potted_red_glowshroom", RED_GLOWSHROOM, light(7));
	public static final DeferredBlock<FlowerPotBlock> POTTED_ORANGE_GLOWSHROOM = flower_pot("potted_orange_glowshroom", ORANGE_GLOWSHROOM, light(7));
	public static final DeferredBlock<FlowerPotBlock> POTTED_YELLOW_GLOWSHROOM = flower_pot("potted_yellow_glowshroom", YELLOW_GLOWSHROOM, light(7));
	public static final DeferredBlock<FlowerPotBlock> POTTED_GREEN_GLOWSHROOM = flower_pot("potted_green_glowshroom", GREEN_GLOWSHROOM, light(7));
	public static final DeferredBlock<FlowerPotBlock> POTTED_TURQUOISE_GLOWSHROOM = flower_pot("potted_turquoise_glowshroom", TURQUOISE_GLOWSHROOM, light(7));
	public static final DeferredBlock<FlowerPotBlock> POTTED_BLUE_GLOWSHROOM = flower_pot("potted_blue_glowshroom", BLUE_GLOWSHROOM, light(7));

	public static final List<DeferredBlock<Block>> ORE_BASES = List.of(
			FELDSPAR, GRAY_SOAPSTONE, GREEN_SOAPSTONE, WHITE_SOAPSTONE, MOONSTONE, SHALE, MARBLE, CHALK, LIMESTONE, GNEISS, BASALT, PEAT, TROPICAL_DIRT, FOREST_DIRT, CONIFEROUS_DIRT);

	public static final MachinaOre COAL_ORE = ore("coal_ore", true, Blocks.COAL_ORE);
	public static final MachinaOre IRON_ORE = ore("iron_ore", true, Blocks.IRON_ORE);
	public static final MachinaOre COPPER_ORE = ore("copper_ore", true, Blocks.COPPER_ORE);
	public static final MachinaOre GOLD_ORE = ore("gold_ore", true, Blocks.GOLD_ORE);
	public static final MachinaOre REDSTONE_ORE = ore("redstone_ore", true, Blocks.REDSTONE_ORE, true);
	public static final MachinaOre NETHER_QUARTZ_ORE = ore("quartz_ore", true, Blocks.NETHER_QUARTZ_ORE);
	public static final MachinaOre EMERALD_ORE = ore("emerald_ore", true, Blocks.EMERALD_ORE);
	public static final MachinaOre LAPIS_ORE = ore("lapis_ore", true, Blocks.LAPIS_ORE);
	public static final MachinaOre DIAMOND_ORE = ore("diamond_ore", true, Blocks.DIAMOND_ORE);
	public static final MachinaOre ALUMINUM_ORE = ore("aluminum_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre NICKEL_ORE = ore("nickel_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre TIN_ORE = ore("tin_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre ZINC_ORE = ore("zinc_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre LOW_GRADE_TITANIUM_ORE = ore("low_grade_titanium_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre LEAD_ORE = ore("lead_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre BORON_ORE = ore("boron_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre PALLADIUM_ORE = ore("palladium_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre SILVER_ORE = ore("silver_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre FLUORITE_ORE = ore("fluorite_ore", false, Blocks.COAL_ORE);
	public static final MachinaOre SALTPETER_ORE = ore("saltpeter_ore", false, Blocks.COAL_ORE);
	public static final MachinaOre PYRITE_ORE = ore("pyrite_ore", false, Blocks.COAL_ORE);
	public static final MachinaOre BISMUTH_ORE = ore("bismuth_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre MAGNETITE_ORE = ore("magnetite_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre GYPSUM_ORE = ore("gypsum_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre PERCHLORATE_ORE = ore("perchlorate_ore", false, Blocks.COAL_ORE);
	public static final MachinaOre ILMENITE_ORE = ore("ilmenite_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre PLATINUM_ORE = ore("platinum_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre IRIDIUM_ORE = ore("iridium_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre OSMIUM_ORE = ore("osmium_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre COBALT_ORE = ore("cobalt_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre URANINITE_ORE = ore("uraninite_ore", false, Blocks.IRON_ORE);
	public static final MachinaOre THORIUM_ORE = ore("thorium_ore", false, Blocks.IRON_ORE);
	//@formatter:on

	public static record MachinaOre(ResourceLocation name, Supplier<? extends Block> stoneOre,
			Map<ResourceKey<Block>, DeferredBlock<OreBlock>> map) {
	}

	public static MachinaOre ore(String name, boolean vanilla, Block props) {
		return ore(name, vanilla, props, false);
	}

	public static MachinaOre ore(String name, boolean vanilla, Block props, boolean lit) {
		BiFunction<Block, Block.Properties, OreBlock> creator = lit ? LitOreBlock::new : OreBlock::new;

		Map<ResourceKey<Block>, DeferredBlock<OreBlock>> ores = new TreeMap<>();
		Supplier<? extends Block> stoneOre;
		if (vanilla) {
			stoneOre = () -> props;
		} else {
			DeferredBlock<OreBlock> stone = register("stone_" + name, props, a -> a,
					p -> creator.apply(Blocks.STONE, p));
			ores.put(ResourceKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("stone")), stone);
			stoneOre = stone;
		}
		ORE_BASES.forEach(base -> {
			ores.put(base.getKey(), register(base.getKey().location().getPath() + "_" + name, props, a -> a,
					p -> creator.apply(base.get(), p)));
		});
		ResourceLocation loc = MachinaRL.create(name);
		MachinaOre ore = new MachinaOre(loc, stoneOre, ores);
		ORES.put(loc, ore);
		return ore;
	}

	private static WoodType registerWoodType(String name) {
		String id = Machina.MOD_ID + ":" + name;
		return WoodType.register(new WoodType(id, new BlockSetType(id)));
	}

	private static Function<Block.Properties, Block.Properties> light(int light) {
		return p -> p.lightLevel(s -> light);
	}

	private static <T extends Block> Supplier<T> of(Block block, Function<Block.Properties, Block.Properties> extra,
			Function<Block.Properties, T> constructor) {
		return () -> constructor.apply((extra.apply(Block.Properties.ofFullCopy(block))));
	}

	public static <T extends Block> DeferredBlock<T> _register(String name, Supplier<T> block) {
		return (DeferredBlock<T>) BLOCKS.register(name, block);
	}

	public static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> block) {
		DeferredBlock<T> ro = _register(name, block);
		registerBlockItem(name, ro);
		return ro;
	}

	public static DeferredBlock<Block> block(String name, Block prop) {
		return register(name, prop, a -> a, Block::new);
	}

	public static DeferredBlock<Block> cutout(String name, Block prop) {
		return register(name, prop, a -> a.noOcclusion(), Block::new);
	}

	public static DeferredBlock<FallingBlock> falling(String name, int dustRGBA, Block prop) {
		return register(name, prop, a -> a, p -> new ColoredFallingBlock(new ColorRGBA(dustRGBA), p));
	}

	public static DeferredBlock<SlabBlock> slab(String name, Block prop) {
		return register(name, prop, a -> a, SlabBlock::new);
	}

	public static DeferredBlock<StairBlock> stairs(String name, DeferredBlock<Block> block, Block prop) {
		return register(name, prop, a -> a, p -> new StairBlock(block.get().defaultBlockState(), p));
	}

	public static DeferredBlock<WallBlock> wall(String name, Block prop) {
		return register(name, prop, a -> a.hasPostProcess(BlockInit::always), WallBlock::new);
	}

	public static DeferredBlock<RotatedPillarBlock> log(String name, Block prop) {
		return register(name, prop, a -> a, RotatedPillarBlock::new);
	}

	public static DeferredBlock<LeavesBlock> leaves(String name, Block prop) {
		return register(name, prop, a -> a, LeavesBlock::new);
	}

	public static DeferredBlock<MachinaSignBlock> sign(String name, Block prop, WoodType wood) {
		DeferredBlock<MachinaSignBlock> s = registerNI(name, prop, a -> a, p -> new MachinaSignBlock(p, wood));
		SIGNS.add(s);
		return s;
	}

	public static DeferredBlock<MachinaWallSignBlock> wall_sign(String name, Block prop, WoodType wood) {
		DeferredBlock<MachinaWallSignBlock> s = registerNI(name, prop, a -> a, p -> new MachinaWallSignBlock(p, wood));
		SIGNS.add(s);
		return s;
	}

	public static DeferredBlock<MachinaHangingSignBlock> hanging_sign(String name, Block prop, WoodType wood) {
		DeferredBlock<MachinaHangingSignBlock> s = registerNI(name, prop, a -> a,
				p -> new MachinaHangingSignBlock(p, wood));
		HANGING_SIGNS.add(s);
		return s;
	}

	public static DeferredBlock<MachinaHangingWallSignBlock> wall_hanging_sign(String name, Block prop, WoodType wood) {
		DeferredBlock<MachinaHangingWallSignBlock> s = registerNI(name, prop, a -> a,
				p -> new MachinaHangingWallSignBlock(p, wood));
		HANGING_SIGNS.add(s);
		return s;
	}

	public static DeferredBlock<ButtonBlock> wood_button(String name, Block prop, WoodType wood) {
		return register(name, prop, a -> a, p -> new ButtonBlock(wood.setType(), 30, p));
	}

	public static DeferredBlock<DoorBlock> wood_door(String name, Block prop, WoodType wood) {
		return register(name, prop, a -> a, p -> new DoorBlock(wood.setType(), p));
	}

	public static DeferredBlock<TrapDoorBlock> wood_trapdoor(String name, Block prop, WoodType wood) {
		return register(name, prop, a -> a, p -> new TrapDoorBlock(wood.setType(), p));
	}

	public static DeferredBlock<FenceBlock> fence(String name, Block prop) {
		return register(name, prop, a -> a, FenceBlock::new);
	}

	public static DeferredBlock<FenceGateBlock> fence_gate(String name, Block prop, WoodType wood) {
		return register(name, prop, a -> a, p -> new FenceGateBlock(wood, p));
	}

	public static DeferredBlock<PressurePlateBlock> wood_pressure_plate(String name, Block prop, WoodType wood) {
		return register(name, prop, a -> a, p -> new PressurePlateBlock(wood.setType(), p));
	}

	public static DeferredBlock<ButtonBlock> stone_button(String name, Block prop) {
		return register(name, prop, a -> a, p -> new ButtonBlock(BlockSetType.STONE, 20, p));
	}

	public static DeferredBlock<PressurePlateBlock> stone_pressure_plate(String name, Block prop) {
		return register(name, prop, a -> a, p -> new PressurePlateBlock(BlockSetType.STONE, p));
	}

	public static DeferredBlock<FlowerBlock> flower(String name, Holder<MobEffect> effect, int duration, Block prop,
			Function<Block.Properties, Block.Properties> extra) {
		return register(name, prop, extra, p -> new FlowerBlock(effect, duration, p));
	}

	public static DeferredBlock<FlowerBlock> flower(String name, Holder<MobEffect> effect, int duration, Block prop) {
		return register(name, prop, a -> a, p -> new FlowerBlock(effect, duration, p));
	}

	public static DeferredBlock<TallFlowerBlock> tall_flower(String name, Block prop) {
		return register(name, prop, a -> a, TallFlowerBlock::new);
	}

	public static DeferredBlock<FlowerPotBlock> flower_pot(String name, DeferredBlock<? extends Block> flower) {
		return _register(name, BlockInit.of(Blocks.FLOWER_POT, a -> a, p -> new FlowerPotBlock(null, flower, p)));
	}

	public static DeferredBlock<FlowerPotBlock> flower_pot(String name, DeferredBlock<FlowerBlock> flower,
			Function<Block.Properties, Block.Properties> extra) {
		return _register(name, BlockInit.of(Blocks.FLOWER_POT, extra, p -> new FlowerPotBlock(null, flower, p)));
	}

	public static DeferredBlock<PebbleBlock> pebbles(String name) {
		return register(name, Blocks.ANDESITE, a -> a.noCollission().noOcclusion(), PebbleBlock::new);
	}

	public static DeferredBlock<SmallFlowerBlock> groundlily(String name) {
		return register(name, Blocks.PINK_PETALS, a -> a, SmallFlowerBlock::new);
	}

	public static DeferredBlock<CrystalBlock> chemical(String name, Block props, String chem) {
		return registerCI(name, props, a -> a, CrystalBlock::new,
				ro -> new ChemicalBlockItem(ro.get(), new Item.Properties(), chem));
	}

	public static DeferredBlock<MachinaWaterlilyBlock> waterlily(String name) {
		return registerCI(name, Blocks.LILY_PAD, a -> a, MachinaWaterlilyBlock::new,
				ro -> new PlaceOnWaterBlockItem(ro.get(), new Item.Properties()));
	}

	public static <T extends Block> DeferredBlock<T> register(String name, Block prop,
			Function<Block.Properties, T> constructor) {
		return register(name, prop, a -> a, constructor);
	}

	public static <T extends Block> DeferredBlock<T> registerNI(String name, Block prop,
			Function<Block.Properties, Block.Properties> extra, Function<Block.Properties, T> constructor) {
		return _register(name, BlockInit.of(prop, extra, constructor));
	}

	public static <T extends Block> DeferredBlock<T> registerCI(String name, Block prop,
			Function<Block.Properties, Block.Properties> extra, Function<Block.Properties, T> constructor,
			Function<DeferredBlock<T>, ? extends BlockItem> item) {
		DeferredBlock<T> ro = _register(name, BlockInit.of(prop, extra, constructor));
		ItemInit.ITEMS.register(name, () -> item.apply(ro));
		return ro;
	}

	public static <T extends Block> DeferredBlock<T> register(String name, Block prop,
			Function<Block.Properties, Block.Properties> extra, Function<Block.Properties, T> constructor) {
		DeferredBlock<T> ro = _register(name, BlockInit.of(prop, extra, constructor));
		registerBlockItem(name, ro);
		return ro;
	}

	private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
		ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}

	private static boolean always(BlockState state, BlockGetter getter, BlockPos pos) {
		return true;
	}
}
