package com.machina.registration.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.machina.Machina;
import com.machina.api.starchart.planet_trait.PlanetTrait;
import com.machina.api.starchart.planet_trait.PlanetTraitConstraint;

import net.neoforged.neoforge.registries.DeferredRegister;

public class PlanetTraitInit {
	public static final DeferredRegister<PlanetTrait> TRAITS = DeferredRegister.create(RegistryInit.TRAIT,
			Machina.MOD_ID);
	public static final List<PlanetTraitConstraint> CONSTRAINTS = new ArrayList<>();

	//@formatter:off
	public static final Supplier<PlanetTrait> ALWAYS_RAINING = create("always_raining", 0x1c4ed6);
//	public static final Supplier<PlanetTrait> ALWAYS_RAINING = create("always_raining", 0x1c4ed6);
//	public static final Supplier<PlanetTrait> ALWAYS_RAINING = create("always_raining", 0x1c4ed6);
//	public static final Supplier<PlanetTrait> ALWAYS_RAINING = create("always_raining", 0x1c4ed6);
//	public static final Supplier<PlanetTrait> ALWAYS_RAINING = create("always_raining", 0x1c4ed6);
//	public static final Supplier<PlanetTrait> ALWAYS_RAINING = create("always_raining", 0x1c4ed6);
	
	@SuppressWarnings("unchecked")
	PlanetTraitConstraint WEATHER_CONSTRAINT = constrain(ALWAYS_RAINING);
	//@formatter:on

	private static final Supplier<PlanetTrait> create(String name, int color) {
		return TRAITS.register(name, () -> new PlanetTrait(name, color));
	}

	@SuppressWarnings("unchecked")
	private static final PlanetTraitConstraint constrain(Supplier<PlanetTrait>... traits) {
		PlanetTraitConstraint constraint = new PlanetTraitConstraint(
				Stream.of(traits).map(Supplier::get).collect(Collectors.toSet()));
		CONSTRAINTS.add(constraint);
		return constraint;
	}
}
