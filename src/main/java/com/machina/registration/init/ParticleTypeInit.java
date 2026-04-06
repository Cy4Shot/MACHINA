package com.machina.registration.init;

import com.machina.Machina;
import com.machina.particle.DustStormParticleType;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ParticleTypeInit {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister
			.create(Registries.PARTICLE_TYPE, Machina.MOD_ID);

	public static final DeferredHolder<ParticleType<?>, DustStormParticleType> DUST_STORM = PARTICLE_TYPES
			.register("dust_storm", () -> new DustStormParticleType(true));
}
