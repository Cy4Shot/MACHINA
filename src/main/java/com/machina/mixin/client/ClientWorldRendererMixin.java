package com.machina.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.machina.client.ClientStarchart;
import com.machina.client.shader.renderer.AtmRenderer;
import com.machina.client.shader.renderer.FogRenderer;
import com.machina.client.shader.renderer.ScannerRenderer;
import com.machina.registration.init.AttributeInit;
import com.machina.util.helper.PlanetHelper;
import com.machina.world.gen.PlanetTerrainGenerator;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.world.World;

@Mixin(WorldRenderer.class)
public class ClientWorldRendererMixin {

	@SuppressWarnings("resource")
	@Inject(method = "Lnet/minecraft/client/renderer/WorldRenderer;renderLevel(Lcom/mojang/blaze3d/matrix/MatrixStack;FJZLnet/minecraft/client/renderer/ActiveRenderInfo;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/util/math/vector/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OutlineLayerBuffer;endOutlineBatch()V", shift = At.Shift.AFTER))
	public void renderLevel(MatrixStack pMatrixStack, float pPartialTicks, long pFinishTimeNano,
			boolean pDrawBlockOutline, ActiveRenderInfo pActiveRenderInfo, GameRenderer pGameRenderer,
			LightTexture pLightmap, Matrix4f pProjection, CallbackInfo ci) {
		// TODO: Config!
		RegistryKey<World> dim = Minecraft.getInstance().level.dimension();
		if (PlanetHelper.isDimensionPlanet(dim)) {
			if (PlanetTerrainGenerator
					.getProcessor(ClientStarchart.getPlanetData(dim).getAttribute(AttributeInit.SURFACE_SHAPE))
					.isGas()) {
				FogRenderer.render(pMatrixStack, pProjection);
			} else {
				AtmRenderer.render(pMatrixStack, pProjection);
			}
		}
		ScannerRenderer.render(pMatrixStack, pProjection);
	}
}