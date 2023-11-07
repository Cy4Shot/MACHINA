package com.machina.world;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.machina.api.network.PacketSender;
import com.machina.api.network.s2c.S2CUpdateDimensionList;
import com.machina.api.util.MachinaRL;
import com.machina.world.data.PlanetDimensionData;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.border.BorderChangeListener.DelegateBorderChangeListener;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

public class PlanetRegistrationHandler {
	public static void sendPlayerToDimension(ServerPlayer serverPlayer, ServerLevel targetWorld, BlockPos pos) {
		targetWorld.getChunk(pos);
		serverPlayer.teleportTo(targetWorld, pos.getX(), pos.getY(), pos.getZ(), serverPlayer.getRotationVector().x,
				serverPlayer.getRotationVector().y);
	}

	public static ServerLevel createPlanet(MinecraftServer server, String id) {
		PlanetDimensionData.getDefaultInstance(server).addId(id);
		return getOrCreateWorld(server, ResourceKey.create(Registries.DIMENSION, new MachinaRL(id)),
				PlanetFactory::createDimension);
	}

	public static ServerLevel getOrCreateWorld(MinecraftServer server, ResourceKey<Level> worldKey,
			BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory) {

		Map<ResourceKey<Level>, ServerLevel> map = server.forgeGetWorldMap();

		if (map.containsKey(worldKey)) {
			return map.get(worldKey);
		}
		return createAndRegisterWorldAndDimension(server, map, worldKey, dimensionFactory);
	}

	@SuppressWarnings("unchecked")
	private static ServerLevel createAndRegisterWorldAndDimension(MinecraftServer server,
			Map<ResourceKey<Level>, ServerLevel> map, ResourceKey<Level> worldKey,
			BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory) {

		final ServerLevel overworld = server.getLevel(Level.OVERWORLD);
		final ResourceKey<LevelStem> dimensionKey = ResourceKey.create(Registries.LEVEL_STEM, worldKey.location());
		final LevelStem dimension = dimensionFactory.apply(server, dimensionKey);

		final ChunkProgressListener chunkProgressListener = server.progressListenerFactory.create(11);
		final Executor executor = server.executor;
		final LevelStorageSource.LevelStorageAccess anvilConverter = server.storageSource;
		final WorldData worldData = server.getWorldData();
		final WorldOptions worldGenSettings = worldData.worldGenOptions();
		final DerivedLevelData derivedLevelData = new DerivedLevelData(worldData, worldData.overworldData());

		// register the actual dimension
		LayeredRegistryAccess<RegistryLayer> registries = server.registries();
		RegistryAccess.ImmutableRegistryAccess composite = (RegistryAccess.ImmutableRegistryAccess) registries
				.compositeAccess();

		Map<ResourceKey<? extends Registry<?>>, Registry<?>> regmap = new HashMap<>(composite.registries);
		ResourceKey<? extends Registry<?>> key = ResourceKey
				.create(ResourceKey.createRegistryKey(new ResourceLocation("root")), new ResourceLocation("dimension"));
		MappedRegistry<LevelStem> oldRegistry = (MappedRegistry<LevelStem>) regmap.get(key);
		Lifecycle oldLifecycle = oldRegistry.registryLifecycle();

		final MappedRegistry<LevelStem> newRegistry = new MappedRegistry<>(Registries.LEVEL_STEM, oldLifecycle, false);
		for (Map.Entry<ResourceKey<LevelStem>, LevelStem> entry : oldRegistry.entrySet()) {
			final ResourceKey<LevelStem> oldKey = entry.getKey();
			final ResourceKey<Level> oldLevelKey = ResourceKey.create(Registries.DIMENSION, oldKey.location());
			final LevelStem dim = entry.getValue();
			if (dim != null && oldLevelKey != worldKey) {
				Registry.register(newRegistry, oldKey, dim);
			}
		}
		Registry.register(newRegistry, dimensionKey, dimension);
		regmap.replace(key, newRegistry);
		composite.registries = (Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>>) regmap;

		final ServerLevel newWorld = new ServerLevel(server, executor, anvilConverter, derivedLevelData, worldKey,
				dimension, chunkProgressListener, false, BiomeManager.obfuscateSeed(worldGenSettings.seed()),
				ImmutableList.of(), false, null);
		overworld.getWorldBorder().addListener(new DelegateBorderChangeListener(newWorld.getWorldBorder()));

		map.put(worldKey, newWorld);
		server.markWorldsDirty();
		MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(newWorld));
		PacketSender.sendToClients(new S2CUpdateDimensionList(ImmutableSet.of(worldKey), ImmutableSet.of()));

		return newWorld;
	}
}