package com.cy4.machina.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.cy4.machina.init.TagInit;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.task.SwimTask;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.world.server.ServerWorld;

@Mixin(SwimTask.class)
public abstract class SwimTaskMixin {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getFluidHeight(Lnet/minecraft/tags/ITag;)D", ordinal = 0), method = "checkExtraStartConditions(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/entity/MobEntity;)Z")
	private boolean machina$checkExtraStartConditionsFluidHeight(MobEntity entity, ITag<Fluid> fluid, ServerWorld pLevel,
			MobEntity pOwner) {
		return entity.getFluidHeight(FluidTags.WATER) > entity.getFluidJumpThreshold()
				|| entity.getFluidHeight(TagInit.Fluids.NOT_ACTUALLY_WATER) > entity.getFluidJumpThreshold();
	}

}
