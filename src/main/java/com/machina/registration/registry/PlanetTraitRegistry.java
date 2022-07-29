package com.machina.registration.registry;

import java.util.Optional;

import com.machina.planet.trait.PlanetTrait;
import com.machina.util.reflection.TargetField;
import com.machina.util.text.MachinaRL;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class PlanetTraitRegistry {
	public static final IForgeRegistry<PlanetTrait> REGISTRY = null;

	public static void createRegistry(RegistryEvent.NewRegistry event) {
		RegistryHelper.<PlanetTrait>registerRegistry(new TargetField(PlanetTraitRegistry.class, "REGISTRY"),
				PlanetTrait.class, new MachinaRL("planet_trait"), Optional.of(new MachinaRL("none")),
				Optional.of(new MachinaRL("planet_trait_registry")));
	}
}
