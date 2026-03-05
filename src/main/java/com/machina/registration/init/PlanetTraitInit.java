package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.starchart.planet_trait.PlanetTrait;

import net.neoforged.neoforge.registries.DeferredRegister;

public class PlanetTraitInit {
	public static final DeferredRegister<PlanetTrait> TRAITS = DeferredRegister.create(RegistryInit.TRAIT,
			Machina.MOD_ID);

	//@formatter:off
	public static final Supplier<PlanetTrait> ALWAYS_RAINING = create("always_raining", 0x1c4ed6);
	//@formatter:on

	private static final Supplier<PlanetTrait> create(String name, int color) {
		return TRAITS.register(name, () -> new PlanetTrait(name, color));
	}
}
