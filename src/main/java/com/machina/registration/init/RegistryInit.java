package com.machina.registration.init;

import com.google.common.collect.Maps;
import com.machina.Machina;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.starchart.planet_biome.RockMaker;
import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.api.util.MachinaRL;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.*;
import net.minecraftforge.registries.IForgeRegistry.AddCallback;
import net.minecraftforge.registries.IForgeRegistry.CreateCallback;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public class RegistryInit {
    public static final DeferredRegister<TreeMaker> TREES = DeferredRegister.create(new MachinaRL("tree"),
            Machina.MOD_ID);
    public static final DeferredRegister<RockMaker> ROCKS = DeferredRegister.create(new MachinaRL("rock"),
            Machina.MOD_ID);
    public static final DeferredRegister<RocketPart<?>> ROCKET_PARTS = DeferredRegister
            .create(new MachinaRL("rocket_parts"), Machina.MOD_ID);

    public static final Supplier<IForgeRegistry<TreeMaker>> TREE_REGISTRY = TREES.makeRegistry(RegistryBuilder::new);
    public static final Supplier<IForgeRegistry<RockMaker>> ROCK_REGISTRY = ROCKS.makeRegistry(RegistryBuilder::new);
    public static final Supplier<IForgeRegistry<RocketPart<?>>> ROCKET_PARTS_REGISTRY = ROCKET_PARTS
            .makeRegistry(() -> new RegistryBuilder<RocketPart<?>>().addCallback(RocketPartCallbacks.INSTANCE));

    public static class RocketPartCallbacks implements CreateCallback<RocketPart<?>>, AddCallback<RocketPart<?>> {
        public static final ResourceLocation ROCKET_PART_TO_ITEM = new MachinaRL("rocket_part_to_item");

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
            map.put(obj, new MachinaRL("rocket_part_" + key.location().getPath()));
        }
    }
}
