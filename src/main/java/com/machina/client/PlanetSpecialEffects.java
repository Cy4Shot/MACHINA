package com.machina.client;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.machina.api.client.ClientBiomeSettings;
import com.machina.api.client.ClientStarchart;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeClientSettings;
import com.machina.api.util.math.ColorUtil;
import com.machina.api.util.math.ColorUtil.RGBA;
import com.machina.client.weather.WeatherRenderer;
import com.machina.weather.WeatherEvent;
import com.machina.weather.manager.ClientWeatherManager;
import com.machina.weather.system.ClientWeatherSystem;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlanetSpecialEffects extends DimensionSpecialEffects {

	private static final ResourceLocation MOON_LOCATION = ResourceLocation
			.withDefaultNamespace("textures/environment/moon_phases.png");
	private static final ResourceLocation SUN_LOCATION = ResourceLocation
			.withDefaultNamespace("textures/environment/sun.png");

	private static final Minecraft mc = Minecraft.getInstance();

	public PlanetSpecialEffects() {
		super(192, true, SkyType.NORMAL, false, false);
	}

	@Override
	public @NotNull Vec3 getBrightnessDependentFogColor(Vec3 col, float brightness) {
		return col.multiply(brightness * 0.94F + 0.06F, brightness * 0.94F + 0.06F, brightness * 0.91F + 0.09F);
	}

	@Override
	public boolean isFoggyAt(int x, int z) {
		return false;
	}

	@Override
	public boolean renderSnowAndRain(@NotNull ClientLevel level, int ticks, float partialTick,
			@NotNull LightTexture lightTexture, double camX, double camY, double camZ) {
		ClientWeatherSystem weathersystem = ClientWeatherManager.getSystem(level);
		if (weathersystem != null) {
			WeatherEvent event = weathersystem.getCurrentEvent();
			WeatherRenderer<?> renderer = ClientWeatherManager.getRenderer(event);
			if (renderer != null) {
				renderer.renderWeather(level, ticks, partialTick, lightTexture, camX, camY, camZ);
			}
		}
		return true;
	}

	@Override
	public boolean renderClouds(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, double camX,
			double camY, double camZ, Matrix4f modelViewMatrix, Matrix4f projectionMatrix) {
		return true;
	}

	@Override
	public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f modelViewMatrix, Camera camera,
			Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
		Planet planet = ClientStarchart.getPlanet(level);
		if (planet == null) {
			return false;
		}
		setupFog.run();

		// Calculate sky & fog color
		boolean hasAtmosphere = true;
		Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
		Vec3 skyColor = getSkyColor(planet, cameraPos, level.getTimeOfDay(partialTick));
		setupFogColor(camera, partialTick, level, skyColor, mc.options.getEffectiveRenderDistance());
		if (skyColor == null) {
			hasAtmosphere = false;
			skyColor = Vec3.ZERO;
		}

		// Setup Posestack
		PoseStack posestack = new PoseStack();
		posestack.mulPose(modelViewMatrix);

		// Skybox
		Tesselator tesselator = Tesselator.getInstance();
		RenderSystem.depthMask(false);
		RenderSystem.setShaderColor((float) skyColor.x, (float) skyColor.y, (float) skyColor.z, 1.0F);
		ShaderInstance shaderinstance = RenderSystem.getShader();

		mc.levelRenderer.skyBuffer.bind();
		mc.levelRenderer.skyBuffer.drawWithShader(posestack.last().pose(), projectionMatrix, shaderinstance);
		VertexBuffer.unbind();

		mc.levelRenderer.darkBuffer.bind();
		mc.levelRenderer.darkBuffer.drawWithShader(posestack.last().pose(), projectionMatrix, shaderinstance);
		VertexBuffer.unbind();

		// Sunrise
		RenderSystem.enableBlend();
		if (hasAtmosphere) {
			float[] afloat = this.getSunriseColor(level.getTimeOfDay(partialTick), partialTick);
			if (afloat != null) {
				RenderSystem.setShader(GameRenderer::getPositionColorShader);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				posestack.pushPose();
				posestack.mulPose(Axis.XP.rotationDegrees(90.0F));
				float f3 = Mth.sin(level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
				posestack.mulPose(Axis.ZP.rotationDegrees(f3));
				posestack.mulPose(Axis.ZP.rotationDegrees(90.0F));
				Matrix4f matrix4f = posestack.last().pose();
				BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN,
						DefaultVertexFormat.POSITION_COLOR);
				bufferbuilder.addVertex(matrix4f, 0.0F, 100.0F, 0.0F).setColor(afloat[0], afloat[1], afloat[2],
						afloat[3]);

				for (int j = 0; j <= 16; j++) {
					float f7 = (float) j * (float) (Math.PI * 2) / 16.0F;
					float f8 = Mth.sin(f7);
					float f9 = Mth.cos(f7);
					bufferbuilder.addVertex(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3])
							.setColor(afloat[0], afloat[1], afloat[2], 0.0F);
				}

				BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
				posestack.popPose();
			}
		}

		// Sun & Moon
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
				GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		posestack.pushPose();
		float f11 = 1.0F - 0f;// level.getRainLevel(partialTick);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
		posestack.mulPose(Axis.YP.rotationDegrees(-90.0F));
		posestack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
		Matrix4f matrix4f1 = posestack.last().pose();
		float f12 = 30.0F;
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, SUN_LOCATION);
		BufferBuilder bufferbuilder1 = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder1.addVertex(matrix4f1, -f12, 100.0F, -f12).setUv(0.0F, 0.0F);
		bufferbuilder1.addVertex(matrix4f1, f12, 100.0F, -f12).setUv(1.0F, 0.0F);
		bufferbuilder1.addVertex(matrix4f1, f12, 100.0F, f12).setUv(1.0F, 1.0F);
		bufferbuilder1.addVertex(matrix4f1, -f12, 100.0F, f12).setUv(0.0F, 1.0F);
		BufferUploader.drawWithShader(bufferbuilder1.buildOrThrow());
		f12 = 20.0F;
		RenderSystem.setShaderTexture(0, MOON_LOCATION);
		int k = level.getMoonPhase();
		int l = k % 4;
		int i1 = k / 4 % 2;
		float f13 = (float) (l + 0) / 4.0F;
		float f14 = (float) (i1 + 0) / 2.0F;
		float f15 = (float) (l + 1) / 4.0F;
		float f16 = (float) (i1 + 1) / 2.0F;
		bufferbuilder1 = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder1.addVertex(matrix4f1, -f12, -100.0F, f12).setUv(f15, f16);
		bufferbuilder1.addVertex(matrix4f1, f12, -100.0F, f12).setUv(f13, f16);
		bufferbuilder1.addVertex(matrix4f1, f12, -100.0F, -f12).setUv(f13, f14);
		bufferbuilder1.addVertex(matrix4f1, -f12, -100.0F, -f12).setUv(f15, f14);
		BufferUploader.drawWithShader(bufferbuilder1.buildOrThrow());

		// Stars
		float f10 = hasAtmosphere ? level.getStarBrightness(partialTick) * f11 : 1f;
		if (f10 > 0.0F) {
			RenderSystem.setShaderColor(f10, f10, f10, f10);
			FogRenderer.setupNoFog();
			mc.levelRenderer.starBuffer.bind();
			mc.levelRenderer.starBuffer.drawWithShader(posestack.last().pose(), projectionMatrix,
					GameRenderer.getPositionShader());
			VertexBuffer.unbind();
			setupFog.run();
		}

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.depthMask(true);
		return true; // Prevent vanilla
	}

	public static Vec3 getSkyColor(Planet planet, Vec3 cameraPos, float timeOfDay) {
		float cloudFactor = (float) planet.cloud_cover(); // 0 = clear, 1 = full clouds
		float tempFactor = (float) (planet.surf_temp() - 200) / 400f; // normalize temp roughly 200K–600K
		tempFactor = Mth.clamp(tempFactor, 0f, 1f);

		double scatterR = 0.0f;
		double scatterG = 0.0f;
		double scatterB = 0.0f;

		// Wavelengths (nm)
		double lambdaR = 680f;
		double lambdaG = 530f;
		double lambdaB = 470f;

		// Helper: Rayleigh ~ 1 / λ^4
		double rayleighR = 1f / (lambdaR * lambdaR * lambdaR * lambdaR);
		double rayleighG = 1f / (lambdaG * lambdaG * lambdaG * lambdaG);
		double rayleighB = 1f / (lambdaB * lambdaB * lambdaB * lambdaB);

		// Sum contributions of each gas
		scatterR += planet.GN2() * 1.0d * rayleighR;
		scatterG += planet.GN2() * 1.0d * rayleighG;
		scatterB += planet.GN2() * 1.0d * rayleighB;

		scatterR += planet.GO2() * 1.0d * rayleighR;
		scatterG += planet.GO2() * 1.0d * rayleighG;
		scatterB += planet.GO2() * 1.0d * rayleighB;

		scatterR += planet.GCO2() * 0.9d * rayleighR;
		scatterG += planet.GCO2() * 0.9d * rayleighG;
		scatterB += planet.GCO2() * 0.9d * rayleighB;

		scatterR += planet.GH2() * 0.8d * rayleighR;
		scatterG += planet.GH2() * 0.8d * rayleighG;
		scatterB += planet.GH2() * 0.8d * rayleighB;

		scatterR += planet.GH2O() * 0.5d * rayleighR;
		scatterG += planet.GH2O() * 0.5d * rayleighG;
		scatterB += planet.GH2O() * 0.5d * rayleighB;

		double total = scatterR + scatterG + scatterB;
		if (total > 0f) {
			scatterR /= total;
			scatterG /= total;
			scatterB /= total;
		}

		if (scatterR + scatterG + scatterB == 0) {
			return null; // Early exit to prevent lerping
		}

		scatterR = Mth.lerp(cloudFactor * 0.5D, scatterR, 1D);
		scatterG = Mth.lerp(cloudFactor * 0.5D, scatterG, 1D);
		scatterB = Mth.lerp(cloudFactor * 0.5D, scatterB, 1D);

		scatterR = Mth.lerp(tempFactor, scatterR, scatterR + 0.2D);
		scatterB = Mth.lerp(tempFactor, scatterB, scatterB - 0.1D);

		scatterR = Mth.clamp(scatterR, 0D, 1D);
		scatterG = Mth.clamp(scatterG, 0D, 1D);
		scatterB = Mth.clamp(scatterB, 0D, 1D);

		double sunFactor = Mth.cos(timeOfDay * ((float) Math.PI * 2)) * 2.0F + 0.5F;
		sunFactor = Mth.clamp(sunFactor, 0.0F, 1.0F);
		return new Vec3(scatterR * sunFactor, scatterG * sunFactor, scatterB * sunFactor);
	}

	public void setupFogColor(Camera camera, float partialTicks, ClientLevel level, @Nullable Vec3 skyColor,
			int renderDistanceChunks) {
		if (skyColor == null)
			skyColor = Vec3.ZERO;

		ClientWeatherSystem weathersystem = ClientWeatherManager.getSystem(level);
		if (weathersystem != null) {
			WeatherEvent event = weathersystem.getCurrentEvent();
			if (event != null && event.getFogFar() > 0) {
				// 1. Apply biome tint
				ResourceLocation biome = level.getBiome(mc.player.blockPosition()).getKey().location();
				RGBA tint = ColorUtil.ofRGBA(ClientBiomeSettings.BIOME_SETTINGS
						.getOrDefault(biome, PlanetBiomeClientSettings.DEFAULT).weather_tint());

				// 2. Apply weather tint
				tint = tint.mul(ColorUtil.ofRGBA(event.getFogTint()));

				// 3. Apply sky tint
				if (skyColor == Vec3.ZERO) {
					skyColor = tint.vec3();
				} else {
					skyColor = skyColor.multiply(tint.vec3());
				}
			}
		}

		float f4 = 0.25F + 0.75F * (float) renderDistanceChunks / 32.0F;
		f4 = 1.0F - (float) Math.pow((double) f4, 0.25);
		float f6 = (float) skyColor.x();
		float f8 = (float) skyColor.y();
		float f10 = (float) skyColor.z();
		float fogRed = (float) skyColor.x();
		float fogGreen = (float) skyColor.y();
		float fogBlue = (float) skyColor.z();
		if (renderDistanceChunks >= 4) {
			float f12 = Mth.sin(level.getSunAngle(partialTicks)) > 0.0F ? -1.0F : 1.0F;
			Vector3f vector3f = new Vector3f(f12, 0.0F, 0.0F);
			float f16 = camera.getLookVector().dot(vector3f);
			if (f16 < 0.0F) {
				f16 = 0.0F;
			}

			if (f16 > 0.0F) {
				float[] afloat = getSunriseColor(level.getTimeOfDay(partialTicks), partialTicks);
				if (afloat != null) {
					f16 *= afloat[3];
					fogRed = fogRed * (1.0F - f16) + afloat[0] * f16;
					fogGreen = fogGreen * (1.0F - f16) + afloat[1] * f16;
					fogBlue = fogBlue * (1.0F - f16) + afloat[2] * f16;
				}
			}
		}

		fogRed = fogRed + (f6 - fogRed) * f4;
		fogGreen = fogGreen + (f8 - fogGreen) * f4;
		fogBlue = fogBlue + (f10 - fogBlue) * f4;
		float f13 = level.getRainLevel(partialTicks);
		if (f13 > 0.0F) {
			float f14 = 1.0F - f13 * 0.5F;
			float f17 = 1.0F - f13 * 0.4F;
			fogRed *= f14;
			fogGreen *= f14;
			fogBlue *= f17;
		}

		float f15 = level.getThunderLevel(partialTicks);
		if (f15 > 0.0F) {
			float f18 = 1.0F - f15 * 0.5F;
			fogRed *= f18;
			fogGreen *= f18;
			fogBlue *= f18;
		}

		float f5 = ((float) camera.getPosition().y - (float) level.getMinBuildHeight())
				* level.getLevelData().getClearColorScale();

		if (f5 < 1.0F) {
			if (f5 < 0.0F) {
				f5 = 0.0F;
			}

			f5 *= f5;
			fogRed *= f5;
			fogGreen *= f5;
			fogBlue *= f5;
		}

		float f7;
		if (camera.getEntity() instanceof LivingEntity livingEntity && livingEntity.hasEffect(MobEffects.NIGHT_VISION)
				&& !livingEntity.hasEffect(MobEffects.DARKNESS)) {
			f7 = GameRenderer.getNightVisionScale(livingEntity, partialTicks);
		} else {
			f7 = 0.0F;
		}

		if (fogRed != 0.0F && fogGreen != 0.0F && fogBlue != 0.0F) {
			float f9 = Math.min(1.0F / fogRed, Math.min(1.0F / fogGreen, 1.0F / fogBlue));
			fogRed = fogRed * (1.0F - f7) + fogRed * f9 * f7;
			fogGreen = fogGreen * (1.0F - f7) + fogGreen * f9 * f7;
			fogBlue = fogBlue * (1.0F - f7) + fogBlue * f9 * f7;
		}

		RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);
		RenderSystem.setShaderFogColor(fogRed, fogGreen, fogBlue);
		RenderSystem.clear(16640, Minecraft.ON_OSX); // This is very cheeky
	}
}