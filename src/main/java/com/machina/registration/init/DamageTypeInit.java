package com.machina.registration.init;

import com.machina.api.util.MachinaRL;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypeInit {

	public static final ResourceKey<DamageType> RADIATION = register("radiation");

	private static ResourceKey<DamageType> register(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, MachinaRL.create(name));
	}

	public static void bootstrap(BootstrapContext<DamageType> context) {
		context.register(RADIATION, new DamageType("radiation", 0.0F));
	}
}
