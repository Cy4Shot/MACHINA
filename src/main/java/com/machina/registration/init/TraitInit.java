package com.machina.registration.init;

import com.machina.planet.trait.Trait;
import com.machina.registration.registry.TraitRegistry;
import com.machina.util.reflection.ClassHelper;
import com.machina.util.text.MachinaRL;

import net.minecraftforge.event.RegistryEvent;

public final class TraitInit {

	public static final Trait NONE = new Trait(0);

	public static void register(final RegistryEvent.Register<Trait> event) {
		ClassHelper.<Trait>doWithStatics(TraitInit.class,
				(name, data) -> register(name.toLowerCase(), data));
	}

	private static void register(String name, Trait trait) {
		trait.setRegistryName(new MachinaRL(name));
		TraitRegistry.REGISTRY.register(trait);
	}
}
