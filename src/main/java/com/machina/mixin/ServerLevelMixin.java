package com.machina.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.machina.Machina;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

@Mixin(ServerLevel.class)
public class ServerLevelMixin extends LevelMixin {

	@Inject(method = "Lnet/minecraft/server/level/ServerLevel;getSeed()J", at = @At("RETURN"), cancellable = true)
	private void getSeed(CallbackInfoReturnable<Long> ret) {
		ResourceLocation rl = dimension().location();
		if (!rl.getNamespace().equals(Machina.MOD_ID))
			return;
		int id = Integer.parseInt(dimension().location().getPath());
		ret.setReturnValue(ret.getReturnValue() + id);
	}
}
