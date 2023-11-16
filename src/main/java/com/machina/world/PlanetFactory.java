package com.machina.world;

import com.machina.Machina;
import com.machina.api.util.MachinaRL;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PlanetFactory {

	public static final ResourceKey<DimensionType> TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE,
			new MachinaRL(Machina.MOD_ID));

	public static LevelStem createDimension(MinecraftServer server, ResourceKey<LevelStem> key) {
		// TODO: Own Biome
		return new LevelStem(getDimensionType(server),
				new PlanetChunkGenerator(
						new FixedBiomeSource(ServerLifecycleHooks.getCurrentServer().registryAccess()
								.lookup(Registries.BIOME).get().get(Biomes.THE_VOID).get()),
						key, server.overworld().getSeed()));
	}

	public static Holder<DimensionType> getDimensionType(MinecraftServer server) {
		return server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).getHolderOrThrow(TYPE_KEY);
	}
}
