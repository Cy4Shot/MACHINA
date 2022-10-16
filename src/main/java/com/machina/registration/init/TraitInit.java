package com.machina.registration.init;

import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.registry.PlanetTraitRegistry;
import com.machina.util.reflection.ClassHelper;
import com.machina.util.text.MachinaRL;

import net.minecraftforge.event.RegistryEvent;

public final class TraitInit {

	public static final PlanetTrait NONE = new PlanetTrait(0);

	public static void register(final RegistryEvent.Register<PlanetTrait> event) {
		ClassHelper.<PlanetTrait>doWithStatics(TraitInit.class,
				(name, data) -> register(name.toLowerCase(), data));
	}

	private static void register(String name, PlanetTrait trait) {
		trait.setRegistryName(new MachinaRL(name));
		PlanetTraitRegistry.REGISTRY.register(trait);
	}
}
