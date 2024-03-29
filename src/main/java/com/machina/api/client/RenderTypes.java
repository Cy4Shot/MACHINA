package com.machina.api.client;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.machina.api.util.MachinaRL;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard.CullStateShard;
import net.minecraft.client.renderer.RenderStateShard.LightmapStateShard;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderStateShard.TransparencyStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.client.renderer.RenderType.CompositeState.CompositeStateBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.setup.LodestoneShaderRegistry;

public class RenderTypes {

	// Sodium compat
	public static boolean LARGER_BUFFER_SOURCES = ModList.get().isLoaded("rubidium");

	public static final TransparencyStateShard CELESTIAL_TRANSPARENCY = new TransparencyStateShard(
			"celestial_transparency", () -> {
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			}, () -> {
				RenderSystem.disableBlend();
				RenderSystem.defaultBlendFunc();
			});

	public static final TransparencyStateShard ORBIT_TRANSPARENCY = new TransparencyStateShard("orbit_transparency",
			() -> {
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
			}, () -> {
				RenderSystem.disableBlend();
				RenderSystem.defaultBlendFunc();
			});

	//@formatter:off
	private static final Function<ResourceLocation, RenderType> CELESTIAL = 
			tex -> create("machina:starchart_celestial",
					DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, Mode.TRIANGLES,
					CompositeState.builder()
							.setShaderState(new ShaderStateShard(GameRenderer::getRendertypeSolidShader))
							.setTransparencyState(CELESTIAL_TRANSPARENCY)
							.setTextureState(new TextureStateShard(tex, false, false))
							.setLightmapState(new LightmapStateShard(true))
							.setCullState(new CullStateShard(true)));
			
	public static final RenderType ORBIT = create("machina:orbit",
			DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, Mode.DEBUG_LINES,
			CompositeState.builder()
				.setShaderState(LodestoneShaderRegistry.TRIANGLE_TEXTURE.getShard())
				.setTransparencyState(ORBIT_TRANSPARENCY)
				.setTextureState(new TextureStateShard(new MachinaRL("textures/gui/starchart/white.png"), false, false))
				.setLightmapState(new LightmapStateShard(true))
				.setCullState(new CullStateShard(false)));
	//@formatter:on

	// Use a map to avoid creating the same render type twice.
	private static Map<String, RenderType> CELESTIALS = new HashMap<>();

	public static RenderType getOrCreateCelestial(String tex) {
		return CELESTIALS.computeIfAbsent(tex,
				t -> CELESTIAL.apply(new MachinaRL("textures/gui/starchart/" + t + ".png")));
	}

	public static RenderType create(String name, VertexFormat format, Mode mode, CompositeStateBuilder builder) {
		int size = LARGER_BUFFER_SOURCES ? 262144 : 256;
		RenderType type = RenderType.create(name, format, mode, size, false, false, builder.createCompositeState(true));
		RenderHandler.addRenderType(type);
		return type;
	}
}
