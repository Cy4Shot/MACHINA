package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.world.PlanetChunkGenerator;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ChunkGeneratorInit {
	public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister
			.create(Registries.CHUNK_GENERATOR, Machina.MOD_ID);

	public static final Supplier<MapCodec<PlanetChunkGenerator>> PLANET = CHUNK_GENERATORS.register("planet",
			() -> PlanetChunkGenerator.CODEC);
}
