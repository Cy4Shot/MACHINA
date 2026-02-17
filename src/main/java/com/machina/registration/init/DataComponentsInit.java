package com.machina.registration.init;

import java.util.Map;

import com.machina.Machina;
import com.machina.api.item.ConnectorFilterItem;
import com.machina.api.rocket.RocketProps;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentsInit {
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister
            .createDataComponents(Registries.DATA_COMPONENT_TYPE, Machina.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ConnectorFilterItem.Mode>> FILTER_MODE = REGISTRAR
            .registerComponentType("filter_mode", builder -> builder.persistent(ConnectorFilterItem.Mode.CODEC)
                    .networkSynchronized(ConnectorFilterItem.Mode.STREAM_CODEC));

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
