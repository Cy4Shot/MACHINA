package com.cy4.machina.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.api.planet.PlanetUtils;
import com.cy4.machina.init.EffectInit;
import com.cy4.machina.init.PlanetTraitInit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	
	private LivingEntityMixin(EntityType<?> pType, World pLevel) {
		super(pType, pLevel);
	}

	@Inject(method = "Lnet/minecraft/entity/LivingEntity;addEffect(Lnet/minecraft/potion/EffectInstance;)Z", at = @At("HEAD"), cancellable = true)
	private void addEffectBan(EffectInstance effect, CallbackInfoReturnable<Boolean> callback) {
		if (PlanetUtils.isDimensionPlanet(level.dimension())) {
			if (CapabilityPlanetTrait.worldHasTrait(level, PlanetTraitInit.SUPERHOT) && effect.getEffect() == Effects.FIRE_RESISTANCE) {
				callback.setReturnValue(false);
			}
		}
		
		setEffectPlanetOnly(EffectInit.SUPERHOT_RESISTANCE, effect, callback);
	}
	
	private void setEffectPlanetOnly(Effect effect, EffectInstance instace, CallbackInfoReturnable<Boolean> callback) {
		if (instace.getEffect() != effect)
			return;
		if (!PlanetUtils.isDimensionPlanet(level.dimension())) {
			callback.setReturnValue(false);
		}
	}

}
