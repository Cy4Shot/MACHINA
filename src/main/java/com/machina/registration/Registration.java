package com.machina.registration;

import com.machina.Machina;
import com.machina.config.ClientConfig;
import com.machina.config.CommonConfig;
import com.machina.network.MachinaNetwork;
import com.machina.registration.init.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class Registration {

    public static void register(IEventBus bus) {
        bus.addListener(Registration::onCommonSetup);

        registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
        registerConfig(ModConfig.Type.COMMON, CommonConfig.COMMON_SPEC);

        PlanetTreeInit.TREES.register(bus);
        PlanetRockInit.ROCKS.register(bus);
        SoundInit.SOUNDS.register(bus);
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
    }

    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        MachinaNetwork.init();
    }

    private static void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
        ModLoadingContext.get().registerConfig(type, spec,
                Machina.MOD_ID + "-" + type.toString().toLowerCase() + ".toml");
    }
}