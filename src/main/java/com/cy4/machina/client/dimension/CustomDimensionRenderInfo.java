package com.cy4.machina.client.dimension;

import org.lwjgl.opengl.GL11;

import com.cy4.machina.Machina;
import com.cy4.machina.client.QuadRenderingUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.ICloudRenderHandler;
import net.minecraftforge.client.ISkyRenderHandler;
import net.minecraftforge.client.IWeatherParticleRenderHandler;
import net.minecraftforge.client.IWeatherRenderHandler;

public class CustomDimensionRenderInfo extends DimensionRenderInfo {

	public static void registerDimensionRenderInfo() {
		DimensionRenderInfo.EFFECTS.put(Machina.MACHINA_ID, new CustomDimensionRenderInfo());
	}

	public CustomDimensionRenderInfo() {
		super(Float.NaN, false, FogType.NONE, true, false);

		this.setSkyRenderHandler(new CustomSkyRenderer());
		this.setCloudRenderHandler(new CustomCloudRenderer());
		this.setWeatherRenderHandler(new CustomWeatherRenderer());
		this.setWeatherParticleRenderHandler(new CustomWeatherParticleRenderer());
	}

	@Override
	public Vector3d getBrightnessDependentFogColor(Vector3d color, float scale) {
		return color.scale(scale);
	}

	@Override
	public boolean isFoggyAt(int x, int y) {
		return true;
	}

	// No idea what the inputs are.
	@Override
	public float[] getSunriseColor(float p_230492_1_, float p_230492_2_) {
		return null;
	}

	public class CustomSkyRenderer implements ISkyRenderHandler {

		private VertexBuffer sky;
		private VertexBuffer fog;

		public CustomSkyRenderer() {
			sky = QuadRenderingUtils.createBuffer(sky, (bb) -> QuadRenderingUtils.makeCube(bb, 100));
			fog = QuadRenderingUtils.createBuffer(fog, (bb) -> QuadRenderingUtils.makeCylinder(bb, 7, 10, 100));
		}
		

		@Override
		public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc) {
			TextureManager tm = mc.getTextureManager();
			int currTicks = mc.levelRenderer.ticks;
			float time = (currTicks % 360000) * 0.000017453292F;
			
			RenderSystem.enableTexture();
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(516, 0.0F);
			GL11.glEnable(GL11.GL_BLEND);
			RenderSystem.depthMask(false);

			tm.bind(new ResourceLocation(Machina.MOD_ID, "textures/environment/sky/starfield01.png"));
			QuadRenderingUtils.renderBuffer(matrixStack, sky, DefaultVertexFormats.POSITION_TEX, 1f, 1f, 1f, 1f);
			
			matrixStack.pushPose();
			matrixStack.last().pose().multiply(new Quaternion(0, -time * 4, 0, false));
			tm.bind(new ResourceLocation("textures/block/white_concrete.png"));
			QuadRenderingUtils.renderBuffer(matrixStack, fog, DefaultVertexFormats.POSITION_TEX, 1f, 1f, 1f, 1f);
			matrixStack.popPose();
			
			RenderSystem.depthMask(true);
		}
	}

	public class CustomCloudRenderer implements ICloudRenderHandler {

		@Override
		public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc,
				double viewEntityX, double viewEntityY, double viewEntityZ) {
		}
	}

	public class CustomWeatherRenderer implements IWeatherRenderHandler {

		@Override
		public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc, LightTexture lightmapIn,
				double xIn, double yIn, double zIn) {
		}
	}

	public class CustomWeatherParticleRenderer implements IWeatherParticleRenderHandler {

		@Override
		public void render(int ticks, ClientWorld world, Minecraft mc, ActiveRenderInfo activeRenderInfoIn) {
			
		}
	}
}
