package com.machina.api.starchart.planet_biome;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicates;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBigRock;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.post.SDFFruitPlacer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface RockMaker {

    SDF build(PlanetBiomeBigRock config, RandomSource random, WorldGenLevel l, BlockPos p);

    default List<SDFFruitPlacer> extras(RandomSource random, PlanetBiomeBigRock cfg) {
        BlockState f = cfg.extra();

        return Arrays.asList(
                new SDFFruitPlacer(random, Direction.NORTH, cfg.side_extra_chance(), f, Predicates.alwaysTrue()),
                new SDFFruitPlacer(random, Direction.SOUTH, cfg.side_extra_chance(), f, Predicates.alwaysTrue()),
                new SDFFruitPlacer(random, Direction.WEST, cfg.side_extra_chance(), f, Predicates.alwaysTrue()),
                new SDFFruitPlacer(random, Direction.EAST, cfg.side_extra_chance(), f, Predicates.alwaysTrue()),

                new SDFFruitPlacer(random, Direction.DOWN, cfg.down_extra_chance(), f, Predicates.alwaysTrue()),

                new SDFFruitPlacer(random, Direction.UP, cfg.up_extra_chance(), f, Predicates.alwaysTrue()));
    }
    
    default boolean allowsWaterPlacement() {
        return false;
    }
}