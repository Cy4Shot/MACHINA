package com.machina.registration.init;

import java.util.ArrayList;
import java.util.List;

import com.machina.Machina;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.starchart.planet_biome.PlanetSurface;
import com.machina.api.starchart.planet_biome.RockMaker;
import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.api.util.MachinaRL;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = Machina.MOD_ID)
public class RegistryInit {
	private static final List<Registry<?>> REGISTRIES = new ArrayList<>();

	public static final Registry<TreeMaker> TREE_REGISTRY = createRegistry(MachinaRL.create("tree"));
	public static final Registry<RockMaker> ROCK_REGISTRY = createRegistry(MachinaRL.create("rock"));
	public static final Registry<PlanetSurface> SURFACE_REGISTRY = createRegistry(MachinaRL.create("surface"));
	public static final Registry<RocketPart> ROCKET_PART_REGISTRY = createRegistry(MachinaRL.create("rocket_part"));

	private static final <T> Registry<T> createRegistry(ResourceLocation RL) {
		Registry<T> reg = new RegistryBuilder<T>(ResourceKey.createRegistryKey(RL)).sync(true)
				.defaultKey(MachinaRL.create("empty")).maxId(256).create();
		REGISTRIES.add(reg);
		return reg;
	}

	@SubscribeEvent
	public static void registerRegistries(NewRegistryEvent event) {
		REGISTRIES.forEach(event::register);
	}
}
