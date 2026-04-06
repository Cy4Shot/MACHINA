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

public class DustStormParticleType extends ParticleType<DustStormParticleType.DustStormParticleOptions> {

	public static record DustStormParticleOptions(int color) implements ParticleOptions {

		public static final MapCodec<DustStormParticleOptions> CODEC = Codec.INT.fieldOf("color")
				.xmap(DustStormParticleOptions::new, DustStormParticleOptions::color);

		public static final StreamCodec<ByteBuf, DustStormParticleOptions> STREAM_CODEC = ByteBufCodecs.INT
				.map(DustStormParticleOptions::new, DustStormParticleOptions::color);

		@Override
		public ParticleType<?> getType() {
			return ParticleTypeInit.DUST_STORM.get();
		}
	}

	public DustStormParticleType(boolean overrideLimitter) {
		super(overrideLimitter);
	}

	@Override
	public MapCodec<DustStormParticleOptions> codec() {
		return DustStormParticleOptions.CODEC;
	}

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, DustStormParticleOptions> streamCodec() {
		return DustStormParticleOptions.STREAM_CODEC;
	}
}
