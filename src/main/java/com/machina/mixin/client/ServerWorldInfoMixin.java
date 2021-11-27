package com.machina.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.machina.Machina;
import com.machina.api.config.MachinaClientConfig;
import com.mojang.serialization.Lifecycle;

import net.minecraft.world.storage.ServerWorldInfo;

@Mixin(ServerWorldInfo.class)
public class ServerWorldInfoMixin {

	@Inject(method = "Lnet/minecraft/world/storage/ServerWorldInfo;worldGenSettingsLifecycle()Lcom/mojang/serialization/Lifecycle;", at = @At("HEAD"), cancellable = true)
	private void machina$noExperimentalScreen(CallbackInfoReturnable<Lifecycle> callback) {
		if (Boolean.TRUE.equals(MachinaClientConfig.DISABLE_EXPERIMENTAL_SETTINGS_SCREEN.get())) {
			Machina.LOGGER.info(
					"Dear Mojang, we know that world gen is experimental and it's not supported, because we (mods) are not supported. We will always love you and you will never love us. (now fuck your experimental screen)");
			callback.setReturnValue(Lifecycle.stable());
		}
	}

}
