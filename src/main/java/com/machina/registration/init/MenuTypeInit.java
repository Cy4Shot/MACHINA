package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.block.menu.AtmosphericSeparatorMenu;
import com.machina.block.menu.BatteryMenu;
import com.machina.block.menu.ChemicalGeneratorMenu;
import com.machina.block.menu.ComposterVatMenu;
import com.machina.block.menu.CompressorMenu;
import com.machina.block.menu.CreativeBatteryMenu;
import com.machina.block.menu.ElectricPumpMenu;
import com.machina.block.menu.ElectricSmelterMenu;
import com.machina.block.menu.ElectrolyzerMenu;
import com.machina.block.menu.FissionReactorMenu;
import com.machina.block.menu.FurnaceGeneratorMenu;
import com.machina.block.menu.GeothermalGeneratorMenu;
import com.machina.block.menu.GrinderMenu;
import com.machina.block.menu.MelterMenu;
import com.machina.block.menu.ReactionChamberMenu;
import com.machina.block.menu.RocketAssemblyStationMenu;
import com.machina.block.menu.RocketPartBenchMenu;
import com.machina.block.menu.RocketRefuelingStationMenu;
import com.machina.block.menu.SawmillMenu;
import com.machina.block.menu.SolidifierMenu;
import com.machina.block.menu.TankMenu;
import com.machina.block.menu.connector.FluidPipeMenu;
import com.machina.block.menu.connector.ItemConduitMenu;
import com.machina.item.menu.AdvancedItemFilterMenu;
import com.machina.item.menu.FluidFilterMenu;
import com.machina.item.menu.ItemFilterMenu;
import com.machina.rocket.RocketMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuTypeInit {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU,
			Machina.MOD_ID);

	//@formatter:off
    public static final Supplier<MenuType<RocketMenu>> ROCKET =
            register("rocket", RocketMenu::new);
	public static final Supplier<MenuType<ItemConduitMenu>> ITEM_CONDUIT =
			register("item_conduit", ItemConduitMenu::new);
	public static final Supplier<MenuType<ItemFilterMenu>> ITEM_FILTER =
			register("item_filter",	ItemFilterMenu::new);
	public static final Supplier<MenuType<AdvancedItemFilterMenu>> ADVANCED_ITEM_FILTER =
			register("advanced_item_filter", AdvancedItemFilterMenu::new);
	public static final Supplier<MenuType<FluidPipeMenu>> FLUID_PIPE =
			register("fluid_pipe", FluidPipeMenu::new);
	public static final Supplier<MenuType<FluidFilterMenu>> FLUID_FILTER =
			register("fluid_filter", FluidFilterMenu::new);
	public static final Supplier<MenuType<BatteryMenu>> BATTERY =
			register("battery", BatteryMenu::new);
	public static final Supplier<MenuType<TankMenu>> TANK =
			register("tank", TankMenu::new);
	public static final Supplier<MenuType<CreativeBatteryMenu>> CREATIVE_BATTERY =
			register("creative_battery", CreativeBatteryMenu::new);
	public static final Supplier<MenuType<FurnaceGeneratorMenu>> FURNACE_GENERATOR =
			register("furnace_generator", FurnaceGeneratorMenu::new);
	public static final Supplier<MenuType<ChemicalGeneratorMenu>> CHEMICAL_GENERATOR =
			register("chemical_generator", ChemicalGeneratorMenu::new);
	public static final Supplier<MenuType<ElectricSmelterMenu>> ELECTRIC_SMELTER =
			register("eletric_smelter", ElectricSmelterMenu::new);
	public static final Supplier<MenuType<GrinderMenu>> GRINDER =
			register("grinder", GrinderMenu::new);
	public static final Supplier<MenuType<CompressorMenu>> COMPRESSOR =
			register("compressor", CompressorMenu::new);
	public static final Supplier<MenuType<MelterMenu>> MELTER =
			register("melter", MelterMenu::new);
	public static final Supplier<MenuType<SolidifierMenu>> SOLIDIFIER =
			register("solidifier", SolidifierMenu::new);
	public static final Supplier<MenuType<ReactionChamberMenu>> REACTION_CHAMBER =
			register("reaction_chamber", ReactionChamberMenu::new);
	public static final Supplier<MenuType<ComposterVatMenu>> COMPOSTER_VAT =
			register("composter_vat", ComposterVatMenu::new);
	public static final Supplier<MenuType<SawmillMenu>> SAWMILL =
			register("sawmill", SawmillMenu::new);
	public static final Supplier<MenuType<ElectrolyzerMenu>> ELECTROLYZER =
            register("electrolyzer", ElectrolyzerMenu::new);
	public static final Supplier<MenuType<ElectricPumpMenu>> ELECTRIC_PUMP =
            register("electric_pump", ElectricPumpMenu::new);
	public static final Supplier<MenuType<AtmosphericSeparatorMenu>> ATMOSPHERIC_SEPARATOR =
            register("atmospheric_separator", AtmosphericSeparatorMenu::new);
	public static final Supplier<MenuType<RocketPartBenchMenu>> ROCKET_PART_BENCH =
            register("rocket_part_bench", RocketPartBenchMenu::new);
	public static final Supplier<MenuType<RocketAssemblyStationMenu>> ROCKET_ASSEMBLY_STATION =
            register("rocket_assembly_station", RocketAssemblyStationMenu::new);
	public static final Supplier<MenuType<RocketRefuelingStationMenu>> ROCKET_REFUELING_STATION =
            register("rocket_refueling_station", RocketRefuelingStationMenu::new);
	public static final Supplier<MenuType<GeothermalGeneratorMenu>> GEOTHERMAL_GENERATOR =
            register("geothermal_generator", GeothermalGeneratorMenu::new);
	public static final Supplier<MenuType<FissionReactorMenu>> FISSION_REACTOR =
            register("fission_reactor", FissionReactorMenu::new);
	//@formatter:on

	private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String n,
			MenuType.MenuSupplier<T> sup) {
		return MENU_TYPES.register(n, () -> new MenuType<>(sup, FeatureFlags.DEFAULT_FLAGS));
	}

	private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String n,
			IContainerFactory<T> sup) {
		return MENU_TYPES.register(n, () -> IMenuTypeExtension.create(sup));
	}
}
