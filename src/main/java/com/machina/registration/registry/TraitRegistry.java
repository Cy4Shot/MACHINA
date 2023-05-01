package com.machina.registration.registry;

import java.util.Optional;

import com.machina.planet.trait.Trait;
import com.machina.util.reflection.TargetField;
import com.machina.util.text.MachinaRL;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class TraitRegistry {
	public static IForgeRegistry<Trait> REGISTRY = null;

	public static void createRegistry(RegistryEvent.NewRegistry event) {
		RegistryHelper.<Trait>registerRegistry(new TargetField(TraitRegistry.class, "REGISTRY"),
				Trait.class, new MachinaRL("planet_trait"), Optional.of(new MachinaRL("none")),
				Optional.of(new MachinaRL("planet_trait_registry")));
	}
}
