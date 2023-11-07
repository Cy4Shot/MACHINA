package com.machina.world;

import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.machina.Machina;
import com.machina.api.util.MachinaRL;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class PlanetFactory {

	public static final Function<MinecraftServer, MultiNoiseBiomeSource> BIOME_SOURCE = s -> MultiNoiseBiomeSource
			.createFromList(generateBiomes(b -> getBiome(s).apply(b)));

	public static final ResourceKey<DimensionType> TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE,
			new MachinaRL(Machina.MOD_ID));

	public static LevelStem createDimension(MinecraftServer server, ResourceKey<LevelStem> key) {
		return new LevelStem(getDimensionType(server),
				new PlanetChunkGenerator(BIOME_SOURCE.apply(server), key, server.getLevel(Level.OVERWORLD).getSeed()));
	}

	public static Holder<DimensionType> getDimensionType(MinecraftServer server) {
		return server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).getHolderOrThrow(TYPE_KEY);
	}

	public static Function<ResourceKey<Biome>, Holder<Biome>> getBiome(MinecraftServer server) {
		return b -> Holder.direct(server.registryAccess().registryOrThrow(Registries.BIOME).getOrThrow(b));
	}

	static Climate.ParameterList<Holder<Biome>> generateBiomes(Function<ResourceKey<Biome>, Holder<Biome>> decomposer) {
		ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
		new PlanetBiomeBuilder().addBiomes(bs -> builder.add(bs.mapSecond(decomposer)));
		return new Climate.ParameterList<>(builder.build());
	}
}
