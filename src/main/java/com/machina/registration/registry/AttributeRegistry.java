package com.machina.registration.registry;

import java.util.Optional;

import com.machina.planet.attribute.AttributeType;
import com.machina.util.reflection.ClassHelper;
import com.machina.util.reflection.TargetField;
import com.machina.util.text.MachinaRL;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class AttributeRegistry {
	public static IForgeRegistry<AttributeType<?>> REGISTRY = null;

	public static void createRegistry(RegistryEvent.NewRegistry event) {
		RegistryHelper.<AttributeType<?>>registerRegistry(
				new TargetField(AttributeRegistry.class, "REGISTRY"),
				ClassHelper.withWildcard(AttributeType.class), new MachinaRL("planet_attribute_type"),
				Optional.empty(), Optional.empty());
	}

}
