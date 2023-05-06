package com.machina.client.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.machina.Machina;
import com.machina.multiblock.ClientMultiblock;
import com.machina.util.Color;
import com.machina.util.MachinaRL;
import com.machina.util.StringUtils;
import com.machina.util.math.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.text2speech.Narrator;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.CharacterManager;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@OnlyIn(Dist.CLIENT)
public class UIHelper {

	public enum StippleType {
		NONE((short) 0x0000),
		FULL((short) 0xFFFF),
		DOTTED((short) 0x0101),
		DASHED((short) 0x00FF),
		DOT_DASH((short) 0x1C47);

		public final short code;

		StippleType(final short code) {
			this.code = code;
		}
	}

	public static final MachinaRL SCIFI_EL = new MachinaRL("textures/gui/scifi_el.png");
	public static final MachinaRL TRMNL_EL = new MachinaRL("textures/gui/trmnl_el.png");
	public static final MachinaRL STCHT_EL = new MachinaRL("textures/gui/stcht_el.png");
	public static final MachinaRL LARGE_EL = new MachinaRL("textures/gui/large_el.png");
	public static final MachinaRL PRGRS_EL = new MachinaRL("textures/gui/prgrs_el.png");
	public static final MachinaRL BLPRT_EL = new MachinaRL("textures/gui/blprt_el.png");
	public static final MachinaRL STARS_BG = new MachinaRL("textures/gui/stars_bg.png");
	private static Minecraft mc = Minecraft.getInstance();
	private static TextureManager tm = mc.getTextureManager();

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

	public static int getWidth(String text) {
		return mc.font.width(text);
	}

	public static int getWidth(IReorderingProcessor text) {
		return mc.font.width(text);
	}

	public static int getWidth(ITextComponent text) {
		return mc.font.width(text);
	}

	public static void drawCenteredStringWithBorder(MatrixStack matrixStack, String text, float x, float y, int color,
			int borderColor) {
		drawStringWithBorder(matrixStack, text, x - getWidth(text) / 2, y, color, borderColor);
	}

	public static int drawStringWithBorder(MatrixStack matrixStack, String text, float x, float y, int color,
			int borderColor) {
		mc.font.draw(matrixStack, text, x - 1, y, borderColor);
		mc.font.draw(matrixStack, text, x + 1, y, borderColor);
		mc.font.draw(matrixStack, text, x, y - 1, borderColor);
		mc.font.draw(matrixStack, text, x, y + 1, borderColor);
		return mc.font.draw(matrixStack, text, x, y, color) + 2;
	}

	public static void drawCenteredStringWithBorder(MatrixStack matrixStack, IReorderingProcessor text, float x,
			float y, int color, int borderColor) {
		drawStringWithBorder(matrixStack, text, x - getWidth(text) / 2, y, color, borderColor);
	}

	public static int drawStringWithBorder(MatrixStack matrixStack, IReorderingProcessor text, float x, float y,
			int color, int borderColor) {
		mc.font.draw(matrixStack, text, x - 1, y, borderColor);
		mc.font.draw(matrixStack, text, x + 1, y, borderColor);
		mc.font.draw(matrixStack, text, x, y - 1, borderColor);
		mc.font.draw(matrixStack, text, x, y + 1, borderColor);
		return mc.font.draw(matrixStack, text, x, y, color) + 2;
	}

	public static <T extends IFormattableTextComponent> void drawCenteredStringWithBorder(MatrixStack matrixStack,
			T text, float x, float y, int color, int borderColor) {
		drawStringWithBorder(matrixStack, text, x - getWidth(text) / 2, y, color, borderColor);
	}

	public static <T extends IFormattableTextComponent> int drawStringWithBorder(MatrixStack matrixStack, T text,
			float x, float y, int color, int borderColor) {
		mc.font.draw(matrixStack, text.getString(), x - 1, y, borderColor);
		mc.font.draw(matrixStack, text.getString(), x + 1, y, borderColor);
		mc.font.draw(matrixStack, text.getString(), x, y - 1, borderColor);
		mc.font.draw(matrixStack, text.getString(), x, y + 1, borderColor);
		return mc.font.draw(matrixStack, text, x, y, color) + 2;
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

	public static void innerLine(Matrix4f pMatrix, float pMinX, float pMinY, float pMaxX, float pMaxY, int pColor,
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

	public static void polygon(MatrixStack pPoseStack, float cX, float cY, float r, int edges, int pColor, float width,
			StippleType stippleType) {
		polygon(pPoseStack.last().pose(), cX, cY, r, edges, pColor, width, stippleType);
	}

	private static void polygon(Matrix4f pMatrix, float cX, float cY, float r, int sides, int pColor, float width,
			StippleType stippleType) {
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
		for (int i = 0; i < sides; i++) {
			bufferbuilder
					.vertex(pMatrix, (float) (cX + (Math.sin(2 * Math.PI * i / sides) * r)),
							(float) (cY - (Math.cos(2 * Math.PI * i / sides) * r)), 0.0F)
					.color(f, f1, f2, f3).endVertex();
		}
		bufferbuilder.vertex(pMatrix, (float) (cX + (Math.sin(0) * r)), (float) (cY - (Math.cos(0) * r)), 0.0F)
				.color(f, f1, f2, f3).endVertex();
		bufferbuilder.end();

		WorldVertexBufferUploader.end(bufferbuilder);
		GL11.glDisable(GL11.GL_LINE_STIPPLE);
		GL11.glLineStipple(1, StippleType.FULL.code);
		RenderSystem.lineWidth(1f);
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
	}

	public static void ellipse(MatrixStack stack, float cX, float cY, float rX, float rY, int sides, float angle,
			int col, float width, StippleType type) {

		float f3 = (col >> 24 & 255) / 255.0F;
		float f = (col >> 16 & 255) / 255.0F;
		float f1 = (col >> 8 & 255) / 255.0F;
		float f2 = (col & 255) / 255.0F;

		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.defaultBlendFunc();
		RenderSystem.lineWidth(width);
		GL11.glLineStipple(1, type.code);
		GL11.glEnable(GL11.GL_LINE_STIPPLE);

		bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

		float xo = 0;
		float yo = 0;
		float xn = 0;
		float yn = 0;

		for (int i = 0; i < (sides + 1); i++) {
			xn = (float) (Math.sin(Math.toRadians(angle)) * rX);
			yn = (float) (Math.cos(Math.toRadians(angle)) * rY);
			if (i > 0) {
				bufferbuilder.vertex(cX + xo, cY + yo, 0F).color(f, f1, f2, f3).endVertex();
				if (i == sides) {
					bufferbuilder.vertex(cX + xn, cY + yn, 0F).color(f, f1, f2, f3).endVertex();
				}
			}
			xo = xn;
			yo = yn;
			angle += (360f / sides);
		}

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

	public static void betterBlit(MatrixStack ms, float x, float y, float uOff, float vOff, float w, float h,
			float texX, float texY) {
		innerBlit(ms.last().pose(), x, x + w, y, y + h, 0f, uOff / texX, (uOff + w) / texX, vOff / texY,
				(vOff + h) / texY);
	}

	public static void blitTransp(MatrixStack ms, float x, float y, float uOff, float vOff, float w, float h,
			float tex) {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.blendColor(1f, 1f, 1f, 1f);
		betterBlit(ms, x, y, uOff, vOff, w, h, tex);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	}

	public static float blitOffset = 0f;

	private static void innerBlit(Matrix4f pMatrix, float pX1, float pX2, float pY1, float pY2, float pBlitOffset,
			float pMinU, float pMaxU, float pMinV, float pMaxV) {
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.vertex(pMatrix, pX1, pY2, pBlitOffset + blitOffset).uv(pMinU, pMaxV).endVertex();
		bufferbuilder.vertex(pMatrix, pX2, pY2, pBlitOffset + blitOffset).uv(pMaxU, pMaxV).endVertex();
		bufferbuilder.vertex(pMatrix, pX2, pY1, pBlitOffset + blitOffset).uv(pMaxU, pMinV).endVertex();
		bufferbuilder.vertex(pMatrix, pX1, pY1, pBlitOffset + blitOffset).uv(pMinU, pMinV).endVertex();
		bufferbuilder.end();
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.end(bufferbuilder);
	}

	public static void sizedBlitTransp(MatrixStack poseStack, float x, float y, float width, float height, float srcX,
			float srcY, float srcWidth, float srcHeight, float tex) {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.blendColor(1f, 1f, 1f, 1f);
		sizedBlit(poseStack, x, y, width, height, srcX, srcY, srcWidth, srcHeight, tex);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	}

	public static void sizedBlit(MatrixStack poseStack, float x, float y, float width, float height, float srcX,
			float srcY, float srcW, float srcH, float tex) {
		Matrix4f pose = poseStack.last().pose();
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.vertex(pose, x, y + height, blitOffset).uv((srcX / tex), (srcY + srcH) / tex).endVertex();
		bufferbuilder.vertex(pose, x + width, y + height, blitOffset).uv((srcX + srcW) / tex, (srcY + srcH) / tex)
				.endVertex();
		bufferbuilder.vertex(pose, x + width, y, blitOffset).uv((srcX + srcW) / tex, srcY / tex).endVertex();
		bufferbuilder.vertex(pose, x, y, blitOffset).uv(srcX / tex, srcY / tex).endVertex();
		bufferbuilder.end();
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.end(bufferbuilder);
	}

	public static int renderWrappedText(MatrixStack matrixStack, IFormattableTextComponent text, int maxWidth,
			int padding) {
		FontRenderer fontRenderer = mc.font;

		List<ITextProperties> lines = getLines(TextComponentUtils.mergeStyles(text.copy(), text.getStyle()),
				maxWidth - 3 * padding);
		List<IReorderingProcessor> processors = LanguageMap.getInstance().getVisualOrder(lines);

		for (int i = 0; i < processors.size(); i++) {
			fontRenderer.draw(matrixStack, processors.get(i), padding, (10 * i) + padding, 0xFF_192022);
		}

		return processors.size();
	}

	private static final int[] LINE_BREAK_VALUES = new int[] { 0, 10, -10, 25, -25 };

	private static List<ITextProperties> getLines(ITextComponent component, int maxWidth) {
		Minecraft minecraft = Minecraft.getInstance();

		CharacterManager charactermanager = minecraft.font.getSplitter();
		List<ITextProperties> list = null;
		float f = Float.MAX_VALUE;

		for (int i : LINE_BREAK_VALUES) {
			List<ITextProperties> list1 = charactermanager.splitLines(component, maxWidth - i, Style.EMPTY);
			float f1 = Math.abs(getTextWidth(charactermanager, list1) - maxWidth);
			if (f1 <= 10.0F) {
				return list1;
			}

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

	public static void renderItem(ItemStack stack, float x, float y) {
		tryRenderGuiItem((LivingEntity) null, stack, x, y);
	}

	public static void renderTintedItem(MatrixStack m, ItemStack stack, int x, int y, int r, int g, int b,
			float alpha) {
		mc.getItemRenderer().renderAndDecorateFakeItem(stack, x, y);
		RenderSystem.depthFunc(516);
		AbstractGui.fill(m, x, y, x + 16, y + 16, Color.getIntFromColor(r, g, b, (int) (alpha * 255f)));
		RenderSystem.depthFunc(515);

	}

	public static void withAlpha(Runnable run, float alpha) {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.color4f(1f, 1f, 1f, alpha);
		run.run();
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	}

	public static void renderFluid(MatrixStack m, FluidStack fluid, int x, int y, int sx, int sy, int fx, int fy,
			int blit, int pX, int pY, boolean showSize) {
		if (fluid.getFluid() != Fluids.EMPTY) {
			TextureAtlasSprite icon = getFluidTexture(fluid, FluidType.STILL);
			if (icon != null) {
				color(fluid);
				drawTiledSprite(m, x, y + fy - sy, sy - 1, sx - 1, sy - 1, icon, 16, 16, blit, TilingDirection.UP_LEFT);
				resetColor();

				if (pX > x - 1 && pX < x - 1 + fx && pY > y - 1 && pY < y - 1 + fy) {
					int color = new Color(fluid.getFluid().getAttributes().getColor()).maxBrightness().toInt();
					renderLabel(
							m, showSize
									? Arrays.asList(fluid.getDisplayName(), StringUtils
											.toComp(MathUtil.engineering((float) fluid.getAmount() / 1000f, "B"))
											.setStyle(Style.EMPTY
													.withColor(net.minecraft.util.text.Color.fromRgb(0xFF_00fefe))))
									: Collections.singletonList(fluid.getDisplayName()),
							pX, pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc, color);
				}
			}
		}
	}

	// https://github.com/mekanism/Mekanism/blob/160d59e8d4b11aec446fc4d7d84b9f01dba5da68/src/main/java/mekanism/client/gui/GuiUtils.java
	public static void drawTiledSprite(MatrixStack matrix, int xPosition, int yPosition, int yOffset, int desiredWidth,
			int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel,
			TilingDirection tilingDirection) {
		drawTiledSprite(matrix, xPosition, yPosition, yOffset, desiredWidth, desiredHeight, sprite, textureWidth,
				textureHeight, zLevel, tilingDirection, true);
	}

	// https://github.com/mekanism/Mekanism/blob/160d59e8d4b11aec446fc4d7d84b9f01dba5da68/src/main/java/mekanism/client/gui/GuiUtils.java
	public static void drawTiledSprite(MatrixStack matrix, int xPosition, int yPosition, int yOffset, int desiredWidth,
			int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel,
			TilingDirection tilingDirection, boolean blendAlpha) {
		if (desiredWidth == 0 || desiredHeight == 0 || textureWidth == 0 || textureHeight == 0) {
			return;
		}
		tm.bind(AtlasTexture.LOCATION_BLOCKS);
		int xTileCount = desiredWidth / textureWidth;
		int xRemainder = desiredWidth - (xTileCount * textureWidth);
		int yTileCount = desiredHeight / textureHeight;
		int yRemainder = desiredHeight - (yTileCount * textureHeight);
		int yStart = yPosition + yOffset;
		float uMin = sprite.getU0();
		float uMax = sprite.getU1();
		float vMin = sprite.getV0();
		float vMax = sprite.getV1();
		float uDif = uMax - uMin;
		float vDif = vMax - vMin;
		if (blendAlpha) {
			RenderSystem.enableBlend();
			RenderSystem.enableAlphaTest();
		}
		BufferBuilder vertexBuffer = Tessellator.getInstance().getBuilder();
		vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		Matrix4f matrix4f = matrix.last().pose();
		for (int xTile = 0; xTile <= xTileCount; xTile++) {
			int width = (xTile == xTileCount) ? xRemainder : textureWidth;
			if (width == 0) {
				break;
			}
			int x = xPosition + (xTile * textureWidth);
			int maskRight = textureWidth - width;
			int shiftedX = x + textureWidth - maskRight;
			float uLocalDif = uDif * maskRight / textureWidth;
			float uLocalMin;
			float uLocalMax;
			if (tilingDirection.right) {
				uLocalMin = uMin;
				uLocalMax = uMax - uLocalDif;
			} else {
				uLocalMin = uMin + uLocalDif;
				uLocalMax = uMax;
			}
			for (int yTile = 0; yTile <= yTileCount; yTile++) {
				int height = (yTile == yTileCount) ? yRemainder : textureHeight;
				if (height == 0) {
					break;
				}
				int y = yStart - ((yTile + 1) * textureHeight);
				int maskTop = textureHeight - height;
				float vLocalDif = vDif * maskTop / textureHeight;
				float vLocalMin;
				float vLocalMax;
				if (tilingDirection.down) {
					vLocalMin = vMin;
					vLocalMax = vMax - vLocalDif;
				} else {
					vLocalMin = vMin + vLocalDif;
					vLocalMax = vMax;
				}
				vertexBuffer.vertex(matrix4f, x, y + textureHeight, zLevel).uv(uLocalMin, vLocalMax).endVertex();
				vertexBuffer.vertex(matrix4f, shiftedX, y + textureHeight, zLevel).uv(uLocalMax, vLocalMax).endVertex();
				vertexBuffer.vertex(matrix4f, shiftedX, y + maskTop, zLevel).uv(uLocalMax, vLocalMin).endVertex();
				vertexBuffer.vertex(matrix4f, x, y + maskTop, zLevel).uv(uLocalMin, vLocalMin).endVertex();
			}
		}
		vertexBuffer.end();
		WorldVertexBufferUploader.end(vertexBuffer);
		if (blendAlpha) {
			RenderSystem.disableAlphaTest();
			RenderSystem.disableBlend();
		}
	}

	public static TextureAtlasSprite getFluidTexture(@Nonnull FluidStack fluidStack, @Nonnull FluidType type) {
		Fluid fluid = fluidStack.getFluid();
		ResourceLocation spriteLocation;
		if (type == FluidType.STILL) {
			spriteLocation = fluid.getAttributes().getStillTexture(fluidStack);
		} else {
			spriteLocation = fluid.getAttributes().getFlowingTexture(fluidStack);
		}
		return getSprite(spriteLocation);
	}

	public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
		return mc.getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(spriteLocation);
	}

	public static void resetColor() {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
	}

	public static void color(@Nonnull FluidStack fluid) {
		if (!fluid.isEmpty()) {
			color(fluid.getFluid().getAttributes().getColor(fluid));
		}
	}

	public static float getRed(int color) {
		return (color >> 16 & 0xFF) / 255.0F;
	}

	public static float getGreen(int color) {
		return (color >> 8 & 0xFF) / 255.0F;
	}

	public static float getBlue(int color) {
		return (color & 0xFF) / 255.0F;
	}

	public static float getAlpha(int color) {
		return (color >> 24 & 0xFF) / 255.0F;
	}

	public static void color(int color) {
		RenderSystem.color4f(getRed(color), getGreen(color), getBlue(color), getAlpha(color));
	}

	public static void click() {
		mc.getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	public static void bindScifi() {
		bind(SCIFI_EL);
	}

	public static void bindTrmnl() {
		bind(TRMNL_EL);
	}

	public static void bindStcht() {
		bind(STCHT_EL);
	}

	public static void bindStars() {
		bind(STARS_BG);
	}

	public static void bindLarge() {
		bind(LARGE_EL);
	}

	public static void bindPrgrs() {
		bind(PRGRS_EL);
	}

	public static void bindBlprt() {
		bind(BLPRT_EL);
	}

	public static void bind(ResourceLocation rl) {
		resetColor();
		mc.textureManager.bind(rl);
	}

	public static int levelTicks() {
		return mc.levelRenderer.ticks;
	}

	public static void close() {
		mc.forceSetScreen(null);
	}

	public static void renderLabel(MatrixStack stack, List<? extends ITextProperties> text, int mouseX, int mouseY,
			int bgCol, int borColStart, int borColEnd) {
		drawHoveringText(stack, text, mouseX, mouseY, mc.screen.width, mc.screen.height, -1, bgCol, borColStart,
				borColEnd, -1, mc.font);
	}

	public static void renderLabel(MatrixStack stack, List<? extends ITextProperties> text, int mouseX, int mouseY,
			int bgCol, int borColStart, int borColEnd, int fgCol) {
		drawHoveringText(stack, text, mouseX, mouseY, mc.screen.width, mc.screen.height, -1, bgCol, borColStart,
				borColEnd, fgCol, mc.font);
	}

	public static void renderUnboundLabel(MatrixStack stack, List<? extends ITextProperties> text, int mouseX,
			int mouseY, int bgCol, int borColStart, int borColEnd) {
		renderUnboundLabel(stack, text, mouseX, mouseY, bgCol, borColStart, borColEnd, -1, mc.font);
	}

	public static void renderUnboundLabel(MatrixStack stack, List<? extends ITextProperties> text, int mouseX,
			int mouseY, int backgroundColor, int borderColorStart, int borderColorEnd, int fgCol, FontRenderer font) {
		if (!text.isEmpty()) {

			RenderSystem.disableRescaleNormal();
			RenderSystem.disableDepthTest();
			int tooltipTextWidth = 0;

			for (ITextProperties textLine : text) {
				int textLineWidth = font.width(textLine);
				if (textLineWidth > tooltipTextWidth)
					tooltipTextWidth = textLineWidth;
			}

			int tooltipX = mouseX + 12;
			int tooltipY = mouseY - 12;
			int tooltipHeight = 8;

			if (text.size() > 1) {
				tooltipHeight += (text.size() - 1) * 10;
				if (text.size() > 1)
					tooltipHeight += 2; // gap between title lines and next lines
			}

			final int zLevel = 400;

			stack.pushPose();
			Matrix4f mat = stack.last().pose();
			drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3,
					backgroundColor, backgroundColor);
			drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3,
					tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
			drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3,
					tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
			drawGradientRect(mat, zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3,
					backgroundColor, backgroundColor);
			drawGradientRect(mat, zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3,
					tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
			drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1,
					tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
			drawGradientRect(mat, zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1,
					tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart,
					borderColorEnd);
			drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1,
					borderColorStart, borderColorStart);
			drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3,
					tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

			IRenderTypeBuffer.Impl renderType = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
			stack.translate(0.0D, 0.0D, zLevel);

			for (int lineNumber = 0; lineNumber < text.size(); ++lineNumber) {
				ITextProperties line = text.get(lineNumber);
				if (line != null)
					font.drawInBatch(LanguageMap.getInstance().getVisualOrder(line), (float) tooltipX, (float) tooltipY,
							fgCol, true, mat, renderType, false, 0, 15728880);

				if (lineNumber + 1 == 1)
					tooltipY += 2;

				tooltipY += 10;
			}

			renderType.endBatch();
			stack.popPose();

			RenderSystem.enableDepthTest();
			RenderSystem.enableRescaleNormal();
		}
	}

	public static void drawHoveringText(MatrixStack stack, List<? extends ITextProperties> text, int mouseX, int mouseY,
			int screenWidth, int screenHeight, int maxTextWidth, int backgroundColor, int borderColorStart,
			int borderColorEnd, int fgCol, FontRenderer font) {
		if (!text.isEmpty()) {

			RenderSystem.disableRescaleNormal();
			RenderSystem.disableDepthTest();
			int tooltipTextWidth = 0;

			for (ITextProperties textLine : text) {
				int textLineWidth = font.width(textLine);
				if (textLineWidth > tooltipTextWidth)
					tooltipTextWidth = textLineWidth;
			}

			boolean needsWrap = false;

			int titleLinesCount = 1;
			int tooltipX = mouseX + 12;
			if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
				tooltipX = mouseX - 16 - tooltipTextWidth;
				if (tooltipX < 4) // if the tooltip doesn't fit on the screen
				{
					if (mouseX > screenWidth / 2)
						tooltipTextWidth = mouseX - 12 - 8;
					else
						tooltipTextWidth = screenWidth - 16 - mouseX;
					needsWrap = true;
				}
			}

			if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
				tooltipTextWidth = maxTextWidth;
				needsWrap = true;
			}

			if (needsWrap) {
				int wrappedTooltipWidth = 0;
				List<ITextProperties> wrappedTextLines = new ArrayList<>();
				for (int i = 0; i < text.size(); i++) {
					ITextProperties textLine = text.get(i);
					List<ITextProperties> wrappedLine = font.getSplitter().splitLines(textLine, tooltipTextWidth,
							Style.EMPTY);
					if (i == 0)
						titleLinesCount = wrappedLine.size();

					for (ITextProperties line : wrappedLine) {
						int lineWidth = font.width(line);
						if (lineWidth > wrappedTooltipWidth)
							wrappedTooltipWidth = lineWidth;
						wrappedTextLines.add(line);
					}
				}
				tooltipTextWidth = wrappedTooltipWidth;
				text = wrappedTextLines;

				if (mouseX > screenWidth / 2)
					tooltipX = mouseX - 16 - tooltipTextWidth;
				else
					tooltipX = mouseX + 12;
			}

			int tooltipY = mouseY - 12;
			int tooltipHeight = 8;

			if (text.size() > 1) {
				tooltipHeight += (text.size() - 1) * 10;
				if (text.size() > titleLinesCount)
					tooltipHeight += 2; // gap between title lines and next lines
			}

			if (tooltipY < 4)
				tooltipY = 4;
			else if (tooltipY + tooltipHeight + 4 > screenHeight)
				tooltipY = screenHeight - tooltipHeight - 4;

			final int zLevel = 400;

			stack.pushPose();
			Matrix4f mat = stack.last().pose();
			drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3,
					backgroundColor, backgroundColor);
			drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3,
					tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
			drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3,
					tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
			drawGradientRect(mat, zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3,
					backgroundColor, backgroundColor);
			drawGradientRect(mat, zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3,
					tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
			drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1,
					tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
			drawGradientRect(mat, zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1,
					tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart,
					borderColorEnd);
			drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1,
					borderColorStart, borderColorStart);
			drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3,
					tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

			IRenderTypeBuffer.Impl renderType = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
			stack.translate(0.0D, 0.0D, zLevel);

			for (int lineNumber = 0; lineNumber < text.size(); ++lineNumber) {
				ITextProperties line = text.get(lineNumber);
				if (line != null)
					font.drawInBatch(LanguageMap.getInstance().getVisualOrder(line), (float) tooltipX, (float) tooltipY,
							fgCol, true, mat, renderType, false, 0, 15728880);

				if (lineNumber + 1 == titleLinesCount)
					tooltipY += 2;

				tooltipY += 10;
			}

			renderType.endBatch();
			stack.popPose();

			RenderSystem.enableDepthTest();
			RenderSystem.enableRescaleNormal();
		}
	}

	public static void drawGradientRect(Matrix4f mat, int zLevel, int left, int top, int right, int bottom,
			int startColor, int endColor) {
		float startAlpha = (float) (startColor >> 24 & 255) / 255.0F;
		float startRed = (float) (startColor >> 16 & 255) / 255.0F;
		float startGreen = (float) (startColor >> 8 & 255) / 255.0F;
		float startBlue = (float) (startColor & 255) / 255.0F;
		float endAlpha = (float) (endColor >> 24 & 255) / 255.0F;
		float endRed = (float) (endColor >> 16 & 255) / 255.0F;
		float endGreen = (float) (endColor >> 8 & 255) / 255.0F;
		float endBlue = (float) (endColor & 255) / 255.0F;

		RenderSystem.enableDepthTest();
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.shadeModel(GL11.GL_SMOOTH);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuilder();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		buffer.vertex(mat, right, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
		buffer.vertex(mat, left, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
		buffer.vertex(mat, left, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
		buffer.vertex(mat, right, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
		tessellator.end();

		RenderSystem.shadeModel(GL11.GL_FLAT);
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
	}

	// https://github.com/mekanism/Mekanism/blob/1.16.x/src/main/java/mekanism/client/render/MekanismRenderer.java
	public enum FluidType {
		STILL,
		FLOWING
	}

	// https://github.com/mekanism/Mekanism/blob/160d59e8d4b11aec446fc4d7d84b9f01dba5da68/src/main/java/mekanism/client/gui/GuiUtils.java
	public enum TilingDirection {
		DOWN_RIGHT(true, true),
		DOWN_LEFT(true, false),
		UP_RIGHT(false, true),
		UP_LEFT(false, false);

		private final boolean down;
		private final boolean right;

		TilingDirection(boolean down, boolean right) {
			this.down = down;
			this.right = right;
		}
	}

	public static void tts(String text) {
		Narrator.getNarrator().say(text, false);
	}

	public static void renderMultiblock(MatrixStack ms, ClientMultiblock mb, int xPos, int yPos, int s, float par,
			float rotY, float rotX, Predicate<BlockPos> transparency) {
		Vector3i size = mb.mb.size;
		int sizeX = size.getX();
		int sizeY = size.getY();
		int sizeZ = size.getZ();
		float maxX = 90;
		float maxY = 90;
		float diag = (float) Math.sqrt(sizeX * sizeX + sizeZ * sizeZ);
		float scaleX = maxX / diag;
		float scaleY = maxY / sizeY;
		float scale = -Math.min(scaleX, scaleY) * s;

		ms.pushPose();
		ms.translate(xPos, yPos, 100);
		ms.scale(scale, scale, scale);
		ms.translate(-(float) sizeX / 2, -(float) sizeY / 2, 0);
		Vector4f eye = new Vector4f(0, 0, -100, 1);
		Matrix4f rotMat = new Matrix4f();
		rotMat.setIdentity();
		ms.mulPose(Vector3f.XP.rotationDegrees(-rotX - 30F));
		rotMat.multiply(Vector3f.XP.rotationDegrees(rotX + 30));

		float offX = (float) -sizeX / 2;
		float offZ = (float) -sizeZ / 2 + 1;
		ms.translate(-offX, 0, -offZ);
		ms.mulPose(Vector3f.YP.rotationDegrees(rotY + 45));
		rotMat.multiply(Vector3f.YP.rotationDegrees(-rotY - 45));
		ms.translate(offX, 0, offZ);

		eye.transform(rotMat);
		eye.normalize();
		renderElements(ms, mb, size, eye, par, transparency, rotX > -30F);

		ms.popPose();
	}

	private static IRenderTypeBuffer.Impl mbBuffers = null;

	private static void renderElements(MatrixStack ms, ClientMultiblock mb, Vector3i dest, Vector4f eye, float par,
			Predicate<BlockPos> transparency, boolean flip) {
		if (mbBuffers == null) {
			mbBuffers = initBuffers(mc.renderBuffers().bufferSource(), 0.2f);
		}

		IRenderTypeBuffer.Impl buffers = mc.renderBuffers().bufferSource();

		ms.pushPose();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		ms.translate(0, 0, -1);

		doWorldRenderPass(ms, mbBuffers, buffers, mb, dest, eye, transparency, flip);
		mbBuffers.endBatch();
		buffers.endBatch();

		ms.popPose();
	}

	private static void doWorldRenderPass(MatrixStack ms, @Nonnull IRenderTypeBuffer.Impl tpBuffers,
			@Nonnull IRenderTypeBuffer.Impl nmBuffers, ClientMultiblock mb, Vector3i dest, Vector4f eye,
			Predicate<BlockPos> transparency, boolean flip) {
		Random rand = new Random();
		long seed = rand.nextLong();
		boolean last = false;
		for (int y = 0; y < dest.getY(); y++) {
			for (int x = 0; x < dest.getX(); x++) {
				for (int z = 0; z < dest.getZ(); z++) {
					BlockPos pos = new BlockPos(x, flip ? y : dest.getY() - y - 1, z);
					boolean tp = !transparency.test(pos);
					if (last != tp) {
						(last ? nmBuffers : tpBuffers).endBatch();
					}
					mb = mb.restrict(has -> tp ? !transparency.test(has) : transparency.test(has));
					BlockState bs = mb.getBlockState(pos);

					ms.pushPose();
					ms.translate(pos.getX(), pos.getY(), pos.getZ());
					for (RenderType layer : RenderType.chunkBufferLayers()) {
						if (RenderTypeLookup.canRenderInLayer(bs, layer)) {
							ForgeHooksClient.setRenderLayer(layer);
							IVertexBuilder buffer = (tp ? nmBuffers : tpBuffers).getBuffer(layer);
							Vector3d vector3d = bs.getOffset(mb, pos);
							ms.translate(vector3d.x, vector3d.y, vector3d.z);
							IBakedModel model = mc.getBlockRenderer().getBlockModel(bs);
							IModelData modelData = model.getModelData(mb, pos, bs, EmptyModelData.INSTANCE);
							mc.getBlockRenderer().getModelRenderer().renderModelFlat(mb, model, bs, pos, ms, buffer,
									!tp, rand, seed, OverlayTexture.NO_OVERLAY, modelData);
							ForgeHooksClient.setRenderLayer(null);
						}
					}
					ms.popPose();
					last = tp;
				}
			}
		}
	}

	private static IRenderTypeBuffer.Impl initBuffers(IRenderTypeBuffer.Impl original, float alpha) {
		BufferBuilder fallback = ObfuscationReflectionHelper.getPrivateValue(IRenderTypeBuffer.Impl.class, original,
				"field_228457_a_");
		Map<RenderType, BufferBuilder> layerBuffers = ObfuscationReflectionHelper
				.getPrivateValue(IRenderTypeBuffer.Impl.class, original, "field_228458_b_");
		Map<RenderType, BufferBuilder> remapped = new Object2ObjectLinkedOpenHashMap<>();
		for (Map.Entry<RenderType, BufferBuilder> e : layerBuffers.entrySet()) {
			remapped.put(MultiblockRenderType.remap(e.getKey(), alpha), e.getValue());
		}
		return new MultiblockBuffers(fallback, remapped, alpha);
	}

	private static class MultiblockBuffers extends IRenderTypeBuffer.Impl {

		private final float alpha;

		protected MultiblockBuffers(BufferBuilder fallback, Map<RenderType, BufferBuilder> layerBuffers, float alpha) {
			super(fallback, layerBuffers);
			this.alpha = alpha;
		}

		@Override
		public IVertexBuilder getBuffer(RenderType type) {
			return super.getBuffer(MultiblockRenderType.remap(type, alpha));
		}
	}

	private static class MultiblockRenderType extends RenderType {
		private static Map<RenderType, RenderType> remappedTypes = new IdentityHashMap<>();

		private MultiblockRenderType(RenderType original, float alpha) {
			super(String.format("%s_%s_multiblock", original.toString(), Machina.MOD_ID), original.format(),
					original.mode(), original.bufferSize(), original.affectsCrumbling(), true, () -> {
						original.setupRenderState();

						RenderSystem.disableDepthTest();
						RenderSystem.enableBlend();
						RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA,
								GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
						RenderSystem.blendColor(1, 1, 1, alpha);
					}, () -> {
						RenderSystem.blendColor(1, 1, 1, 1);
						RenderSystem.defaultBlendFunc();
						RenderSystem.disableBlend();
						RenderSystem.enableDepthTest();

						original.clearRenderState();
					});
		}

		@Override
		public boolean equals(@Nullable Object other) {
			return this == other;
		}

		@Override
		public int hashCode() {
			return System.identityHashCode(this);
		}

		public static RenderType remap(RenderType in, float alpha) {
			if (in instanceof MultiblockRenderType) {
				return in;
			} else {
				return remappedTypes.computeIfAbsent(in, a -> new MultiblockRenderType(a, alpha));
			}
		}
	}

	private static void tryRenderGuiItem(@Nullable LivingEntity p_239387_1_, ItemStack p_239387_2_, float p_239387_3_,
			float p_239387_4_) {
		if (!p_239387_2_.isEmpty()) {
			mc.getItemRenderer().blitOffset += 50f;
			try {
				renderGuiItem(p_239387_2_, p_239387_3_, p_239387_4_,
						mc.getItemRenderer().getModel(p_239387_2_, (World) null, p_239387_1_));
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
				CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
				crashreportcategory.setDetail("Item Type", () -> {
					return String.valueOf((Object) p_239387_2_.getItem());
				});
				crashreportcategory.setDetail("Registry Name",
						() -> String.valueOf(p_239387_2_.getItem().getRegistryName()));
				crashreportcategory.setDetail("Item Damage", () -> {
					return String.valueOf(p_239387_2_.getDamageValue());
				});
				crashreportcategory.setDetail("Item NBT", () -> {
					return String.valueOf((Object) p_239387_2_.getTag());
				});
				crashreportcategory.setDetail("Item Foil", () -> {
					return String.valueOf(p_239387_2_.hasFoil());
				});
				throw new ReportedException(crashreport);
			}
			mc.getItemRenderer().blitOffset -= 50f;
		}
	}

	private static void renderGuiItem(ItemStack pStack, float pX, float pY, IBakedModel pBakedmodel) {
		RenderSystem.pushMatrix();
		mc.textureManager.bind(AtlasTexture.LOCATION_BLOCKS);
		mc.textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS).setFilter(false, false);
		RenderSystem.enableRescaleNormal();
		RenderSystem.enableAlphaTest();
		RenderSystem.defaultAlphaFunc();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.translatef(pX, pY, mc.getItemRenderer().blitOffset);
		RenderSystem.translatef(8.0F, 8.0F, 0.0F);
		RenderSystem.scalef(1.0F, -1.0F, 1.0F);
		RenderSystem.scalef(16.0F, 16.0F, 16.0F);
		MatrixStack matrixstack = new MatrixStack();
		IRenderTypeBuffer.Impl irendertypebuffer$impl = mc.renderBuffers().bufferSource();
		boolean flag = !pBakedmodel.usesBlockLight();
		if (flag) {
			RenderHelper.setupForFlatItems();
		}

		mc.getItemRenderer().render(pStack, ItemCameraTransforms.TransformType.GUI, false, matrixstack,
				irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, pBakedmodel);
		irendertypebuffer$impl.endBatch();
		RenderSystem.enableDepthTest();
		if (flag) {
			RenderHelper.setupFor3DItems();
		}

		RenderSystem.disableAlphaTest();
		RenderSystem.disableRescaleNormal();
		RenderSystem.popMatrix();
	}
}
