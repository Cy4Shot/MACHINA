package com.machina.registration.init;

import com.machina.Machina;
import com.machina.api.rocket.RocketProps;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;

public class EntityDataSerializerInit {

    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister
            .create(Keys.ENTITY_DATA_SERIALIZERS, Machina.MOD_ID);

    //@formatter:off
	public static final RegistryObject<EntityDataSerializer<RocketProps>> ROCKET_PROPS = ENTITY_DATA_SERIALIZERS.register("rocket_props", () -> RocketProps.SERIALIZER);
	//@formatter:on
}
