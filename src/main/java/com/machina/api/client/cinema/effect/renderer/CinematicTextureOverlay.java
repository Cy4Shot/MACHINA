package com.machina.api.client.cinema.effect.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class CinematicTextureOverlay {
	public static final Minecraft mc = Minecraft.getInstance();

	public static boolean render = false;
	public static ResourceLocation rl = null;
	public static float opacity = 0f;

	public static void renderOverlay() {
		if (!render)
			return;

		Tesselator tesselator = Tesselator.getInstance();
		int scaledWidth = mc.getWindow().getGuiScaledWidth();
		int scaledHeight = mc.getWindow().getGuiScaledHeight();

		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
		RenderSystem.setShaderTexture(0, rl);

		BufferBuilder builder = tesselator.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		builder.addVertex(0.0F, scaledHeight, -90.0F).setUv(0.0F, 1.0F);
		builder.addVertex(scaledWidth, scaledHeight, -90.0F).setUv(1.0F, 1.0F);
		builder.addVertex(scaledWidth, 0.0F, -90.0F).setUv(1.0F, 0.0F);
		builder.addVertex(0.0F, 0.0F, -90.0F).setUv(0.0F, 0.0F);
		BufferUploader.drawWithShader(builder.build());

		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.disableBlend();
	}
}
