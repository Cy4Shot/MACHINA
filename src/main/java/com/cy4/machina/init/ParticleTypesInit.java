/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.init;

import com.cy4.machina.api.annotation.registries.RegisterParticleType;
import com.cy4.machina.api.annotation.registries.RegistryHolder;

import net.minecraft.particles.BasicParticleType;

@RegistryHolder
public final class ParticleTypesInit {

	@RegisterParticleType("electricity_spark")
	public static final BasicParticleType ELECTRICITY_SPARK = new BasicParticleType(true);

}
