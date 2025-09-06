package com.machina.world.feature.tree;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeTree;
import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.SplineUtil;
import com.machina.api.util.math.sdf.operator.SDFUnion;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

public class DeadRadialBaobabTree implements TreeMaker {
    private final List<Vector3f> BRANCH = Arrays.asList(new Vector3f(0, 0, 0), new Vector3f(0.1F, 0.3F, 0),
            new Vector3f(0.4F, 0.6F, 0), new Vector3f(0.8F, 0.8F, 0), new Vector3f(1, 1, 0));

    @Override
    public SDF build(PlanetBiomeTree config, RandomSource random, WorldGenLevel l, BlockPos p) {
        float size = MathUtil.randRange(random, 6, 12);
        List<Vector3f> spline = SplineUtil.makeSpline(0, 0, 0, 0, size, 0, 6);
        SplineUtil.offsetParts(spline, random, 1F, 0, 1F);

        float radius = size * MathUtil.randRange(random, 0.9F, 0.95F);

        SDF stem = SplineUtil.buildSDF(spline, 2.3F, 1.2F, config.wood());

        int count = (int) radius;
        for (int i = 0; i < count; i++) {
            if (random.nextInt(3) != 0)
                continue;

            float angle = (float) i / (float) count * Mth.PI * 2;
            float scale = radius * MathUtil.randRange(random, 0.85F, 1.15F);

            List<Vector3f> branch = SplineUtil.copySpline(BRANCH);
            SplineUtil.rotateSpline(branch, angle);
            SplineUtil.scale(branch, scale);
            SDF b1 = SplineUtil.buildSDF(branch, 1, 1, config.wood());
            stem = new SDFUnion(stem, b1);
        }

        return stem;
    }

    @Override
    public BlockState getLeafAttachment(PlanetBiomeTree config, RandomSource random) {
        return config.wood();
    }
}
