package com.machina.world;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.machina.util.MachinaRL;
import com.machina.world.data.PlanetDimensionData;
import com.mojang.serialization.Lifecycle;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.chunk.listener.IChunkStatusListenerFactory;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.IServerConfiguration;
import net.minecraft.world.storage.SaveFormat.LevelSave;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

// https://gist.github.com/Commoble/7db2ef25f94952a4d2e2b7e3d4be53e0
public class DynamicDimensionHelper {

	public static final Function<MinecraftServer, IChunkStatusListenerFactory> CHUNK_STATUS_LISTENER_FACTORY_FIELD = getInstanceField(
			MinecraftServer.class, "field_213220_d");
	public static final Function<MinecraftServer, Executor> BACKGROUND_EXECUTOR_FIELD = getInstanceField(
			MinecraftServer.class, "field_213217_au");
	public static final Function<MinecraftServer, LevelSave> ANVIL_CONVERTER_FOR_ANVIL_FILE_FIELD = getInstanceField(
			MinecraftServer.class, "field_71310_m");

	public static void sendPlayerToDimension(ServerPlayerEntity serverPlayer, ServerWorld targetWorld,
			Vector3d targetVec) {
		targetWorld.getChunk(new BlockPos(targetVec));
		serverPlayer.teleportTo(targetWorld, targetVec.x(), targetVec.y(), targetVec.z(),
				serverPlayer.getRotationVector().x, serverPlayer.getRotationVector().y);
	}

	public static ServerWorld createPlanet(MinecraftServer server, String id) {

		PlanetDimensionData.getDefaultInstance(server).addId(id);

		ServerWorld world = getOrCreateWorld(server, RegistryKey.create(Registry.DIMENSION_REGISTRY, new MachinaRL(id)),
				DynamicDimensionFactory::createDimension);
		return world;
	}

	public static ServerWorld getOrCreateWorld(MinecraftServer server, RegistryKey<World> worldKey,
			BiFunction<MinecraftServer, RegistryKey<Dimension>, Dimension> dimensionFactory) {

		Map<RegistryKey<World>, ServerWorld> map = server.forgeGetWorldMap();

		if (map.containsKey(worldKey)) {
			return map.get(worldKey);
		} else {
			return createAndRegisterWorldAndDimension(server, map, worldKey, dimensionFactory);
		}
	}

	private static ServerWorld createAndRegisterWorldAndDimension(MinecraftServer server,
			Map<RegistryKey<World>, ServerWorld> map, RegistryKey<World> worldKey,
			BiFunction<MinecraftServer, RegistryKey<Dimension>, Dimension> dimensionFactory) {
		ServerWorld overworld = server.getLevel(World.OVERWORLD);
		RegistryKey<Dimension> dimensionKey = RegistryKey.create(Registry.LEVEL_STEM_REGISTRY, worldKey.location());
		Dimension dimension = dimensionFactory.apply(server, dimensionKey);

		// Preload 11 Chunks
		IChunkStatusListener chunkListener = CHUNK_STATUS_LISTENER_FACTORY_FIELD.apply(server).create(11);
		Executor executor = BACKGROUND_EXECUTOR_FIELD.apply(server);
		LevelSave levelSave = ANVIL_CONVERTER_FOR_ANVIL_FILE_FIELD.apply(server);

		IServerConfiguration serverConfig = server.getWorldData();
		DimensionGeneratorSettings dimensionGeneratorSettings = serverConfig.worldGenSettings();

		dimensionGeneratorSettings.dimensions().register(dimensionKey, dimension, Lifecycle.experimental());
		DerivedWorldInfo derivedWorldInfo = new DerivedWorldInfo(serverConfig, serverConfig.overworldData());

		ServerWorld newWorld = new ServerWorld(server, executor, levelSave, derivedWorldInfo, worldKey,
				dimension.type(), chunkListener, dimension.generator(), dimensionGeneratorSettings.isDebug(),
				BiomeManager.obfuscateSeed(dimensionGeneratorSettings.seed()), ImmutableList.of(), false);

		overworld.getWorldBorder().addListener(new IBorderListener.Impl(newWorld.getWorldBorder()));
		map.put(worldKey, newWorld);
		server.markWorldsDirty();
		MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(newWorld));
		return newWorld;
	}

	@SuppressWarnings("unchecked")
	static <FIELDHOLDER, FIELDTYPE> Function<FIELDHOLDER, FIELDTYPE> getInstanceField(
			Class<FIELDHOLDER> fieldHolderClass, String fieldName) {
		Field field = ObfuscationReflectionHelper.findField(fieldHolderClass, fieldName);

		return instance -> {
			try {
				return (FIELDTYPE) (field.get(instance));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		};
	}
}
