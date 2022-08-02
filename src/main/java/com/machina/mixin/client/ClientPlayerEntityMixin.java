package com.machina.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.machina.registration.init.TagInit;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;isEyeInFluid(Lnet/minecraft/tags/ITag;)Z", ordinal = 0), method = "getWaterVision")
	private boolean machina$getWaterVision(ClientPlayerEntity player, ITag<Fluid> fluid) {
		return !player.isEyeInFluid(FluidTags.WATER) && !player.isEyeInFluid(TagInit.Fluids.NOT_ACTUALLY_WATER);
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;isEyeInFluid(Lnet/minecraft/tags/ITag;)Z"), method = "aiStep")
	private boolean machina$aiStep(ClientPlayerEntity player, ITag<Fluid> fluid) {
		return !player.isEyeInFluid(FluidTags.WATER) && !player.isEyeInFluid(TagInit.Fluids.NOT_ACTUALLY_WATER);
	}
}