package com.machina.world.biome;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;

public class PlanetBiomeProvider extends BiomeSource {

	public static final Codec<PlanetBiomeProvider> makeCodec() {
		//@formatter:off
		return RecordCodecBuilder.create(instance -> instance.group(
			RegistryOps.retrieveRegistryLookup(Registries.WORLD_PRESET).forGetter(PlanetBiomeProvider::getWorldPresetLookup),
			RegistryOps.retrieveRegistryLookup(Registries.BIOME).forGetter(PlanetBiomeProvider::getBiomeLookup))
		.apply(instance, PlanetBiomeProvider::new));
		//@formatter:on
	}

	private final List<Holder<Biome>> biomes;
	private final Set<TagKey<Biome>> biomeCategories;
	private final Map<ResourceLocation, Holder<Biome>> biomeMapping = new HashMap<>();
	private final HolderLookup.RegistryLookup<WorldPreset> worldPresetLookup;
	private final HolderLookup.RegistryLookup<Biome> biomeLookup;
	private final PlanetBiomeSettings settings;
	private final MultiNoiseBiomeSource multiNoiseBiomeSource;
	private final boolean defaultBiomes;

	public PlanetBiomeProvider(RegistryAccess access) {
		this(access.lookup(Registries.WORLD_PRESET).get(), access.lookup(Registries.BIOME).get());
	}

	public PlanetBiomeProvider(HolderLookup.RegistryLookup<WorldPreset> worldPresetLookup,
			HolderLookup.RegistryLookup<Biome> biomeLookup) {
		super();
		this.settings = new PlanetBiomeSettings();
		this.biomeLookup = biomeLookup;
		this.worldPresetLookup = worldPresetLookup;
		Optional<Holder.Reference<WorldPreset>> worldPreset = worldPresetLookup.get(WorldPresets.NORMAL);
		multiNoiseBiomeSource = (MultiNoiseBiomeSource) worldPreset.get().get().overworld().get().generator()
				.getBiomeSource();
		biomes = getBiomes(biomeLookup, settings);
		biomeCategories = getBiomeCategories(settings);

		defaultBiomes = biomes.isEmpty() && biomeCategories.isEmpty();
		biomeLookup.listElements().forEach(this::getMappedBiome);
	}

	public HolderLookup.RegistryLookup<WorldPreset> getWorldPresetLookup() {
		return worldPresetLookup;
	}

	public PlanetBiomeSettings getSettings() {
		return settings;
	}

	private static List<Holder<Biome>> getDefaultBiomes(HolderLookup.RegistryLookup<Biome> biomeLookup,
			PlanetBiomeSettings settings) {
		Set<ResourceLocation> biomes = settings.getBiomes();
		if (biomes.isEmpty()) {
			return biomeLookup.listElements().collect(Collectors.toList());
		}
		return biomes.stream().map(rl -> biomeLookup.get(ResourceKey.create(Registries.BIOME, rl))).map(Optional::get)
				.collect(Collectors.toList());
	}

	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		return getDefaultBiomes(biomeLookup, settings).stream();
	}

	private boolean isCategoryMatching(Holder<Biome> biome) {
		if (biomeCategories.isEmpty()) {
			return true;
		}

		return biomeLookup.getOrThrow(biome.unwrapKey().get()).tags().filter(biomeCategories::contains).findAny()
				.isPresent();
	}

	@SuppressWarnings("unchecked")
	private Holder<Biome> getMappedBiome(Holder<Biome> biome) {
		if (defaultBiomes) {
			return biome;
		}
		return biomeMapping.computeIfAbsent(biome.unwrapKey().get().location(), resourceLocation -> {
			List<Holder<Biome>> biomes = getBiomes(biomeLookup, settings);
			final float[] minDist = { 1000000000 };
			final Holder<?>[] desired = { biome };
			if (biomes.isEmpty()) {
				// Biomes was empty. Try to get one with the correct category
				// @todo why does the first one have to fail???
				if (!isCategoryMatching((Holder<Biome>) desired[0])) {
					biomeLookup.listElements().forEach(b -> {
						if (isCategoryMatching(b)) {
							float dist = distance(b, biome);
							if (dist < minDist[0]) {
								desired[0] = b;
								minDist[0] = dist;
							}
						}
					});
				}
			} else {
				// If there are biomes we try to find one while also keeping category in mind
				for (Holder<Biome> b : biomes) {
					if (biomeCategories.isEmpty() || isCategoryMatching(b)) {
						float dist = distance(b, biome);
						if (dist < minDist[0]) {
							desired[0] = b;
							minDist[0] = dist;
						}
					}
				}
			}
			return (Holder<Biome>) desired[0];
		});
	}

	private float distance(Holder<Biome> biome1, Holder<Biome> biome2) {
		if (Objects.equals(biome1, biome2)) {
			return -1;
		}
		if (Objects.equals(biome1.value(), biome2.value())) {
			return -1;
		}
		Set<TagKey<Biome>> tags1 = biome1.tags().collect(Collectors.toSet());
		Set<TagKey<Biome>> tags2 = biome2.tags().collect(Collectors.toSet());
		tags1.removeAll(tags2);
		tags1 = biome1.tags().collect(Collectors.toSet());
		tags2.removeAll(tags1);
		float d1 = Math.max(tags1.size(), tags2.size()); // Use the number of differences in tags as a measure
		float d2 = Math.abs(biome1.value().getBaseTemperature() - biome2.value().getBaseTemperature());
		return d1 + d2 * d2;// @todo 1.19.4 + d3 * d3 + d4;
	}

	private List<Holder<Biome>> getBiomes(HolderLookup.RegistryLookup<Biome> holderLookup,
			PlanetBiomeSettings settings) {
		return settings.getBiomes().stream().map(rl -> biomeLookup.get(ResourceKey.create(Registries.BIOME, rl)))
				.map(Optional::get).collect(Collectors.toList());
	}

	private Set<TagKey<Biome>> getBiomeCategories(PlanetBiomeSettings settings) {
		return settings.getBiomeCategories();
	}

	public HolderLookup.RegistryLookup<Biome> getBiomeLookup() {
		return biomeLookup;
	}

	@Nonnull
	@Override
	protected Codec<? extends BiomeSource> codec() {
		return makeCodec();
	}

	@Override
	@Nonnull
	public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler climate) {
		if (defaultBiomes) {
			return multiNoiseBiomeSource.getNoiseBiome(x, y, z, climate);
		}
		return getMappedBiome(multiNoiseBiomeSource.getNoiseBiome(x, y, z, climate));
	}
}