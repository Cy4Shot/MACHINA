package com.machina.api.starchart.planet_type;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseSettings;

public record PlanetType(ResourceLocation name, int iconY, Shape shape, List<BiomePlacement> biomes, BlockState base) {

    public static final Codec<PlanetType> CODEC = RecordCodecBuilder
            .create(instance -> instance
                    .group(ResourceLocation.CODEC.fieldOf("name").forGetter(PlanetType::name),
                            Codec.INT.fieldOf("iconY").forGetter(PlanetType::iconY),
                            Shape.CODEC.fieldOf("shape").forGetter(PlanetType::shape),
                            Codec.list(BiomePlacement.CODEC).fieldOf("biomes").forGetter(PlanetType::biomes),
                            BlockState.CODEC.fieldOf("base").forGetter(PlanetType::base))
                    .apply(instance, PlanetType::new));

    public record Shape(int sea_level, NoiseSettings noise_settings) {
        public static final Codec<Shape> CODEC = RecordCodecBuilder.create(instance -> instance
                .group(Codec.INT.fieldOf("sea_level").forGetter(Shape::sea_level),
                        NoiseSettings.CODEC.fieldOf("noise_settings").forGetter(Shape::noise_settings))
                .apply(instance, Shape::new));
    }

    public record BiomePlacement(ResourceLocation biome, List<String> placements) {
        public static final Codec<BiomePlacement> CODEC = RecordCodecBuilder.create(instance -> instance
                .group(ResourceLocation.CODEC.fieldOf("biome").forGetter(BiomePlacement::biome),
                        Codec.list(Codec.STRING).fieldOf("placements").forGetter(BiomePlacement::placements))
                .apply(instance, BiomePlacement::new));
    }
}
