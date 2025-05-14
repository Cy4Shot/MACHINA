package com.machina.registration;

import com.machina.Machina;
import com.machina.api.rocket.part.RocketPart;
import com.machina.config.ClientConfig;
import com.machina.config.CommonConfig;
import com.machina.network.MachinaNetwork;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.BlockStateProviderInit;
import com.machina.registration.init.ChunkGeneratorInit;
import com.machina.registration.init.EntityTypeInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.MaterialRuleInit;
import com.machina.registration.init.MenuTypeInit;
import com.machina.registration.init.PlanetRockInit;
import com.machina.registration.init.PlanetTreeInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RegistryInit;
import com.machina.registration.init.RocketPartInit;
import com.machina.registration.init.SoundInit;
import com.machina.registration.init.TabInit;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.GameData;

public class Registration {

	@SuppressWarnings("unchecked")
	public static void register(IEventBus bus) {
		bus.addListener(Registration::onCommonSetup);

		registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
		registerConfig(ModConfig.Type.COMMON, CommonConfig.COMMON_SPEC);

		ResourceKey<? extends Registry<RocketPart<?>>> key = RegistryInit.ROCKET_PARTS.getRegistryKey();
		((WritableRegistry<WritableRegistry<?>>) BuiltInRegistries.REGISTRY).register(
				(ResourceKey<WritableRegistry<?>>) key, GameData.getWrapper(key, Lifecycle.stable(), "rocket_parts"),
				Lifecycle.stable());

		PlanetTreeInit.TREES.register(bus);
		PlanetRockInit.ROCKS.register(bus);
		SoundInit.SOUNDS.register(bus);
		ItemInit.ITEMS.register(bus);
		RocketPartInit.ROCKET_PARTS.register(bus);
		BlockInit.BLOCKS.register(bus);
		FluidInit.FLUIDS.register(bus);
		FluidInit.FLUID_TYPES.register(bus);
		EntityTypeInit.ENTITY_TYPES.register(bus);
		TabInit.CREATIVE_MODE_TABS.register(bus);
		BlockEntityInit.BLOCK_ENTITY_TYPES.register(bus);
		MenuTypeInit.MENU_TYPES.register(bus);
		BlockStateProviderInit.BLOCK_STATE_PROVIDERS.register(bus);
		MaterialRuleInit.MATERIAL_RULES.register(bus);
		ChunkGeneratorInit.CHUNK_GENERATORS.register(bus);
		RecipeInit.RECIPE_TYPES.register(bus);
		RecipeInit.RECIPE_SERIALIZERS.register(bus);
	}

	public static void onCommonSetup(final FMLCommonSetupEvent event) {
		MachinaNetwork.init();
	}

	private static void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
		ModLoadingContext.get().registerConfig(type, spec,
				Machina.MOD_ID + "-" + type.toString().toLowerCase() + ".toml");
	}
}