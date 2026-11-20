package com.machina.client.screen.overlay;

import com.machina.Machina;
import com.machina.api.util.MachinaRL;
import com.machina.registration.init.EffectInit;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = Machina.MOD_ID, value = Dist.CLIENT)
public class RadiationOverlay {

	private static final ResourceLocation NOISE_TEXTURE = MachinaRL.create("textures/gui/radiation_noise.png");
	private static final RandomSource RANDOM = RandomSource.create();
	private static float noiseOffsetX = 0;
	private static float noiseOffsetY = 0;
	private static long lastUpdateTime = 0;

	@SubscribeEvent
	public static void onRenderGuiLayerPost(RenderGuiLayerEvent.Post event) {
		if (!event.getName().equals(VanillaGuiLayers.CAMERA_OVERLAYS)) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player == null || mc.options.hideGui) {
			return;
		}

		MobEffectInstance radiation = player.getEffect(EffectInit.RADIATION);
		if (radiation == null) {
			return;
		}

		int amplifier = radiation.getAmplifier();
		float alpha = Math.min(0.15f + amplifier * 0.15f, 0.4f);

		renderFilmGrain(event.getGuiGraphics(), mc, alpha);
	}

	private static void renderFilmGrain(GuiGraphics graphics, Minecraft mc, float alpha) {
		int screenWidth = mc.getWindow().getGuiScaledWidth();
		int screenHeight = mc.getWindow().getGuiScaledHeight();

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastUpdateTime > 50) {
			noiseOffsetX = RANDOM.nextFloat();
			noiseOffsetY = RANDOM.nextFloat();
			lastUpdateTime = currentTime;
		}

		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
		RenderSystem.setShaderTexture(0, NOISE_TEXTURE);

		float u0 = noiseOffsetX;
		float v0 = noiseOffsetY;
		float u1 = u0 + (screenWidth / 64.0f);
		float v1 = v0 + (screenHeight / 64.0f);

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		buffer.addVertex(0, screenHeight, 0).setUv(u0, v1);
		buffer.addVertex(screenWidth, screenHeight, 0).setUv(u1, v1);
		buffer.addVertex(screenWidth, 0, 0).setUv(u1, v0);
		buffer.addVertex(0, 0, 0).setUv(u0, v0);
		BufferUploader.drawWithShader(buffer.buildOrThrow());

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
	}
}
