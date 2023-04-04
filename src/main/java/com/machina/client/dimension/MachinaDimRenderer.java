package com.machina.client.dimension;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.machina.Machina;
import com.machina.client.ClientStarchart;
import com.machina.client.util.QuadBufferRenderer;
import com.machina.registration.init.AttributeInit;
import com.machina.util.Color;
import com.machina.util.text.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.fluid.FluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.LightType;
import net.minecraft.world.gen.Heightmap;
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
			QuadBufferRenderer.render(matrixStack, fog, DefaultVertexFormats.POSITION_TEX, FogRenderer.fogRed,
					FogRenderer.fogGreen, FogRenderer.fogBlue, 1f);
			matrixStack.popPose();

			RenderSystem.depthMask(true);
			RenderSystem.disableTexture();
		}
	}

	public static class CustomCloudRenderer implements ICloudRenderHandler {

		@Override
		public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc,
				double viewEntityX, double viewEntityY, double viewEntityZ) {
		}
	}

	public static class CustomWeatherRenderer implements IWeatherRenderHandler {

		private static final ResourceLocation RAIN_LOCATION = new MachinaRL("textures/environment/rain.png");

		private int rainSoundTime;
		private final float[] rainSizeX = new float[1024];
		private final float[] rainSizeZ = new float[1024];

		public CustomWeatherRenderer() {
			for (int i = 0; i < 32; ++i) {
				for (int j = 0; j < 32; ++j) {
					float f = (float) (j - 16);
					float f1 = (float) (i - 16);
					float f2 = MathHelper.sqrt(f * f + f1 * f1);
					this.rainSizeX[i << 5 | j] = -f1 / f2;
					this.rainSizeZ[i << 5 | j] = f / f2;
				}
			}
		}

		private void renderRain(ClientWorld world, Minecraft mc, LightTexture lm, float t, double pX, double pY,
				double pZ, float r, float g, float b) {
			lm.turnOnLightLayer();
			int i = MathHelper.floor(pX);
			int j = MathHelper.floor(pY);
			int k = MathHelper.floor(pZ);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuilder();
			RenderSystem.enableAlphaTest();
			RenderSystem.disableCull();
			RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.defaultAlphaFunc();
			RenderSystem.enableDepthTest();
			int l = 5;
			if (Minecraft.useFancyGraphics()) {
				l = 10;
			}

			RenderSystem.depthMask(Minecraft.useShaderTransparency());
			int i1 = -1;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

			for (int j1 = k - l; j1 <= k + l; ++j1) {
				for (int k1 = i - l; k1 <= i + l; ++k1) {
					int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
					double d0 = (double) this.rainSizeX[l1] * 0.5D;
					double d1 = (double) this.rainSizeZ[l1] * 0.5D;
					blockpos$mutable.set(k1, 0, j1);
					int i2 = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, blockpos$mutable).getY();
					int j2 = j - l;
					int k2 = j + l;
					if (j2 < i2) {
						j2 = i2;
					}

					if (k2 < i2) {
						k2 = i2;
					}

					int l2 = i2;
					if (i2 < j) {
						l2 = j;
					}

					if (j2 != k2) {
						Random random = new Random(
								(long) (k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761));
						blockpos$mutable.set(k1, j2, j1);
						if (i1 != 0) {
							if (i1 >= 0) {
								tessellator.end();
							}

							i1 = 0;
							mc.getTextureManager().bind(RAIN_LOCATION);
							bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE);
						}

						int i3 = mc.levelRenderer.ticks + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761
								& 31;
						float f3 = -((float) i3 + t) / 32.0F * (3.0F + random.nextFloat());
						double d2 = (double) ((float) k1 + 0.5F) - pX;
						double d4 = (double) ((float) j1 + 0.5F) - pZ;
						float f4 = MathHelper.sqrt(d2 * d2 + d4 * d4) / (float) l;
						float f5 = ((1.0F - f4 * f4) * 0.5F + 0.5F);
						blockpos$mutable.set(k1, l2, j1);
						int j3 = getLightColor(world, blockpos$mutable);
						bufferbuilder
								.vertex((double) k1 - pX - d0 + 0.5D, (double) k2 - pY, (double) j1 - pZ - d1 + 0.5D)
								.uv(0.0F, (float) j2 * 0.25F + f3).color(r, g, b, f5).uv2(j3).endVertex();
						bufferbuilder
								.vertex((double) k1 - pX + d0 + 0.5D, (double) k2 - pY, (double) j1 - pZ + d1 + 0.5D)
								.uv(1.0F, (float) j2 * 0.25F + f3).color(r, g, b, f5).uv2(j3).endVertex();
						bufferbuilder
								.vertex((double) k1 - pX + d0 + 0.5D, (double) j2 - pY, (double) j1 - pZ + d1 + 0.5D)
								.uv(1.0F, (float) k2 * 0.25F + f3).color(r, g, b, f5).uv2(j3).endVertex();
						bufferbuilder
								.vertex((double) k1 - pX - d0 + 0.5D, (double) j2 - pY, (double) j1 - pZ - d1 + 0.5D)
								.uv(0.0F, (float) k2 * 0.25F + f3).color(r, g, b, f5).uv2(j3).endVertex();
					}
				}
			}

			if (i1 >= 0) {
				tessellator.end();
			}

			RenderSystem.enableCull();
			RenderSystem.disableBlend();
			RenderSystem.defaultAlphaFunc();
			RenderSystem.disableAlphaTest();
			lm.turnOffLightLayer();
		}

		private ParticleStatus stat(Minecraft mc) {
			ParticleStatus particlestatus = mc.options.particles;
			if (particlestatus == ParticleStatus.DECREASED && mc.level.random.nextInt(3) == 0) {
				particlestatus = ParticleStatus.MINIMAL;
			}

			return particlestatus;
		}

		public void tickRain(ClientWorld world, Minecraft mc, ActiveRenderInfo c) {
			float f = 1 / (Minecraft.useFancyGraphics() ? 1.0F : 2.0F);
			Random random = new Random((long) mc.levelRenderer.ticks * 312987231L);
			BlockPos blockpos = new BlockPos(c.getPosition());
			BlockPos blockpos1 = null;
			int i = (int) (100.0F * f * f) / (mc.options.particles == ParticleStatus.DECREASED ? 2 : 1);

			for (int j = 0; j < i; ++j) {
				int k = random.nextInt(21) - 10;
				int l = random.nextInt(21) - 10;
				BlockPos blockpos2 = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, blockpos.offset(k, 0, l))
						.below();
				if (blockpos2.getY() > 0 && blockpos2.getY() <= blockpos.getY() + 10
						&& blockpos2.getY() >= blockpos.getY() - 10) {
					blockpos1 = blockpos2;
					if (mc.options.particles == ParticleStatus.MINIMAL) {
						break;
					}

					double d0 = random.nextDouble();
					double d1 = random.nextDouble();
					BlockState blockstate = world.getBlockState(blockpos2);
					FluidState fluidstate = world.getFluidState(blockpos2);
					double pX = (double) blockpos2.getX() + d0;
					double pY = (double) blockpos2.getY()
							+ Math.max(blockstate.getCollisionShape(world, blockpos2).max(Direction.Axis.Y, d0, d1),
									(double) fluidstate.getHeight(world, blockpos2));
					double pZ = (double) blockpos2.getZ() + d1;

					IParticleData iparticledata = !fluidstate.is(FluidTags.LAVA) && !blockstate.is(Blocks.MAGMA_BLOCK)
							&& !CampfireBlock.isLitCampfire(blockstate) ? ParticleTypes.RAIN : ParticleTypes.SMOKE;
					if (c.getPosition().distanceToSqr(pX, pY, pZ) <= 1024.0D && stat(mc) != ParticleStatus.MINIMAL) {
						Particle o = mc.particleEngine.createParticle(iparticledata, pX, pY, pZ, 0D, 0D, 0D);
						Color col = ClientStarchart.getPlanetData(world.dimension())
								.getAttribute(AttributeInit.PALETTE)[3];
						o.setColor(col.r(), col.g(), col.b());
					}
				}
			}

			if (blockpos1 != null && random.nextInt(3) < this.rainSoundTime++) {
				this.rainSoundTime = 0;
				if (blockpos1.getY() > blockpos.getY() + 1
						&& world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, blockpos).getY() > MathHelper
								.floor((float) blockpos.getY())) {
					world.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1F, 0.5F,
							false);
				} else {
					world.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2F, 1.0F, false);
				}
			}
		}

		@Override
		public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc, LightTexture lightmapIn,
				double xIn, double yIn, double zIn) {
			RenderSystem.pushMatrix();
			RenderSystem.depthMask(false);
			Color c = ClientStarchart.getPlanetData(world.dimension()).getAttribute(AttributeInit.PALETTE)[3];
//			this.renderRain(world, mc, lightmapIn, partialTicks, xIn, yIn, zIn, c.r(), c.g(), c.b());
			RenderSystem.depthMask(true);
			RenderSystem.popMatrix();

			if (!mc.isPaused())
				this.tickRain(world, mc, mc.gameRenderer.getMainCamera());
		}

		public static int getLightColor(IBlockDisplayReader pLightReader, BlockPos pBlockPos) {
			return getLightColor(pLightReader, pLightReader.getBlockState(pBlockPos), pBlockPos);
		}

		public static int getLightColor(IBlockDisplayReader pLightReader, BlockState pBlockState, BlockPos pBlockPos) {
			if (pBlockState.emissiveRendering(pLightReader, pBlockPos)) {
				return 15728880;
			} else {
				int i = pLightReader.getBrightness(LightType.SKY, pBlockPos);
				int j = pLightReader.getBrightness(LightType.BLOCK, pBlockPos);
				int k = pBlockState.getLightValue(pLightReader, pBlockPos);
				if (j < k) {
					j = k;
				}
				return i << 20 | j << 4;
			}
		}
	}

	public static class CustomWeatherParticleRenderer implements IWeatherParticleRenderHandler {

		@Override
		public void render(int ticks, ClientWorld world, Minecraft mc, ActiveRenderInfo activeRenderInfoIn) {
		}
	}
}
