package com.machina.world.biome;

import com.machina.api.starchart.Starchart;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.planet_type.PlanetType;
import com.machina.api.starchart.planet_type.PlanetType.BiomePlacement;
import com.machina.api.util.PlanetHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.Parameter;
import net.minecraft.world.level.biome.Climate.ParameterList;
import net.minecraft.world.level.biome.Climate.ParameterPoint;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class PlanetBiomeSource {

    private static final String MIDDLE = "MIDDLE";
    private static final String PEAK = "PEAK";
    private static final String SLOPE = "SLOPE";
    private static final String PLATEAU = "PLATEAU";
    private static final String BEACH = "BEACH";
    private static final String RIVER = "RIVER";
    private static final String CAVE_SHALLOW = "CAVE_SHALLOW";
    private static final String CAVE_MIDDLE = "CAVE_MIDDLE";
    private static final String CAVE_DEEP = "CAVE_DEEP";
    private static final String DEEP_OCEAN = "DEEP_OCEAN";
    private static final String OCEAN = "OCEAN";

    private static final Set<String> WATER_ONLY = Set.of(DEEP_OCEAN, OCEAN, BEACH, RIVER);

    private static final Parameter ZERO = Parameter.point(0.0F);
    private static final Parameter ONE = Parameter.point(1.0F);
    private static final Parameter FULL = Parameter.span(-1.0F, 1.0F);
    private static final Parameter[] erosions = new Parameter[]{Parameter.span(-1.0F, -0.78F),
            Parameter.span(-0.78F, -0.375F), Parameter.span(-0.375F, -0.2225F), Parameter.span(-0.2225F, 0.05F),
            Parameter.span(0.05F, 0.45F), Parameter.span(0.45F, 0.55F), Parameter.span(0.55F, 1.0F)};
    private static final Parameter[] depths = new Parameter[]{Climate.Parameter.span(0.8F, 1.0F),
            Climate.Parameter.span(1.0F, 1.2F), Climate.Parameter.span(1.2F, 1.4F)};
    private static final Parameter deepOceanCont = Parameter.span(-1.05F, -0.455F);
    private static final Parameter oceanCont = Parameter.span(-0.455F, -0.19F);
    private static final Parameter coastCont = Parameter.span(-0.19F, -0.11F);
    private static final Parameter inlandCont = Parameter.span(-0.11F, 0.55F);
    private static final Parameter nearInlandCont = Parameter.span(-0.11F, 0.03F);
    private static final Parameter midInlandCont = Parameter.span(0.03F, 0.3F);
    private static final Parameter farInlandCont = Parameter.span(0.3F, 1.0F);

    private final List<Pair<ParameterPoint, Holder<Biome>>> biomes;
    private final Planet planet;
    private final PlanetType type;
    private final RegistryLookup<Biome> biomeReg;

    public PlanetBiomeSource(ResourceKey<LevelStem> key, long seed, RegistryAccess lookup) {
        this.biomes = new ArrayList<>();
        this.planet = Starchart.system(seed).planets().get(PlanetHelper.getIdDim(key));
        this.type = planet.type();
        this.biomeReg = lookup.lookup(Registries.BIOME).get();

        addLandBiomes();
        addOceanBiomes();
        addUndergroundBiomes();
    }

    public static List<ParameterPoint> spawnTarget() {
        return List.of(
                new ParameterPoint(FULL, FULL, Parameter.span(inlandCont, FULL), FULL, ZERO,
                        Parameter.span(-1.0F, -0.16F), 0L),
                new ParameterPoint(FULL, FULL, Parameter.span(inlandCont, FULL), FULL, ZERO,
                        Parameter.span(0.16F, 1.0F), 0L));
    }

    private List<BiomePlacement> withCategory(String category) {
        if (!planet.hasGenLiquid() && WATER_ONLY.contains(category)) {
            return List.of();
        }
        return type.biomes().stream().filter(x -> x.placements().contains(category)).collect(Collectors.toList());
    }

    public static List<Parameter> generateIntervals(int n) {
        List<Parameter> intervals = new ArrayList<>();
        float intervalSize = 2.0f / n;
        for (int i = 0; i < n; i++) {
            float minInterval = -1.0f + i * intervalSize;
            float maxInterval = minInterval + intervalSize;
            intervals.add(Parameter.span(minInterval, maxInterval));
        }
        return intervals;
    }

    private void forall(String category, BiConsumer<Holder<Biome>, Parameter> action) {
        List<BiomePlacement> matching = withCategory(category);
        List<Parameter> intervals = generateIntervals(matching.size());
        for (int i = 0; i < matching.size(); i++) {
            ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, matching.get(i).biome());
            action.accept(this.biomeReg.get(key).get(), intervals.get(i));
        }
    }

    private void addOceanBiomes() {
        if (planet.hasGenLiquid()) {
            forall(DEEP_OCEAN, (ocean, v) -> addOceanBiome(deepOceanCont, ocean, v));
            forall(OCEAN, (ocean, v) -> addOceanBiome(oceanCont, ocean, v));
        }
    }

    private void addUndergroundBiomes() {
        forall(CAVE_SHALLOW, (cave, v) -> addUndergroundBiome(depths[0], cave, v));
        forall(CAVE_MIDDLE, (cave, v) -> addUndergroundBiome(depths[1], cave, v));
        forall(CAVE_DEEP, (cave, v) -> addUndergroundBiome(depths[2], cave, v));
    }

    private void addLandBiomes() {
        addMidSlice(Parameter.span(-1.0F, -0.93333334F));
        addHighSlice(Parameter.span(-0.93333334F, -0.7666667F));
        addPeaks(Parameter.span(-0.7666667F, -0.56666666F));
        addHighSlice(Parameter.span(-0.56666666F, -0.4F));
        addMidSlice(Parameter.span(-0.4F, -0.26666668F));
        addLowSlice(Parameter.span(-0.26666668F, -0.05F));
        addValleys(Parameter.span(-0.05F, 0.05F));
        addLowSlice(Parameter.span(0.05F, 0.26666668F));
        addMidSlice(Parameter.span(0.26666668F, 0.4F));
        addHighSlice(Parameter.span(0.4F, 0.56666666F));
        addPeaks(Parameter.span(0.56666666F, 0.7666667F));
        addHighSlice(Parameter.span(0.7666667F, 0.93333334F));
        addMidSlice(Parameter.span(0.93333334F, 1.0F));
    }

    private void addPeaks(Parameter depth) {
        forall(PLATEAU, (biome, v) -> {
            addSurfaceBiome(Parameter.span(midInlandCont, farInlandCont), erosions[2], depth, biome, v);
            addSurfaceBiome(farInlandCont, erosions[3], depth, biome, v);
        });

        forall(PEAK, (biome, v) -> {
            addSurfaceBiome(Parameter.span(coastCont, farInlandCont), erosions[0], depth, biome, v);
            addSurfaceBiome(Parameter.span(midInlandCont, farInlandCont), erosions[1], depth, biome, v);
        });
        forall(MIDDLE, (biome, v) -> {
            addSurfaceBiome(Parameter.span(coastCont, nearInlandCont), Parameter.span(erosions[2], erosions[3]), depth,
                    biome, v);
            addSurfaceBiome(Parameter.span(coastCont, farInlandCont), erosions[4], depth, biome, v);
            addSurfaceBiome(Parameter.span(coastCont, farInlandCont), erosions[6], depth, biome, v);
        });

    }

    private void addHighSlice(Parameter depth) {
        forall(PLATEAU, (biome, v) -> {
            addSurfaceBiome(Parameter.span(midInlandCont, farInlandCont), erosions[2], depth, biome, v);
            addSurfaceBiome(farInlandCont, erosions[3], depth, biome, v);
        });
        forall(SLOPE, (biome, v) -> {
            addSurfaceBiome(nearInlandCont, erosions[0], depth, biome, v);
            addSurfaceBiome(Parameter.span(midInlandCont, farInlandCont), erosions[1], depth, biome, v);
        });
        forall(PEAK, (biome, v) -> addSurfaceBiome(Parameter.span(midInlandCont, farInlandCont), erosions[0], depth,
                biome, v));
        forall(MIDDLE, (biome, v) -> {
            addSurfaceBiome(coastCont, Parameter.span(erosions[0], erosions[1]), depth, biome, v);
            addSurfaceBiome(Parameter.span(coastCont, nearInlandCont), Parameter.span(erosions[2], erosions[3]), depth,
                    biome, v);
            addSurfaceBiome(Parameter.span(coastCont, farInlandCont), erosions[4], depth, biome, v);
            addSurfaceBiome(Parameter.span(coastCont, farInlandCont), erosions[6], depth, biome, v);
        });
    }

    private void addMidSlice(Parameter depth) {
        forall(PLATEAU, (biome, v) -> {
            addSurfaceBiome(farInlandCont, erosions[1], depth, biome, v);
            addSurfaceBiome(farInlandCont, erosions[2], depth, biome, v);
        });
        forall(BEACH, (biome, v) -> {
            if (depth.max() < 0L) {
                addSurfaceBiome(coastCont, erosions[4], depth, biome, v);
            }
            if (depth.max() < 0L) {
                addSurfaceBiome(coastCont, erosions[6], depth, biome, v);
            }
        });
        forall(SLOPE, (biome, v) -> addSurfaceBiome(Parameter.span(nearInlandCont, farInlandCont), erosions[0], depth,
                biome, v));
        forall(MIDDLE, (biome, v) -> {
            addSurfaceBiome(nearInlandCont, erosions[2], depth, biome, v);
            addSurfaceBiome(Parameter.span(coastCont, nearInlandCont), erosions[3], depth, biome, v);
            if (depth.max() < 0L) {
                addSurfaceBiome(Parameter.span(nearInlandCont, farInlandCont), erosions[4], depth, biome, v);
            } else {
                addSurfaceBiome(Parameter.span(coastCont, farInlandCont), erosions[4], depth, biome, v);
            }
            if (depth.max() >= 0L) {
                addSurfaceBiome(coastCont, erosions[6], depth, biome, v);
            }
            addSurfaceBiome(Parameter.span(nearInlandCont, farInlandCont), erosions[6], depth, biome, v);
        });
    }

    private void addLowSlice(Parameter depth) {
        forall(BEACH, (biome, v) -> {
            addSurfaceBiome(coastCont, Parameter.span(erosions[3], erosions[4]), depth, biome, v);
            addSurfaceBiome(coastCont, erosions[6], depth, biome, v);
        });
        forall(MIDDLE, (biome, v) -> {
            addSurfaceBiome(nearInlandCont, Parameter.span(erosions[2], erosions[3]), depth, biome, v);
            addSurfaceBiome(Parameter.span(nearInlandCont, farInlandCont), erosions[4], depth, biome, v);
            addSurfaceBiome(Parameter.span(midInlandCont, farInlandCont), erosions[5], depth, biome, v);

            addSurfaceBiome(Parameter.span(nearInlandCont, farInlandCont), erosions[6], depth, biome, v);
        });
    }

    private void addValleys(Parameter depth) {
        forall(BEACH, (biome, v) -> {
            if (depth.max() < 0L) {
                addSurfaceBiome(coastCont, Parameter.span(erosions[0], erosions[1]), depth, biome, v);
            }
        });
        forall(RIVER, (biome, v) -> {
            if (depth.max() >= 0L) {
                addSurfaceBiome(coastCont, Parameter.span(erosions[0], erosions[1]), depth, biome, v);
            }
            addSurfaceBiome(nearInlandCont, Parameter.span(erosions[0], erosions[1]), depth, biome, v);
            addSurfaceBiome(Parameter.span(coastCont, farInlandCont), Parameter.span(erosions[2], erosions[5]), depth,
                    biome, v);
            addSurfaceBiome(coastCont, erosions[6], depth, biome, v);
            addSurfaceBiome(Parameter.span(inlandCont, farInlandCont), erosions[6], depth, biome, v);
        });
    }

    private void addSurfaceBiome(Parameter continentalness, Parameter erosion, Parameter weirdness, Holder<Biome> biome,
                                 Parameter variant) {
        if (biome == null)
            return;
        addBiome(variant, continentalness, erosion, ZERO, weirdness, biome);
        addBiome(variant, continentalness, erosion, ONE, weirdness, biome);
    }

    private void addOceanBiome(Parameter continentalness, Holder<Biome> biome, Parameter variant) {
        addBiome(variant, continentalness, PlanetBiomeSource.FULL, ZERO, PlanetBiomeSource.FULL, biome);
    }

    private void addUndergroundBiome(Parameter depth, Holder<Biome> biome, Parameter variant) {
        addBiome(variant, PlanetBiomeSource.FULL, PlanetBiomeSource.FULL, depth, PlanetBiomeSource.FULL, biome);
    }

    private void addBiome(Parameter variant, Parameter continentalness, Parameter erosion, Parameter depth,
                          Parameter weirdness, Holder<Biome> biome) {
        biomes.add(Pair.of(Climate.parameters(variant, FULL, continentalness, erosion, depth, weirdness, (float) 0.0),
                biome));
    }

    public MultiNoiseBiomeSource build() {
        return MultiNoiseBiomeSource.createFromList(new ParameterList<>(biomes));
    }
}