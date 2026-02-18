package com.machina.world;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import com.google.common.collect.ImmutableList;
import com.machina.api.network.s2c.S2CUpdateDimensionList;
import com.machina.api.util.MachinaRL;
import com.machina.world.data.PlanetDimensionData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.BorderChangeListener.DelegateBorderChangeListener;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.WorldData;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlanetRegistrationHandler {
	public static void sendPlayerToDimension(ServerPlayer serverPlayer, ServerLevel targetWorld, BlockPos pos) {
		targetWorld.getChunk(pos);
		serverPlayer.teleportTo(targetWorld, pos.getX(), pos.getY(), pos.getZ(), serverPlayer.getRotationVector().x,
				serverPlayer.getRotationVector().y);
	}

	public static ServerLevel createPlanet(MinecraftServer server, int id) {
		PlanetDimensionData.getDefaultInstance(server).addId(id);
		return getOrCreateWorld(server, ResourceKey.create(Registries.DIMENSION, MachinaRL.create(id)),
				PlanetFactory::createDimension, id);
	}

	@SuppressWarnings("deprecation")
	public static ServerLevel getOrCreateWorld(MinecraftServer server, ResourceKey<Level> worldKey,
			BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory, int id) {
		Map<ResourceKey<Level>, ServerLevel> map = server.forgeGetWorldMap();
		ServerLevel existingLevel = map.get(worldKey);
		return existingLevel == null ? createAndRegister(server, map, worldKey, dimensionFactory, id) : existingLevel;
	}

	@SuppressWarnings("deprecation")
	private static ServerLevel createAndRegister(MinecraftServer server, Map<ResourceKey<Level>, ServerLevel> map,
			ResourceKey<Level> worldKey,
			BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory, int id) {

		final ServerLevel overworld = server.getLevel(Level.OVERWORLD);
		final ResourceKey<LevelStem> dimensionKey = ResourceKey.create(Registries.LEVEL_STEM, worldKey.location());
		final LevelStem dimension = dimensionFactory.apply(server, dimensionKey);

		final ChunkProgressListener chunkProgressListener = server.progressListenerFactory.create(11);
		final WorldData worldData = server.getWorldData();
		final DerivedLevelData derivedLevelData = new DerivedLevelData(worldData, worldData.overworldData());

		Registry<LevelStem> dimRegFrozen = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM);
		if (dimRegFrozen instanceof MappedRegistry<LevelStem> dimReg) {
			dimReg.unfreeze();
			dimReg.register(dimensionKey, dimension, RegistrationInfo.BUILT_IN);
		} else {
			throw new IllegalStateException(String.format(
					"Unable to register dimension %s -- dimension registry not writable", dimensionKey.location()));
		}

		Objects.requireNonNull(overworld);

		final ServerLevel newWorld = new ServerLevel(server, server.executor, server.storageSource, derivedLevelData,
				worldKey, dimension, chunkProgressListener, worldData.isDebugWorld(), overworld.getSeed() + id,
				ImmutableList.of(), false, null);

		overworld.getWorldBorder().addListener(new DelegateBorderChangeListener(newWorld.getWorldBorder()));

		map.put(worldKey, newWorld);
		server.markWorldsDirty();
		NeoForge.EVENT_BUS.post(new LevelEvent.Load(newWorld));
		PacketDistributor.sendToAllPlayers(new S2CUpdateDimensionList(worldKey));

		return newWorld;
	}
}