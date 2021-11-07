package com.cy4.machina.api.planet;

import com.cy4.machina.Machina;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class PlanetTrait extends ForgeRegistryEntry<PlanetTrait> {

	public static IForgeRegistry<PlanetTrait> registry;

	public static void createRegistry(RegistryEvent.NewRegistry event) {
		RegistryBuilder<PlanetTrait> registryBuilder = new RegistryBuilder<>();
		registryBuilder.setName(new ResourceLocation(Machina.MOD_ID, "planet_trait_registry"));
		registryBuilder.setType(PlanetTrait.class);
		registry = registryBuilder.create();
	}
	
	public String test;
	
	public PlanetTrait(String test) {
		this.test = test;
	}

}
