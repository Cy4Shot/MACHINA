package com.machina.registration.init;

import com.machina.Machina;
import com.machina.api.rocket.RocketCosts;
import com.machina.api.rocket.RocketProps;
import com.machina.rocket.DimensionSerializer;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;

public class EntityDataSerializerInit {

    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister
            .create(Keys.ENTITY_DATA_SERIALIZERS, Machina.MOD_ID);

    //@formatter:off
	public static final RegistryObject<EntityDataSerializer<RocketProps>> ROCKET_PROPS = ENTITY_DATA_SERIALIZERS.register("rocket_props", () -> RocketProps.SERIALIZER);
    public static final RegistryObject<EntityDataSerializer<RocketCosts>> ROCKET_COSTS = ENTITY_DATA_SERIALIZERS.register("rocket_costs", () -> RocketCosts.SERIALIZER);
	public static final RegistryObject<EntityDataSerializer<ResourceKey<Level>>> DIMENSION = ENTITY_DATA_SERIALIZERS.register("dimension", () -> DimensionSerializer.SERIALIZER);
	//@formatter:on
}
