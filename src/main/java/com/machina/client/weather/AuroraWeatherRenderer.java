package com.machina.client.weather;

import com.machina.api.client.shader.ShaderHandler;
import com.machina.weather.events.AuroraWeatherEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.ShaderInstance;

public class AuroraWeatherRenderer implements WeatherRenderer<AuroraWeatherEvent> {

	@Override
	public void renderWeather(ClientLevel level, int ticks, float partialTick, LightTexture lightTexture, double camX,
			double camY, double camZ) {
		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(false);

		ShaderInstance shader = ShaderHandler.AURORA.instance();
		shader.apply();
		shader.safeGetUniform("GameTime").set(ticks + partialTick);
		shader.safeGetUniform("AuroraStrength").set(10.0F);
		shader.safeGetUniform("CameraXZ").set((float) camX, (float) camZ);
		RenderSystem.setShader(() -> shader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION);

		float radius = 520.0f;
		int stacks = 16;
		int slices = 32;

		for (int i = 0; i < stacks; i++) {
			float lat0 = (float) Math.PI * (-0.5f + (float) i / stacks);
			float lat1 = (float) Math.PI * (-0.5f + (float) (i + 1) / stacks);

			float y0 = (float) Math.sin(lat0);
			float y1 = (float) Math.sin(lat1);
			float r0 = (float) Math.cos(lat0);
			float r1 = (float) Math.cos(lat1);

			for (int j = 0; j < slices; j++) {
				float lng0 = (float) (2 * Math.PI * j / slices);
				float lng1 = (float) (2 * Math.PI * (j + 1) / slices);

				float x0 = (float) Math.cos(lng0);
				float z0 = (float) Math.sin(lng0);
				float x1 = (float) Math.cos(lng1);
				float z1 = (float) Math.sin(lng1);

				// triangle 1
				buffer.addVertex(x0 * r0 * radius, y0 * radius, z0 * r0 * radius);
				buffer.addVertex(x0 * r1 * radius, y1 * radius, z0 * r1 * radius);
				buffer.addVertex(x1 * r1 * radius, y1 * radius, z1 * r1 * radius);

				// triangle 2
				buffer.addVertex(x0 * r0 * radius, y0 * radius, z0 * r0 * radius);
				buffer.addVertex(x1 * r1 * radius, y1 * radius, z1 * r1 * radius);
				buffer.addVertex(x1 * r0 * radius, y0 * radius, z1 * r0 * radius);
			}
		}

		BufferUploader.drawWithShader(buffer.buildOrThrow());

		RenderSystem.enableCull();
		RenderSystem.depthMask(true);
		RenderSystem.disableBlend();
	}

}
