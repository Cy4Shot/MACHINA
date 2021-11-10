package com.cy4.machina.client.dimension;

import org.lwjgl.opengl.GL11;

import com.cy4.machina.Machina;
import com.cy4.machina.client.RenderingUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
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

		public CustomSkyRenderer() {
			BufferBuilder bb = Tessellator.getInstance().getBuilder();
			sky = new VertexBuffer(DefaultVertexFormats.POSITION_TEX);
			RenderingUtils.makeCube(bb, 100);
			bb.end();
			sky.upload(bb);
		}
		

		@SuppressWarnings("resource")
		@Override
		public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc) {
			RenderSystem.enableTexture();
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(516, 0.0F);
			GL11.glEnable(GL11.GL_BLEND);
			RenderSystem.depthMask(false);

			matrixStack.pushPose();
			matrixStack.last().normal().mul(new Quaternion(0, 0, 0, false));
			mc.getTextureManager().bind(new ResourceLocation("textures/environment/end_sky.png"));
//			new DynamicTexture(new NativeImage(PixelFormat.RGBA, 100, 100, false)).bind();
			RenderSystem.color4f(252F / 255F, 144F / 255F, 3F / 255F, 1F); // Col
			sky.bind();
			DefaultVertexFormats.POSITION_TEX.setupBufferState(0L);
			sky.draw(matrixStack.last().pose(), GL11.GL_QUADS);
			VertexBuffer.unbind();
			DefaultVertexFormats.POSITION_TEX.clearBufferState();
			matrixStack.popPose();

			RenderSystem.enableTexture();
			RenderSystem.depthMask(true);
		}
	}

	public class CustomCloudRenderer implements ICloudRenderHandler {

		@Override
		public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc,
				double viewEntityX, double viewEntityY, double viewEntityZ) {
			// TODO
		}
	}

	public class CustomWeatherRenderer implements IWeatherRenderHandler {

		@Override
		public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc, LightTexture lightmapIn,
				double xIn, double yIn, double zIn) {
			// TODO
		}
	}

	public class CustomWeatherParticleRenderer implements IWeatherParticleRenderHandler {

		@Override
		public void render(int ticks, ClientWorld world, Minecraft mc, ActiveRenderInfo activeRenderInfoIn) {
			// TODO
		}
	}
}
