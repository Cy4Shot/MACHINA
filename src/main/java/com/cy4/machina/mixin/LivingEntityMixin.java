package com.cy4.machina.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cy4.machina.api.events.LivingEntityAddEffectEvent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	private LivingEntityMixin(EntityType<?> pType, World pLevel) {
		super(pType, pLevel);
	}

	@Inject(method = "Lnet/minecraft/entity/LivingEntity;addEffect(Lnet/minecraft/potion/EffectInstance;)Z", at = @At("HEAD"), cancellable = true)
	private void addEffectBan(EffectInstance effect, CallbackInfoReturnable<Boolean> callback) {
		if (LivingEntityAddEffectEvent.onLivingAddEffect((LivingEntity) (Object) this, effect)) {
			callback.setReturnValue(false);
		}
	}

}
