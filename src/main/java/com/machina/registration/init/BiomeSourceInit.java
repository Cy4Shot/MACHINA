package com.machina.registration.init;

import com.machina.Machina;
import com.machina.world.biome.PlanetBiomeProvider;
import com.mojang.serialization.Codec;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BiomeSourceInit {
	public static final DeferredRegister<Codec<? extends BiomeSource>> BIOME_SOURCES = DeferredRegister
			.create(Registries.BIOME_SOURCE, Machina.MOD_ID);

	public static final RegistryObject<Codec<PlanetBiomeProvider>> PLANET = BIOME_SOURCES.register("planet",
			PlanetBiomeProvider::makeCodec);
}
