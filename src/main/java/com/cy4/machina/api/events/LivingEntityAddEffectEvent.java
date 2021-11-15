package com.cy4.machina.api.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class LivingEntityAddEffectEvent extends Event {

	private final LivingEntity entity;
	private final EffectInstance effect;

	private LivingEntityAddEffectEvent(LivingEntity entity, EffectInstance effect) {
		this.entity = entity;
		this.effect = effect;
	}

	public EffectInstance getEffect() {
		return effect;
	}

	public LivingEntity getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 * @param effect
	 * @return if true, the event was cancelled
	 */
	public static boolean onLivingAddEffect(LivingEntity entity, EffectInstance effect) {
		return MinecraftForge.EVENT_BUS.post(new LivingEntityAddEffectEvent(entity, effect));
	}

}
