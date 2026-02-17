package com.machina.registration.init;

import java.util.List;
import java.util.Map;

import com.machina.Machina;
import com.machina.api.item.ConnectorFilterItem;
import com.machina.api.rocket.RocketProps;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.util.reflect.MachinaCodecs;
import com.machina.api.util.reflect.MachinaStreamCodecs;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentsInit {
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister
            .createDataComponents(Registries.DATA_COMPONENT_TYPE, Machina.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ConnectorFilterItem.Mode>> FILTER_MODE = REGISTRAR
            .registerComponentType("filter_mode", builder -> builder.persistent(ConnectorFilterItem.Mode.CODEC)
                    .networkSynchronized(ConnectorFilterItem.Mode.STREAM_CODEC));
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Item>> ITEM = REGISTRAR
            .registerComponentType("item", builder -> builder.persistent(MachinaCodecs.ITEM)
                    .networkSynchronized(MachinaStreamCodecs.ITEM));
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<Item>>> ITEMS = REGISTRAR
            .registerComponentType("items", builder -> builder.persistent(MachinaCodecs.ITEM.listOf())
                    .networkSynchronized(MachinaStreamCodecs.ITEM.apply(ByteBufCodecs.list())));
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Fluid>> FLUID = REGISTRAR
            .registerComponentType("fluid", builder -> builder.persistent(MachinaCodecs.FLUID)
                    .networkSynchronized(MachinaStreamCodecs.FLUID));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENERGY = REGISTRAR
            .registerComponentType("energy", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RocketProps>> ROCKET_PROPS = REGISTRAR
            .registerComponentType("rocket_props",
                    builder -> builder.persistent(RocketProps.CODEC).networkSynchronized(RocketProps.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Map<RocketPartType, RocketPart<?>>>> ROCKET_PARTS = REGISTRAR
            .registerComponentType("rocket_parts", builder -> builder.persistent(RocketProps.PARTMAP_CODEC)
                    .networkSynchronized(RocketProps.PARTMAP_STREAM_CODEC));
}
