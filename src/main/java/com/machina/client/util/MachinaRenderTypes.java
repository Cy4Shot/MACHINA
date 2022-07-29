package com.machina.client.util;

import java.util.function.Consumer;

import com.machina.util.text.MachinaRL;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Util;

public class MachinaRenderTypes {

	public static final RenderType PREVIEW_TYPE = RenderType.create("machina_preview", DefaultVertexFormats.BLOCK, 7,
			131072, true, true,
			RenderType.State.builder()
					.setTextureState(new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS, false, false))
					.setTransparencyState(new RenderState.TransparencyState("translucent_transparency", () -> {
						RenderSystem.enableBlend();
						RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
								GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
								GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
					}, () -> {
						RenderSystem.disableBlend();
						RenderSystem.defaultBlendFunc();
					})).setDiffuseLightingState(new RenderState.DiffuseLightingState(true))
					.setAlphaState(new RenderState.AlphaState(0.004F)).setCullState(new RenderState.CullState(false))
					.setLightmapState(new RenderState.LightmapState(true))
					.setOverlayState(new RenderState.OverlayState(true)).createCompositeState(true));

	public static final RenderType ROCKET_CONSTRUCT = RenderType.create("rocket_construct",
			DefaultVertexFormats.POSITION_TEX, 7, 256,
			RenderType.State.builder()
					.setTextureState(new RenderState.TextureState(new MachinaRL("textures/rocket/constructing.png"),
							true, false))
					.setWriteMaskState(new RenderState.WriteMaskState(true, false))
					.setCullState(new RenderState.CullState(false))
					.setDepthTestState(new RenderState.DepthTestState("<=", 515))
					.setTransparencyState(new RenderState.TransparencyState("glint_transparency", () -> {
						RenderSystem.enableBlend();
						RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR,
								GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ZERO,
								GlStateManager.DestFactor.ONE);
					}, () -> {
						RenderSystem.disableBlend();
						RenderSystem.defaultBlendFunc();
					})).setTexturingState(new RenderState.TexturingState("entity_glint_texturing", () -> {
						RenderSystem.matrixMode(5890);
						RenderSystem.pushMatrix();
						RenderSystem.loadIdentity();
						long i = Util.getMillis() * 8L;
						float f = (float) (i % 110000L) / 110000.0F;
						float f1 = (float) (i % 30000L) / 30000.0F;
						RenderSystem.translatef(-f, f1, 0.0F);
						RenderSystem.rotatef(10.0F, 0.0F, 0.0F, 1.0F);
						RenderSystem.scalef(0.16f, 0.16f, 0.16f);
						RenderSystem.matrixMode(5888);
					}, () -> {
						RenderSystem.matrixMode(5890);
						RenderSystem.popMatrix();
						RenderSystem.matrixMode(5888);
					})).createCompositeState(false));

	public static final RenderType ROCKET_MISSING = RenderType.create("rocket_missing",
			DefaultVertexFormats.POSITION_TEX, 7, 256,
			RenderType.State.builder()
					.setTextureState(
							new RenderState.TextureState(new MachinaRL("textures/rocket/missing.png"), true, false))
					.setWriteMaskState(new RenderState.WriteMaskState(true, false))
					.setCullState(new RenderState.CullState(false))
					.setDepthTestState(new RenderState.DepthTestState("<=", 515))
					.setTransparencyState(new RenderState.TransparencyState("glint_transparency", () -> {
						RenderSystem.enableBlend();
						RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR,
								GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ZERO,
								GlStateManager.DestFactor.ONE);
					}, () -> {
						RenderSystem.disableBlend();
						RenderSystem.defaultBlendFunc();
					})).setTexturingState(new RenderState.TexturingState("entity_glint_texturing", () -> {
						RenderSystem.matrixMode(5890);
						RenderSystem.pushMatrix();
						RenderSystem.loadIdentity();
						long i = Util.getMillis() * 8L;
						float f = (float) (i % 110000L) / 110000.0F;
						float f1 = (float) (i % 30000L) / 30000.0F;
						RenderSystem.translatef(-f, f1, 0.0F);
						RenderSystem.rotatef(10.0F, 0.0F, 0.0F, 1.0F);
						RenderSystem.scalef(0.16f, 0.16f, 0.16f);
						RenderSystem.matrixMode(5888);
					}, () -> {
						RenderSystem.matrixMode(5890);
						RenderSystem.popMatrix();
						RenderSystem.matrixMode(5888);
					})).createCompositeState(false));

	public static void doWithType(RenderType type, Consumer<IVertexBuilder> code) {
		IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().renderBuffers().bufferSource();
		IVertexBuilder buffer = buffers.getBuffer(type);
		code.accept(buffer);
		buffers.endBatch(type);
	}
}