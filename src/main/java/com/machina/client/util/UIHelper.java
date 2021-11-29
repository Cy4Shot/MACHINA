/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.client.util;

import java.util.List;
import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import com.machina.api.client.util.Rectangle;
import com.machina.api.util.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.CharacterManager;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentUtils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIHelper {

	public enum StippleType {

		NONE((short) 0x0000), FULL((short) 0xFFFF), DOTTED((short) 0x0101), DASHED((short) 0x00FF),
		DOT_DASH((short) 0x1C47);

		public final short code;

		StippleType(final short code) {
			this.code = code;
		}
	}

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

	public static void renderContainerBorder(MatrixStack matrixStack, Rectangle rec) {
		Minecraft.getInstance().getTextureManager().bind(ELEMENTS);
		RenderSystem.enableBlend();

		blit(matrixStack, rec.x0 - 9, rec.y0 - 18, 0, 0, 15, 24);
		blit(matrixStack, rec.x1 - 7, rec.y0 - 18, 18, 0, 15, 24);
		blit(matrixStack, rec.x0 - 9, rec.y1 - 7, 0, 27, 15, 16);
		blit(matrixStack, rec.x1 - 7, rec.y1 - 7, 18, 27, 15, 16);

		matrixStack.pushPose();
		matrixStack.translate(rec.x0 + 6, rec.y0 - 18, 0);
		matrixStack.scale(rec.x1 - rec.x0 - 13, 1, 1);
		blit(matrixStack, 0, 0, 16, 0, 1, 24);
		matrixStack.translate(0, rec.y1 - rec.y0 + 11, 0);
		blit(matrixStack, 0, 0, 16, 27, 1, 16);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(rec.x0 - 9, rec.y0 + 6, 0);
		matrixStack.scale(1, rec.y1 - rec.y0 - 13, 1);
		blit(matrixStack, 0, 0, 0, 25, 15, 1);
		matrixStack.translate(rec.x1 - rec.x0 + 2, 0, 0);
		blit(matrixStack, 0, 0, 18, 25, 15, 1);
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

	public static void box(MatrixStack pPoseStack, float pMinX, float pMinY, float pMaxX, float pMaxY, int pColor,
			float width, StippleType stippleType) {
		innerLine(pPoseStack.last().pose(), pMinX, pMinY, pMinX, pMaxY, pColor, width, stippleType);
		innerLine(pPoseStack.last().pose(), pMinX, pMaxY, pMaxX, pMaxY, pColor, width, stippleType);
		innerLine(pPoseStack.last().pose(), pMaxX, pMaxY, pMaxX, pMinY, pColor, width, stippleType);
		innerLine(pPoseStack.last().pose(), pMaxX, pMinY, pMinX, pMinY, pColor, width, stippleType);
	}

	public static void line(MatrixStack pPoseStack, float pMinX, float pMinY, float pMaxX, float pMaxY, int pColor,
			float width, StippleType stippleType) {
		innerLine(pPoseStack.last().pose(), pMinX, pMinY, pMaxX, pMaxY, pColor, width, stippleType);
	}

	private static void innerLine(Matrix4f pMatrix, float pMinX, float pMinY, float pMaxX, float pMaxY, int pColor,
			float width, StippleType stippleType) {
		float f3 = (pColor >> 24 & 255) / 255.0F;
		float f = (pColor >> 16 & 255) / 255.0F;
		float f1 = (pColor >> 8 & 255) / 255.0F;
		float f2 = (pColor & 255) / 255.0F;

		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.defaultBlendFunc();
		RenderSystem.lineWidth(width);
		GL11.glLineStipple(1, stippleType.code);
		GL11.glEnable(GL11.GL_LINE_STIPPLE);

		bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.vertex(pMatrix, pMaxX, pMaxY, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.vertex(pMatrix, pMinX, pMinY, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.end();

		WorldVertexBufferUploader.end(bufferbuilder);
		GL11.glDisable(GL11.GL_LINE_STIPPLE);
		GL11.glLineStipple(1, StippleType.FULL.code);
		RenderSystem.lineWidth(1f);
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
	}

	public static void blit(MatrixStack ms, int x, int y, int uOff, int vOff, int w, int h) {
		betterBlit(ms, x, y, uOff, vOff, w, h, 256);
	}

	public static void betterBlit(MatrixStack ms, float x, float y, float uOff, float vOff, float w, float h,
			float tex) {
		innerBlit(ms.last().pose(), x, x + w, y, y + h, 0f, uOff / tex, (uOff + w) / tex, vOff / tex, (vOff + h) / tex);
	}

	private static void innerBlit(Matrix4f pMatrix, float pX1, float pX2, float pY1, float pY2, float pBlitOffset,
			float pMinU, float pMaxU, float pMinV, float pMaxV) {
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.vertex(pMatrix, pX1, pY2, pBlitOffset).uv(pMinU, pMaxV).endVertex();
		bufferbuilder.vertex(pMatrix, pX2, pY2, pBlitOffset).uv(pMaxU, pMaxV).endVertex();
		bufferbuilder.vertex(pMatrix, pX2, pY1, pBlitOffset).uv(pMaxU, pMinV).endVertex();
		bufferbuilder.vertex(pMatrix, pX1, pY1, pBlitOffset).uv(pMinU, pMinV).endVertex();
		bufferbuilder.end();
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.end(bufferbuilder);
	}

	public static int renderWrappedText(MatrixStack matrixStack, IFormattableTextComponent text, int maxWidth,
			int padding) {
		Minecraft minecraft = Minecraft.getInstance();
		FontRenderer fontRenderer = minecraft.font;

		List<ITextProperties> lines = getLines(TextComponentUtils.mergeStyles(text.copy(), text.getStyle()),
				maxWidth - 3 * padding);
		List<IReorderingProcessor> processors = LanguageMap.getInstance().getVisualOrder(lines);

		for (int i = 0; i < processors.size(); i++) {
			fontRenderer.draw(matrixStack, processors.get(i), padding, (10 * i) + padding, 0xFF_192022);
		}

		return processors.size();
	}

	private static final int[] LINE_BREAK_VALUES = new int[] {
			0, 10, -10, 25, -25
	};

	private static List<ITextProperties> getLines(ITextComponent component, int maxWidth) {
		Minecraft minecraft = Minecraft.getInstance();

		CharacterManager charactermanager = minecraft.font.getSplitter();
		List<ITextProperties> list = null;
		float f = Float.MAX_VALUE;

		for (int i : LINE_BREAK_VALUES) {
			List<ITextProperties> list1 = charactermanager.splitLines(component, maxWidth - i, Style.EMPTY);
			float f1 = Math.abs(getTextWidth(charactermanager, list1) - maxWidth);
			if (f1 <= 10.0F) { return list1; }

			if (f1 < f) {
				f = f1;
				list = list1;
			}
		}

		return list;
	}

	private static float getTextWidth(CharacterManager manager, List<ITextProperties> text) {
		return (float) text.stream().mapToDouble(manager::stringWidth).max().orElse(0.0D);
	}
}
