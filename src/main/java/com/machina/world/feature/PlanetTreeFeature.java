package com.machina.world.feature;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeTree;
import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.registration.init.RegistryInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class PlanetTreeFeature extends Feature<PlanetTreeFeature.PlanetTreeFeatureConfig> {

    public record PlanetTreeFeatureConfig(PlanetBiomeTree tree) implements FeatureConfiguration {
        public static final Codec<PlanetTreeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
                .group(PlanetBiomeTree.CODEC.fieldOf("tree").forGetter(PlanetTreeFeatureConfig::tree))
                .apply(instance, PlanetTreeFeatureConfig::new));

        public TreeMaker getTree() {
            return RegistryInit.TREE_REGISTRY.get().getValue(tree.tree());
        }
    }

    public PlanetTreeFeature() {
        super(PlanetTreeFeatureConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlanetTreeFeatureConfig> ctx) {
        PlanetBiomeTree cfg = ctx.config().tree();
        BlockPos origin = ctx.origin();
        RandomSource random = ctx.random();
        WorldGenLevel level = ctx.level();

//		if (!level.getBlockState(origin.below()).is(BlockTagInit.PLANET_GROWABLE)) {
//			return false;
//		}

        TreeMaker maker = ctx.config().getTree();
        SDF tree = maker.build(cfg, random, level, origin);
        if (tree == null)
            return false;

        if (random.nextFloat() < cfg.tree_fruit_chance()) {
            BlockState f = MathUtil.randomInList(cfg.fruit(), random);
            tree = tree.addPostProcesses(maker.fruit(random, cfg, f));
        }

        tree.fillRecursive(level, origin);
        return true;
    }
}