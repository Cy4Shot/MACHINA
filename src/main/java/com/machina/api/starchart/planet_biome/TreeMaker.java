package com.machina.api.starchart.planet_biome;

import java.util.List;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeTree;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.post.SDFFruitPlacer;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface TreeMaker {

    SDF build(PlanetBiomeTree config, RandomSource random, WorldGenLevel l, BlockPos p);

    BlockState getLeafAttachment(PlanetBiomeTree config, RandomSource random);

    default List<SDFFruitPlacer> fruit(RandomSource random, PlanetBiomeTree cfg, BlockState f) {

        return cfg.fruit_dirs().stream().map(d -> new SDFFruitPlacer(random, d, cfg.fruit_chance(), f,
                s -> s.equals(getLeafAttachment(cfg, random)))).toList();
    }
    
    default boolean allowsWaterPlacement() {
        return false;
    }
}