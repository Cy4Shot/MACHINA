package com.machina.client.weather;

import com.machina.Machina;
import com.machina.api.client.shader.ShaderHandler;
import com.machina.weather.events.AuroraWeatherEvent;
import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

public class AuroraWeatherRenderer implements WeatherRenderer<AuroraWeatherEvent> {

	private static final float RADIUS = 520.0f;
	private static final int STACKS = 16;
	private static final int SLICES = 32;
	private static final int START_STACK = STACKS / 2;
	private static final float PI = (float) Math.PI;
	private static final float TWO_PI = PI * 2.0f;
	private static final float[] AURORA_DOME_VERTICES = buildDomeVertices();
	private static final ResourceLocation AURORA_NOISE_TEX = ResourceLocation.fromNamespaceAndPath(Machina.MOD_ID,
			"textures/environment/aurora_noise.png");
	private static final float DEFAULT_AURORA_STRENGTH = 10.0F;

	private ShaderInstance cachedShader;
	private AbstractUniform gameTimeUniform;
	private AbstractUniform auroraStrengthUniform;
	private AbstractUniform cameraXZUniform;

	private void ensureUniforms(ShaderInstance shader) {
		if (shader == cachedShader) {
			return;
		}
		cachedShader = shader;
		gameTimeUniform = shader.safeGetUniform("GameTime");
		auroraStrengthUniform = shader.safeGetUniform("AuroraStrength");
		cameraXZUniform = shader.safeGetUniform("CameraXZ");
	}

	private static float[] buildDomeVertices() {
		int triangleCount = (STACKS - START_STACK) * SLICES * 2;
		float[] vertices = new float[triangleCount * 3 * 3];
		int index = 0;

		for (int i = START_STACK; i < STACKS; i++) {
			float lat0 = PI * (-0.5f + (float) i / STACKS);
			float lat1 = PI * (-0.5f + (float) (i + 1) / STACKS);

			float y0 = (float) Math.sin(lat0);
			float y1 = (float) Math.sin(lat1);
			float r0 = (float) Math.cos(lat0);
			float r1 = (float) Math.cos(lat1);

			for (int j = 0; j < SLICES; j++) {
				float lng0 = TWO_PI * j / SLICES;
				float lng1 = TWO_PI * (j + 1) / SLICES;

				float x0 = (float) Math.cos(lng0);
				float z0 = (float) Math.sin(lng0);
				float x1 = (float) Math.cos(lng1);
				float z1 = (float) Math.sin(lng1);

				index = addVertex(vertices, index, x0 * r0 * RADIUS, y0 * RADIUS, z0 * r0 * RADIUS);
				index = addVertex(vertices, index, x0 * r1 * RADIUS, y1 * RADIUS, z0 * r1 * RADIUS);
				index = addVertex(vertices, index, x1 * r1 * RADIUS, y1 * RADIUS, z1 * r1 * RADIUS);

				index = addVertex(vertices, index, x0 * r0 * RADIUS, y0 * RADIUS, z0 * r0 * RADIUS);
				index = addVertex(vertices, index, x1 * r1 * RADIUS, y1 * RADIUS, z1 * r1 * RADIUS);
				index = addVertex(vertices, index, x1 * r0 * RADIUS, y0 * RADIUS, z1 * r0 * RADIUS);
			}
		}

		return vertices;
	}

	private static int addVertex(float[] vertices, int index, float x, float y, float z) {
		vertices[index++] = x;
		vertices[index++] = y;
		vertices[index++] = z;
		return index;
	}

	@Override
	public void renderWeather(ClientLevel level, float intensity, int ticks, float partialTick,
			LightTexture lightTexture, double camX, double camY, double camZ) {
		float finalIntensity = intensity * level.getStarBrightness(partialTick);
		if (finalIntensity <= 0.0F) {
			return;
		}
		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(false);

		ShaderInstance shader = ShaderHandler.AURORA.instance();
		shader.apply();
		ensureUniforms(shader);
		if (gameTimeUniform != null) {
			gameTimeUniform.set(ticks + partialTick);
		}
		if (auroraStrengthUniform != null) {
			auroraStrengthUniform.set(DEFAULT_AURORA_STRENGTH * finalIntensity);
		}
		if (cameraXZUniform != null) {
			cameraXZUniform.set((float) camX, (float) camZ);
		}
		RenderSystem.setShader(() -> shader);
		RenderSystem.setShaderTexture(0, AURORA_NOISE_TEX);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, finalIntensity);

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION);

		for (int i = 0; i < AURORA_DOME_VERTICES.length; i += 3) {
			buffer.addVertex(AURORA_DOME_VERTICES[i], AURORA_DOME_VERTICES[i + 1], AURORA_DOME_VERTICES[i + 2]);
		}

		BufferUploader.drawWithShader(buffer.buildOrThrow());

		RenderSystem.enableCull();
		RenderSystem.depthMask(true);
		RenderSystem.disableBlend();
	}

}
