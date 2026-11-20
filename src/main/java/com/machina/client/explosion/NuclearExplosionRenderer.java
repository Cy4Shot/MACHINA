package com.machina.client.explosion;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import com.machina.api.client.shader.ShaderHandler;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.phys.Vec3;

public class NuclearExplosionRenderer {

	private static boolean textureLoaded = false;

	public static void render(PoseStack poseStack, Camera camera, float partialTick) {
		if (!NuclearExplosionManager.hasActiveExplosions()) {
			return;
		}

		// Lazy load the 3D noise texture
		if (!textureLoaded) {
			NoiseTexture3D.load(Minecraft.getInstance().getResourceManager());
			textureLoaded = true;
		}

		if (!NoiseTexture3D.isLoaded()) {
			return;
		}

		Vec3 cameraPos = camera.getPosition();

		for (NuclearExplosion explosion : NuclearExplosionManager.getActiveExplosions()) {
			renderExplosion(poseStack, explosion, cameraPos);
		}
	}

	private static void renderExplosion(PoseStack poseStack, NuclearExplosion explosion, Vec3 cameraPos) {
		ShaderInstance shader = ShaderHandler.NUKE_GLOW.instance();
		if (shader == null) return;

		Vec3 pos = explosion.getPosition();
		float progress = explosion.getProgress();
		float time = explosion.getTime();
		float power = explosion.getPower();

		// Bounding box size - must encompass the entire cloud
		float scale = power * 15.0f;
		float boxSize = scale * 10.0f;
		float cloudHeight = progress * 8.0f * scale;

		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.disableCull();
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);

		// Bind 3D noise texture to texture unit 0
		NoiseTexture3D.bind(0);

		// Set shader uniforms
		if (shader.getUniform("CameraPos") != null) {
			shader.getUniform("CameraPos").set((float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z);
		}
		if (shader.getUniform("ExplosionCenter") != null) {
			shader.getUniform("ExplosionCenter").set((float) pos.x, (float) pos.y, (float) pos.z);
		}
		if (shader.getUniform("Time") != null) {
			shader.getUniform("Time").set(time);
		}
		if (shader.getUniform("Progress") != null) {
			shader.getUniform("Progress").set(progress);
		}
		if (shader.getUniform("Power") != null) {
			shader.getUniform("Power").set(power);
		}

		RenderSystem.setShader(() -> shader);

		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLES,
				DefaultVertexFormat.POSITION);

		// Render bounding box in world space (not camera-relative)
		float minX = (float) pos.x - boxSize;
		float maxX = (float) pos.x + boxSize;
		float minY = (float) pos.y - scale * 2;
		float maxY = (float) pos.y + cloudHeight + boxSize;
		float minZ = (float) pos.z - boxSize;
		float maxZ = (float) pos.z + boxSize;

		float cx = (float) cameraPos.x;
		float cy = (float) cameraPos.y;
		float cz = (float) cameraPos.z;

		poseStack.pushPose();
		Matrix4f matrix = poseStack.last().pose();

		// Render all 6 faces of the bounding box
		addQuad(buffer, matrix, cx, cy, cz, minX, minY, minZ, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ);
		addQuad(buffer, matrix, cx, cy, cz, maxX, minY, maxZ, minX, minY, maxZ, minX, maxY, maxZ, maxX, maxY, maxZ);
		addQuad(buffer, matrix, cx, cy, cz, minX, minY, maxZ, minX, minY, minZ, minX, maxY, minZ, minX, maxY, maxZ);
		addQuad(buffer, matrix, cx, cy, cz, maxX, minY, minZ, maxX, minY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ);
		addQuad(buffer, matrix, cx, cy, cz, minX, minY, maxZ, maxX, minY, maxZ, maxX, minY, minZ, minX, minY, minZ);
		addQuad(buffer, matrix, cx, cy, cz, minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, minX, maxY, maxZ);

		BufferUploader.drawWithShader(buffer.buildOrThrow());

		poseStack.popPose();

		// Unbind 3D texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL12.glBindTexture(GL12.GL_TEXTURE_3D, 0);

		RenderSystem.depthMask(true);
		RenderSystem.enableCull();
		RenderSystem.disableBlend();
		RenderSystem.enableDepthTest();
	}

	private static void addQuad(BufferBuilder buffer, Matrix4f matrix, float cx, float cy, float cz,
								 float x1, float y1, float z1, float x2, float y2, float z2,
								 float x3, float y3, float z3, float x4, float y4, float z4) {
		buffer.addVertex(matrix, x1 - cx, y1 - cy, z1 - cz);
		buffer.addVertex(matrix, x2 - cx, y2 - cy, z2 - cz);
		buffer.addVertex(matrix, x3 - cx, y3 - cy, z3 - cz);
		buffer.addVertex(matrix, x1 - cx, y1 - cy, z1 - cz);
		buffer.addVertex(matrix, x3 - cx, y3 - cy, z3 - cz);
		buffer.addVertex(matrix, x4 - cx, y4 - cy, z4 - cz);
	}
}
