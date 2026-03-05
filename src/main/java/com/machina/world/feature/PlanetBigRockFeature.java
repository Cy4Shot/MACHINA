package com.machina.world.feature;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBigRock;
import com.machina.api.starchart.planet_biome.RockMaker;
import com.machina.api.util.math.sdf.SDF;
import com.machina.registration.init.RegistryInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class PlanetBigRockFeature extends Feature<PlanetBigRockFeature.PlanetBigRockFeatureConfig> {

	public record PlanetBigRockFeatureConfig(PlanetBiomeBigRock rock) implements FeatureConfiguration {
		public static final Codec<PlanetBigRockFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(PlanetBiomeBigRock.CODEC.fieldOf("rock").forGetter(PlanetBigRockFeatureConfig::rock))
				.apply(instance, PlanetBigRockFeatureConfig::new));

		public RockMaker getRock() {
			return RegistryInit.ROCK.get(rock.rock());
		}
	}

	public PlanetBigRockFeature() {
		super(PlanetBigRockFeatureConfig.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<PlanetBigRockFeatureConfig> ctx) {
		PlanetBiomeBigRock cfg = ctx.config().rock();
		BlockPos origin = ctx.origin();
		RandomSource random = ctx.random();
		WorldGenLevel level = ctx.level();

		RockMaker maker = ctx.config().getRock();
		if (!maker.allowsWaterPlacement() && !level.getFluidState(origin).isEmpty())
			return false;

		SDF rock = maker.build(cfg, random, level, origin);
		if (rock == null)
			return false;

		rock = rock.setReplaceFunction(s -> true);
		rock = rock.addPostProcesses(maker.extras(random, cfg));

		rock.fillRecursive(level, origin);
		return true;
	}
}