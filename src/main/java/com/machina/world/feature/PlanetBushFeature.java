package com.machina.world.feature;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBush;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.operator.SDFDisplacement;
import com.machina.api.util.math.sdf.operator.SDFScale3D;
import com.machina.api.util.math.sdf.post.SDFChanceFilter;
import com.machina.api.util.math.sdf.primitive.SDFSphere;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class PlanetBushFeature extends Feature<PlanetBushFeature.PlanetBushFeatureConfig> {

    public record PlanetBushFeatureConfig(PlanetBiomeBush bush) implements FeatureConfiguration {

        public static final Codec<PlanetBushFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
                .group(PlanetBiomeBush.CODEC.fieldOf("bush").forGetter(PlanetBushFeatureConfig::bush))
                .apply(instance, PlanetBushFeatureConfig::new));
    }

    public PlanetBushFeature() {
        super(PlanetBushFeatureConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlanetBushFeatureConfig> ctx) {
        PlanetBushFeatureConfig config = ctx.config();
        RandomSource rand = ctx.random();
        WorldGenLevel level = ctx.level();
        BlockPos pos = ctx.origin();

        int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
        BlockPos place = new BlockPos(pos.getX(), y, pos.getZ());

        float sx = MathUtil.randRange(rand, 0.5f, 1.5f);
        float sy = MathUtil.randRange(rand, 0.3f, 2.5f) / config.bush().radius();
        float sz = MathUtil.randRange(rand, 0.5f, 1.5f);

        SDF blob = new SDFSphere(config.bush().radius()).setBlock(config.bush().block());
        blob = new SDFScale3D(blob, sx, sy, sz);
        blob = new SDFDisplacement(blob, rand, 4f);
        blob.addPostProcess(new SDFChanceFilter(rand, 0.85f));
        blob.fillRecursiveShift(level, place, p -> {
            if (!level.getBlockState(p.immutable()).isAir()) {
                return false;
            }
            int shifted = 0;
            while (!level.getBlockState(p.immutable().below()).isSolid()) {
                p.move(Direction.DOWN);
                if (++shifted > 3) {
                    return false;
                }
            }
            return level.getBlockState(p.immutable()).isAir();
        });

        return true;
    }
}