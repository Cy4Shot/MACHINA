package com.machina.client.dimension;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.machina.Machina;
import com.machina.client.ClientStarchart;
import com.machina.client.util.QuadBufferRenderer;
import com.machina.client.util.UIHelper;
import com.machina.util.math.MathUtil;
import com.machina.util.math.VecUtil;
import com.machina.util.server.PlanetHelper;
import com.machina.util.text.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ICloudRenderHandler;
import net.minecraftforge.client.ISkyRenderHandler;
import net.minecraftforge.client.IWeatherParticleRenderHandler;
import net.minecraftforge.client.IWeatherRenderHandler;

@OnlyIn(Dist.CLIENT)
public class MachinaDimRenderer extends DimensionRenderInfo {

	public static void registerDimensionRenderInfo() {
		Machina.LOGGER.warn("Registering Machina Planet Render Info.");
		DimensionRenderInfo.EFFECTS.put(Machina.MACHINA_ID, new MachinaDimRenderer());
	}

	public MachinaDimRenderer() {
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

	@Override
	public float[] getSunriseColor(float p_230492_1_, float p_230492_2_) {
		return null;
	}

	public static class CustomSkyRenderer implements ISkyRenderHandler {

		public static final ResourceLocation STARS = new MachinaRL("textures/environment/sky/sky.png");
		public static final ResourceLocation FOG = new MachinaRL("textures/environment/sky/fog.png");
		public static final ResourceLocation PLANETS = new MachinaRL("textures/gui/planets.png");

		private VertexBuffer sky;
		private VertexBuffer fog;
		private VertexBuffer stars;
		private final VertexFormat starFormat = DefaultVertexFormats.POSITION;

		public CustomSkyRenderer() {
			sky = QuadBufferRenderer.create(sky, bb -> QuadBufferRenderer.cube(bb, 100));
			fog = QuadBufferRenderer.create(fog, bb -> QuadBufferRenderer.cylinder(bb, 10, 70, 100));
			createStars();
		}

		private void createStars() {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuilder();
			if (stars != null) {
				stars.close();
			}

			stars = new VertexBuffer(starFormat);
			this.drawStars(bufferbuilder);
			bufferbuilder.end();
			stars.upload(bufferbuilder);
		}

		private void drawStars(BufferBuilder buffer) {
			Random random = new Random(10842L);
			buffer.begin(7, DefaultVertexFormats.POSITION);

			for (int i = 0; i < 3000; ++i) {
				double d0 = (double) (random.nextFloat() * 2.0F - 1.0F);
				double d1 = (double) (random.nextFloat() * 2.0F - 1.0F);
				double d2 = (double) (random.nextFloat() * 2.0F - 1.0F);
				double d3 = (double) (0.15F + random.nextFloat() * 0.1F);
				double d4 = d0 * d0 + d1 * d1 + d2 * d2;
				if (d4 < 1.0D && d4 > 0.01D) {
					d4 = 1.0D / Math.sqrt(d4);
					d0 = d0 * d4;
					d1 = d1 * d4;
					d2 = d2 * d4;
					double s = 100D + random.nextDouble() * 40D;
					double d5 = d0 * s;
					double d6 = d1 * s;
					double d7 = d2 * s;
					double d8 = Math.atan2(d0, d2);
					double d9 = Math.sin(d8);
					double d10 = Math.cos(d8);
					double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
					double d12 = Math.sin(d11);
					double d13 = Math.cos(d11);
					double d14 = random.nextDouble() * Math.PI * 2.0D;
					double d15 = Math.sin(d14);
					double d16 = Math.cos(d14);

					for (int j = 0; j < 4; ++j) {
						double d18 = (double) ((j & 2) - 1) * d3;
						double d19 = (double) ((j + 1 & 2) - 1) * d3;
						double d21 = d18 * d16 - d19 * d15;
						double d22 = d19 * d16 + d18 * d15;
						double d23 = d21 * d12 + 0.0D * d13;
						double d24 = 0.0D * d12 - d21 * d13;
						double d25 = d24 * d9 - d22 * d10;
						double d26 = d22 * d9 + d24 * d10;
						buffer.vertex(d5 + d25, d6 + d23, d7 + d26).endVertex();
					}
				}
			}
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

			tm.bind(STARS);
			QuadBufferRenderer.render(matrixStack, sky, DefaultVertexFormats.POSITION_TEX, 1f, 1f, 1f, 1f);
			matrixStack.pushPose();
			matrixStack.last().pose().multiply(new Quaternion(0, -time * 4, 0, false));

			createStars();
			RenderSystem.depthMask(true);
			RenderSystem.disableTexture();
			RenderSystem.color4f(1f, 1f, 1f, 0.7f);
			stars.bind();
			starFormat.setupBufferState(0L);
			stars.draw(matrixStack.last().pose(), 7);
			VertexBuffer.unbind();
			starFormat.clearBufferState();
			RenderSystem.disableBlend();
			RenderSystem.enableTexture();
			RenderSystem.depthMask(false);

			tm.bind(FOG);
//			QuadBufferRenderer.render(matrixStack, fog, DefaultVertexFormats.POSITION_TEX, FogRenderer.fogRed,
//					FogRenderer.fogGreen, FogRenderer.fogBlue, 1f);
			matrixStack.popPose();

			drawPlanets(matrixStack, world, partialTicks);

			RenderSystem.depthMask(true);
			RenderSystem.disableTexture();
		}

		// Some code here was ~~stolen~~ BORROWED from Botania, which is a mod by Vazkii
		@SuppressWarnings("resource")
		private void drawPlanets(MatrixStack ms, ClientWorld world, float partialTicks) {
			Tessellator tessellator = Tessellator.getInstance();

			float rain = 1.0F - world.getRainLevel(partialTicks);
			float celAng = world.getTimeOfDay(partialTicks);
			float effCelAng = celAng;
			if (celAng > 0.5) {
				effCelAng = 0.5F - (celAng - 0.5F);
			}
			float scale = 20F;
			float lowA = Math.max(0F, effCelAng - 0.3F) * rain;
			float a = Math.max(0.1F, lowA);

			RenderSystem.blendFuncSeparate(770, 771, 1, 0);
			ms.pushPose();
			RenderSystem.color4f(1F, 1F, 1F, a * 4);
			ms.mulPose(new Vector3f(0.5F, 0.5F, 0F).rotationDegrees(90));

			for (int p = 0; p < ClientStarchart.getStarchart().size(); p++) {
				if (PlanetHelper.getId(world.dimension()) == p)
					continue;

				Random g = new Random(p);
				ms.mulPose(VecUtil.randNorm(g).rotationDegrees(g.nextFloat() * 360));
				scale = MathUtil.randRange(10F, 10F, g);

				float tx = 0.01f;
				float t1 = (world.getGameTime() + partialTicks + g.nextFloat() * 10000) * tx;
				float t2 = (world.getGameTime() + partialTicks + g.nextFloat() * 10000) * tx;
				float t3 = (world.getGameTime() + partialTicks + g.nextFloat() * 10000) * tx;
				ms.mulPose(Vector3f.XP.rotationDegrees(t1));
				ms.mulPose(Vector3f.YP.rotationDegrees(t2));
				ms.mulPose(Vector3f.ZP.rotationDegrees(t3));

				// Todo: Manually detect planet positions
				Minecraft.getInstance().textureManager.bind(PLANETS);
				Matrix4f mat = ms.last().pose();
				tessellator.getBuilder().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				tessellator.getBuilder().vertex(mat, -scale, 100, -scale).uv(0.5F, 0.0F).endVertex();
				tessellator.getBuilder().vertex(mat, scale, 100, -scale).uv(1F, 0.0F).endVertex();
				tessellator.getBuilder().vertex(mat, scale, 100, scale).uv(1F, 0.015625F).endVertex();
				tessellator.getBuilder().vertex(mat, -scale, 100, scale).uv(0.5F, 0.015625F).endVertex();
				tessellator.end();
			}
			UIHelper.resetColor();
			ms.popPose();
		}
	}

	public static class CustomCloudRenderer implements ICloudRenderHandler {

		@Override
		public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc,
				double viewEntityX, double viewEntityY, double viewEntityZ) {
		}
	}

	public static class CustomWeatherRenderer implements IWeatherRenderHandler {

		@Override
		public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc, LightTexture lightmapIn,
				double xIn, double yIn, double zIn) {
		}
	}

	public static class CustomWeatherParticleRenderer implements IWeatherParticleRenderHandler {

		@Override
		public void render(int ticks, ClientWorld world, Minecraft mc, ActiveRenderInfo activeRenderInfoIn) {
		}
	}
}
