package com.machina.world;

import java.util.List;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.machina.api.starchart.planet_trait.PlanetOreTrait;
import com.machina.world.biome.PlanetBiome;

import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Aquifer.FluidPicker;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunction.FunctionContext;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.DensityFunctions.BeardifierOrMarker;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.material.MaterialRuleList;

public class PlanetNoiseChunk extends NoiseChunk {

	public final static int VEIN_MAX_Y = 120;
	public final static int VEIN_MIN_Y = -60;
	private final static float VEIN_THRESHOLD = 0.23f; // Lower == common
	private final static float VEIN_SPAWN_CHANCE = 0.8f; // Lower == broken veins
	private final static float VEIN_MIN_RICHNESS_NOISE = 0.4f;
	private final static float VEIN_MAX_RICHNESS_NOISE = 0.6f;
	private final static float VEIN_MIN_RICHNESS = 0.3f;
	private final static float VEIN_MAX_RICHNESS = 0.8f;
	private final static float VEIN_GAP_THRESHOLD = -0.6f; // Lower == smoother
	private final static float VEIN_RAW_ORE_CHANCE = 0.05f;
	private final static float VEIN_TEMPERATURE_SCALE = 24f;

	private final NoiseChunk.BlockStateFiller planetBlockStateRule;

	public static PlanetNoiseChunk forChunk(ChunkAccess chunk, RandomState state,
			DensityFunctions.BeardifierOrMarker beardifierOrMarker, NoiseGeneratorSettings noiseGeneratorSettings,
			Aquifer.FluidPicker fluidPicker, Blender blender, List<PlanetOreTrait> oreTraits, BiomeSource biomeSource) {
		NoiseSettings noisesettings = noiseGeneratorSettings.noiseSettings().clampToHeightAccessor(chunk);
		ChunkPos chunkpos = chunk.getPos();
		int i = 16 / noisesettings.getCellWidth();
		return new PlanetNoiseChunk(i, state, chunkpos.getMinBlockX(), chunkpos.getMinBlockZ(), noisesettings,
				beardifierOrMarker, noiseGeneratorSettings, fluidPicker, blender, oreTraits, biomeSource);
	}

	private static PlanetOreTrait pickTrait(List<PlanetOreTrait> traits, double noise) {
		if (traits.isEmpty())
			return null;

		double normalized = (noise + 1.0) / 2.0;
		int index = (int) (normalized * traits.size());
		index = Mth.clamp(index, 0, traits.size() - 1);
		return traits.get(index);
	}

	protected static NoiseChunk.BlockStateFiller createOreVeins(BiomeSource biomeSource, List<PlanetOreTrait> traits,
			DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap, DensityFunction veinType,
			PositionalRandomFactory random, Climate.Sampler sampler, BlockState base) {

		return ctx -> {
			double toggle = veinToggle.compute(ctx);
			int y = ctx.blockY();

			double typeNoise = veinType.compute(new FunctionContext() {
				@Override
				public int blockX() {
					return (int) (ctx.blockX() * VEIN_TEMPERATURE_SCALE);
				}

				@Override
				public int blockY() {
					return (int) (ctx.blockY() * VEIN_TEMPERATURE_SCALE);
				}

				@Override
				public int blockZ() {
					return (int) (ctx.blockZ() * VEIN_TEMPERATURE_SCALE);
				}
			});
			PlanetOreTrait trait = pickTrait(traits, typeNoise);
			if (trait == null)
				return null;

			int top = VEIN_MAX_Y - y;
			int bottom = y - VEIN_MIN_Y;
			if (bottom < 0 || top < 0)
				return null;

			int dist = Math.min(top, bottom);
			double falloff = Mth.clampedMap(dist, 0.0, 20.0, -0.2, 0.0);
			double absToggle = Math.abs(toggle);
			if (absToggle + falloff < VEIN_THRESHOLD) {
				return null;
			}

			RandomSource rand = random.at(ctx.blockX(), y, ctx.blockZ());

			if (rand.nextFloat() > VEIN_SPAWN_CHANCE)
				return null;
			if (veinRidged.compute(ctx) >= 0.0)
				return null;

			double richness = Mth.clampedMap(absToggle, VEIN_MIN_RICHNESS_NOISE, VEIN_MAX_RICHNESS_NOISE,
					VEIN_MIN_RICHNESS, VEIN_MAX_RICHNESS);

			Supplier<BlockState> filler = () -> {
				int quartX = QuartPos.fromBlock(ctx.blockX());
				int quartY = QuartPos.fromBlock(ctx.blockY());
				int quartZ = QuartPos.fromBlock(ctx.blockZ());
				Holder<Biome> biome = biomeSource.getNoiseBiome(quartX, quartY, quartZ, sampler);
				if (biome.value() instanceof PlanetBiome planetBiome) {
					return planetBiome.getBaseBlock();
				}
				return base;
			};

			if (rand.nextFloat() < richness && veinGap.compute(ctx) > VEIN_GAP_THRESHOLD) {
				return rand.nextFloat() < VEIN_RAW_ORE_CHANCE ? trait.getRawOreBlock(filler)
						: trait.getOreBlock(filler);
			} else {
				return filler.get();
			}
		};
	}

	public PlanetNoiseChunk(int cellCountXZ, RandomState random, int firstNoiseX, int firstNoiseZ,
			NoiseSettings noiseSettings, BeardifierOrMarker beardifier, NoiseGeneratorSettings noiseGeneratorSettings,
			FluidPicker fluidPicker, Blender blendifier, List<PlanetOreTrait> traits, BiomeSource biomeSource) {
		super(cellCountXZ, random, firstNoiseX, firstNoiseZ, noiseSettings, beardifier, noiseGeneratorSettings,
				fluidPicker, blendifier);

		NoiseRouter noiserouter = random.router();
		NoiseRouter noiserouter1 = noiserouter.mapAll(this::wrap);

		Builder<NoiseChunk.BlockStateFiller> builder = ImmutableList.builder();
		DensityFunction densityfunction = DensityFunctions
				.cacheAllInCell(
						DensityFunctions.add(noiserouter1.finalDensity(), DensityFunctions.BeardifierMarker.INSTANCE))
				.mapAll(this::wrap);
		builder.add(p_209217_ -> this.aquifer().computeSubstance(p_209217_, densityfunction.compute(p_209217_)));
		builder.add(createOreVeins(biomeSource, traits, noiserouter1.veinToggle(), noiserouter1.veinRidged(),
				noiserouter1.veinGap(), noiserouter1.temperature(), random.oreRandom(), random.sampler(), noiseGeneratorSettings.defaultBlock()));
		this.planetBlockStateRule = new MaterialRuleList(builder.build());
	}

	@Override
	public BlockState getInterpolatedState() {
		return this.planetBlockStateRule.calculate(this);
	}

}
