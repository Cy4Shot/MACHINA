package com.cy4.machina.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.cy4.machina.item.ThermalRegulatorSuit;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.OverlayRenderer;

@Mixin(OverlayRenderer.class)
public class OverlayRendererMixin {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;isOnFire()Z"), method = "net/minecraft/client/renderer/OverlayRenderer.renderScreenEffect(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/matrix/MatrixStack;)V")
	private static boolean machina$renderOverlaysMixin(ClientPlayerEntity player, Minecraft minecraft, MatrixStack matrixStack) {
		if (player.isCreative()) {
			return false;
		}
		if (player.isOnFire()) {
			return !ThermalRegulatorSuit.isFullSuit(player);
		}
		return false;
	}

}
