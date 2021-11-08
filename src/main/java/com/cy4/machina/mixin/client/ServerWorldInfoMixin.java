package com.cy4.machina.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cy4.machina.Machina;
import com.cy4.machina.config.ClientConfig;
import com.mojang.serialization.Lifecycle;

import net.minecraft.world.storage.ServerWorldInfo;

@Mixin(ServerWorldInfo.class)
public class ServerWorldInfoMixin {

	@Inject(method = "Lnet/minecraft/world/storage/ServerWorldInfo;worldGenSettingsLifecycle()Lcom/mojang/serialization/Lifecycle;", at = @At("HEAD"), cancellable = true)
	private void noExperimentalScreen(CallbackInfoReturnable<Lifecycle> callback) {
		if (Boolean.TRUE.equals(ClientConfig.DISABLE_EXPERIMENTAL_SETTINGS_SCREEN.get())) {
			Machina.LOGGER.info(
					"Dear Mojang, we know that world gen is experimental and it's not supported, because we (mods) are not supported. We will always love you and you will never love us. (now fuck your experimental screen)");
			callback.setReturnValue(Lifecycle.stable());
		}
	}

}
