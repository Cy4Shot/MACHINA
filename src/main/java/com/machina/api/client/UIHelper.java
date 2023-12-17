package com.machina.api.client;

import java.util.function.Consumer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.machina.api.util.MachinaRL;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class UIHelper {

	public static final Minecraft mc = Minecraft.getInstance();

	public static final ResourceLocation TEX_STARS = new MachinaRL("textures/gui/stars_bg.png");

	public static void bindStars() {
		RenderSystem.setShaderTexture(0, TEX_STARS);
	}

	public static void drawStringShadow(GuiGraphics gui, String text, float x, float y, int col) {
		gui.drawString(mc.font, text, (int) x + 2, (int) y + 2, 0);
		gui.drawString(mc.font, text, (int) x, (int) y, col);
	}

	public static void drawStringShadow(GuiGraphics gui, Component text, float x, float y, int col) {
		gui.drawString(mc.font, text, (int) x + 2, (int) y + 2, 0);
		gui.drawString(mc.font, text, (int) x, (int) y, col);
	}

	public static void blit(PoseStack ms, int x, int y, int uOff, int vOff, int w, int h) {
		betterBlit(ms, x, y, uOff, vOff, w, h, 256);
	}

	public static void betterBlit(PoseStack ms, float x, float y, float uOff, float vOff, float w, float h, float tex) {
		innerBlit(ms.last().pose(), x, x + w, y, y + h, 0f, uOff / tex, (uOff + w) / tex, vOff / tex, (vOff + h) / tex);
	}

	public static void betterBlit(PoseStack ms, float x, float y, float uOff, float vOff, float w, float h, float texX,
			float texY) {
		innerBlit(ms.last().pose(), x, x + w, y, y + h, 0f, uOff / texX, (uOff + w) / texX, vOff / texY,
				(vOff + h) / texY);
	}

	public static void blitTransp(PoseStack ms, float x, float y, float uOff, float vOff, float w, float h, float tex) {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		betterBlit(ms, x, y, uOff, vOff, w, h, tex);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	}

	public static float blitOffset = 0f;

	private static void innerBlit(Matrix4f pMatrix, float pX1, float pX2, float pY1, float pY2, float pBlitOffset,
			float pMinU, float pMaxU, float pMinV, float pMaxV) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.vertex(pMatrix, pX1, pY2, pBlitOffset + blitOffset).uv(pMinU, pMaxV).endVertex();
		bufferbuilder.vertex(pMatrix, pX2, pY2, pBlitOffset + blitOffset).uv(pMaxU, pMaxV).endVertex();
		bufferbuilder.vertex(pMatrix, pX2, pY1, pBlitOffset + blitOffset).uv(pMaxU, pMinV).endVertex();
		bufferbuilder.vertex(pMatrix, pX1, pY1, pBlitOffset + blitOffset).uv(pMinU, pMinV).endVertex();
		BufferUploader.drawWithShader(bufferbuilder.end());
	}

	public static void sizedBlitTransp(PoseStack poseStack, float x, float y, float width, float height, float srcX,
			float srcY, float srcWidth, float srcHeight, float tex) {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(1f, 1F, 1F, 1f);
		sizedBlit(poseStack, x, y, width, height, srcX, srcY, srcWidth, srcHeight, tex);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	}

	public static void sizedBlit(PoseStack poseStack, float x, float y, float width, float height, float srcX,
			float srcY, float srcW, float srcH, float tex) {
		Matrix4f pose = poseStack.last().pose();
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.vertex(pose, x, y + height, blitOffset).uv((srcX / tex), (srcY + srcH) / tex).endVertex();
		bufferbuilder.vertex(pose, x + width, y + height, blitOffset).uv((srcX + srcW) / tex, (srcY + srcH) / tex)
				.endVertex();
		bufferbuilder.vertex(pose, x + width, y, blitOffset).uv((srcX + srcW) / tex, srcY / tex).endVertex();
		bufferbuilder.vertex(pose, x, y, blitOffset).uv(srcX / tex, srcY / tex).endVertex();
		BufferUploader.drawWithShader(bufferbuilder.end());
	}

	public static void renderOverflowHidden(GuiGraphics gui, Consumer<PoseStack> bg) {
		gui.pose().pushPose();
		RenderSystem.enableDepthTest();
		gui.pose().translate(0, 0, 950);
		RenderSystem.colorMask(false, false, false, false);
		gui.fill(4680, 2260, -4680, -2260, 0xff_000000);
		RenderSystem.colorMask(true, true, true, true);
		gui.pose().translate(0, 0, -950);
		RenderSystem.depthFunc(GL11.GL_GEQUAL);
		bg.accept(gui.pose());
		gui.pose().translate(0, 0, -950);
		RenderSystem.colorMask(false, false, false, false);
		gui.fill(4680, 2260, -4680, -2260, 0xff_000000);
		RenderSystem.colorMask(true, true, true, true);
		gui.pose().translate(0, 0, 950);
		RenderSystem.depthFunc(GL11.GL_LEQUAL);
		RenderSystem.disableDepthTest();
		gui.pose().popPose();
	}

	@SuppressWarnings("resource")
	public static void drawLines(GuiGraphics gui, double[] lines, int col) {
		PoseStack poseStack = gui.pose();
		float f3 = (col >> 24 & 255) / 255.0F;
		float f = (col >> 16 & 255) / 255.0F;
		float f1 = (col >> 8 & 255) / 255.0F;
		float f2 = (col & 255) / 255.0F;

		poseStack.pushPose();
		float a = (float) gui.guiHeight() / gui.guiWidth();
		float s = 2f / gui.guiWidth();
		BufferBuilder buffer = Tesselator.getInstance().getBuilder();
		VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
		buffer.begin(Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

		for (int i = 0; i < lines.length; i += 2) {
			buffer.vertex(lines[i] * a * s, lines[i + 1] * s, 0).color(f, f1, f2, f3).endVertex();
		}

		vertexBuffer.bind();
		vertexBuffer.upload(buffer.end());
		Matrix4f mat = poseStack.last().pose();
		vertexBuffer.drawWithShader(mat, mat, GameRenderer.getPositionColorShader());
		poseStack.popPose();
		VertexBuffer.unbind();
	}

	public static double[] orbit(float cx, float cy, float width, float eccentricity, float angle, int resolution) {
		return ellipse(cx, cy, width, width * (float) Math.sqrt(1 - eccentricity * eccentricity), resolution, angle);
	}

	public static double[] ellipse(float cX, float cY, float rX, float rY, int sides, float angle) {
		double[] buf = new double[sides * 4 + 4];
		float xn = 0, yn = 0;

		for (int i = 0; i < (sides + 1); i++) {
			xn = (float) (Math.sin(Math.toRadians(angle)) * rX);
			yn = (float) (Math.cos(Math.toRadians(angle)) * rY);
			if (i == 0) {
				buf[0] = cX + xn;
				buf[1] = cY + yn;
			} else {
				buf[i * 4 - 2] = cX + xn;
				buf[i * 4 - 1] = cY + yn;
				buf[i * 4] = cX + xn;
				buf[i * 4 + 1] = cY + yn;
			}
			angle += (360f / sides);
		}

		buf[sides * 4 + 2] = cX + xn;
		buf[sides * 4 + 3] = cY + yn;

		return buf;
	}
}
