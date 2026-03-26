package com.machina.registration.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.gson.JsonObject;
import com.machina.Machina;
import com.machina.api.starchart.planet_biome.placement.PlacementModifier;
import com.machina.api.starchart.planet_biome.placement.PlacementModifierType;
import com.machina.world.feature.placement.DontPlaceOnModifier;
import com.machina.world.feature.placement.OnlyPlaceOnModifier;
import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PlanetPlacementModifierInit {
	public static final DeferredRegister<PlacementModifierType> PLACEMENT_MODIFIERS = DeferredRegister
			.create(RegistryInit.PLACEMENT_MODIFIER, Machina.MOD_ID);
	public static final Map<ResourceLocation, Codec<? extends PlacementModifier>> CODECS = new HashMap<>();

	//@formatter:off
	public static final Supplier<PlacementModifierType> ONLY_PLACE_ON = register("only_place_on", OnlyPlaceOnModifier::new, OnlyPlaceOnModifier.CODEC);
	public static final Supplier<PlacementModifierType> DONT_PLACE_ON = register("dont_place_on", DontPlaceOnModifier::new, DontPlaceOnModifier.CODEC);
	//@formatter:on

	private static final DeferredHolder<PlacementModifierType, PlacementModifierType> register(String name,
			Function<JsonObject, PlacementModifier> factory, Codec<? extends PlacementModifier> codec) {
		DeferredHolder<PlacementModifierType, PlacementModifierType> holder = PLACEMENT_MODIFIERS.register(name,
				() -> new PlacementModifierType(factory));
		CODECS.put(holder.getId(), codec);
		return holder;
	}
}
