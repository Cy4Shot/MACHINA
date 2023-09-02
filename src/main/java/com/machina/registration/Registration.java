package com.machina.registration;

import com.machina.Machina;
import com.machina.config.ClientConfig;
import com.machina.config.CommonConfig;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TabInit;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Registration {
	public static void register(IEventBus bus) {
		CommonConfig.init();
		registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
		registerConfig(ModConfig.Type.COMMON, CommonConfig.COMMON_SPEC);

//		bus.addGenericListener(Item.class, BlockInit::registerBlockItems);

		BlockInit.BLOCKS.register(bus);
		ItemInit.ITEMS.register(bus);
		BlockInit.registerBlockItems();
		FluidInit.FLUIDS.register(bus);
		FluidInit.FLUID_TYPES.register(bus);
		TabInit.CREATIVE_MODE_TABS.register(bus);
	}

	private static void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
		ModLoadingContext.get().registerConfig(type, spec,
				Machina.MOD_ID + "-" + type.toString().toLowerCase() + ".toml");
	}
}