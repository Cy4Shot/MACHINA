package com.machina.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.machina.registration.init.TagInit;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	
	private PlayerEntityMixin(EntityType<? extends LivingEntity> p_i48577_1_, World p_i48577_2_) {
		super(p_i48577_1_, p_i48577_2_);
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isEyeInFluid(Lnet/minecraft/tags/ITag;)Z", ordinal = 0), method = "updateIsUnderwater")
	private boolean machina$updateIsUnderwater(PlayerEntity player, ITag<Fluid> fluid) {
		return player.isEyeInFluid(FluidTags.WATER) || player.isEyeInFluid(TagInit.Fluids.NOT_ACTUALLY_WATER);
	}

}
