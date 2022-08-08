package com.machina.world.feature.planet;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.Placement;

public abstract class PlanetBaseFeature extends Feature<NoFeatureConfig> {

	public PlanetBaseFeature() {
		super(NoFeatureConfig.CODEC);
	}

	@Override
	public boolean place(ISeedReader region, ChunkGenerator chunk, Random rand, BlockPos pos, NoFeatureConfig conf) {
		return place(region, chunk, rand, pos);
	}
	
	public abstract boolean place(ISeedReader region, ChunkGenerator chunk, Random rand, BlockPos pos);

	public ConfiguredFeature<NoFeatureConfig, ?> config() {
		return this.configured(new NoFeatureConfig());
	}

	public ConfiguredFeature<?, ?> count(int count) {
		return this.config().decorated(Placement.COUNT_MULTILAYER.configured(new FeatureSpreadConfig(count)));
	}
}