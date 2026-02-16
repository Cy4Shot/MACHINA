package com.machina.registration.init;

import com.machina.Machina;
import com.machina.block.entity.MachinaHangingSignBlockEntity;
import com.machina.block.entity.MachinaSignBlockEntity;
import com.machina.block.entity.connector.EnergyCableBlockEntity;
import com.machina.block.entity.connector.FluidPipeBlockEntity;
import com.machina.block.entity.connector.ItemConduitBlockEntity;
import com.machina.block.entity.machine.*;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister
            .create(Registries.BLOCK_ENTITY_TYPE, Machina.MOD_ID);

    //@formatter:off
	public static final Supplier<BlockEntityType<ItemConduitBlockEntity>> ITEM_CONDUIT = register("item_conduit",
            ItemConduitBlockEntity::new, BlockInit.ITEM_CONDUIT::get);
	public static final Supplier<BlockEntityType<EnergyCableBlockEntity>> ENERGY_CABLE = register("energy_cable",
            EnergyCableBlockEntity::new, BlockInit.ENERGY_CABLE::get);
	public static final Supplier<BlockEntityType<FluidPipeBlockEntity>> FLUID_PIPE = register("fluid_pipe",
			FluidPipeBlockEntity::new, BlockInit.FLUID_PIPE::get);
	public static final Supplier<BlockEntityType<BatteryBlockEntity>> BATTERY = register("battery",
            BatteryBlockEntity::new, BlockInit.BATTERY::get);
	public static final Supplier<BlockEntityType<TankBlockEntity>> TANK = register("tank",
            TankBlockEntity::new, BlockInit.TANK::get);
	public static final Supplier<BlockEntityType<CreativeBatteryBlockEntity>> CREATIVE_BATTERY = register("creaitve_battery",
            CreativeBatteryBlockEntity::new, BlockInit.CREATIVE_BATTERY::get);
	public static final Supplier<BlockEntityType<MachineCaseBlockEntity>> MACHINE_CASE = register("machine_case",
            MachineCaseBlockEntity::new, BlockInit.BASIC_MACHINE_CASE::get);
	public static final Supplier<BlockEntityType<FurnaceGeneratorBlockEntity>> FURNACE_GENERATOR = register("furnace_generator",
            FurnaceGeneratorBlockEntity::new, BlockInit.FURNACE_GENERATOR::get);
	public static final Supplier<BlockEntityType<ChemicalGeneratorBlockEntity>> CHEMICAL_GENERATOR = register("chemical_generator",
            ChemicalGeneratorBlockEntity::new, BlockInit.CHEMICAL_GENERATOR::get);
	public static final Supplier<BlockEntityType<ElectricSmelterBlockEntity>> ELECTRIC_SMELTER = register("electric_smelter",
			ElectricSmelterBlockEntity::new, BlockInit.ELECTRIC_SMELTER::get);
	public static final Supplier<BlockEntityType<GrinderBlockEntity>> GRINDER = register("grinder",
            GrinderBlockEntity::new, BlockInit.GRINDER::get);
	public static final Supplier<BlockEntityType<CompressorBlockEntity>> COMPRESSOR = register("compressor",
			CompressorBlockEntity::new, BlockInit.COMPRESSOR::get);
	public static final Supplier<BlockEntityType<MelterBlockEntity>> MELTER = register("melter",
			MelterBlockEntity::new, BlockInit.MELTER::get);
	public static final Supplier<BlockEntityType<SolidifierBlockEntity>> SOLIDIFIER = register("solidifier",
			SolidifierBlockEntity::new, BlockInit.SOLIDIFIER::get);
	public static final Supplier<BlockEntityType<ReactionChamberBlockEntity>> REACTION_CHAMBER = register("reaction_chamber",
			ReactionChamberBlockEntity::new, BlockInit.REACTION_CHAMBER::get);
	public static final Supplier<BlockEntityType<ComposterVatBlockEntity>> COMPOSTER_VAT = register("composter_vat",
			ComposterVatBlockEntity::new, BlockInit.COMPOSTER_VAT::get);
	public static final Supplier<BlockEntityType<SawmillBlockEntity>> SAWMILL = register("sawmill",
			SawmillBlockEntity::new, BlockInit.SAWMILL::get);
	public static final Supplier<BlockEntityType<ElectrolyzerBlockEntity>> ELECTROLYZER = register("electrolyzer",
			ElectrolyzerBlockEntity::new, BlockInit.ELECTROLYZER::get);
	public static final Supplier<BlockEntityType<ElectricPumpBlockEntity>> ELECTRIC_PUMP = register("electric_pump",
			ElectricPumpBlockEntity::new, BlockInit.ELECTRIC_PUMP::get);
	public static final Supplier<BlockEntityType<AtmosphericSeparatorBlockEntity>> ATMOSPHERIC_SEPARATOR = register("atmospheric_separator",
			AtmosphericSeparatorBlockEntity::new, BlockInit.ATMOSPHERIC_SEPARATOR::get);
	public static final Supplier<BlockEntityType<RocketPartBenchBlockEntity>> ROCKET_PART_BENCH = register("rocket_part_bench",
			RocketPartBenchBlockEntity::new, BlockInit.ROCKET_PART_BENCH::get);
	public static final Supplier<BlockEntityType<RocketAssemblyStationBlockEntity>> ROCKET_ASSEMBLY_STATION = register("rocket_assembly_station",
			RocketAssemblyStationBlockEntity::new, BlockInit.ROCKET_ASSEMBLY_STATION::get);
	//@formatter:on

    public static final Supplier<BlockEntityType<MachinaSignBlockEntity>> SIGN = registerMany("sign",
            MachinaSignBlockEntity::new, () -> BlockInit.SIGNS.stream().map(Supplier::get).toArray(Block[]::new));
    public static final Supplier<BlockEntityType<MachinaHangingSignBlockEntity>> HANGING_SIGN = registerMany(
            "hanging_sign", MachinaHangingSignBlockEntity::new,
            () -> BlockInit.HANGING_SIGNS.stream().map(Supplier::get).toArray(Block[]::new));

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String n,
            BlockEntityType.BlockEntitySupplier<T> s, Supplier<Block> b) {
        return BLOCK_ENTITY_TYPES.register(n, () -> BlockEntityType.Builder.of(s, b.get()).build(null));
    }

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerMany(String n,
            BlockEntityType.BlockEntitySupplier<T> s, Supplier<Block[]> b) {
        return BLOCK_ENTITY_TYPES.register(n, () -> BlockEntityType.Builder.of(s, b.get()).build(null));
    }
}
