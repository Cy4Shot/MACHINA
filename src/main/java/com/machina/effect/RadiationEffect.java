package com.machina.effect;

import com.machina.registration.init.DamageTypeInit;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class RadiationEffect extends MobEffect {

	public RadiationEffect() {
		super(MobEffectCategory.HARMFUL, 0x7FFF00);
	}

	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		DamageSource source = new DamageSource(
				entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
						.getHolderOrThrow(DamageTypeInit.RADIATION));
		entity.hurt(source, 1.0F + amplifier);
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		int interval = 10 >> amplifier;
		if (interval > 0) {
			return duration % interval == 0;
		}
		return true;
	}
}
