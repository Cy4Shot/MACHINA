package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.rocket.RocketEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityTypeInit {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE,
			Machina.MOD_ID);

	//@formatter:off
	public static final Supplier<EntityType<RocketEntity>> ROCKET = ENTITY_TYPES.register("rocket",
            () -> EntityType.Builder.<RocketEntity>of(RocketEntity::new, MobCategory.MISC).sized(1.0F, 1.0F)
                    .build(Machina.MOD_ID + ":rocket"));
	//@formatter:on
}
