package com.machina.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.ItemRenderer;
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

	public static final RenderType ROCKET_PREVIEW = RenderType.create("rocket_glint", DefaultVertexFormats.POSITION_TEX,
			7, 256,
			RenderType.State.builder()
					.setTextureState(new RenderState.TextureState(ItemRenderer.ENCHANT_GLINT_LOCATION, true, false))
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
}