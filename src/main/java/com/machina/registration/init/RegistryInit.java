package com.machina.registration.init;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.starchart.planet_biome.PlanetSurface;
import com.machina.api.starchart.planet_biome.RockMaker;
import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.api.util.MachinaRL;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class RegistryInit {

    public static final Registry<TreeMaker> TREE_REGISTRY = createRegistry(MachinaRL.create("tree"));
    public static final Registry<RockMaker> ROCK_REGISTRY = createRegistry(MachinaRL.create("tree"));
    public static final Registry<PlanetSurface> SURFACE_REGISTRY = createRegistry(MachinaRL.create("tree"));
    public static final Registry<RocketPart<?>> ROCKET_PART_REGISTRY = createRegistry(MachinaRL.create("tree"));

    private static final <T> Registry<T> createRegistry(ResourceLocation RL) {
        return new RegistryBuilder<T>(ResourceKey.createRegistryKey(RL)).sync(true)
                .defaultKey(MachinaRL.create("empty")).maxId(256).create();
    }
}
