package com.machina.registration.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.machina.Machina;
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
import com.machina.block.machine.BatteryBlock;
import com.machina.block.machine.CompressorBlock;
import com.machina.block.machine.CreativeBatteryBlock;
import com.machina.block.machine.FurnaceGeneratorBlock;
import com.machina.block.machine.GrinderBlock;
import com.machina.block.machine.MachineCaseBlock;
import com.machina.block.machine.TankBlock;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.DoorBlock;
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
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	public static final List<RegistryObject<? extends Block>> SIGNS = new ArrayList<>();
	public static final List<RegistryObject<? extends Block>> HANGING_SIGNS = new ArrayList<>();

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			Machina.MOD_ID);

	//@formatter:off
	public static final RegistryObject<ItemConduitBlock> ITEM_CONDUIT = register("item_conduit", ItemConduitBlock::new);
	public static final RegistryObject<EnergyCableBlock> ENERGY_CABLE = register("energy_cable", EnergyCableBlock::new);
	public static final RegistryObject<FluidPipeBlock> FLUID_PIPE = register("fluid_pipe", FluidPipeBlock::new);
	public static final RegistryObject<BatteryBlock> BATTERY = register("battery", Blocks.IRON_BLOCK, BatteryBlock::new);
	public static final RegistryObject<TankBlock> TANK = register("tank", Blocks.IRON_BLOCK, TankBlock::new);
	public static final RegistryObject<CreativeBatteryBlock> CREATIVE_BATTERY = register("creative_battery", Blocks.IRON_BLOCK, CreativeBatteryBlock::new);
	public static final RegistryObject<MachineCaseBlock> BASIC_MACHINE_CASE = register("basic_machine_case", Blocks.IRON_BLOCK, MachineCaseBlock::new);
	public static final RegistryObject<FurnaceGeneratorBlock> FURNACE_GENERATOR = register("furnace_generator", Blocks.IRON_BLOCK, FurnaceGeneratorBlock::new);
	public static final RegistryObject<GrinderBlock> GRINDER = register("grinder", Blocks.IRON_BLOCK, GrinderBlock::new);
	public static final RegistryObject<CompressorBlock> COMPRESSOR = register("compressor", Blocks.IRON_BLOCK, CompressorBlock::new);
	
	public static final RegistryObject<Block> ALUMINUM_BLOCK = block("aluminum_block", Blocks.IRON_BLOCK);
	public static final RegistryObject<Block> ALUMINUM_ORE = block("aluminum_ore", Blocks.IRON_ORE);

	public static final RegistryObject<Block> ANTHRACITE = block("anthracite", Blocks.ANDESITE);
	public static final RegistryObject<SlabBlock> ANTHRACITE_SLAB = slab("anthracite_slab", Blocks.ANDESITE_SLAB);
	public static final RegistryObject<StairBlock> ANTHRACITE_STAIRS = stairs("anthracite_stairs", ANTHRACITE, Blocks.ANDESITE_SLAB);
	public static final RegistryObject<WallBlock> ANTHRACITE_WALL = wall("anthracite_wall", Blocks.ANDESITE_WALL);
	public static final RegistryObject<PressurePlateBlock> ANTHRACITE_PRESSURE_PLATE = stone_pressure_plate("anthracite_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final RegistryObject<ButtonBlock> ANTHRACITE_BUTTON = stone_button("anthracite_button", Blocks.STONE_BUTTON);
	public static final RegistryObject<PebbleBlock> ANTHRACITE_PEBBLES = pebbles("anthracite_pebbles");
	
	public static final RegistryObject<Block> FELDSPAR = block("feldspar", Blocks.ANDESITE);
	public static final RegistryObject<SlabBlock> FELDSPAR_SLAB = slab("feldspar_slab", Blocks.ANDESITE_SLAB);
	public static final RegistryObject<StairBlock> FELDSPAR_STAIRS = stairs("feldspar_stairs", FELDSPAR, Blocks.ANDESITE_SLAB);
	public static final RegistryObject<WallBlock> FELDSPAR_WALL = wall("feldspar_wall", Blocks.ANDESITE_WALL);
	public static final RegistryObject<PressurePlateBlock> FELDSPAR_PRESSURE_PLATE = stone_pressure_plate("feldspar_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final RegistryObject<ButtonBlock> FELDSPAR_BUTTON = stone_button("feldspar_button", Blocks.STONE_BUTTON);
	public static final RegistryObject<PebbleBlock> FELDSPAR_PEBBLES = pebbles("feldspar_pebbles");
	
	public static final RegistryObject<Block> GRAY_SOAPSTONE = block("gray_soapstone", Blocks.ANDESITE);
	public static final RegistryObject<SlabBlock> GRAY_SOAPSTONE_SLAB = slab("gray_soapstone_slab", Blocks.ANDESITE_SLAB);
	public static final RegistryObject<StairBlock> GRAY_SOAPSTONE_STAIRS = stairs("gray_soapstone_stairs", GRAY_SOAPSTONE, Blocks.ANDESITE_SLAB);
	public static final RegistryObject<WallBlock> GRAY_SOAPSTONE_WALL = wall("gray_soapstone_wall", Blocks.ANDESITE_WALL);
	public static final RegistryObject<PressurePlateBlock> GRAY_SOAPSTONE_PRESSURE_PLATE = stone_pressure_plate("gray_soapstone_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final RegistryObject<ButtonBlock> GRAY_SOAPSTONE_BUTTON = stone_button("gray_soapstone_button", Blocks.STONE_BUTTON);
	public static final RegistryObject<PebbleBlock> GRAY_SOAPSTONE_PEBBLES = pebbles("gray_soapstone_pebbles");
	
	public static final RegistryObject<Block> GREEN_SOAPSTONE = block("green_soapstone", Blocks.ANDESITE);
	public static final RegistryObject<SlabBlock> GREEN_SOAPSTONE_SLAB = slab("green_soapstone_slab", Blocks.ANDESITE_SLAB);
	public static final RegistryObject<StairBlock> GREEN_SOAPSTONE_STAIRS = stairs("green_soapstone_stairs", GREEN_SOAPSTONE, Blocks.ANDESITE_SLAB);
	public static final RegistryObject<WallBlock> GREEN_SOAPSTONE_WALL = wall("green_soapstone_wall", Blocks.ANDESITE_WALL);
	public static final RegistryObject<PressurePlateBlock> GREEN_SOAPSTONE_PRESSURE_PLATE = stone_pressure_plate("green_soapstone_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final RegistryObject<ButtonBlock> GREEN_SOAPSTONE_BUTTON = stone_button("green_soapstone_button", Blocks.STONE_BUTTON);
	public static final RegistryObject<PebbleBlock> GREEN_SOAPSTONE_PEBBLES = pebbles("green_soapstone_pebbles");
	
	public static final RegistryObject<Block> WHITE_SOAPSTONE = block("white_soapstone", Blocks.ANDESITE);
	public static final RegistryObject<SlabBlock> WHITE_SOAPSTONE_SLAB = slab("white_soapstone_slab", Blocks.ANDESITE_SLAB);
	public static final RegistryObject<StairBlock> WHITE_SOAPSTONE_STAIRS = stairs("white_soapstone_stairs", WHITE_SOAPSTONE, Blocks.ANDESITE_SLAB);
	public static final RegistryObject<WallBlock> WHITE_SOAPSTONE_WALL = wall("white_soapstone_wall", Blocks.ANDESITE_WALL);
	public static final RegistryObject<PressurePlateBlock> WHITE_SOAPSTONE_PRESSURE_PLATE = stone_pressure_plate("white_soapstone_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final RegistryObject<ButtonBlock> WHITE_SOAPSTONE_BUTTON = stone_button("white_soapstone_button", Blocks.STONE_BUTTON);
	public static final RegistryObject<PebbleBlock> WHITE_SOAPSTONE_PEBBLES = pebbles("white_soapstone_pebbles");
	
	public static final RegistryObject<Block> SHALE = block("shale", Blocks.ANDESITE);
	public static final RegistryObject<SlabBlock> SHALE_SLAB = slab("shale_slab", Blocks.ANDESITE_SLAB);
	public static final RegistryObject<StairBlock> SHALE_STAIRS = stairs("shale_stairs", SHALE, Blocks.ANDESITE_SLAB);
	public static final RegistryObject<WallBlock> SHALE_WALL = wall("shale_wall", Blocks.ANDESITE_WALL);
	public static final RegistryObject<PressurePlateBlock> SHALE_PRESSURE_PLATE = stone_pressure_plate("shale_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final RegistryObject<ButtonBlock> SHALE_BUTTON = stone_button("shale_button", Blocks.STONE_BUTTON);
	public static final RegistryObject<PebbleBlock> SHALE_PEBBLES = pebbles("shale_pebbles");
	
	public static final RegistryObject<Block> TECTONITE = block("tectonite", Blocks.ANDESITE);
	public static final RegistryObject<SlabBlock> TECTONITE_SLAB = slab("tectonite_slab", Blocks.ANDESITE_SLAB);
	public static final RegistryObject<StairBlock> TECTONITE_STAIRS = stairs("tectonite_stairs", TECTONITE, Blocks.ANDESITE_SLAB);
	public static final RegistryObject<WallBlock> TECTONITE_WALL = wall("tectonite_wall", Blocks.ANDESITE_WALL);
	public static final RegistryObject<PressurePlateBlock> TECTONITE_PRESSURE_PLATE = stone_pressure_plate("tectonite_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final RegistryObject<ButtonBlock> TECTONITE_BUTTON = stone_button("tectonite_button", Blocks.STONE_BUTTON);
	public static final RegistryObject<PebbleBlock> TECTONITE_PEBBLES = pebbles("tectonite_pebbles");
	
	public static final RegistryObject<Block> MARBLE = block("marble", Blocks.ANDESITE);
	public static final RegistryObject<SlabBlock> MARBLE_SLAB = slab("marble_slab", Blocks.ANDESITE_SLAB);
	public static final RegistryObject<StairBlock> MARBLE_STAIRS = stairs("marble_stairs", MARBLE, Blocks.ANDESITE_SLAB);
	public static final RegistryObject<WallBlock> MARBLE_WALL = wall("marble_wall", Blocks.ANDESITE_WALL);
	public static final RegistryObject<PressurePlateBlock> MARBLE_PRESSURE_PLATE = stone_pressure_plate("marble_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final RegistryObject<ButtonBlock> MARBLE_BUTTON = stone_button("marble_button", Blocks.STONE_BUTTON);
	public static final RegistryObject<PebbleBlock> MARBLE_PEBBLES = pebbles("marble_pebbles");
	
	public static final RegistryObject<Block> CHALK = block("chalk", Blocks.ANDESITE);
	public static final RegistryObject<SlabBlock> CHALK_SLAB = slab("chalk_slab", Blocks.ANDESITE_SLAB);
	public static final RegistryObject<StairBlock> CHALK_STAIRS = stairs("chalk_stairs", CHALK, Blocks.ANDESITE_SLAB);
	public static final RegistryObject<WallBlock> CHALK_WALL = wall("chalk_wall", Blocks.ANDESITE_WALL);
	public static final RegistryObject<PressurePlateBlock> CHALK_PRESSURE_PLATE = stone_pressure_plate("chalk_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final RegistryObject<ButtonBlock> CHALK_BUTTON = stone_button("chalk_button", Blocks.STONE_BUTTON);
	public static final RegistryObject<PebbleBlock> CHALK_PEBBLES = pebbles("chalk_pebbles");
	
	public static final RegistryObject<Block> LIMESTONE = block("limestone", Blocks.ANDESITE);
	public static final RegistryObject<SlabBlock> LIMESTONE_SLAB = slab("limestone_slab", Blocks.ANDESITE_SLAB);
	public static final RegistryObject<StairBlock> LIMESTONE_STAIRS = stairs("limestone_stairs", LIMESTONE, Blocks.ANDESITE_SLAB);
	public static final RegistryObject<WallBlock> LIMESTONE_WALL = wall("limestone_wall", Blocks.ANDESITE_WALL);
	public static final RegistryObject<PressurePlateBlock> LIMESTONE_PRESSURE_PLATE = stone_pressure_plate("limestone_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final RegistryObject<ButtonBlock> LIMESTONE_BUTTON = stone_button("limestone_button", Blocks.STONE_BUTTON);
	public static final RegistryObject<PebbleBlock> LIMESTONE_PEBBLES = pebbles("limestone_pebbles");
	
	public static final RegistryObject<Block> MIGMATITE = block("migmatite", Blocks.ANDESITE);
	public static final RegistryObject<SlabBlock> MIGMATITE_SLAB = slab("migmatite_slab", Blocks.ANDESITE_SLAB);
	public static final RegistryObject<StairBlock> MIGMATITE_STAIRS = stairs("migmatite_stairs", MIGMATITE, Blocks.ANDESITE_SLAB);
	public static final RegistryObject<WallBlock> MIGMATITE_WALL = wall("migmatite_wall", Blocks.ANDESITE_WALL);
	public static final RegistryObject<PressurePlateBlock> MIGMATITE_PRESSURE_PLATE = stone_pressure_plate("migmatite_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final RegistryObject<ButtonBlock> MIGMATITE_BUTTON = stone_button("migmatite_button", Blocks.STONE_BUTTON);
	public static final RegistryObject<PebbleBlock> MIGMATITE_PEBBLES = pebbles("migmatite_pebbles");
	
	public static final RegistryObject<Block> GNEISS = block("gneiss", Blocks.ANDESITE);
	public static final RegistryObject<SlabBlock> GNEISS_SLAB = slab("gneiss_slab", Blocks.ANDESITE_SLAB);
	public static final RegistryObject<StairBlock> GNEISS_STAIRS = stairs("gneiss_stairs", GNEISS, Blocks.ANDESITE_SLAB);
	public static final RegistryObject<WallBlock> GNEISS_WALL = wall("gneiss_wall", Blocks.ANDESITE_WALL);
	public static final RegistryObject<PressurePlateBlock> GNEISS_PRESSURE_PLATE = stone_pressure_plate("gneiss_pressure_plate", Blocks.STONE_PRESSURE_PLATE);
	public static final RegistryObject<ButtonBlock> GNEISS_BUTTON = stone_button("gneiss_button", Blocks.STONE_BUTTON);
	public static final RegistryObject<PebbleBlock> GNEISS_PEBBLES = pebbles("gneiss_pebbles");
	
	public static final RegistryObject<Block> TROPICAL_GRASS_BLOCK = block("tropical_grass_block", Blocks.GRASS_BLOCK);
	public static final RegistryObject<Block> FOREST_GRASS_BLOCK = block("forest_grass_block", Blocks.GRASS_BLOCK);
	public static final RegistryObject<Block> CONIFEROUS_GRASS_BLOCK = block("coniferous_grass_block", Blocks.GRASS_BLOCK);
	public static final RegistryObject<Block> WINDSWEPT_GRASS_BLOCK = block("windswept_grass_block", Blocks.GRASS_BLOCK);
	public static final RegistryObject<Block> MYCELIAL_GRASS_BLOCK = block("mycelial_grass_block", Blocks.GRASS_BLOCK);
	
	public static final RegistryObject<Block> TROPICAL_DIRT = block("tropical_dirt", Blocks.DIRT);
	public static final RegistryObject<StairBlock> TROPICAL_DIRT_STAIRS = stairs("tropical_dirt_stairs", TROPICAL_DIRT, Blocks.DIRT);
	public static final RegistryObject<SlabBlock> TROPICAL_DIRT_SLAB = slab("tropical_dirt_slab", Blocks.DIRT);
	
	public static final RegistryObject<Block> FOREST_DIRT = block("forest_dirt", Blocks.DIRT);
	public static final RegistryObject<StairBlock> FOREST_DIRT_STAIRS = stairs("forest_dirt_stairs", FOREST_DIRT, Blocks.DIRT);
	public static final RegistryObject<SlabBlock> FOREST_DIRT_SLAB = slab("forest_dirt_slab", Blocks.DIRT);
	
	public static final RegistryObject<Block> CONIFEROUS_DIRT = block("coniferous_dirt", Blocks.DIRT);
	public static final RegistryObject<StairBlock> CONIFEROUS_DIRT_STAIRS = stairs("coniferous_dirt_stairs", CONIFEROUS_DIRT, Blocks.DIRT);
	public static final RegistryObject<SlabBlock> CONIFEROUS_DIRT_SLAB = slab("coniferous_dirt_slab", Blocks.DIRT);
	
	public static final RegistryObject<Block> WINDSWEPT_DIRT = block("windswept_dirt", Blocks.DIRT);
	public static final RegistryObject<StairBlock> WINDSWEPT_DIRT_STAIRS = stairs("windswept_dirt_stairs", WINDSWEPT_DIRT, Blocks.DIRT);
	public static final RegistryObject<SlabBlock> WINDSWEPT_DIRT_SLAB = slab("windswept_dirt_slab", Blocks.DIRT);
	
	public static final RegistryObject<Block> MYCELIAL_DIRT = block("mycelial_dirt", Blocks.DIRT);
	public static final RegistryObject<StairBlock> MYCELIAL_DIRT_STAIRS = stairs("mycelial_dirt_stairs", MYCELIAL_DIRT, Blocks.DIRT);
	public static final RegistryObject<SlabBlock> MYCELIAL_DIRT_SLAB = slab("mycelial_dirt_slab", Blocks.DIRT);
	
	public static final RegistryObject<Block> SILT = block("silt", Blocks.MUD);
	public static final RegistryObject<StairBlock> SILT_STAIRS = stairs("silt_stairs", SILT, Blocks.MUD);
	public static final RegistryObject<SlabBlock> SILT_SLAB = slab("silt_slab", Blocks.MUD);
	
	public static final RegistryObject<Block> PEAT = block("peat", Blocks.MUD);
	public static final RegistryObject<StairBlock> PEAT_STAIRS = stairs("peat_stairs", PEAT, Blocks.MUD);
	public static final RegistryObject<SlabBlock> PEAT_SLAB = slab("peat_slab", Blocks.MUD);
	
	public static final WoodType TROPICAL = registerWoodType("tropical");
	public static final WoodType DEAD_TROPICAL = registerWoodType("dead_tropical");
	public static final WoodType PINE = registerWoodType("pine");
	public static final WoodType CONIFEROUS = registerWoodType("coniferous");
	public static final WoodType CYCAD = registerWoodType("cycad");
	
	public static final RegistryObject<RotatedPillarBlock> TROPICAL_LOG = log("tropical_log", Blocks.OAK_LOG);
	public static final RegistryObject<RotatedPillarBlock> TROPICAL_WOOD = log("tropical_wood", Blocks.OAK_WOOD);
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TROPICAL_LOG = log("stripped_tropical_log", Blocks.STRIPPED_OAK_LOG);
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TROPICAL_WOOD = log("stripped_tropical_wood", Blocks.STRIPPED_OAK_WOOD);
	public static final RegistryObject<Block> TROPICAL_PLANKS = block("tropical_planks", Blocks.OAK_PLANKS);
	public static final RegistryObject<LeavesBlock> TROPICAL_LEAVES = leaves("tropical_leaves", Blocks.OAK_LEAVES);
	public static final RegistryObject<SlabBlock> TROPICAL_SLAB = slab("tropical_slab", Blocks.OAK_SLAB);
	public static final RegistryObject<StairBlock> TROPICAL_STAIRS = stairs("tropical_stairs", TROPICAL_PLANKS, Blocks.OAK_STAIRS);
	public static final RegistryObject<MachinaSignBlock> TROPICAL_SIGN = sign("tropical_sign", Blocks.OAK_SIGN, TROPICAL);
	public static final RegistryObject<MachinaWallSignBlock> TROPICAL_WALL_SIGN = wall_sign("tropical_wall_sign", Blocks.OAK_WALL_SIGN, TROPICAL);
	public static final RegistryObject<MachinaHangingSignBlock> TROPICAL_HANGING_SIGN = hanging_sign("tropical_hanging_sign", Blocks.OAK_HANGING_SIGN, TROPICAL);
	public static final RegistryObject<MachinaHangingWallSignBlock> TROPICAL_WALL_HANGING_SIGN = wall_hanging_sign("tropical_wall_hanging_sign", Blocks.OAK_WALL_HANGING_SIGN, TROPICAL);
	public static final RegistryObject<ButtonBlock> TROPICAL_BUTTON = wood_button("tropical_button", Blocks.OAK_BUTTON, TROPICAL);
	public static final RegistryObject<DoorBlock> TROPICAL_DOOR = wood_door("tropical_door", Blocks.OAK_DOOR, TROPICAL);
	public static final RegistryObject<TrapDoorBlock> TROPICAL_TRAPDOOR = wood_trapdoor("tropical_trapdoor", Blocks.OAK_TRAPDOOR, TROPICAL);
	public static final RegistryObject<FenceBlock> TROPICAL_FENCE = fence("tropical_fence", Blocks.OAK_FENCE);
	public static final RegistryObject<FenceGateBlock> TROPICAL_FENCE_GATE = fence_gate("tropical_fence_gate", Blocks.OAK_FENCE_GATE, TROPICAL);
	public static final RegistryObject<PressurePlateBlock> TROPICAL_PRESSURE_PLATE = wood_pressure_plate("tropical_pressure_plate", Blocks.OAK_PRESSURE_PLATE, TROPICAL);
	
	public static final RegistryObject<RotatedPillarBlock> DEAD_TROPICAL_LOG = log("dead_tropical_log", Blocks.OAK_LOG);
	public static final RegistryObject<RotatedPillarBlock> DEAD_TROPICAL_WOOD = log("dead_tropical_wood", Blocks.OAK_WOOD);
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_DEAD_TROPICAL_LOG = log("stripped_dead_tropical_log", Blocks.STRIPPED_OAK_LOG);
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_DEAD_TROPICAL_WOOD = log("stripped_dead_tropical_wood", Blocks.STRIPPED_OAK_WOOD);
	public static final RegistryObject<Block> DEAD_TROPICAL_PLANKS = block("dead_tropical_planks", Blocks.OAK_PLANKS);
	public static final RegistryObject<LeavesBlock> DEAD_TROPICAL_LEAVES = leaves("dead_tropical_leaves", Blocks.OAK_LEAVES);
	public static final RegistryObject<SlabBlock> DEAD_TROPICAL_SLAB = slab("dead_tropical_slab", Blocks.OAK_SLAB);
	public static final RegistryObject<StairBlock> DEAD_TROPICAL_STAIRS = stairs("dead_tropical_stairs", DEAD_TROPICAL_PLANKS, Blocks.OAK_STAIRS);
	public static final RegistryObject<MachinaSignBlock> DEAD_TROPICAL_SIGN = sign("dead_tropical_sign", Blocks.OAK_SIGN, DEAD_TROPICAL);
	public static final RegistryObject<MachinaWallSignBlock> DEAD_TROPICAL_WALL_SIGN = wall_sign("dead_tropical_wall_sign", Blocks.OAK_WALL_SIGN, DEAD_TROPICAL);
	public static final RegistryObject<MachinaHangingSignBlock> DEAD_TROPICAL_HANGING_SIGN = hanging_sign("dead_tropical_hanging_sign", Blocks.OAK_HANGING_SIGN, DEAD_TROPICAL);
	public static final RegistryObject<MachinaHangingWallSignBlock> DEAD_TROPICAL_WALL_HANGING_SIGN = wall_hanging_sign("dead_tropical_wall_hanging_sign", Blocks.OAK_WALL_HANGING_SIGN, DEAD_TROPICAL);
	public static final RegistryObject<ButtonBlock> DEAD_TROPICAL_BUTTON = wood_button("dead_tropical_button", Blocks.OAK_BUTTON, DEAD_TROPICAL);
	public static final RegistryObject<DoorBlock> DEAD_TROPICAL_DOOR = wood_door("dead_tropical_door", Blocks.OAK_DOOR, DEAD_TROPICAL);
	public static final RegistryObject<TrapDoorBlock> DEAD_TROPICAL_TRAPDOOR = wood_trapdoor("dead_tropical_trapdoor", Blocks.OAK_TRAPDOOR, DEAD_TROPICAL);
	public static final RegistryObject<FenceBlock> DEAD_TROPICAL_FENCE = fence("dead_tropical_fence", Blocks.OAK_FENCE);
	public static final RegistryObject<FenceGateBlock> DEAD_TROPICAL_FENCE_GATE = fence_gate("dead_tropical_fence_gate", Blocks.OAK_FENCE_GATE, DEAD_TROPICAL);
	public static final RegistryObject<PressurePlateBlock> DEAD_TROPICAL_PRESSURE_PLATE = wood_pressure_plate("dead_tropical_pressure_plate", Blocks.OAK_PRESSURE_PLATE, TROPICAL);
	
	public static final RegistryObject<RotatedPillarBlock> PINE_LOG = log("pine_log", Blocks.OAK_LOG);
	public static final RegistryObject<RotatedPillarBlock> PINE_WOOD = log("pine_wood", Blocks.OAK_WOOD);
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_PINE_LOG = log("stripped_pine_log", Blocks.STRIPPED_OAK_LOG);
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_PINE_WOOD = log("stripped_pine_wood", Blocks.STRIPPED_OAK_WOOD);
	public static final RegistryObject<Block> PINE_PLANKS = block("pine_planks", Blocks.OAK_PLANKS);
	public static final RegistryObject<LeavesBlock> PINE_LEAVES = leaves("pine_leaves", Blocks.OAK_LEAVES);
	public static final RegistryObject<SlabBlock> PINE_SLAB = slab("pine_slab", Blocks.OAK_SLAB);
	public static final RegistryObject<StairBlock> PINE_STAIRS = stairs("pine_stairs", PINE_PLANKS, Blocks.OAK_STAIRS);
	public static final RegistryObject<MachinaSignBlock> PINE_SIGN = sign("pine_sign", Blocks.OAK_SIGN, PINE);
	public static final RegistryObject<MachinaWallSignBlock> PINE_WALL_SIGN = wall_sign("pine_wall_sign", Blocks.OAK_WALL_SIGN, PINE);
	public static final RegistryObject<MachinaHangingSignBlock> PINE_HANGING_SIGN = hanging_sign("pine_hanging_sign", Blocks.OAK_HANGING_SIGN, PINE);
	public static final RegistryObject<MachinaHangingWallSignBlock> PINE_WALL_HANGING_SIGN = wall_hanging_sign("pine_wall_hanging_sign", Blocks.OAK_WALL_HANGING_SIGN, PINE);
	public static final RegistryObject<ButtonBlock> PINE_BUTTON = wood_button("pine_button", Blocks.OAK_BUTTON, PINE);
	public static final RegistryObject<DoorBlock> PINE_DOOR = wood_door("pine_door", Blocks.OAK_DOOR, PINE);
	public static final RegistryObject<TrapDoorBlock> PINE_TRAPDOOR = wood_trapdoor("pine_trapdoor", Blocks.OAK_TRAPDOOR, PINE);
	public static final RegistryObject<FenceBlock> PINE_FENCE = fence("pine_fence", Blocks.OAK_FENCE);
	public static final RegistryObject<FenceGateBlock> PINE_FENCE_GATE = fence_gate("pine_fence_gate", Blocks.OAK_FENCE_GATE, PINE);
	public static final RegistryObject<PressurePlateBlock> PINE_PRESSURE_PLATE = wood_pressure_plate("pine_pressure_plate", Blocks.OAK_PRESSURE_PLATE, PINE);
	
	public static final RegistryObject<RotatedPillarBlock> CONIFEROUS_LOG = log("coniferous_log", Blocks.OAK_LOG);
	public static final RegistryObject<RotatedPillarBlock> CONIFEROUS_WOOD = log("coniferous_wood", Blocks.OAK_WOOD);
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_CONIFEROUS_LOG = log("stripped_coniferous_log", Blocks.STRIPPED_OAK_LOG);
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_CONIFEROUS_WOOD = log("stripped_coniferous_wood", Blocks.STRIPPED_OAK_WOOD);
	public static final RegistryObject<Block> CONIFEROUS_PLANKS = block("coniferous_planks", Blocks.OAK_PLANKS);
	public static final RegistryObject<LeavesBlock> GREEN_CONIFEROUS_LEAVES = leaves("green_coniferous_leaves", Blocks.OAK_LEAVES);
	public static final RegistryObject<LeavesBlock> YELLOW_CONIFEROUS_LEAVES = leaves("yellow_coniferous_leaves", Blocks.OAK_LEAVES);
	public static final RegistryObject<LeavesBlock> ORANGE_CONIFEROUS_LEAVES = leaves("orange_coniferous_leaves", Blocks.OAK_LEAVES);
	public static final RegistryObject<LeavesBlock> RED_CONIFEROUS_LEAVES = leaves("red_coniferous_leaves", Blocks.OAK_LEAVES);
	public static final RegistryObject<SlabBlock> CONIFEROUS_SLAB = slab("coniferous_slab", Blocks.OAK_SLAB);
	public static final RegistryObject<StairBlock> CONIFEROUS_STAIRS = stairs("coniferous_stairs", CONIFEROUS_PLANKS, Blocks.OAK_STAIRS);
	public static final RegistryObject<MachinaSignBlock> CONIFEROUS_SIGN = sign("coniferous_sign", Blocks.OAK_SIGN, CONIFEROUS);
	public static final RegistryObject<MachinaWallSignBlock> CONIFEROUS_WALL_SIGN = wall_sign("coniferous_wall_sign", Blocks.OAK_WALL_SIGN, CONIFEROUS);
	public static final RegistryObject<MachinaHangingSignBlock> CONIFEROUS_HANGING_SIGN = hanging_sign("coniferous_hanging_sign", Blocks.OAK_HANGING_SIGN, CONIFEROUS);
	public static final RegistryObject<MachinaHangingWallSignBlock> CONIFEROUS_WALL_HANGING_SIGN = wall_hanging_sign("coniferous_wall_hanging_sign", Blocks.OAK_WALL_HANGING_SIGN, CONIFEROUS);
	public static final RegistryObject<ButtonBlock> CONIFEROUS_BUTTON = wood_button("coniferous_button", Blocks.OAK_BUTTON, CONIFEROUS);
	public static final RegistryObject<DoorBlock> CONIFEROUS_DOOR = wood_door("coniferous_door", Blocks.OAK_DOOR, CONIFEROUS);
	public static final RegistryObject<TrapDoorBlock> CONIFEROUS_TRAPDOOR = wood_trapdoor("coniferous_trapdoor", Blocks.OAK_TRAPDOOR, CONIFEROUS);
	public static final RegistryObject<FenceBlock> CONIFEROUS_FENCE = fence("coniferous_fence", Blocks.OAK_FENCE);
	public static final RegistryObject<FenceGateBlock> CONIFEROUS_FENCE_GATE = fence_gate("coniferous_fence_gate", Blocks.OAK_FENCE_GATE, CONIFEROUS);
	public static final RegistryObject<PressurePlateBlock> CONIFEROUS_PRESSURE_PLATE = wood_pressure_plate("coniferous_pressure_plate", Blocks.OAK_PRESSURE_PLATE, CONIFEROUS);
	
	public static final RegistryObject<RotatedPillarBlock> CYCAD_LOG = log("cycad_log", Blocks.OAK_LOG);
	public static final RegistryObject<RotatedPillarBlock> CYCAD_WOOD = log("cycad_wood", Blocks.OAK_WOOD);
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_CYCAD_LOG = log("stripped_cycad_log", Blocks.STRIPPED_OAK_LOG);
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_CYCAD_WOOD = log("stripped_cycad_wood", Blocks.STRIPPED_OAK_WOOD);
	public static final RegistryObject<Block> CYCAD_PLANKS = block("cycad_planks", Blocks.OAK_PLANKS);
	public static final RegistryObject<LeavesBlock> CYCAD_LEAVES = leaves("cycad_leaves", Blocks.OAK_LEAVES);
	public static final RegistryObject<SlabBlock> CYCAD_SLAB = slab("cycad_slab", Blocks.OAK_SLAB);
	public static final RegistryObject<StairBlock> CYCAD_STAIRS = stairs("cycad_stairs", CYCAD_PLANKS, Blocks.OAK_STAIRS);
	public static final RegistryObject<MachinaSignBlock> CYCAD_SIGN = sign("cycad_sign", Blocks.OAK_SIGN, CYCAD);
	public static final RegistryObject<MachinaWallSignBlock> CYCAD_WALL_SIGN = wall_sign("cycad_wall_sign", Blocks.OAK_WALL_SIGN, CYCAD);
	public static final RegistryObject<MachinaHangingSignBlock> CYCAD_HANGING_SIGN = hanging_sign("cycad_hanging_sign", Blocks.OAK_HANGING_SIGN, CYCAD);
	public static final RegistryObject<MachinaHangingWallSignBlock> CYCAD_WALL_HANGING_SIGN = wall_hanging_sign("cycad_wall_hanging_sign", Blocks.OAK_WALL_HANGING_SIGN, CYCAD);
	public static final RegistryObject<ButtonBlock> CYCAD_BUTTON = wood_button("cycad_button", Blocks.OAK_BUTTON, CYCAD);
	public static final RegistryObject<DoorBlock> CYCAD_DOOR = wood_door("cycad_door", Blocks.OAK_DOOR, CYCAD);
	public static final RegistryObject<TrapDoorBlock> CYCAD_TRAPDOOR = wood_trapdoor("cycad_trapdoor", Blocks.OAK_TRAPDOOR, CYCAD);
	public static final RegistryObject<FenceBlock> CYCAD_FENCE = fence("cycad_fence", Blocks.OAK_FENCE);
	public static final RegistryObject<FenceGateBlock> CYCAD_FENCE_GATE = fence_gate("cycad_fence_gate", Blocks.OAK_FENCE_GATE, CYCAD);
	public static final RegistryObject<PressurePlateBlock> CYCAD_PRESSURE_PLATE = wood_pressure_plate("cycad_pressure_plate", Blocks.OAK_PRESSURE_PLATE, CYCAD);
	
	public static final RegistryObject<Block> BROWN_MUSHROOM_STALK = block("brown_mushroom_stalk", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> GREEN_MUSHROOM_STALK = block("green_mushroom_stalk", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> PURPLE_MUSHROOM_STALK = block("purple_mushroom_stalk", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> YELLOW_MUSHROOM_STALK = block("yellow_mushroom_stalk", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> BROWN_MUSHROOM_CAP = block("brown_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> RED_MUSHROOM_CAP = block("red_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> PURPLE_MUSHROOM_CAP = block("purple_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> YELLOW_MUSHROOM_CAP = block("yellow_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> BROWN_MUSHROOM_GILLS = block("brown_mushroom_gills", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> RED_MUSHROOM_GILLS = block("red_mushroom_gills", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> PURPLE_MUSHROOM_GILLS = block("purple_mushroom_gills", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> YELLOW_MUSHROOM_GILLS = block("yellow_mushroom_gills", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> SPECKLED_BROWN_MUSHROOM_CAP = block("speckled_brown_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> SPECKLED_RED_MUSHROOM_CAP = block("speckled_red_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> SPECKLED_PURPLE_MUSHROOM_CAP = block("speckled_purple_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> SPECKLED_YELLOW_MUSHROOM_CAP = block("speckled_yellow_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> IMBUED_BROWN_MUSHROOM_CAP = block("imbued_brown_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> IMBUED_RED_MUSHROOM_CAP = block("imbued_red_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> IMBUED_PURPLE_MUSHROOM_CAP = block("imbued_purple_mushroom_cap", Blocks.MUSHROOM_STEM);
	public static final RegistryObject<Block> IMBUED_YELLOW_MUSHROOM_CAP = block("imbued_yellow_mushroom_cap", Blocks.MUSHROOM_STEM);

	public static final RegistryObject<BushBlock> TROPICAL_GRASS = register("tropical_grass", Blocks.GRASS, BushBlock::new);
	public static final RegistryObject<BushBlock> TWISTED_GRASS = register("twisted_grass", Blocks.GRASS, BushBlock::new);
	public static final RegistryObject<BushBlock> CONIFEROUS_GRASS = register("coniferous_grass", Blocks.GRASS, BushBlock::new);
	public static final RegistryObject<BushBlock> SHORT_CONIFEROUS_GRASS = register("short_coniferous_grass", Blocks.GRASS, BushBlock::new);
	public static final RegistryObject<BushBlock> WINDSWEPT_GRASS = register("windswept_grass", Blocks.GRASS, BushBlock::new);
	public static final RegistryObject<BushBlock> MYCELIAL_GRASS = register("mycelial_grass", Blocks.GRASS, BushBlock::new);
	
	public static final RegistryObject<SmallFlowerBlock> CLOVER = register("clover", Blocks.PINK_PETALS, SmallFlowerBlock::new);
	public static final RegistryObject<SmallFlowerBlock> SPINDLESPROUT = register("spindlesprout", Blocks.FERN, SmallFlowerBlock::new);
	public static final RegistryObject<SmallFlowerBlock> SMALL_FERN = register("small_fern", Blocks.FERN, SmallFlowerBlock::new);
	public static final RegistryObject<SmallFlowerBlock> DEAD_SMALL_FERN = register("dead_small_fern", Blocks.FERN, SmallFlowerBlock::new);
	public static final RegistryObject<SmallFlowerBlock> NEEDLEGRASS = register("needlegrass", Blocks.FERN, SmallFlowerBlock::new);
	public static final RegistryObject<TallFlowerBlock> NEEDLETHATCH = tall_flower("needlethatch", Blocks.SUNFLOWER);
	public static final RegistryObject<TallFlowerBlock> SPINDLEGRASS = tall_flower("spindlegrass", Blocks.SUNFLOWER);

	public static final RegistryObject<PinkPetalsBlock> PURPLE_PETALS = register("purple_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	public static final RegistryObject<PinkPetalsBlock> RED_PETALS = register("red_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	public static final RegistryObject<PinkPetalsBlock> ORANGE_PETALS = register("orange_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	public static final RegistryObject<PinkPetalsBlock> YELLOW_PETALS = register("yellow_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	public static final RegistryObject<PinkPetalsBlock> GREEN_PETALS = register("green_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	public static final RegistryObject<PinkPetalsBlock> TURQUOISE_PETALS = register("turquoise_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	public static final RegistryObject<PinkPetalsBlock> BLUE_PETALS = register("blue_petals", Blocks.PINK_PETALS, PinkPetalsBlock::new);
	
	public static final RegistryObject<TallFlowerBlock> ORPHEUM = tall_flower("orpheum", Blocks.PEONY);

	public static final RegistryObject<FlowerBlock> DRAGON_PEONY = flower("dragon_peony", () -> MobEffects.LEVITATION, 5, Blocks.DANDELION);
	public static final RegistryObject<FlowerBlock> SPRUCE_CUP = flower("spruce_cup", () -> MobEffects.UNLUCK, 50, Blocks.BROWN_MUSHROOM);
	
	public static final RegistryObject<FlowerPotBlock> POTTED_DRAGON_PEONY = flower_pot("potted_dragon_peony", DRAGON_PEONY);
	public static final RegistryObject<FlowerPotBlock> POTTED_SPRUCE_CUP = flower_pot("potted_spruce_cup", SPRUCE_CUP);
	public static final RegistryObject<FlowerPotBlock> POTTED_SPINDLESPROUT = flower_pot("potted_spindlesprout", SPINDLESPROUT);
	public static final RegistryObject<FlowerPotBlock> POTTED_SMALL_FERN = flower_pot("potted_small_fern", SMALL_FERN);
	public static final RegistryObject<FlowerPotBlock> POTTED_DEAD_SMALL_FERN = flower_pot("potted_dead_small_fern", DEAD_SMALL_FERN);
	public static final RegistryObject<FlowerPotBlock> POTTED_NEEDLEGRASS = flower_pot("potted_needlegrass", NEEDLEGRASS);
	
	public static final RegistryObject<SmallFlowerBlock> PURPLE_GROUNDLILY = groundlily("purple_groundlily");
	public static final RegistryObject<SmallFlowerBlock> PINK_GROUNDLILY = groundlily("pink_groundlily");
	public static final RegistryObject<SmallFlowerBlock> RED_GROUNDLILY = groundlily("red_groundlily");
	public static final RegistryObject<SmallFlowerBlock> ORANGE_GROUNDLILY = groundlily("orange_groundlily");
	public static final RegistryObject<SmallFlowerBlock> YELLOW_GROUNDLILY = groundlily("yellow_groundlily");
	public static final RegistryObject<SmallFlowerBlock> GREEN_GROUNDLILY = groundlily("green_groundlily");
	public static final RegistryObject<SmallFlowerBlock> TURQUOISE_GROUNDLILY = groundlily("turquoise_groundlily");
	public static final RegistryObject<SmallFlowerBlock> BLUE_GROUNDLILY = groundlily("blue_groundlily");
	
	public static final RegistryObject<MachinaWaterlilyBlock> PURPLE_WATERLILY = waterlily("purple_waterlily");
	public static final RegistryObject<MachinaWaterlilyBlock> PINK_WATERLILY = waterlily("pink_waterlily");
	public static final RegistryObject<MachinaWaterlilyBlock> RED_WATERLILY = waterlily("red_waterlily");
	public static final RegistryObject<MachinaWaterlilyBlock> ORANGE_WATERLILY = waterlily("orange_waterlily");
	public static final RegistryObject<MachinaWaterlilyBlock> YELLOW_WATERLILY = waterlily("yellow_waterlily");
	public static final RegistryObject<MachinaWaterlilyBlock> GREEN_WATERLILY = waterlily("green_waterlily");
	public static final RegistryObject<MachinaWaterlilyBlock> TURQUOISE_WATERLILY = waterlily("turquoise_waterlily");
	public static final RegistryObject<MachinaWaterlilyBlock> BLUE_WATERLILY = waterlily("blue_waterlily");

	public static final RegistryObject<FlowerBlock> PURPLE_GLOWSHROOM = flower("purple_glowshroom", () -> MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(6));
	public static final RegistryObject<FlowerBlock> PINK_GLOWSHROOM = flower("pink_glowshroom", () -> MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final RegistryObject<FlowerBlock> RED_GLOWSHROOM = flower("red_glowshroom", () -> MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final RegistryObject<FlowerBlock> ORANGE_GLOWSHROOM = flower("orange_glowshroom", () -> MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final RegistryObject<FlowerBlock> YELLOW_GLOWSHROOM = flower("yellow_glowshroom", () -> MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final RegistryObject<FlowerBlock> GREEN_GLOWSHROOM = flower("green_glowshroom", () -> MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final RegistryObject<FlowerBlock> TURQUOISE_GLOWSHROOM = flower("turquoise_glowshroom", () -> MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final RegistryObject<FlowerBlock> BLUE_GLOWSHROOM = flower("blue_glowshroom", () -> MobEffects.GLOWING, 30, Blocks.BROWN_MUSHROOM, light(7));
	public static final RegistryObject<FlowerPotBlock> POTTED_PURPLE_GLOWSHROOM = flower_pot("potted_purple_glowshroom", PURPLE_GLOWSHROOM, light(7));
	public static final RegistryObject<FlowerPotBlock> POTTED_PINK_GLOWSHROOM = flower_pot("potted_pink_glowshroom", PINK_GLOWSHROOM, light(7));
	public static final RegistryObject<FlowerPotBlock> POTTED_RED_GLOWSHROOM = flower_pot("potted_red_glowshroom", RED_GLOWSHROOM, light(7));
	public static final RegistryObject<FlowerPotBlock> POTTED_ORANGE_GLOWSHROOM = flower_pot("potted_orange_glowshroom", ORANGE_GLOWSHROOM, light(7));
	public static final RegistryObject<FlowerPotBlock> POTTED_YELLOW_GLOWSHROOM = flower_pot("potted_yellow_glowshroom", YELLOW_GLOWSHROOM, light(7));
	public static final RegistryObject<FlowerPotBlock> POTTED_GREEN_GLOWSHROOM = flower_pot("potted_green_glowshroom", GREEN_GLOWSHROOM, light(7));
	public static final RegistryObject<FlowerPotBlock> POTTED_TURQUOISE_GLOWSHROOM = flower_pot("potted_turquoise_glowshroom", TURQUOISE_GLOWSHROOM, light(7));
	public static final RegistryObject<FlowerPotBlock> POTTED_BLUE_GLOWSHROOM = flower_pot("potted_blue_glowshroom", BLUE_GLOWSHROOM, light(7));
	//@formatter:on

	private static WoodType registerWoodType(String name) {
		String id = Machina.MOD_ID + ":" + name;
		return WoodType.register(new WoodType(id, new BlockSetType(id)));
	}

	private static Function<Block.Properties, Block.Properties> light(int light) {
		return p -> p.lightLevel(s -> light);
	}

	private static <T extends Block> Supplier<T> of(Block block, Function<Block.Properties, Block.Properties> extra,
			Function<Block.Properties, T> constructor) {
		return () -> constructor.apply((extra.apply(Block.Properties.copy(block))));
	}

	public static <T extends Block> RegistryObject<T> _register(String name, Supplier<T> block) {
		return BLOCKS.register(name, block);
	}

	public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
		RegistryObject<T> ro = _register(name, block);
		registerBlockItem(name, ro);
		return ro;
	}

	public static RegistryObject<Block> block(String name, Block prop) {
		return register(name, prop, a -> a, Block::new);
	}

	public static RegistryObject<SlabBlock> slab(String name, Block prop) {
		return register(name, prop, a -> a, SlabBlock::new);
	}

	public static RegistryObject<StairBlock> stairs(String name, RegistryObject<Block> block, Block prop) {
		return register(name, prop, a -> a, p -> new StairBlock(() -> block.get().defaultBlockState(), p));
	}

	public static RegistryObject<WallBlock> wall(String name, Block prop) {
		return register(name, prop, a -> a.hasPostProcess(BlockInit::always), WallBlock::new);
	}

	public static RegistryObject<RotatedPillarBlock> log(String name, Block prop) {
		return register(name, prop, a -> a, RotatedPillarBlock::new);
	}

	public static RegistryObject<LeavesBlock> leaves(String name, Block prop) {
		return register(name, prop, a -> a, LeavesBlock::new);
	}

	public static RegistryObject<MachinaSignBlock> sign(String name, Block prop, WoodType wood) {
		RegistryObject<MachinaSignBlock> s = registerNI(name, prop, a -> a, p -> new MachinaSignBlock(p, wood));
		SIGNS.add(s);
		return s;
	}

	public static RegistryObject<MachinaWallSignBlock> wall_sign(String name, Block prop, WoodType wood) {
		RegistryObject<MachinaWallSignBlock> s = registerNI(name, prop, a -> a, p -> new MachinaWallSignBlock(p, wood));
		SIGNS.add(s);
		return s;
	}

	public static RegistryObject<MachinaHangingSignBlock> hanging_sign(String name, Block prop, WoodType wood) {
		RegistryObject<MachinaHangingSignBlock> s = registerNI(name, prop, a -> a,
				p -> new MachinaHangingSignBlock(p, wood));
		HANGING_SIGNS.add(s);
		return s;
	}

	public static RegistryObject<MachinaHangingWallSignBlock> wall_hanging_sign(String name, Block prop,
			WoodType wood) {
		RegistryObject<MachinaHangingWallSignBlock> s = registerNI(name, prop, a -> a,
				p -> new MachinaHangingWallSignBlock(p, wood));
		HANGING_SIGNS.add(s);
		return s;
	}

	public static RegistryObject<ButtonBlock> wood_button(String name, Block prop, WoodType wood) {
		return register(name, prop, a -> a, p -> new ButtonBlock(p, wood.setType(), 30, true));
	}

	public static RegistryObject<DoorBlock> wood_door(String name, Block prop, WoodType wood) {
		return register(name, prop, a -> a, p -> new DoorBlock(p, wood.setType()));
	}

	public static RegistryObject<TrapDoorBlock> wood_trapdoor(String name, Block prop, WoodType wood) {
		return register(name, prop, a -> a, p -> new TrapDoorBlock(p, wood.setType()));
	}

	public static RegistryObject<FenceBlock> fence(String name, Block prop) {
		return register(name, prop, a -> a, FenceBlock::new);
	}

	public static RegistryObject<FenceGateBlock> fence_gate(String name, Block prop, WoodType wood) {
		return register(name, prop, a -> a, p -> new FenceGateBlock(p, wood));
	}

	public static RegistryObject<PressurePlateBlock> wood_pressure_plate(String name, Block prop, WoodType wood) {
		return register(name, prop, a -> a,
				p -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, p, wood.setType()));
	}

	public static RegistryObject<ButtonBlock> stone_button(String name, Block prop) {
		return register(name, prop, a -> a, p -> new ButtonBlock(p, BlockSetType.STONE, 20, false));
	}

	public static RegistryObject<PressurePlateBlock> stone_pressure_plate(String name, Block prop) {
		return register(name, prop, a -> a,
				p -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, p, BlockSetType.STONE));
	}

	public static RegistryObject<FlowerBlock> flower(String name, Supplier<MobEffect> effect, int duration, Block prop,
			Function<Block.Properties, Block.Properties> extra) {
		return register(name, prop, extra, p -> new FlowerBlock(effect, duration, p));
	}

	public static RegistryObject<FlowerBlock> flower(String name, Supplier<MobEffect> effect, int duration,
			Block prop) {
		return register(name, prop, a -> a, p -> new FlowerBlock(effect, duration, p));
	}

	public static RegistryObject<TallFlowerBlock> tall_flower(String name, Block prop) {
		return register(name, prop, a -> a, TallFlowerBlock::new);
	}

	public static RegistryObject<FlowerPotBlock> flower_pot(String name, RegistryObject<? extends Block> flower) {
		return _register(name, BlockInit.of(Blocks.FLOWER_POT, a -> a, p -> new FlowerPotBlock(flower.get(), p)));
	}

	public static RegistryObject<FlowerPotBlock> flower_pot(String name, RegistryObject<FlowerBlock> flower,
			Function<Block.Properties, Block.Properties> extra) {
		return _register(name, BlockInit.of(Blocks.FLOWER_POT, extra, p -> new FlowerPotBlock(flower.get(), p)));
	}

	public static RegistryObject<PebbleBlock> pebbles(String name) {
		return register(name, Blocks.ANDESITE, a -> a.noCollission().noOcclusion(), PebbleBlock::new);
	}

	public static RegistryObject<SmallFlowerBlock> groundlily(String name) {
		return register(name, Blocks.PINK_PETALS, a -> a, SmallFlowerBlock::new);
	}

	public static RegistryObject<MachinaWaterlilyBlock> waterlily(String name) {
		return registerCI(name, Blocks.LILY_PAD, a -> a, MachinaWaterlilyBlock::new,
				ro -> new PlaceOnWaterBlockItem(ro.get(), new Item.Properties()));
	}

	public static <T extends Block> RegistryObject<T> register(String name, Block prop,
			Function<Block.Properties, T> constructor) {
		return register(name, prop, a -> a, constructor);
	}

	public static <T extends Block> RegistryObject<T> registerNI(String name, Block prop,
			Function<Block.Properties, Block.Properties> extra, Function<Block.Properties, T> constructor) {
		return _register(name, BlockInit.of(prop, extra, constructor));
	}

	public static <T extends Block> RegistryObject<T> registerCI(String name, Block prop,
			Function<Block.Properties, Block.Properties> extra, Function<Block.Properties, T> constructor,
			Function<RegistryObject<T>, ? extends BlockItem> item) {
		RegistryObject<T> ro = _register(name, BlockInit.of(prop, extra, constructor));
		ItemInit.ITEMS.register(name, () -> item.apply(ro));
		return ro;
	}

	public static <T extends Block> RegistryObject<T> register(String name, Block prop,
			Function<Block.Properties, Block.Properties> extra, Function<Block.Properties, T> constructor) {
		RegistryObject<T> ro = _register(name, BlockInit.of(prop, extra, constructor));
		registerBlockItem(name, ro);
		return ro;
	}

	private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
		ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}

	private static boolean always(BlockState state, BlockGetter getter, BlockPos pos) {
		return true;
	}
}
