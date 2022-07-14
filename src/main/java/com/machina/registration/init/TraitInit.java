package com.machina.registration.init;

import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.registry.PlanetTraitRegistry;
import com.machina.trait.FrozenTrait;
import com.machina.trait.HeightMultiplierTrait;
import com.machina.trait.NoiseSettingsTrait;
import com.machina.trait.WaterHeightTrait;
import com.machina.util.MachinaRL;
import com.machina.util.reflection.ClassHelper;
import com.machina.world.settings.DynamicNoiseSettings;

import net.minecraftforge.event.RegistryEvent;

public final class TraitInit {

	public static final PlanetTrait NONE = new PlanetTrait(0);
	public static final PlanetTrait WATER_WORLD = new WaterHeightTrait(0x169fde, 100);
	public static final PlanetTrait CONTINENTAL = new WaterHeightTrait(0x24bf2a, 70);
	public static final PlanetTrait MOUNTAINOUS = new HeightMultiplierTrait(0x807f7e, 3.0f);
	public static final PlanetTrait HILLY = new HeightMultiplierTrait(0x613407, 1.5f);
	public static final PlanetTrait FLAT = new HeightMultiplierTrait(0x449e41, 0.5f);
	public static final PlanetTrait LAKES = new PlanetTrait(0x098aed);
	public static final PlanetTrait FROZEN = new FrozenTrait(0x0aabf5);
	public static final PlanetTrait ISLANDS = new NoiseSettingsTrait(0x48dde8, DynamicNoiseSettings.ISLAND_TYPE);

	public static void register(final RegistryEvent.Register<PlanetTrait> event) {
		ClassHelper.<PlanetTrait>doWithStatics(TraitInit.class,
				(name, data) -> register(name.toLowerCase(), data));
	}

	private static void register(String name, PlanetTrait trait) {
		trait.setRegistryName(new MachinaRL(name));
		PlanetTraitRegistry.REGISTRY.register(trait);
	}
}
