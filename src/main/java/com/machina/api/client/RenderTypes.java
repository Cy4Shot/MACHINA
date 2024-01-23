package com.machina.api.client;

import java.util.function.Function;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard.CullStateShard;
import net.minecraft.client.renderer.RenderStateShard.LightmapStateShard;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderStateShard.TransparencyStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import team.lodestar.lodestone.handlers.RenderHandler;

public class RenderTypes {

	public static boolean LARGER_BUFFER_SOURCES = ModList.get().isLoaded("rubidium");

	public static final TransparencyStateShard CELESTIAL_TRANSPARENCY = new TransparencyStateShard(
			"celestial_transparency", () -> {
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			}, () -> {
				RenderSystem.disableBlend();
				RenderSystem.defaultBlendFunc();
			});

	//@formatter:off
	public static final Function<ResourceLocation, RenderType> CELESTIAL = 
			tex -> createGenericRenderType("machina:starchart_celestial",
					DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
					VertexFormat.Mode.TRIANGLES,
					RenderType.CompositeState.builder()
							.setShaderState(new ShaderStateShard(GameRenderer::getRendertypeSolidShader))
							.setTransparencyState(CELESTIAL_TRANSPARENCY)
							.setTextureState(new TextureStateShard(tex, false, false))
							.setLightmapState(new LightmapStateShard(true))
							.setCullState(new CullStateShard(true)));
	//@formatter:on

	public static RenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode,
			RenderType.CompositeState.CompositeStateBuilder builder) {
		int size = LARGER_BUFFER_SOURCES ? 262144 : 256;
		RenderType type = RenderType.create(name, format, mode, size, false, false, builder.createCompositeState(true));
		RenderHandler.addRenderType(type);
		return type;
	}
}
