package com.machina.registration.init;

import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.machina.Machina;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.starchart.planet_biome.PlanetSurface;
import com.machina.api.starchart.planet_biome.RockMaker;
import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.api.util.MachinaRL;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.neoforged.neoforge.registries.callback.AddCallback;
import net.neoforged.neoforge.registries.callback.BakeCallback;
import net.neoforged.neoforge.registries.callback.RegistryCallback;

public class RegistryInit {

    public static final Registry<TreeMaker> TREE_REGISTRY = createRegistry(MachinaRL.create("tree"), null);
    public static final Registry<RockMaker> ROCK_REGISTRY = createRegistry(MachinaRL.create("tree"), null);
    public static final Registry<PlanetSurface> SURFACE_REGISTRY = createRegistry(MachinaRL.create("tree"), null);
    public static final Registry<RocketPart<?>> ROCKET_PART_REGISTRY = createRegistry(MachinaRL.create("tree"),
            RocketPartCallbacks.INSTANCE);

    public static class RocketPartCallbacks implements BakeCallback<RocketPart<?>>, AddCallback<RocketPart<?>> {
        public static final ResourceLocation ROCKET_PART_TO_ITEM = MachinaRL.create("rocket_part_to_item");

        static final RocketPartCallbacks INSTANCE = new RocketPartCallbacks();

        @Override
        public void onCreate(IForgeRegistryInternal<RocketPart<?>> owner, RegistryManager stage) {
            owner.setSlaveMap(ROCKET_PART_TO_ITEM, Maps.newHashMap());
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onAdd(IForgeRegistryInternal<RocketPart<?>> owner, RegistryManager stage, int id,
                ResourceKey<RocketPart<?>> key, RocketPart<?> obj, @Nullable RocketPart<?> oldObj) {
            Map<RocketPart<?>, ResourceLocation> map = (Map<RocketPart<?>, ResourceLocation>) owner
                    .getSlaveMap(ROCKET_PART_TO_ITEM, Map.class);
            map.put(obj, MachinaRL.create("rocket_part_" + key.location().getPath()));
        }

        @Override
        public void onAdd(Registry<RocketPart<?>> registry, int id, ResourceKey<RocketPart<?>> key,
                RocketPart<?> value) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onBake(Registry<RocketPart<?>> registry) {
            // TODO
        }
    }

    private static final <T> Registry<T> createRegistry(ResourceLocation RL, @Nullable RegistryCallback<T> callback) {
        RegistryBuilder<T> builder = new RegistryBuilder<T>(ResourceKey.createRegistryKey(RL)).sync(true)
                .defaultKey(MachinaRL.create("empty")).maxId(256);
        if (callback != null) {
            builder = builder.callback(callback);
        }
        return builder.create();
    }
}
