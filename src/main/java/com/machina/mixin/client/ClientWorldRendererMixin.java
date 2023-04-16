package com.machina.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.machina.client.shader.renderer.FogRenderer;
import com.machina.client.shader.renderer.ScannerRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.vector.Matrix4f;

@Mixin(WorldRenderer.class)
public class ClientWorldRendererMixin {

	@Inject(method = "Lnet/minecraft/client/renderer/WorldRenderer;renderLevel(Lcom/mojang/blaze3d/matrix/MatrixStack;FJZLnet/minecraft/client/renderer/ActiveRenderInfo;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/util/math/vector/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OutlineLayerBuffer;endOutlineBatch()V", shift = At.Shift.AFTER))
	public void renderLevel(MatrixStack pMatrixStack, float pPartialTicks, long pFinishTimeNano,
			boolean pDrawBlockOutline, ActiveRenderInfo pActiveRenderInfo, GameRenderer pGameRenderer,
			LightTexture pLightmap, Matrix4f pProjection, CallbackInfo ci) {
//		RaindropRenderer.render(pMatrixStack, pProjection);
		ScannerRenderer.render(pMatrixStack, pProjection);
		FogRenderer.render(pMatrixStack, pProjection);
	}
}