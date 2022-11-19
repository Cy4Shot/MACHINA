package com.machina.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.machina.registration.init.AttributeInit;
import com.machina.registration.init.DamageSourceInit;
import com.machina.util.server.PlanetHelper;
import com.machina.world.data.PlanetData;
import com.machina.world.data.StarchartData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> pType, World pLevel) {
		super(pType, pLevel);
	}

	@Shadow
	public AttributeModifierManager getAttributes() {
		throw new IllegalStateException("Mixin failed to shadow getAttributes()");
	}

	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/LivingEntity;getAttribute(Lnet/minecraft/entity/ai/attributes/Attribute;)Lnet/minecraft/entity/ai/attributes/ModifiableAttributeInstance;", cancellable = true)
	private void getAttribute(Attribute attribute, CallbackInfoReturnable<ModifiableAttributeInstance> cb) {
		if (attribute.equals(ForgeMod.ENTITY_GRAVITY.get())) {
			RegistryKey<World> dim = this.level.dimension();

			float gravity = 1f;
			if (PlanetHelper.isDimensionPlanet(dim)) {
				PlanetData data = StarchartData.getDataForDimension(dim);
				gravity = data.getAttribute(AttributeInit.GRAVITY);
			}

			ModifiableAttributeInstance attr = getAttributes().getInstance(attribute);
			attr.setBaseValue(0.08f * gravity);
			cb.setReturnValue(attr);
		}
	}

	@Inject(at = @At("TAIL"), method = "Lnet/minecraft/entity/LivingEntity;baseTick()V")
	private void baseTick(CallbackInfo cb) {
		Entity e = this.getEntity();
		boolean inv = e instanceof PlayerEntity && ((PlayerEntity) e).abilities.invulnerable;
		if (this.isAlive()) {
			if (!PlanetHelper.canBreath(this.level.dimension()) && !inv) {
				this.setAirSupply(this.getAirSupply() - 1);
				if (this.getAirSupply() == -20) {
					this.setAirSupply(0);
					Vector3d vector3d = this.getDeltaMovement();

					for (int i = 0; i < 8; ++i) {
						double d2 = this.random.nextDouble() - this.random.nextDouble();
						double d3 = this.random.nextDouble() - this.random.nextDouble();
						double d4 = this.random.nextDouble() - this.random.nextDouble();
						this.level.addParticle(ParticleTypes.BUBBLE, this.getX() + d2, this.getY() + d3,
								this.getZ() + d4, vector3d.x, vector3d.y, vector3d.z);
					}

					this.hurt(DamageSourceInit.SUFFOCATE, 2.0F);
				}
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/LivingEntity;increaseAirSupply(I)I", cancellable = true)
	protected void increaseAirSupply(int pCurrentAir, CallbackInfoReturnable<Integer> cb) {
		if (!PlanetHelper.canBreath(this.level.dimension()))
			cb.setReturnValue(pCurrentAir);
	}
}
