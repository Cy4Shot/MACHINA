package com.machina.registration;

import com.machina.Machina;
import com.machina.config.ClientConfig;
import com.machina.config.CommonConfig;
import com.machina.registration.init.ArgumentTypesInit;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.BlockStateProviderInit;
import com.machina.registration.init.ChunkGeneratorInit;
import com.machina.registration.init.DataComponentsInit;
import com.machina.registration.init.EntityDataSerializerInit;
import com.machina.registration.init.EntityTypeInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FruitInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.MaterialRuleInit;
import com.machina.registration.init.MenuTypeInit;
import com.machina.registration.init.OverworldOresInit;
import com.machina.registration.init.PlanetRockInit;
import com.machina.registration.init.PlanetSurfaceInit;
import com.machina.registration.init.PlanetTraitInit;
import com.machina.registration.init.PlanetTreeInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RocketPartInit;
import com.machina.registration.init.SoundInit;
import com.machina.registration.init.TabInit;
import com.machina.registration.init.WeatherEventInit;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Registration {

	public static void register(IEventBus bus, ModContainer cont) {
		registerConfig(cont, ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
		registerConfig(cont, ModConfig.Type.COMMON, CommonConfig.COMMON_SPEC);

		ArgumentTypesInit.ARGUMENT_TYPES.register(bus);
		PlanetTraitInit.TRAITS.register(bus);
		PlanetTreeInit.TREES.register(bus);
		PlanetRockInit.ROCKS.register(bus);
		PlanetSurfaceInit.SURFACES.register(bus);
		SoundInit.SOUNDS.register(bus);
		DataComponentsInit.DATA_COMPONENTS.register(bus);
		ItemInit.ITEMS.register(bus);
		RocketPartInit.ROCKET_PARTS.register(bus);
		BlockInit.BLOCKS.register(bus);
		FluidInit.FLUIDS.register(bus);
		FluidInit.FLUID_TYPES.register(bus);
		FruitInit.register();
		EntityTypeInit.ENTITY_TYPES.register(bus);
		EntityDataSerializerInit.ENTITY_DATA_SERIALIZERS.register(bus);
		TabInit.CREATIVE_MODE_TABS.register(bus);
		BlockEntityInit.BLOCK_ENTITY_TYPES.register(bus);
		MenuTypeInit.MENU_TYPES.register(bus);
		BlockStateProviderInit.BLOCK_STATE_PROVIDERS.register(bus);
		MaterialRuleInit.MATERIAL_RULES.register(bus);
		ChunkGeneratorInit.CHUNK_GENERATORS.register(bus);
		RecipeInit.RECIPE_TYPES.register(bus);
		RecipeInit.RECIPE_SERIALIZERS.register(bus);
		WeatherEventInit.WEATHER_EVENTS.register(bus);
		OverworldOresInit.register(); // Only used for datagen
	}

	private static void registerConfig(ModContainer cont, ModConfig.Type type, ModConfigSpec spec) {
		cont.registerConfig(type, spec, Machina.MOD_ID + "-" + type.toString().toLowerCase() + ".toml");
	}
}