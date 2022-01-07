package com.machina.api.registry;

import static com.machina.api.ModIDs.MACHINA;

import java.util.Optional;

import com.machina.api.annotation.ChangedByReflection;
import com.machina.api.planet.attribute.PlanetAttributeType;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.helper.ClassHelper;
import com.machina.api.util.helper.CustomRegistryHelper;
import com.machina.api.util.objects.TargetField;
import com.matyrobbrt.lib.registry.annotation.RegisterCustomRegistry;
import com.matyrobbrt.lib.registry.annotation.RegistryHolder;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

@RegistryHolder(modid = MACHINA)
public class PlanetAttributeRegistry {
	@ChangedByReflection(when = "commonSetup (when the registry is built)")
	public static final IForgeRegistry<PlanetAttributeType<?>> REGISTRY = null;

	@RegisterCustomRegistry
	public static void createRegistry(RegistryEvent.NewRegistry event) {
		CustomRegistryHelper.<PlanetAttributeType<?>>registerRegistry(
				new TargetField(PlanetAttributeRegistry.class, "REGISTRY"),
				ClassHelper.withWildcard(PlanetAttributeType.class), new MachinaRL("planet_attribute_type"),
				Optional.empty(), Optional.empty());
	}

}
