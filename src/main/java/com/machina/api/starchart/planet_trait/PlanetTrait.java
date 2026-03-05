package com.machina.api.starchart.planet_trait;

import com.machina.Machina;
import com.machina.registration.init.RegistryInit;
import com.mojang.serialization.Codec;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record PlanetTrait(String name, int color) {
	public static final Codec<PlanetTrait> CODEC = RegistryInit.TRAIT.byNameCodec();
	
	public MutableComponent comp() {
		return Component.translatable(Machina.MOD_ID + ".planet_trait." + name);
	}
}
