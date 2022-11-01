package com.machina.registration.registry;

import java.util.Optional;

import com.machina.planet.attribute.PlanetAttributeType;
import com.machina.util.reflection.ClassHelper;
import com.machina.util.reflection.TargetField;
import com.machina.util.text.MachinaRL;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class PlanetAttributeRegistry {
	public static IForgeRegistry<PlanetAttributeType<?>> REGISTRY = null;

	public static void createRegistry(RegistryEvent.NewRegistry event) {
		RegistryHelper.<PlanetAttributeType<?>>registerRegistry(
				new TargetField(PlanetAttributeRegistry.class, "REGISTRY"),
				ClassHelper.withWildcard(PlanetAttributeType.class), new MachinaRL("planet_attribute_type"),
				Optional.empty(), Optional.empty());
	}

}
