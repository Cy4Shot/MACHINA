package com.machina.world;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;

import com.google.common.base.Suppliers;
import com.machina.api.starchart.Starchart;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.planet_trait.PlanetOreTrait;
import com.machina.api.starchart.planet_trait.PlanetTrait;
import com.machina.api.util.PlanetHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Aquifer.FluidStatus;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;

public class PlanetChunkGenerator extends NoiseBasedChunkGenerator {

	public static final MapCodec<PlanetChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
			.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(c -> c.biomeSource),
					NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(c -> c.settings),
					Codec.INT.fieldOf("id").forGetter(c -> c.id), Codec.LONG.fieldOf("seed").forGetter(c -> c.seed))
			.apply(instance, PlanetChunkGenerator::new));

	final int id;
	final Planet planet;
	final List<PlanetOreTrait> oreTraits;
	final long seed;

	public PlanetChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings,
			ResourceKey<LevelStem> dim, long seed) {
		this(biomeSource, settings, PlanetHelper.getIdDim(dim), seed);
	}

	public PlanetChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings, int id, long seed) {
		super(biomeSource, settings);
		this.id = id;
		this.seed = seed;
		this.planet = Starchart.system(seed).planets().get(id);

		this.oreTraits = new ArrayList<>();
		for (PlanetTrait trait : this.planet.traits()) {
			if (trait instanceof PlanetOreTrait oreTrait) {
				this.oreTraits.add(oreTrait);
			}
		}

		this.globalFluidPicker = Suppliers.memoize(() -> (x, y, z) -> {
			NoiseGeneratorSettings s = this.settings.value();
			return new FluidStatus(s.seaLevel(), s.defaultFluid());
		});
	}

	@Override
	protected @NotNull MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	protected OptionalInt iterateNoiseColumn(LevelHeightAccessor level, RandomState random, int x, int z,
			@Nullable MutableObject<NoiseColumn> column, @Nullable Predicate<BlockState> stoppingState) {
		NoiseSettings noisesettings = this.settings.value().noiseSettings().clampToHeightAccessor(level);
		int i = noisesettings.getCellHeight();
		int j = noisesettings.minY();
		int k = Mth.floorDiv(j, i);
		int l = Mth.floorDiv(noisesettings.height(), i);
		if (l <= 0) {
			return OptionalInt.empty();
		} else {
			BlockState[] ablockstate;
			if (column == null) {
				ablockstate = null;
			} else {
				ablockstate = new BlockState[noisesettings.height()];
				column.setValue(new NoiseColumn(j, ablockstate));
			}

			int i1 = noisesettings.getCellWidth();
			int j1 = Math.floorDiv(x, i1);
			int k1 = Math.floorDiv(z, i1);
			int l1 = Math.floorMod(x, i1);
			int i2 = Math.floorMod(z, i1);
			int j2 = j1 * i1;
			int k2 = k1 * i1;
			double d0 = (double) l1 / (double) i1;
			double d1 = (double) i2 / (double) i1;
			PlanetNoiseChunk noisechunk = new PlanetNoiseChunk(1, random, j2, k2, noisesettings,
					DensityFunctions.BeardifierMarker.INSTANCE, this.settings.value(), this.globalFluidPicker.get(),
					Blender.empty(), this.oreTraits);
			noisechunk.initializeForFirstCellX();
			noisechunk.advanceCellX(0);

			for (int l2 = l - 1; l2 >= 0; l2--) {
				noisechunk.selectCellYZ(l2, 0);

				for (int i3 = i - 1; i3 >= 0; i3--) {
					int j3 = (k + l2) * i + i3;
					double d2 = (double) i3 / (double) i;
					noisechunk.updateForY(j3, d2);
					noisechunk.updateForX(x, d0);
					noisechunk.updateForZ(z, d1);
					BlockState blockstate = noisechunk.getInterpolatedState();
					BlockState blockstate1 = blockstate == null ? this.settings.value().defaultBlock() : blockstate;
					if (ablockstate != null) {
						int k3 = l2 * i + i3;
						ablockstate[k3] = blockstate1;
					}

					if (stoppingState != null && stoppingState.test(blockstate1)) {
						noisechunk.stopInterpolation();
						return OptionalInt.of(j3 + 1);
					}
				}
			}

			noisechunk.stopInterpolation();
			return OptionalInt.empty();
		}
	}

	@Override
	public NoiseChunk createNoiseChunk(ChunkAccess chunk, StructureManager structureManager, Blender blender,
			RandomState random) {
		return PlanetNoiseChunk.forChunk(chunk, random,
				Beardifier.forStructuresInChunk(structureManager, chunk.getPos()), this.settings.value(),
				this.globalFluidPicker.get(), blender, this.oreTraits);
	}
}