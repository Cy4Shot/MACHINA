package com.machina.api.client.screen;

import java.nio.IntBuffer;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import com.machina.Machina;
import com.machina.api.multiblock.ClientMultiblock;
import com.machina.api.multiblock.MultiblockLoader;
import com.machina.api.rocket.RocketPart;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.math.VecUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Axis;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fluids.FluidStack;

public final class MUI {

	public static final int CYAN = 0x00FEFE;
	public static final int RED = 0xFE0000;
	public static final int GREEN = 0x00FE00;
	public static final int WHITE = 0xFFFFFF;

	private static final Minecraft mc = Minecraft.getInstance();

	private static final ResourceLocation JEI_UI = new MachinaRL("textures/gui/jei_ui.png");
	private static final ResourceLocation COMMON_UI = new MachinaRL("textures/gui/common_ui.png");
	private static final ResourceLocation ROCKET_UI = new MachinaRL("textures/gui/rocket_ui.png");
	private static final ResourceLocation BG_OVERLAY = new MachinaRL("textures/gui/bg_overlay.png");

	public static MutableComponent uistr(String key) {
		return Component.translatable("gui.machina." + key);
	}

	public static String uistrs(String key) {
		return Component.translatable("gui.machina." + key).getString();
	}

	public static void blitCommon(GuiGraphics gui, int x, int y, int u, int v, int w, int h) {
		gui.blit(COMMON_UI, x, y, u, v, w, h, 512, 512);
	}

	public static void blitRocket(GuiGraphics gui, int x, int y, int u, int v, int w, int h) {
		gui.blit(ROCKET_UI, x, y, u, v, w, h, 512, 512);
	}

	public static void blitJei(GuiGraphics gui, int x, int y, int u, int v, int w, int h) {
		gui.blit(JEI_UI, x, y, u, v, w, h, 256, 256);
	}

	public static void blitOverlay(GuiGraphics gui, int x, int y, int w, int h) {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(1f, 1f, 1f, 0.1f);
		gui.blit(BG_OVERLAY, x, y, 0, 0, w, h, 512, 512);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	}

	public static void drawString(GuiGraphics gui, Component text, int x, int y) {
		drawString(gui, text, x, y, CYAN);
	}

	public static void drawString(GuiGraphics gui, Component text, int x, int y, int color) {
		gui.drawString(mc.font, text, x, y, color);
	}

	public static void drawCenteredString(GuiGraphics gui, Component text, int x, int y) {
		drawCenteredString(gui, text, x, y, CYAN);
	}

	public static void drawCenteredString(GuiGraphics gui, Component text, int x, int y, int color) {
		gui.drawCenteredString(mc.font, text, x, y, color);
	}

	public static void drawCenteredMultilineString(GuiGraphics gui, Component text, int x, int y, int color, int max,
			int sep) {
		List<FormattedCharSequence> seq = mc.font.split(text, max);
		for (int i = 0; i < seq.size(); i++) {
			gui.drawCenteredString(mc.font, seq.get(i), x, y + i * sep, color);
		}
	}

	public static void drawSlot(GuiGraphics gui, int i, int j, int mx, int my, boolean decoHorizontal,
			boolean decoVertical) {
		int h = mx > i && mx < i + 18 && my > j && my < j + 18 ? 113 : 94;
		blitCommon(gui, i, j, 466, h, 19, 19);

		if (decoHorizontal) {
			blitCommon(gui, i - 6, j + 1, 387, 0, 3, 16);
			blitCommon(gui, i + 21, j + 1, 390, 0, 3, 16);
		}

		if (decoVertical) {
			blitCommon(gui, i + 1, j - 4, 480, 80, 16, 3);
			blitCommon(gui, i + 1, j + 20, 480, 83, 16, 3);
		}
	}

	public static void drawStringVertical(GuiGraphics gui, Component text, int x, int y) {
		gui.pose().pushPose();
		gui.pose().translate(x, y, 0);
		gui.pose().mulPose(VecUtil.rotationDegrees(VecUtil.ZP, 90));
		gui.drawString(mc.font, text, 0, 0, 65278);
		gui.pose().popPose();
	}

	public enum MuiSlot {
		PLUS(475, 0),
		MINUS(485, 0),
		RIGHT(495, 0),
		DOWN(475, 10),
		UP(485, 10),
		LEFT(495, 10),
		ENERGY(499, 23),
		CROSS(499, 33),
		COAL(499, 43),
		DUST(499, 53),
		WHITELIST(499, 63),
		BLACKLIST(499, 73),
		FLUID(499, 83),
		TICK(499, 93),
		TIME(499, 103),
		TEMP(499, 113),
		PLATE(499, 123),
		ROD(499, 133);

		private final int x;
		private final int y;

		MuiSlot(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void draw(GuiGraphics gui, int x, int y) {
			blitCommon(gui, x, y, this.x, this.y, 10, 10);
		}

		public void draw(GuiGraphics gui, int x, int y, long aliveTicks) {
			if (appearDraw(aliveTicks))
				draw(gui, x, y);
		}
	}

	public static void drawBar(GuiGraphics gui, int i, int j, float p, boolean active, String text, String missing,
			TriConsumer<Integer, Integer, Float> drawer) {
		// Bar
		blitCommon(gui, i, j, 366, 21, 133, 18);
		drawer.accept(i, j, p);

		// Deco
		int dec_off = active ? 0 : 6;
		blitCommon(gui, i - 5, j + 2, 387 + dec_off, 0, 3, 16);
		blitCommon(gui, i + 135, j + 2, 390 + dec_off, 0, 3, 16);

		if (!active) {
			gui.drawCenteredString(mc.font, uistr(missing), i + 66, j + 6, RED);
		}

		dec_off = active ? 0 : 38;
		Component c = Component.literal(text);
		int w = mc.font.width(c) / 2 + 2;
		gui.drawCenteredString(mc.font, c, i + 66, j + 20, active ? CYAN : RED);
		blitCommon(gui, i + 66 + w, j + 18, 418 + dec_off, 5, 19, 8);
		blitCommon(gui, i + 66 - w - 20, j + 18, 399 + dec_off, 5, 19, 8);
	}

	public static void drawBarSmall(GuiGraphics gui, int i, int j, float p, boolean active, String text, String missing,
			TriConsumer<Integer, Integer, Float> drawer) {
		// Bar
		blitCommon(gui, i, j, 433, 136, 43, 16);
		drawer.accept(i, j, p);

		// Deco
		int dec_off = active ? 0 : 6;
		blitCommon(gui, i - 5, j, 387 + dec_off, 0, 3, 16);
		blitCommon(gui, i + 45, j, 390 + dec_off, 0, 3, 16);

		if (!active) {
			gui.drawCenteredString(mc.font, uistr(missing), i + 66, j + 6, RED);
		}
	}

	public static void drawBarVert(GuiGraphics gui, int i, int j, float p,
			TriConsumer<Integer, Integer, Float> drawer) {
		// Bar
		blitCommon(gui, i, j, 414, 152, 16, 42);
		drawer.accept(i, j, p);

		// Deco
		blitCommon(gui, i, j - 5, 480, 80, 16, 3);
		blitCommon(gui, i, j + 44, 480, 83, 16, 3);
	}

	public static void drawOverlay(GuiGraphics gui, int w, int h, long aliveTicks) {
		int height = h + 4;

		int k = (int) (aliveTicks / 3 % 4);
		int cw = w / 512;
		int ch = height / 512;
		int tw = cw * 512;
		int th = ch * 512;

		for (int i = 0; i < cw; i++) {
			for (int j = 0; j < ch; j++) {
				blitOverlay(gui, i * 512, j * 512 + k, 512, 512);
			}
		}

		for (int i = 0; i < cw; i++) {
			blitOverlay(gui, i * 512, th + k, 512, height - th);
		}

		for (int j = 0; j < ch; j++) {
			blitOverlay(gui, tw, j * 512 + k, w - tw, 512);
		}

		blitOverlay(gui, tw, th + k, w - tw, height - th);
	}

	public static void drawMultiblock(GuiGraphics gui, ResourceLocation mbloc, int xPos, int yPos, float rotX,
			float rotY, int s, float pt) {
		ClientMultiblock mb = new ClientMultiblock(MultiblockLoader.INSTANCE.get(mbloc));
		Vec3i size = mb.mb.size;
		int sizeX = size.getX();
		int sizeY = size.getY();
		int sizeZ = size.getZ();
		float maxX = 90;
		float maxY = 90;
		float diag = (float) Math.sqrt(sizeX * sizeX + sizeZ * sizeZ);
		float scaleX = maxX / diag;
		float scaleY = maxY / sizeY;
		float scale = -Math.min(scaleX, scaleY) * s;

		gui.pose().pushPose();
		gui.pose().translate(xPos, yPos, 100);
		gui.pose().scale(scale, scale, scale);
		gui.pose().translate(-(float) sizeX / 2, -(float) sizeY / 2, 0);
		Matrix4f rotMat = new Matrix4f();
		rotMat.identity();
		gui.pose().mulPose(VecUtil.rotationDegrees(VecUtil.XP, rotX - 30F));
		rotMat.rotate(VecUtil.rotationDegrees(VecUtil.XP, 30F - rotX));

		float offX = (float) -sizeX / 2;
		float offZ = (float) -sizeZ / 2 + 1;
		gui.pose().translate(-offX, 0, -offZ);
		gui.pose().mulPose(VecUtil.rotationDegrees(VecUtil.YP, 45F - rotY));
		rotMat.rotate(VecUtil.rotationDegrees(VecUtil.YP, rotY - 45F));
		gui.pose().translate(offX, 0, offZ);

		renderElements(gui.pose(), mb, size, pt, pos -> false, rotX < 30F);

		gui.pose().popPose();
	}

	private static BufferSource mbBuffers = null;

	private static void renderElements(PoseStack ms, ClientMultiblock mb, Vec3i dest, float par,
			Predicate<BlockPos> transparency, boolean flip) {
		if (mbBuffers == null) {
			mbBuffers = initBuffers(mc.renderBuffers().bufferSource());
		}

		BufferSource buffers = mc.renderBuffers().bufferSource();

		ms.pushPose();
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		ms.translate(0, 0, -1);

		doWorldRenderPass(ms, mbBuffers, buffers, mb, dest, transparency, flip);
		mbBuffers.endBatch();
		buffers.endBatch();

		ms.popPose();
	}

	private static void doWorldRenderPass(PoseStack ms, @Nonnull BufferSource tpBuffers,
			@Nonnull BufferSource nmBuffers, ClientMultiblock mb, Vec3i dest, Predicate<BlockPos> transparency,
			boolean flip) {
		boolean last = false;
		for (int y = 0; y < dest.getY(); y++) {
			for (int x = 0; x < dest.getX(); x++) {
				for (int z = 0; z < dest.getZ(); z++) {
					BlockPos pos = new BlockPos(x, flip ? y : dest.getY() - y - 1, z);
					boolean tp = !transparency.test(pos);
					if (last != tp) {
						(last ? nmBuffers : tpBuffers).endBatch();
					}
					mb = mb.restrict(has -> tp != transparency.test(has));
					BlockState bs = mb.getBlockState(pos);

					ms.pushPose();
					ms.translate(pos.getX(), pos.getY(), pos.getZ());
					for (RenderType layer : RenderType.chunkBufferLayers()) {
						VertexConsumer buffer = (tp ? nmBuffers : tpBuffers).getBuffer(layer);
						Vec3 vector3d = bs.getOffset(mb, pos);
						ms.translate(vector3d.x, vector3d.y, vector3d.z);
						BakedModel model = mc.getBlockRenderer().getBlockModel(bs);
						ModelData modelData = model.getModelData(mb, pos, bs, ModelData.EMPTY);
						mc.getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffer, bs, model, pos.getX(),
								pos.getY(), pos.getZ(), 255, OverlayTexture.NO_OVERLAY, modelData, layer);
					}
					ms.popPose();
					last = tp;
				}
			}
		}
	}

	private static BufferSource initBuffers(BufferSource original) {
		Map<RenderType, BufferBuilder> remapped = new Object2ObjectLinkedOpenHashMap<>();
		for (Map.Entry<RenderType, BufferBuilder> e : original.fixedBuffers.entrySet()) {
			remapped.put(MultiblockRenderType.remap(e.getKey(), (float) 0.2), e.getValue());
		}
		return new MultiblockBuffers(original.builder, remapped);
	}

	private static class MultiblockBuffers extends BufferSource {

		private final float alpha;

		protected MultiblockBuffers(BufferBuilder fallback, Map<RenderType, BufferBuilder> layerBuffers) {
			super(fallback, layerBuffers);
			this.alpha = (float) 0.2;
		}

		@Override
		public @NotNull VertexConsumer getBuffer(@NotNull RenderType type) {
			return super.getBuffer(MultiblockRenderType.remap(type, alpha));
		}
	}

	private static class MultiblockRenderType extends RenderType {
		private static final Map<RenderType, RenderType> remappedTypes = new IdentityHashMap<>();

		private MultiblockRenderType(RenderType original, float alpha) {
			super(String.format("%s_%s_multiblock", original.toString(), Machina.MOD_ID), original.format(),
					original.mode(), original.bufferSize(), original.affectsCrumbling(), true, () -> {
						original.setupRenderState();

						RenderSystem.disableDepthTest();
						RenderSystem.enableBlend();
						RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA,
								GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
						RenderSystem.setShaderColor(1, 1, 1, alpha);
					}, () -> {
						RenderSystem.setShaderColor(1, 1, 1, 1);
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

	public static void enableClipping(int x, int y, int w, int h) {
		double scale = mc.getWindow().getGuiScale();
		RenderSystem.enableScissor((int) (x * scale), (int) (mc.getWindow().getHeight() - (y + h) * scale), (int) (w * scale), (int) (h * scale));
	}

	public static void disableClipping() {
		RenderSystem.disableScissor();
	}

	public static void renderItem(GuiGraphics gui, int i, int j, int mx, int my, boolean tooltip, ItemStack stack) {
		gui.renderItem(stack, i, j);
		gui.renderItemDecorations(mc.font, stack, i, j, String.valueOf(stack.getCount()));

		if (tooltip && mx > i && mx < i + 16 && my > j && my < j + 16) {
			gui.renderTooltip(mc.font, stack, mx, my);
		}
	}

	public static void renderFluid(GuiGraphics gui, FluidStack fluid, int x, int y, int sx, int sy, int blit) {
		if (!fluid.isEmpty()) {
			TextureAtlasSprite icon = getFluidTexture(fluid);
			if (icon != null) {
				color(IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid));
				drawTiledSprite(gui, x, y, 0, sx, sy, icon, 16, 16, 0, TilingDirection.DOWN_RIGHT);
				resetColor();
			}
		}
	}

	// Mekanism
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

	// https://github.com/mekanism/Mekanism/blob/160d59e8d4b11aec446fc4d7d84b9f01dba5da68/src/main/java/mekanism/client/gui/GuiUtils.java
	public static void drawTiledSprite(GuiGraphics gui, int xPosition, int yPosition, int yOffset, int desiredWidth,
			int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel,
			TilingDirection tilingDirection) {
		drawTiledSprite(gui, xPosition, yPosition, yOffset, desiredWidth, desiredHeight, sprite, textureWidth,
				textureHeight, zLevel, tilingDirection, true);
	}

	// https://github.com/mekanism/Mekanism/blob/160d59e8d4b11aec446fc4d7d84b9f01dba5da68/src/main/java/mekanism/client/gui/GuiUtils.java
	public static void drawTiledSprite(GuiGraphics guiGraphics, int xPosition, int yPosition, int yOffset,
			int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight,
			int zLevel, TilingDirection tilingDirection, boolean blend) {
		if (desiredWidth == 0 || desiredHeight == 0 || textureWidth == 0 || textureHeight == 0) {
			return;
		}
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, sprite.atlasLocation());
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
		if (blend) {
			RenderSystem.enableBlend();
		}
		BufferBuilder vertexBuffer = Tesselator.getInstance().getBuilder();
		vertexBuffer.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		Matrix4f matrix4f = guiGraphics.pose().last().pose();
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
		BufferUploader.drawWithShader(vertexBuffer.end());
		if (blend) {
			RenderSystem.disableBlend();
		}
	}

	public static TextureAtlasSprite getFluidTexture(@Nonnull FluidStack stack) {
		return getSprite(IClientFluidTypeExtensions.of(stack.getFluid()).getStillTexture());
	}

	public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
		return mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(spriteLocation);
	}

	public static void rocketPart(GuiGraphics gui, int x, int y, double scale, float yaw, float pitch,
			RocketPart<?> part) {
		// PoseStack for GUI overlay
		PoseStack vs = RenderSystem.getModelViewStack();
		vs.pushPose();

		// Apply GUI pose first
		vs.mulPoseMatrix(gui.pose().last().pose());
		vs.scale((float) scale, (float) scale, (float) scale);
		vs.translate(x / scale, y / scale, 50.0F);

		RenderSystem.applyModelViewMatrix();

		// PoseStack for model orientation
		PoseStack ps = new PoseStack();
		ps.mulPose(Axis.XN.rotationDegrees(pitch));
		ps.mulPose(Axis.YP.rotationDegrees(yaw));

		MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		EntityRenderDispatcher disp = Minecraft.getInstance().getEntityRenderDispatcher();
		disp.setRenderShadow(false);

		RenderSystem.runAsFancy(() -> {
			part.bake().render(ps, buffer, 15728880, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
		});

		buffer.endBatch();
		disp.setRenderShadow(true);

		vs.popPose();
		RenderSystem.applyModelViewMatrix();
	}

	public static void color(int color) {
		float r = getRed(color);
		float g = getGreen(color);
		float b = getBlue(color);
		float a = getAlpha(color);
		RenderSystem.setShaderColor(r, g, b, a);
	}

	public static void resetColor() {
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}

	public static float getRed(int color) {
		return FastColor.ARGB32.red(color) / 255.0F;
	}

	public static float getGreen(int color) {
		return FastColor.ARGB32.green(color) / 255.0F;
	}

	public static float getBlue(int color) {
		return FastColor.ARGB32.blue(color) / 255.0F;
	}

	public static float getAlpha(int color) {
		return FastColor.ARGB32.alpha(color) / 255.0F;
	}

	private static boolean appearDraw(long elap) {
		return elap > 9 || elap == 5 || elap == 7 || elap == 8;
	}

	public static void click() {
		mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
}
