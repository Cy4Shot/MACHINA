package com.machina.world.carver;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.carver.ICarverConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class DynamicDimensionCarverConfig implements ICarverConfig, IFeatureConfig {

	public static final Codec<DynamicDimensionCarverConfig> CODEC = RecordCodecBuilder.create((builder) -> {
		return builder.group(Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter((config) -> {
			return config.probability;
		})).and(Codec.intRange(0, 25).fieldOf("length").forGetter((config) -> {
			return config.length;
		})).and(Codec.floatRange(0F, 20F).fieldOf("thickness").forGetter((config) -> {
			return config.thickness;
		})).apply(builder, DynamicDimensionCarverConfig::new);
	});
	
	public final float probability;
	public final int length;
	public final float thickness;

	public DynamicDimensionCarverConfig(float p, int l, float t) {
		this.probability = p;
		this.length = l;
		this.thickness = t;
	}

}