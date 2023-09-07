package com.machina.api.client.cinema.effect.renderer;

import com.machina.api.client.UIHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

public class CinematicTextOverlay {
	public static final Minecraft mc = Minecraft.getInstance();

	public static boolean render = false;
	public static Component title, subtitle;
	public static float titleOpacity = 1f;
	public static float subOpacity = 1f;

	public static void renderOverlay(GuiGraphics gui, PoseStack stack, int w, int h) {
		if (!render)
			return;

		int alphaT = (int) (titleOpacity * 256f) << 24;
		int alphaS = (int) (subOpacity * 256f) << 24;
		int i2 = 255 << 24 & -16777216;

		stack.pushPose();
		stack.translate(w / 2, h / 2, 0.0F);
		RenderSystem.enableBlend();
		if (alphaT != 0 || titleOpacity == 1f) {
			stack.pushPose();
			stack.scale(4.0F, 4.0F, 4.0F);
			int j2 = mc.font.width(title);
			drawBackdrop(gui, mc.font, -10, j2, 16777215 | i2);
			UIHelper.drawStringShadow(gui, title, (float) (-j2 / 2), -10.0F, 0xFFFFFF + alphaT);
			stack.popPose();
		}

		if (alphaS != 0 || subOpacity == 1f) {
			stack.pushPose();
			stack.scale(2.0F, 2.0F, 2.0F);
			int l2 = mc.font.width(subtitle);
			drawBackdrop(gui, mc.font, 5, l2, 16777215 | i2);
			UIHelper.drawStringShadow(gui, subtitle, (float) (-l2 / 2), 5.0F, 0xFFFFFF + alphaS);
			stack.popPose();
		}
		RenderSystem.disableBlend();
		stack.popPose();
	}

	static void drawBackdrop(GuiGraphics gui, Font p_93041_, int p_93042_, int p_93043_, int p_93044_) {
		int i = mc.options.getBackgroundColor(0.0F);
		if (i != 0) {
			int j = -p_93043_ / 2;
			gui.fill(j - 2, p_93042_ - 2, j + p_93043_ + 2, p_93042_ + 9 + 2, FastColor.ARGB32.multiply(i, p_93044_));
		}
	}
}
