package com.machina.world.functions;

import javax.annotation.Nullable;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.machina.api.starchart.obj.Planet;
import com.machina.world.biome.PlanetBiome;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

public class PlanetSurfaceRule {

    private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK.defaultBlockState());
    private static final SurfaceRules.RuleSource AIR = makeStateRule(Blocks.AIR.defaultBlockState());

    private static SurfaceRules.RuleSource makeStateRule(BlockState state) {
        return SurfaceRules.state(state);
    }

    public static SurfaceRules.RuleSource planet(Planet p) {
        return planetLike(p, false, true);
    }

    public static SurfaceRules.RuleSource planetLike(Planet p, boolean top, boolean bottom) {
        SurfaceRules.RuleSource top_block = new PlanetBiomeTopBlockRuleSource(AIR, Noises.SURFACE);
        SurfaceRules.RuleSource second_top_block = new PlanetBiomeSecondBlockRuleSource(AIR);
        SurfaceRules.RuleSource rock = new PlanetBiomeThirdBlockRuleSource(makeStateRule(p.type().base()));

        SurfaceRules.RuleSource rs8 = getRuleSource(top_block, second_top_block, rock);
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        if (top) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.not(
                    SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())),
                    BEDROCK));
        }
        if (bottom) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(),
                    VerticalAnchor.aboveBottom(5)), BEDROCK));
        }
        builder.add(SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), rs8));
        builder.add(rock);
        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }

    private static SurfaceRules.@NotNull RuleSource getRuleSource(SurfaceRules.RuleSource top_block,
            SurfaceRules.RuleSource second_top_block, SurfaceRules.RuleSource rock) {
        SurfaceRules.ConditionSource cs7 = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource cs8 = SurfaceRules.waterBlockCheck(0, 0);
        SurfaceRules.ConditionSource cs9 = SurfaceRules.waterStartCheck(-6, -1);
        SurfaceRules.RuleSource rs = SurfaceRules.sequence(SurfaceRules.ifTrue(cs8, top_block), second_top_block);
        SurfaceRules.RuleSource rs2 = SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, rock);
        SurfaceRules.RuleSource rs7 = SurfaceRules.sequence(rs);
        return SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(cs7, rs7)),
                SurfaceRules.ifTrue(cs9,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, second_top_block))),
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, rs2));
    }

    public record PlanetBiomeTopBlockRuleSource(SurfaceRules.RuleSource fallback, ResourceKey<NoiseParameters> noise)
            implements SurfaceRules.RuleSource {
        public static final KeyDispatchDataCodec<PlanetBiomeTopBlockRuleSource> CODEC = KeyDispatchDataCodec
                .of(RecordCodecBuilder.mapCodec(instance -> instance
                        .group(SurfaceRules.RuleSource.CODEC.fieldOf("fallback")
                                .forGetter(PlanetBiomeTopBlockRuleSource::fallback),
                                ResourceKey.codec(Registries.NOISE).fieldOf("noise")
                                        .forGetter(PlanetBiomeTopBlockRuleSource::noise))
                        .apply(instance, PlanetBiomeTopBlockRuleSource::new)));

        public @NotNull KeyDispatchDataCodec<PlanetBiomeTopBlockRuleSource> codec() {
            return CODEC;
        }

        public SurfaceRules.SurfaceRule apply(SurfaceRules.Context ctx) {
            return new StateRule((x, y, z) -> {
                Holder<Biome> biome = ctx.biomeGetter.apply(new BlockPos(x, y, z));
                biome.value();
                if (biome.value() instanceof PlanetBiome) {
                    NormalNoise normalnoise = ctx.randomState.getOrCreateNoise(noise);
                    BlockState state = ((PlanetBiome) biome.value()).surface.getState(x, y, z, normalnoise);
                    if (state != null) {
                        return state;
                    }
                }
                return fallback.apply(ctx).tryApply(x, y, z);
            });
        }
    }

    public record PlanetBiomeSecondBlockRuleSource(SurfaceRules.RuleSource fallback)
            implements SurfaceRules.RuleSource {
        public static final KeyDispatchDataCodec<PlanetBiomeSecondBlockRuleSource> CODEC = KeyDispatchDataCodec
                .of(RecordCodecBuilder.mapCodec(instance -> instance
                        .group(SurfaceRules.RuleSource.CODEC.fieldOf("fallback")
                                .forGetter(PlanetBiomeSecondBlockRuleSource::fallback))
                        .apply(instance, PlanetBiomeSecondBlockRuleSource::new)));

        public @NotNull KeyDispatchDataCodec<PlanetBiomeSecondBlockRuleSource> codec() {
            return CODEC;
        }

        public SurfaceRules.SurfaceRule apply(SurfaceRules.Context ctx) {
            return new StateRule((x, y, z) -> {
                Holder<Biome> biome = ctx.biomeGetter.apply(new BlockPos(x, y, z));
                biome.value();
                if (biome.value() instanceof PlanetBiome) {
                    BlockState state = ((PlanetBiome) biome.value()).getSecondBlock();
                    if (state != null) {
                        return state;
                    }
                }
                return fallback.apply(ctx).tryApply(x, y, z);
            });
        }
    }

    public record PlanetBiomeThirdBlockRuleSource(SurfaceRules.RuleSource fallback) implements SurfaceRules.RuleSource {
        public static final KeyDispatchDataCodec<PlanetBiomeThirdBlockRuleSource> CODEC = KeyDispatchDataCodec
                .of(RecordCodecBuilder.mapCodec(instance -> instance
                        .group(SurfaceRules.RuleSource.CODEC.fieldOf("fallback")
                                .forGetter(PlanetBiomeThirdBlockRuleSource::fallback))
                        .apply(instance, PlanetBiomeThirdBlockRuleSource::new)));

        public @NotNull KeyDispatchDataCodec<PlanetBiomeThirdBlockRuleSource> codec() {
            return CODEC;
        }

        public SurfaceRules.SurfaceRule apply(SurfaceRules.Context ctx) {
            return new StateRule((x, y, z) -> {
                Holder<Biome> biome = ctx.biomeGetter.apply(new BlockPos(x, y, z));
                biome.value();
                if (biome.value() instanceof PlanetBiome) {
                    BlockState state = ((PlanetBiome) biome.value()).getThirdBlock();
                    if (state != null) {
                        return state;
                    }
                }
                return fallback.apply(ctx).tryApply(x, y, z);
            });
        }
    }

    public record StateRule(TriFunction<Integer, Integer, Integer, BlockState> state)
            implements SurfaceRules.SurfaceRule {

        @Nullable
        public BlockState tryApply(int x, int y, int z) {
            return state.apply(x, y, z);
        }
    }
}
