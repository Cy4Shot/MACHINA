package com.machina.particle;

import com.machina.registration.init.ParticleTypeInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class BiomeAshParticleType extends ParticleType<BiomeAshParticleType.BiomeAshParticleOptions> {

	public static record BiomeAshParticleOptions(int color) implements ParticleOptions {

		public static final MapCodec<BiomeAshParticleOptions> CODEC = Codec.INT.fieldOf("color")
				.xmap(BiomeAshParticleOptions::new, BiomeAshParticleOptions::color);

		public static final StreamCodec<ByteBuf, BiomeAshParticleOptions> STREAM_CODEC = ByteBufCodecs.INT
				.map(BiomeAshParticleOptions::new, BiomeAshParticleOptions::color);

		@Override
		public ParticleType<?> getType() {
			return ParticleTypeInit.BIOME_ASH.get();
		}
	}

	public BiomeAshParticleType(boolean overrideLimitter) {
		super(overrideLimitter);
	}

	@Override
	public MapCodec<BiomeAshParticleOptions> codec() {
		return BiomeAshParticleOptions.CODEC;
	}

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, BiomeAshParticleOptions> streamCodec() {
		return BiomeAshParticleOptions.STREAM_CODEC;
	}
}
