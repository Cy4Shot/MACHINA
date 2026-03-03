package com.machina.client.weather;

import com.machina.Machina;
import com.machina.api.util.math.ColorUtil;
import com.machina.weather.events.RainWeatherEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

public class RainWeatherRenderer implements WeatherRenderer<RainWeatherEvent> {

	private static final ResourceLocation RAIN_LOCATION = ResourceLocation.fromNamespaceAndPath(Machina.MOD_ID,
			"textures/environment/rain.png");

	private final float[] rainSizeX = new float[1024];
	private final float[] rainSizeZ = new float[1024];

	public RainWeatherRenderer() {
		for (int i = 0; i < 32; i++) {
			for (int j = 0; j < 32; j++) {
				float f = (float) (j - 16);
				float f1 = (float) (i - 16);
				float f2 = Mth.sqrt(f * f + f1 * f1);
				this.rainSizeX[i << 5 | j] = -f1 / f2;
				this.rainSizeZ[i << 5 | j] = f / f2;
			}
		}
	}

	@Override
	public void renderWeather(ClientLevel level, int ticks, float partialTick, LightTexture lightTexture, double camX,
			double camY, double camZ) {
		lightTexture.turnOnLightLayer();
		int i = Mth.floor(camX);
		int j = Mth.floor(camY);
		int k = Mth.floor(camZ);
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = null;
		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		int l = 5;
		if (Minecraft.useFancyGraphics()) {
			l = 10;
		}

		RenderSystem.depthMask(Minecraft.useShaderTransparency());
		int i1 = -1;
		RenderSystem.setShader(GameRenderer::getParticleShader);
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int j1 = k - l; j1 <= k + l; j1++) {
			for (int k1 = i - l; k1 <= i + l; k1++) {
				int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
				double d0 = (double) this.rainSizeX[l1] * 0.5;
				double d1 = (double) this.rainSizeZ[l1] * 0.5;
				blockpos$mutableblockpos.set((double) k1, camY, (double) j1);
				Biome biome = level.getBiome(blockpos$mutableblockpos).value();
				ColorUtil.ofRGB(biome.getWaterColor()).lerp(ColorUtil.WHITE, 0.5f).setRenderSystemColor();
				int i2 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, k1, j1);
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
					RandomSource randomsource = RandomSource
							.create((long) (k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761));
					blockpos$mutableblockpos.set(k1, j2, j1);
					if (i1 != 0) {
						if (i1 >= 0) {
							BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
						}

						i1 = 0;
						RenderSystem.setShaderTexture(0, RAIN_LOCATION);
						bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
					}

					int i3 = mc.levelRenderer.getTicks() & 131071;
					int j3 = k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 0xFF;
					float f2 = 3.0F + randomsource.nextFloat();
					float f3 = -((float) (i3 + j3) + partialTick) / 32.0F * f2;
					float f4 = f3 % 32.0F;
					double d2 = (double) k1 + 0.5 - camX;
					double d3 = (double) j1 + 0.5 - camZ;
					float f6 = (float) Math.sqrt(d2 * d2 + d3 * d3) / (float) l;
					float f7 = ((1.0F - f6 * f6) * 0.5F + 0.5F) * 0.5f;
					blockpos$mutableblockpos.set(k1, l2, j1);
					int k3 = LevelRenderer.getLightColor(level, blockpos$mutableblockpos);
					bufferbuilder
							.addVertex((float) ((double) k1 - camX - d0 + 0.5), (float) ((double) k2 - camY),
									(float) ((double) j1 - camZ - d1 + 0.5))
							.setUv(0.0F, (float) j2 * 0.25F + f4).setColor(1.0F, 1.0F, 1.0F, f7).setLight(k3);
					bufferbuilder
							.addVertex((float) ((double) k1 - camX + d0 + 0.5), (float) ((double) k2 - camY),
									(float) ((double) j1 - camZ + d1 + 0.5))
							.setUv(1.0F, (float) j2 * 0.25F + f4).setColor(1.0F, 1.0F, 1.0F, f7).setLight(k3);
					bufferbuilder
							.addVertex((float) ((double) k1 - camX + d0 + 0.5), (float) ((double) j2 - camY),
									(float) ((double) j1 - camZ + d1 + 0.5))
							.setUv(1.0F, (float) k2 * 0.25F + f4).setColor(1.0F, 1.0F, 1.0F, f7).setLight(k3);
					bufferbuilder
							.addVertex((float) ((double) k1 - camX - d0 + 0.5), (float) ((double) j2 - camY),
									(float) ((double) j1 - camZ - d1 + 0.5))
							.setUv(0.0F, (float) k2 * 0.25F + f4).setColor(1.0F, 1.0F, 1.0F, f7).setLight(k3);
				}

			}
		}

		if (i1 >= 0) {
			BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
		}

		RenderSystem.enableCull();
		RenderSystem.disableBlend();
		lightTexture.turnOffLightLayer();
	}

}
