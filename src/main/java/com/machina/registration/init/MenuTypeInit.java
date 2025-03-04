package com.machina.registration.init;

import com.machina.Machina;
import com.machina.block.menu.BatteryMenu;
import com.machina.block.menu.CreativeBatteryMenu;
import com.machina.block.menu.FurnaceGeneratorMenu;
import com.machina.block.menu.GrinderMenu;
import com.machina.block.menu.MachineCaseMenu;
import com.machina.block.menu.TankMenu;
import com.machina.block.menu.connector.FluidPipeMenu;
import com.machina.item.menu.FluidFilterMenu;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeInit {
	public static final DeferredRegister<MenuType<	?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
			Machina.MOD_ID);

	//@formatter:off
	public static final RegistryObject<MenuType<FluidPipeMenu>> FLUID_PIPE = register("fluid_pipe",
            FluidPipeMenu::new);
	public static final RegistryObject<MenuType<FluidFilterMenu>> FLUID_FILTER = register("fluid_filter",
			FluidFilterMenu::new);
	public static final RegistryObject<MenuType<BatteryMenu>> BATTERY = register("battery",
            BatteryMenu::new);
	public static final RegistryObject<MenuType<TankMenu>> TANK = register("tank",
			TankMenu::new);
	public static final RegistryObject<MenuType<CreativeBatteryMenu>> CREATIVE_BATTERY = register("creative_battery",
            CreativeBatteryMenu::new);
	public static final RegistryObject<MenuType<MachineCaseMenu>> MACHINE_CASE = register("machine_case",
			MachineCaseMenu::new);
	public static final RegistryObject<MenuType<FurnaceGeneratorMenu>> FURNACE_GENERATOR = register("furnace_generator",
            FurnaceGeneratorMenu::new);
	public static final RegistryObject<MenuType<GrinderMenu>> GRINDER = register("grinder",
            GrinderMenu::new);
	//@formatter:on

	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String n,
			IContainerFactory<T> sup) {
		return MENU_TYPES.register(n, () -> IForgeMenuType.create(sup));
	}
}
