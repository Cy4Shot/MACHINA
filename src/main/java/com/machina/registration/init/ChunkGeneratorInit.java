package com.machina.registration.init;

import com.machina.Machina;
import com.machina.world.PlanetChunkGenerator;
import com.mojang.serialization.Codec;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ChunkGeneratorInit {
	public static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister
			.create(Registries.CHUNK_GENERATOR, Machina.MOD_ID);

	public static final RegistryObject<Codec<PlanetChunkGenerator>> PLANET = CHUNK_GENERATORS.register("planet",
			PlanetChunkGenerator::makeCodec);
}
