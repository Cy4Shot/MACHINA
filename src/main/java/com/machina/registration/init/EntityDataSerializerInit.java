package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.rocket.RocketCosts;
import com.machina.api.rocket.RocketProps;
import com.machina.rocket.DimensionSerializer;
import com.machina.rocket.RocketEntity.RocketStage;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

public class EntityDataSerializerInit {

    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister
            .create(Keys.ENTITY_DATA_SERIALIZERS, Machina.MOD_ID);

    //@formatter:off
    public static final Supplier<EntityDataSerializer<RocketStage>> ROCKET_STAGE = ENTITY_DATA_SERIALIZERS .register("rocket_stage", () -> RocketStage.SERIALIZER);
	public static final Supplier<EntityDataSerializer<RocketProps>> ROCKET_PROPS = ENTITY_DATA_SERIALIZERS.register("rocket_props", () -> RocketProps.SERIALIZER);
    public static final Supplier<EntityDataSerializer<RocketCosts>> ROCKET_COSTS = ENTITY_DATA_SERIALIZERS.register("rocket_costs", () -> RocketCosts.SERIALIZER);
	public static final Supplier<EntityDataSerializer<ResourceKey<Level>>> DIMENSION = ENTITY_DATA_SERIALIZERS.register("dimension", () -> DimensionSerializer.SERIALIZER);
	//@formatter:on
}
