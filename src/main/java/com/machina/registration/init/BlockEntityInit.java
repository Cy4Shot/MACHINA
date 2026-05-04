package com.machina.registration.init;

import java.util.Arrays;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.block.entity.MachinaHangingSignBlockEntity;
import com.machina.block.entity.MachinaSignBlockEntity;
import com.machina.block.entity.SulfurGeyserBlockEntity;
import com.machina.block.entity.connector.EnergyCableBlockEntity;
import com.machina.block.entity.connector.FluidPipeBlockEntity;
import com.machina.block.entity.connector.ItemConduitBlockEntity;
import com.machina.block.entity.machine.AtmosphericSeparatorBlockEntity;
import com.machina.block.entity.machine.BatteryBlockEntity;
import com.machina.block.entity.machine.ChemicalGeneratorBlockEntity;
import com.machina.block.entity.machine.ComposterVatBlockEntity;
import com.machina.block.entity.machine.CompressorBlockEntity;
import com.machina.block.entity.machine.CreativeBatteryBlockEntity;
import com.machina.block.entity.machine.ElectricPumpBlockEntity;
import com.machina.block.entity.machine.ElectricSmelterBlockEntity;
import com.machina.block.entity.machine.ElectrolyzerBlockEntity;
import com.machina.block.entity.machine.FurnaceGeneratorBlockEntity;
import com.machina.block.entity.machine.GrinderBlockEntity;
import com.machina.block.entity.machine.MelterBlockEntity;
import com.machina.block.entity.machine.ReactionChamberBlockEntity;
import com.machina.block.entity.machine.RocketAssemblyStationBlockEntity;
import com.machina.block.entity.machine.RocketPartBenchBlockEntity;
import com.machina.block.entity.machine.RocketRefuelingStationBlockEntity;
import com.machina.block.entity.machine.SawmillBlockEntity;
import com.machina.block.entity.machine.SolidifierBlockEntity;
import com.machina.block.entity.machine.TankBlockEntity;
import com.machina.block.entity.machine.fission_reactor.FissionReactorControllerBlockEntity;
import com.machina.block.entity.machine.fission_reactor.FissionReactorEnergyPortBlockEntity;
import com.machina.block.entity.machine.fission_reactor.FissionReactorItemPortBlockEntity;
import com.machina.block.entity.machine.fission_reactor.FissionReactorPartBlockEntity;
import com.machina.block.entity.machine.fission_reactor.FissionReactorSteamPortBlockEntity;
import com.machina.block.entity.machine.fission_reactor.FissionReactorWaterPortBlockEntity;
import com.machina.block.entity.machine.geothermal_generator.GeothermalGeneratorControllerBlockEntity;
import com.machina.block.entity.machine.geothermal_generator.GeothermalGeneratorPartBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityInit {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister
			.create(Registries.BLOCK_ENTITY_TYPE, Machina.MOD_ID);

	//@formatter:off
	public static final Supplier<BlockEntityType<ItemConduitBlockEntity>> ITEM_CONDUIT
		= register("item_conduit", ItemConduitBlockEntity::new, BlockInit.ITEM_CONDUIT::get);
	public static final Supplier<BlockEntityType<EnergyCableBlockEntity>> ENERGY_CABLE
		= register("energy_cable", EnergyCableBlockEntity::new, BlockInit.ENERGY_CABLE::get);
	public static final Supplier<BlockEntityType<FluidPipeBlockEntity>> FLUID_PIPE
		= register("fluid_pipe",FluidPipeBlockEntity::new, BlockInit.FLUID_PIPE::get);
	public static final Supplier<BlockEntityType<BatteryBlockEntity>> BATTERY
		= register("battery",BatteryBlockEntity::new, BlockInit.BATTERY::get);
	public static final Supplier<BlockEntityType<TankBlockEntity>> TANK
		= register("tank", TankBlockEntity::new, BlockInit.TANK::get);
	public static final Supplier<BlockEntityType<CreativeBatteryBlockEntity>> CREATIVE_BATTERY
		= register("creaitve_battery", CreativeBatteryBlockEntity::new, BlockInit.CREATIVE_BATTERY::get);
	public static final Supplier<BlockEntityType<FurnaceGeneratorBlockEntity>> FURNACE_GENERATOR
		= register("furnace_generator", FurnaceGeneratorBlockEntity::new, BlockInit.FURNACE_GENERATOR::get);
	public static final Supplier<BlockEntityType<ChemicalGeneratorBlockEntity>> CHEMICAL_GENERATOR
		= register("chemical_generator", ChemicalGeneratorBlockEntity::new, BlockInit.CHEMICAL_GENERATOR::get);
	public static final Supplier<BlockEntityType<ElectricSmelterBlockEntity>> ELECTRIC_SMELTER
		= register("electric_smelter", ElectricSmelterBlockEntity::new, BlockInit.ELECTRIC_SMELTER::get);
	public static final Supplier<BlockEntityType<GrinderBlockEntity>> GRINDER
		= register("grinder", GrinderBlockEntity::new, BlockInit.GRINDER::get);
	public static final Supplier<BlockEntityType<CompressorBlockEntity>> COMPRESSOR
		= register("compressor", CompressorBlockEntity::new, BlockInit.COMPRESSOR::get);
	public static final Supplier<BlockEntityType<MelterBlockEntity>> MELTER
		= register("melter", MelterBlockEntity::new, BlockInit.MELTER::get);
	public static final Supplier<BlockEntityType<SolidifierBlockEntity>> SOLIDIFIER
		= register("solidifier", SolidifierBlockEntity::new, BlockInit.SOLIDIFIER::get);
	public static final Supplier<BlockEntityType<ReactionChamberBlockEntity>> REACTION_CHAMBER
		= register("reaction_chamber", ReactionChamberBlockEntity::new, BlockInit.REACTION_CHAMBER::get);
	public static final Supplier<BlockEntityType<ComposterVatBlockEntity>> COMPOSTER_VAT
		= register("composter_vat", ComposterVatBlockEntity::new, BlockInit.COMPOSTER_VAT::get);
	public static final Supplier<BlockEntityType<SawmillBlockEntity>> SAWMILL
		= register("sawmill", SawmillBlockEntity::new, BlockInit.SAWMILL::get);
	public static final Supplier<BlockEntityType<ElectrolyzerBlockEntity>> ELECTROLYZER
		= register("electrolyzer", ElectrolyzerBlockEntity::new, BlockInit.ELECTROLYZER::get);
	public static final Supplier<BlockEntityType<ElectricPumpBlockEntity>> ELECTRIC_PUMP
		= register("electric_pump", ElectricPumpBlockEntity::new, BlockInit.ELECTRIC_PUMP::get);
	public static final Supplier<BlockEntityType<AtmosphericSeparatorBlockEntity>> ATMOSPHERIC_SEPARATOR
		= register("atmospheric_separator", AtmosphericSeparatorBlockEntity::new, BlockInit.ATMOSPHERIC_SEPARATOR::get);
	public static final Supplier<BlockEntityType<RocketPartBenchBlockEntity>> ROCKET_PART_BENCH
		= register("rocket_part_bench",	RocketPartBenchBlockEntity::new, BlockInit.ROCKET_PART_BENCH::get);
	public static final Supplier<BlockEntityType<RocketAssemblyStationBlockEntity>> ROCKET_ASSEMBLY_STATION
		= register("rocket_assembly_station", RocketAssemblyStationBlockEntity::new, BlockInit.ROCKET_ASSEMBLY_STATION::get);
	public static final Supplier<BlockEntityType<RocketRefuelingStationBlockEntity>> ROCKET_REFUELING_STATION
		= register("rocket_refueling_station", RocketRefuelingStationBlockEntity::new, BlockInit.ROCKET_REFUELING_STATION::get);
	
	// Geothermal generator
	public static final Supplier<BlockEntityType<GeothermalGeneratorControllerBlockEntity>> GEOTHERMAL_GENERATOR_CONTROLLER
		= register("geothermal_generator_controller", GeothermalGeneratorControllerBlockEntity::new, BlockInit.GEOTHERMAL_GENERATOR_CONTROLLER::get);
	public static final Supplier<BlockEntityType<GeothermalGeneratorPartBlockEntity>> GEOTHERMAL_GENERATOR_PART
		= register("geothermal_generator_part", GeothermalGeneratorPartBlockEntity::new, BlockInit.GEOTHERMAL_GENERATOR_CASING::get, BlockInit.GEOTHERMAL_SUPPORT_ROD::get);
	
	// Fission reactor
	public static final Supplier<BlockEntityType<FissionReactorControllerBlockEntity>> FISSION_REACTOR_CONTROLLER
		= register("fission_reactor_controller", FissionReactorControllerBlockEntity::new, BlockInit.FISSION_REACTOR_CONTROLLER::get);
	public static final Supplier<BlockEntityType<FissionReactorPartBlockEntity>> FISSION_REACTOR_PART
		= register("fission_reactor_part", FissionReactorPartBlockEntity::new, BlockInit.FISSION_REACTOR_CASING::get, BlockInit.FISSION_REACTOR_GLASS::get, BlockInit.FISSION_FUEL_ROD::get, BlockInit.FISSION_FUEL_ROD_ASSEMBLY::get);
	public static final Supplier<BlockEntityType<FissionReactorEnergyPortBlockEntity>> FISSION_REACTOR_ENERGY_PORT
		= register("fission_reactor_energy_port", FissionReactorEnergyPortBlockEntity::new, BlockInit.FISSION_REACTOR_ENERGY_PORT::get);
	public static final Supplier<BlockEntityType<FissionReactorItemPortBlockEntity>> FISSION_REACTOR_ITEM_PORT
		= register("fission_reactor_item_port", FissionReactorItemPortBlockEntity::new, BlockInit.FISSION_REACTOR_ITEM_PORT::get);
	public static final Supplier<BlockEntityType<FissionReactorWaterPortBlockEntity>> FISSION_REACTOR_WATER_PORT
		= register("fission_reactor_water_port", FissionReactorWaterPortBlockEntity::new, BlockInit.FISSION_REACTOR_WATER_PORT::get);
	public static final Supplier<BlockEntityType<FissionReactorSteamPortBlockEntity>> FISSION_REACTOR_STEAM_PORT
		= register("fission_reactor_steam_port", FissionReactorSteamPortBlockEntity::new, BlockInit.FISSION_REACTOR_STEAM_PORT::get);
	
	// Worldgen
	public static final Supplier<BlockEntityType<SulfurGeyserBlockEntity>> SULFUR_GEYSER = register("sulfur_geyser",
			SulfurGeyserBlockEntity::new, BlockInit.SULFUR_GEYSER::get);

	// Sign
	public static final Supplier<BlockEntityType<MachinaSignBlockEntity>> SIGN = registerMany("sign",
			MachinaSignBlockEntity::new, () -> BlockInit.SIGNS.stream().map(Supplier::get).toArray(Block[]::new));
	public static final Supplier<BlockEntityType<MachinaHangingSignBlockEntity>> HANGING_SIGN = registerMany(
			"hanging_sign", MachinaHangingSignBlockEntity::new,
			() -> BlockInit.HANGING_SIGNS.stream().map(Supplier::get).toArray(Block[]::new));
	//@formatter:on

	@SafeVarargs
	private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String n,
			BlockEntityType.BlockEntitySupplier<T> s, Supplier<Block>... b) {
		return BLOCK_ENTITY_TYPES.register(n, () -> BlockEntityType.Builder
				.of(s, Arrays.stream(b).map(Supplier::get).toArray(Block[]::new)).build(null));
	}

	private static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerMany(String n,
			BlockEntityType.BlockEntitySupplier<T> s, Supplier<Block[]> b) {
		return BLOCK_ENTITY_TYPES.register(n, () -> BlockEntityType.Builder.of(s, b.get()).build(null));
	}
}
