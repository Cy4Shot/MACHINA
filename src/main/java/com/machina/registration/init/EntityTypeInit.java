package com.machina.registration.init;

import com.machina.Machina;
import com.machina.api.rocket.RocketEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeInit {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister
			.create(ForgeRegistries.ENTITY_TYPES, Machina.MOD_ID);

	//@formatter:off
	public static final RegistryObject<EntityType<Entity>> ROCKET = ENTITY_TYPES.register("rocket",
            () -> EntityType.Builder.of(RocketEntity::new, MobCategory.MISC).sized(1.0F, 1.0F)
                    .build(Machina.MOD_ID + ":rocket"));
	//@formatter:on
}
