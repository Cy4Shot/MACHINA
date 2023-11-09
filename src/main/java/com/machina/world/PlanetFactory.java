package com.machina.world;

import com.machina.Machina;
import com.machina.api.util.MachinaRL;
import com.machina.world.biome.PlanetBiomeProvider;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class PlanetFactory {

	public static final ResourceKey<DimensionType> TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE,
			new MachinaRL(Machina.MOD_ID));

	public static LevelStem createDimension(MinecraftServer server, ResourceKey<LevelStem> key) {
		return new LevelStem(getDimensionType(server), new PlanetChunkGenerator(
				new PlanetBiomeProvider(server.registryAccess()), key, server.getLevel(Level.OVERWORLD).getSeed()));
	}

	public static Holder<DimensionType> getDimensionType(MinecraftServer server) {
		return server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).getHolderOrThrow(TYPE_KEY);
	}
}
