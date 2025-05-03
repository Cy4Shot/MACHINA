package com.machina.registration.init;

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
import com.machina.block.menu.FurnaceGeneratorMenu;
import com.machina.block.menu.GrinderMenu;
import com.machina.block.menu.MachineCaseMenu;
import com.machina.block.menu.MelterMenu;
import com.machina.block.menu.ReactionChamberMenu;
import com.machina.block.menu.RocketPartBenchMenu;
import com.machina.block.menu.SawmillMenu;
import com.machina.block.menu.SolidifierMenu;
import com.machina.block.menu.TankMenu;
import com.machina.block.menu.connector.FluidPipeMenu;
import com.machina.block.menu.connector.ItemConduitMenu;
import com.machina.item.menu.AdvancedItemFilterMenu;
import com.machina.item.menu.FluidFilterMenu;
import com.machina.item.menu.ItemFilterMenu;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeInit {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
			Machina.MOD_ID);

	//@formatter:off
	public static final RegistryObject<MenuType<ItemConduitMenu>> ITEM_CONDUIT =
			register("item_conduit", ItemConduitMenu::new);
	public static final RegistryObject<MenuType<ItemFilterMenu>> ITEM_FILTER =
			register("item_filter",	ItemFilterMenu::new);
	public static final RegistryObject<MenuType<AdvancedItemFilterMenu>> ADVANCED_ITEM_FILTER =
			register("advanced_item_filter", AdvancedItemFilterMenu::new);
	public static final RegistryObject<MenuType<FluidPipeMenu>> FLUID_PIPE =
			register("fluid_pipe", FluidPipeMenu::new);
	public static final RegistryObject<MenuType<FluidFilterMenu>> FLUID_FILTER =
			register("fluid_filter", FluidFilterMenu::new);
	public static final RegistryObject<MenuType<BatteryMenu>> BATTERY =
			register("battery", BatteryMenu::new);
	public static final RegistryObject<MenuType<TankMenu>> TANK =
			register("tank", TankMenu::new);
	public static final RegistryObject<MenuType<CreativeBatteryMenu>> CREATIVE_BATTERY =
			register("creative_battery", CreativeBatteryMenu::new);
	public static final RegistryObject<MenuType<MachineCaseMenu>> MACHINE_CASE =
			register("machine_case", MachineCaseMenu::new);
	public static final RegistryObject<MenuType<FurnaceGeneratorMenu>> FURNACE_GENERATOR =
			register("furnace_generator", FurnaceGeneratorMenu::new);
	public static final RegistryObject<MenuType<ChemicalGeneratorMenu>> CHEMICAL_GENERATOR =
			register("chemical_generator", ChemicalGeneratorMenu::new);
	public static final RegistryObject<MenuType<ElectricSmelterMenu>> ELECTRIC_SMELTER =
			register("eletric_smelter", ElectricSmelterMenu::new);
	public static final RegistryObject<MenuType<GrinderMenu>> GRINDER =
			register("grinder", GrinderMenu::new);
	public static final RegistryObject<MenuType<CompressorMenu>> COMPRESSOR =
			register("compressor", CompressorMenu::new);
	public static final RegistryObject<MenuType<MelterMenu>> MELTER =
			register("melter", MelterMenu::new);
	public static final RegistryObject<MenuType<SolidifierMenu>> SOLIDIFIER =
			register("solidifier", SolidifierMenu::new);
	public static final RegistryObject<MenuType<ReactionChamberMenu>> REACTION_CHAMBER =
			register("reaction_chamber", ReactionChamberMenu::new);
	public static final RegistryObject<MenuType<ComposterVatMenu>> COMPOSTER_VAT =
			register("composter_vat", ComposterVatMenu::new);
	public static final RegistryObject<MenuType<SawmillMenu>> SAWMILL =
			register("sawmill", SawmillMenu::new);
	public static final RegistryObject<MenuType<ElectrolyzerMenu>> ELECTROLYZER =
            register("electrolyzer", ElectrolyzerMenu::new);
	public static final RegistryObject<MenuType<ElectricPumpMenu>> ELECTRIC_PUMP =
            register("electric_pump", ElectricPumpMenu::new);
	public static final RegistryObject<MenuType<AtmosphericSeparatorMenu>> ATMOSPHERIC_SEPARATOR =
            register("atmospheric_separator", AtmosphericSeparatorMenu::new);
	public static final RegistryObject<MenuType<RocketPartBenchMenu>> ROCKET_PART_BENCH =
            register("rocket_part_bench", RocketPartBenchMenu::new);
	//@formatter:on

	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String n,
			IContainerFactory<T> sup) {
		return MENU_TYPES.register(n, () -> IForgeMenuType.create(sup));
	}
}
