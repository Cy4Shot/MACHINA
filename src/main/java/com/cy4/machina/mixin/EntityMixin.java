/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.cy4.machina.init.TagInit;

import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;updateFluidHeightAndDoFluidPushing(Lnet/minecraft/tags/ITag;D)Z"), method = "updateInWaterStateAndDoWaterCurrentPushing")
	private boolean machina$doNotActuallyWaterPushing(Entity entity, ITag<Fluid> fluid, double motionScale) {
		if (fluid == FluidTags.WATER) {
			return entity.updateFluidHeightAndDoFluidPushing(FluidTags.WATER, motionScale)
					|| entity.updateFluidHeightAndDoFluidPushing(TagInit.Fluids.NOT_ACTUALLY_WATER, motionScale);
		}
		return entity.updateFluidHeightAndDoFluidPushing(fluid, motionScale);
	}

}
