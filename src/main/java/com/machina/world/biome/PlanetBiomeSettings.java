package com.machina.world.biome;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mojang.serialization.Lifecycle;

import net.minecraft.client.Minecraft;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PlanetBiomeSettings {
	private final Set<TagKey<Biome>> biomeCategories = new HashSet<>();
	private final Map<ResourceLocation, Biome> biomes = new HashMap<>();

	public PlanetBiomeSettings() {
//		registerCategory("test");
		registerBiome("test", new PlanetBiome());

		if (Minecraft.getInstance().isLocalServer()) {
			registerAll(ServerLifecycleHooks.getCurrentServer());
		}
	}

	public Set<TagKey<Biome>> getBiomeCategories() {
		return biomeCategories;
	}

	public Set<ResourceLocation> getBiomes() {
		return biomes.keySet();
	}

	private void registerCategory(String name) {
		biomeCategories.add(TagKey.create(Registries.BIOME, new ResourceLocation(name)));
	}

	private void registerBiome(String name, Biome biome) {
		biomes.put(new ResourceLocation(name), biome);
	}

	private void registerAll(MinecraftServer server) {
		Registry<Biome> regFrozen = server.registryAccess().registryOrThrow(Registries.BIOME);
		if (regFrozen instanceof MappedRegistry<Biome> reg) {
			reg.unfreeze();
			for (Entry<ResourceLocation, Biome> entry : biomes.entrySet()) {
				reg.register(ResourceKey.create(Registries.BIOME, entry.getKey()), entry.getValue(),
						Lifecycle.stable());
			}
			reg.freeze();
		} else {
			throw new IllegalStateException("Biome registry unwritable.");
		}
	}
}
