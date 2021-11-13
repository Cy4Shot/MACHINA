package com.cy4.machina.client.util;

import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import com.cy4.machina.util.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;

public class UIHelper {

	public static final ResourceLocation ELEMENTS = new MachinaRL("textures/gui/elements.png");

	public static void renderOverflowHidden(MatrixStack matrixStack, Consumer<MatrixStack> backgroundRenderer,
			Consumer<MatrixStack> innerRenderer) {
		matrixStack.pushPose();
		RenderSystem.enableDepthTest();
		matrixStack.translate(0, 0, 950);
		RenderSystem.colorMask(false, false, false, false);
		AbstractGui.fill(matrixStack, 4680, 2260, -4680, -2260, 0xff_000000);
		RenderSystem.colorMask(true, true, true, true);
		matrixStack.translate(0, 0, -950);
		RenderSystem.depthFunc(GL11.GL_GEQUAL);
		backgroundRenderer.accept(matrixStack);
		RenderSystem.depthFunc(GL11.GL_LEQUAL);
		innerRenderer.accept(matrixStack);
		RenderSystem.depthFunc(GL11.GL_GEQUAL);
		matrixStack.translate(0, 0, -950);
		RenderSystem.colorMask(false, false, false, false);
		AbstractGui.fill(matrixStack, 4680, 2260, -4680, -2260, 0xff_000000);
		RenderSystem.colorMask(true, true, true, true);
		matrixStack.translate(0, 0, 950);
		RenderSystem.depthFunc(GL11.GL_LEQUAL);
		RenderSystem.disableDepthTest();
		matrixStack.popPose();
	}

	public static void renderContainerBorder(MatrixStack matrixStack, Rectangle rec, Screen s) {
		s.getMinecraft().getTextureManager().bind(ELEMENTS);
		RenderSystem.enableBlend();

		s.blit(matrixStack, rec.x0 - 9, rec.y0 - 18, 0, 0, 15, 24);
		s.blit(matrixStack, rec.x1 - 7, rec.y0 - 18, 18, 0, 15, 24);
		s.blit(matrixStack, rec.x0 - 9, rec.y1 - 7, 0, 27, 15, 16);
		s.blit(matrixStack, rec.x1 - 7, rec.y1 - 7, 18, 27, 15, 16);

		matrixStack.pushPose();
		matrixStack.translate(rec.x0 + 6, rec.y0 - 18, 0);
		matrixStack.scale(rec.x1 - rec.x0 - 13, 1, 1);
		s.blit(matrixStack, 0, 0, 16, 0, 1, 24);
		matrixStack.translate(0, rec.y1 - rec.y0 + 11, 0);
		s.blit(matrixStack, 0, 0, 16, 27, 1, 16);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(rec.x0 - 9, rec.y0 + 6, 0);
		matrixStack.scale(1, rec.y1 - rec.y0 - 13, 1);
		s.blit(matrixStack, 0, 0, 0, 25, 15, 1);
		matrixStack.translate(rec.x1 - rec.x0 + 2, 0, 0);
		s.blit(matrixStack, 0, 0, 18, 25, 15, 1);
		matrixStack.popPose();
	}

	public static void drawStringWithBorder(MatrixStack matrixStack, String text, float x, float y, int color,
			int borderColor) {
		Minecraft mc = Minecraft.getInstance();

		mc.font.draw(matrixStack, text, x - 1, y, borderColor);
		mc.font.draw(matrixStack, text, x + 1, y, borderColor);
		mc.font.draw(matrixStack, text, x, y - 1, borderColor);
		mc.font.draw(matrixStack, text, x, y + 1, borderColor);
		mc.font.draw(matrixStack, text, x, y, color);
	}
}
