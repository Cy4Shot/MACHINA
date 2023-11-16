package com.machina.registration;

import com.machina.Machina;
import com.machina.api.multiblock.MultiblockLoader;
import com.machina.config.ClientConfig;
import com.machina.config.CommonConfig;
import com.machina.network.MachinaNetwork;
import com.machina.registration.init.BiomeSourceInit;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ChunkGeneratorInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TabInit;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class Registration {

	public static final MultiblockLoader MULTIBLOCK_LOADER = new MultiblockLoader();

	public static void register(IEventBus bus) {
		bus.addListener(Registration::onCommonSetup);

		CommonConfig.init();
		registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
		registerConfig(ModConfig.Type.COMMON, CommonConfig.COMMON_SPEC);

		BlockInit.BLOCKS.register(bus);
		ItemInit.ITEMS.register(bus);
		FluidInit.FLUIDS.register(bus);
		FluidInit.FLUID_TYPES.register(bus);
		TabInit.CREATIVE_MODE_TABS.register(bus);
		BlockEntityInit.BLOCK_ENTITY_TYPES.register(bus);
		BiomeSourceInit.BIOME_SOURCES.register(bus);
		ChunkGeneratorInit.CHUNK_GENERATORS.register(bus);
	}

	public static void onCommonSetup(final FMLCommonSetupEvent event) {
		MachinaNetwork.init();

		MultiblockLoader.INSTANCE = MULTIBLOCK_LOADER;
	}

	private static void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
		ModLoadingContext.get().registerConfig(type, spec,
				Machina.MOD_ID + "-" + type.toString().toLowerCase() + ".toml");
	}
}