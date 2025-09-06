package com.machina.world.feature;

import com.machina.api.util.block.WeightedStateProviderProvider;
import com.machina.block.PebbleBlock;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class PlanetGrassFeature extends Feature<PlanetGrassFeature.PlanetGrassFeatureConfig> {

    private static final int XZ_SPREAD = 7;
    private static final int Y_SPREAD = 3;
    private static final int TRIES = 32;

    public record PlanetGrassFeatureConfig(WeightedStateProviderProvider provider) implements FeatureConfiguration {

        public static final Codec<PlanetGrassFeatureConfig> CODEC = RecordCodecBuilder
                .create(instance -> instance
                        .group(WeightedStateProviderProvider.CODEC.fieldOf("provider")
                                .forGetter(PlanetGrassFeatureConfig::provider))
                        .apply(instance, PlanetGrassFeatureConfig::new));
    }

    public PlanetGrassFeature() {
        super(PlanetGrassFeatureConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlanetGrassFeatureConfig> ctx) {
        RandomSource rand = ctx.random();
        BlockPos pos = ctx.origin();
        WorldGenLevel level = ctx.level();
        BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
        int i = 0;
        int j = XZ_SPREAD + 1;
        int k = Y_SPREAD + 1;

        for (int l = 0; l < TRIES; ++l) {
            mbp.setWithOffset(pos, rand.nextInt(j) - rand.nextInt(j), rand.nextInt(k) - rand.nextInt(k),
                    rand.nextInt(j) - rand.nextInt(j));
            if (level.getBlockState(mbp).is(Blocks.AIR)) {
                if (placeBlock(ctx.config(), level, mbp, rand)) {
                    i++;
                }
            }
        }

        return i > 0;
    }

    public boolean placeBlock(PlanetGrassFeatureConfig cfg, WorldGenLevel level, BlockPos pos, RandomSource random) {
        BlockState blockstate = cfg.provider().getState(random, pos);

        // Apply random horizontal facing if the blockstate supports it
        if (blockstate.getOptionalValue(BlockStateProperties.HORIZONTAL_FACING).isPresent()) {
            blockstate = blockstate.setValue(BlockStateProperties.HORIZONTAL_FACING,
                    Direction.Plane.HORIZONTAL.getRandomDirection(random));
        }

        if (blockstate.canSurvive(level, pos)) {
            if (blockstate.getBlock() instanceof DoublePlantBlock) {
                if (!level.isEmptyBlock(pos.above())) {
                    return false;
                }

                DoublePlantBlock.placeAt(level, blockstate, pos, 2);
            } else {
                if (blockstate.getBlock() instanceof PebbleBlock) {
                    PebbleBlock.placeAt(level, blockstate, pos, 2);
                } else {
                    level.setBlock(pos, blockstate, 2);
                }
            }

            return true;
        }
        return false;
    }

}
