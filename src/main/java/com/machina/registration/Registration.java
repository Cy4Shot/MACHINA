package com.machina.registration;

import com.machina.Machina;
import com.machina.client.dimension.CustomDimensionRenderInfo;
import com.machina.config.ClientConfig;
import com.machina.config.CommonConfig;
import com.machina.network.MachinaNetwork;
import com.machina.planet.PlanetTraitPoolManager;
import com.machina.planet.attribute.PlanetAttributeType;
import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.CommandInit;
import com.machina.registration.init.ContainerTypesInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.PlanetAttributeTypesInit;
import com.machina.registration.init.PlanetTraitInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.TileEntityTypesInit;
import com.machina.registration.registry.PlanetAttributeRegistry;
import com.machina.registration.registry.PlanetTraitRegistry;
import com.machina.world.DynamicDimensionHelper;
import com.machina.world.data.PlanetDimensionData;
import com.machina.world.data.StarchartData;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.MOD)
public class Registration {

	public static final ItemGroup MACHINA_ITEM_GROUP = new ItemGroup(ItemGroup.TABS.length, "machinaItemGroup") {
		@Override
		public ItemStack makeIcon() {
			return BlockInit.SHIP_CONSOLE.get().asItem().getDefaultInstance();
		}
	};

	public static PlanetTraitPoolManager planetTraitPoolManager = new PlanetTraitPoolManager();

	public static void register(IEventBus bus) {

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			CustomDimensionRenderInfo.registerDimensionRenderInfo();
		});

		registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
		registerConfig(ModConfig.Type.COMMON, CommonConfig.COMMON_SPEC);

		bus.addListener(Registration::onCommonSetup);
		bus.addGenericListener(Item.class, BlockInit::registerBlockItems);
		bus.addGenericListener(PlanetTrait.class, Registration::registerPlanetTraits);
		bus.addGenericListener(PlanetAttributeType.class, Registration::registerPlanetAttributes);
		bus.addGenericListener(IRecipeSerializer.class, RecipeInit::registerRecipes);

		BlockInit.BLOCKS.register(bus);
		ItemInit.ITEMS.register(bus);
		ContainerTypesInit.CONTAINER_TYPES.register(bus);
		TileEntityTypesInit.TILES.register(bus);

		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, Registration::onServerStart);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, Registration::onRegisterCommands);
	}

	@SubscribeEvent
	public static void onNewRegistry(RegistryEvent.NewRegistry event) {
		PlanetTraitRegistry.createRegistry(event);
		PlanetAttributeRegistry.createRegistry(event);
	}

	public static void onRegisterCommands(final RegisterCommandsEvent event) {
		CommandInit.registerCommands(event);
	}

	public static void onCommonSetup(final FMLCommonSetupEvent event) {
		MachinaNetwork.init();
		PlanetTraitPoolManager.INSTANCE = planetTraitPoolManager;
	}

	private static void registerPlanetTraits(final RegistryEvent.Register<PlanetTrait> event) {
		PlanetTraitInit.register(event);
	}

	private static void registerPlanetAttributes(final RegistryEvent.Register<PlanetAttributeType<?>> event) {
		PlanetAttributeTypesInit.register(event);
	}

	private static void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
		ModLoadingContext.get().registerConfig(type, spec,
				Machina.MOD_ID + "-" + type.toString().toLowerCase() + ".toml");
	}

	// Load all saved data
	public static void onServerStart(final FMLServerStartingEvent event) {

		MinecraftServer server = event.getServer();

		PlanetDimensionData.getDefaultInstance(server).dimensionIds
				.forEach(id -> DynamicDimensionHelper.createPlanet(server, id));

		StarchartData.getDefaultInstance(server).generateIf(server.getLevel(World.OVERWORLD).getSeed());
	}

}
