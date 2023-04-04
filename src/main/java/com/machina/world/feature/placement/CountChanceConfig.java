package com.machina.world.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.placement.IPlacementConfig;

public class CountChanceConfig implements IPlacementConfig {
	public static final Codec<CountChanceConfig> CODEC = RecordCodecBuilder.create((config) -> {
		return config.group(Codec.INT.fieldOf("chance").orElse(0).forGetter((get) -> {
			return get.chance;
		}), Codec.INT.fieldOf("count").orElse(0).forGetter((get) -> {
			return get.count;
		})).apply(config, CountChanceConfig::new);
	});

	public final int chance;
	public final int count;

	public CountChanceConfig(int chance, int count) {
		this.chance = chance;
		this.count = count;
	}
}