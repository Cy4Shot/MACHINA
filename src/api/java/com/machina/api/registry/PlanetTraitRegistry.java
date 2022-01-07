package com.machina.api.registry;

import java.util.Optional;

import com.machina.api.ModIDs;
import com.machina.api.annotation.ChangedByReflection;
import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.helper.CustomRegistryHelper;
import com.machina.api.util.objects.TargetField;
import com.matyrobbrt.lib.registry.annotation.RegisterCustomRegistry;
import com.matyrobbrt.lib.registry.annotation.RegistryHolder;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

@RegistryHolder(modid = ModIDs.MACHINA)
public class PlanetTraitRegistry {

	@ChangedByReflection(when = "commonSetup (when the registry is built)")
	public static final IForgeRegistry<PlanetTrait> REGISTRY = null;

	@RegisterCustomRegistry
	public static void createRegistry(RegistryEvent.NewRegistry event) {
		CustomRegistryHelper.<PlanetTrait>registerRegistry(new TargetField(PlanetTraitRegistry.class, "REGISTRY"),
				PlanetTrait.class, new MachinaRL("planet_trait"), Optional.of(new MachinaRL("none")),
				Optional.of(new MachinaRL("planet_trait_registry")));
	}
}
