package com.machina.registration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.machina.Machina;
import com.machina.client.dimension.MachinaDimRenderer;
import com.machina.config.ClientConfig;
import com.machina.config.CommonConfig;
import com.machina.network.MachinaNetwork;
import com.machina.planet.PlanetTraitPoolManager;
import com.machina.planet.attribute.PlanetAttributeType;
import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.init.AttributeInit;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.CommandInit;
import com.machina.registration.init.ConfiguredFeatureInit;
import com.machina.registration.init.ContainerInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.SoundInit;
import com.machina.registration.init.StructureInit;
import com.machina.registration.init.TileEntityInit;
import com.machina.registration.init.TraitInit;
import com.machina.registration.registry.PlanetAttributeRegistry;
import com.machina.registration.registry.PlanetTraitRegistry;
import com.machina.world.PlanetRegistrationHandler;
import com.machina.world.data.PlanetDimensionData;
import com.machina.world.data.StarchartData;
import com.mojang.serialization.Codec;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.MOD)
public class Registration {

	public static final ItemGroup MAIN_GROUP = new MachinaItemGroup(() -> ItemInit.TRANSISTOR.get(), "main");
	public static final ItemGroup PLANET_GROUP = new MachinaItemGroup(() -> ItemInit.SCANNER.get(), "planet");

	public static PlanetTraitPoolManager TRAIT_POOL_MANAGER = new PlanetTraitPoolManager();

	public static void register(IEventBus bus) {

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			MachinaDimRenderer.registerDimensionRenderInfo();
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
		FluidInit.FLUIDS.register(bus);
		ContainerInit.CONTAINERS.register(bus);
		TileEntityInit.TILES.register(bus);
		StructureInit.STRUCTURES.register(bus);
		SoundInit.SOUNDS.register(bus);

		MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, Registration::addDimensionalSpacing);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, Registration::onServerStart);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, Registration::onRegisterCommands);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, Registration::biomeModification);
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
		PlanetTraitPoolManager.INSTANCE = TRAIT_POOL_MANAGER;

		event.enqueueWork(() -> {
			StructureInit.setupStructures();
			ConfiguredFeatureInit.registerConfiguredStructures();
		});
	}

	private static void registerPlanetTraits(final Register<PlanetTrait> event) {
		TraitInit.register(event);
	}

	private static void registerPlanetAttributes(final Register<PlanetAttributeType<?>> event) {
		AttributeInit.register(event);
	}

	private static void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
		ModLoadingContext.get().registerConfig(type, spec,
				Machina.MOD_ID + "-" + type.toString().toLowerCase() + ".toml");
	}

	// Load all saved data
	public static void onServerStart(final FMLServerStartingEvent event) {

		MinecraftServer server = event.getServer();

		PlanetDimensionData.getDefaultInstance(server).dimensionIds
				.forEach(id -> PlanetRegistrationHandler.createPlanet(server, id));

		StarchartData.getDefaultInstance(server).generateIf(server.getLevel(World.OVERWORLD).getSeed());
	}

	public static void biomeModification(final BiomeLoadingEvent event) {

		// Don't add to MACHINA dimensions, nether, or end.
		if (event.getName() == null || event.getName().getNamespace().equals(Machina.MOD_ID))
			return;
		Category cat = event.getCategory();
		if (cat.equals(Category.NETHER) || cat.equals(Category.THEEND))
			return;

		event.getGeneration().getStructures().add(() -> ConfiguredFeatureInit.CONFIGURED_SHIP);
	}

	private static Method GETCODEC_METHOD;

	@SuppressWarnings({ "unchecked", "resource" })
	public static void addDimensionalSpacing(final WorldEvent.Load event) {
		if (event.getWorld() instanceof ServerWorld) {
			ServerWorld serverWorld = (ServerWorld) event.getWorld();
			try {
				if (GETCODEC_METHOD == null)
					GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
				ResourceLocation cgRL = Registry.CHUNK_GENERATOR
						.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD
								.invoke(serverWorld.getChunkSource().generator));
				if (cgRL != null && cgRL.getNamespace().equals("terraforged"))
					return;
			} catch (Exception e) {
				Machina.LOGGER.error("Was unable to check if " + serverWorld.dimension().location()
						+ " is using Terraforged's ChunkGenerator.");
			}
			if (serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator
					&& serverWorld.dimension().equals(World.OVERWORLD)) {
				return;
			}

			Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(
					serverWorld.getChunkSource().generator.getSettings().structureConfig());
			tempMap.putIfAbsent(StructureInit.SHIP.get(),
					DimensionStructuresSettings.DEFAULTS.get(StructureInit.SHIP.get()));
			serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
		}
	}

}
