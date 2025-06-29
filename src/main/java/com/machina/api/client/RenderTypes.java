package com.machina.api.client;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.joml.Matrix4f;

import com.machina.api.client.shader.ShaderHandler;
import com.machina.api.util.MachinaRL;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard.CullStateShard;
import net.minecraft.client.renderer.RenderStateShard.DepthTestStateShard;
import net.minecraft.client.renderer.RenderStateShard.LightmapStateShard;
import net.minecraft.client.renderer.RenderStateShard.OverlayStateShard;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderStateShard.TexturingStateShard;
import net.minecraft.client.renderer.RenderStateShard.TransparencyStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.client.renderer.RenderType.CompositeState.CompositeStateBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.setup.LodestoneShaderRegistry;

@OnlyIn(Dist.CLIENT)
public class RenderTypes {

	// Sodium compat
	public static final boolean LARGER_BUFFER_SOURCES = ModList.get().isLoaded("rubidium");
	private static final Minecraft mc = Minecraft.getInstance();

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

	public static final TransparencyStateShard CONSTRUCT_TRANSPARENCY = new TransparencyStateShard(
			"construct_transparency", () -> {
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			}, () -> {
				RenderSystem.disableBlend();
				RenderSystem.defaultBlendFunc();
			});

	public static final TransparencyStateShard NO_TRANSPARENCY = new TransparencyStateShard("not_transparent", () -> {
		RenderSystem.disableBlend();
	}, () -> {
	});

	public static final TexturingStateShard GLINT_TEXTURING = new TexturingStateShard("glint_texturing", () -> {
		long i = (long) ((double) Util.getMillis() * mc.options.glintSpeed().get() * 8.0D);
		float f = (float) (i % 110000L) / 110000.0F;
		float f1 = (float) (i % 30000L) / 30000.0F;
		Matrix4f matrix4f = (new Matrix4f()).translation(-f, f1, 0.0F);
		matrix4f.rotateZ(0.17453292F).scale(0.5f);
		RenderSystem.setTextureMatrix(matrix4f);
	}, () -> {
		RenderSystem.resetTextureMatrix();
	});

	//@formatter:off
	private static final Function<ResourceLocation, RenderType> CELESTIAL = 
			tex -> create("machina:starchart_celestial",
					DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, Mode.TRIANGLES,
					true, false,
					CompositeState.builder()
							.setShaderState(new ShaderStateShard(GameRenderer::getRendertypeSolidShader))
							.setTransparencyState(CELESTIAL_TRANSPARENCY)
							.setTextureState(new TextureStateShard(tex, false, false))
							.setLightmapState(new LightmapStateShard(true))
							.setCullState(new CullStateShard(true)));
			
	public static final RenderType ORBIT = create("machina:orbit",
			DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, Mode.DEBUG_LINES,
			true, false,
			CompositeState.builder()
				.setShaderState(LodestoneShaderRegistry.TRIANGLE_TEXTURE.getShard())
				.setTransparencyState(ORBIT_TRANSPARENCY)
				.setTextureState(new TextureStateShard(new MachinaRL("textures/gui/starchart/white.png"), false, false))
				.setLightmapState(new LightmapStateShard(true))
				.setCullState(new CullStateShard(false)));
	
	public static final RenderType CONSTRUCT = create(
		    "machina:rocket_construct",
		    DefaultVertexFormat.NEW_ENTITY,
		    VertexFormat.Mode.QUADS,
		    true, true,
		    CompositeState.builder()
		    	.setShaderState(new ShaderStateShard(new Supplier<ShaderInstance>() {
		    		
		    		// We can't use anonymous lambda because of a Java bug.
					@Override
					public ShaderInstance get() {
						return ShaderHandler.ROCKET_PART_BENCH.instance();
					}
				}))
		        .setTextureState(new TextureStateShard(
		            new MachinaRL("textures/rocket/constructing.png"),
		            false,
		            false
		        ))
		        .setTexturingState(GLINT_TEXTURING)
		        .setTransparencyState(CONSTRUCT_TRANSPARENCY)
		        .setLightmapState(new LightmapStateShard(true))
		        .setOverlayState(new OverlayStateShard(true))
		        .setDepthTestState(new DepthTestStateShard("<=", 515))
		        .setCullState(new CullStateShard(true))
		);
	//@formatter:on

	// Use a map to avoid creating the same render type twice.
	private static final Map<String, RenderType> CELESTIALS = new HashMap<>();

	public static RenderType getOrCreateCelestial(String tex) {
		return CELESTIALS.computeIfAbsent(tex,
				t -> CELESTIAL.apply(new MachinaRL("textures/gui/starchart/" + t + ".png")));
	}

	public static RenderType create(String name, VertexFormat format, Mode mode, boolean affectsOutline,
			boolean sorting, CompositeStateBuilder builder) {
		int size = LARGER_BUFFER_SOURCES ? 262144 : 256;
		RenderType type = RenderType.create(name, format, mode, size, sorting, false,
				builder.createCompositeState(affectsOutline));
		RenderHandler.addRenderType(type);
		return type;
	}
}
