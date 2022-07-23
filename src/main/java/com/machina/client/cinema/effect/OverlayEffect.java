package com.machina.client.cinema.effect;

import org.lwjgl.opengl.GL11;

import com.machina.client.util.UIHelper;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class OverlayEffect {

	public static final Minecraft mc = Minecraft.getInstance();

	public static boolean render = false;
	public static ResourceLocation rl = null;
	public static float opacity = 0f;

	public static void overlay(RenderGameOverlayEvent.Pre event) {
		if (!render)
			return;

		int scaledWidth = mc.getWindow().getGuiScaledWidth();
		int scaledHeight = mc.getWindow().getGuiScaledHeight();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		UIHelper.withAlpha(() -> {
			mc.getTextureManager().bind(rl);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuilder();
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.vertex(0.0D, (double) scaledHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
			bufferbuilder.vertex((double) scaledWidth, (double) scaledHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
			bufferbuilder.vertex((double) scaledWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
			bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
			tessellator.end();
			RenderSystem.depthMask(true);
			RenderSystem.enableDepthTest();
		}, opacity);
		UIHelper.resetColor();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		RenderSystem.disableAlphaTest();
	}

}
