package com.machina.api.starchart.planet_trait;

import com.machina.registration.init.RegistryInit;
import com.mojang.serialization.Codec;

public record PlanetTrait(String name, int color) {
	public static final Codec<PlanetTrait> CODEC = RegistryInit.TRAIT.byNameCodec();
}
