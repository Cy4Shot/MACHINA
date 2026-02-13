package com.machina.world;

import com.machina.Machina;
import com.machina.api.starchart.Starchart;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.planet_type.PlanetType;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.PlanetHelper;
import com.machina.world.biome.PlanetBiomeSource;
import com.machina.world.functions.PlanetDensityFunction;
import com.machina.world.functions.PlanetSurfaceRule;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class PlanetFactory {

    public static final ResourceKey<DimensionType> TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE,
            new MachinaRL(Machina.MOD_ID));

    public static LevelStem createDimension(MinecraftServer server, ResourceKey<LevelStem> key) {
        long seed = server.overworld().getSeed();
        Planet planet = Starchart.system(seed).planets().get(PlanetHelper.getIdDim(key));
        PlanetType type = planet.type();
        RegistryAccess lookup = server.registryAccess();
        MultiNoiseBiomeSource bs = new PlanetBiomeSource(key, seed, lookup).build();

        BlockState fluid = planet.getDominantLiquidBodyBlock();
        int sea_level = type.shape().sea_level();
        if (fluid == null) {
            fluid = Blocks.WATER.defaultBlockState();
            sea_level = -1;
        }

        NoiseGeneratorSettings settings = new NoiseGeneratorSettings(type.shape().getNoiseSettings(), type.base(),
                fluid,
                PlanetDensityFunction.planet(planet, lookup.lookup(Registries.DENSITY_FUNCTION).get(),
                        lookup.lookup(Registries.NOISE).get()),
                PlanetSurfaceRule.planet(planet), PlanetBiomeSource.spawnTarget(), sea_level, false, true, false,
                false);

        return new LevelStem(getDimensionType(server),
                new PlanetChunkGenerator(bs, Holder.direct(settings), key, seed));
    }

    public static Holder<DimensionType> getDimensionType(MinecraftServer server) {
        return server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).getHolderOrThrow(TYPE_KEY);
    }
}
