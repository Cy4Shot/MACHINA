package com.cy4.machina.init;

import com.cy4.machina.api.annotation.registries.RegisterParticleType;
import com.cy4.machina.api.annotation.registries.RegistryHolder;

import net.minecraft.particles.BasicParticleType;

@RegistryHolder
public final class ParticleTypesInit {

	@RegisterParticleType("electricity_spark")
	public static final BasicParticleType ELECTRICITY_SPARK = new BasicParticleType(true);

}
