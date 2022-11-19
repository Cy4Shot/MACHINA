package com.machina.client.shader.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import com.machina.client.shader.ShaderHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.FramebufferConstants;
import net.minecraft.client.util.JSONBlendingMode;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// https://github.com/MightyPirates/Scannable/blob/1.16-forge/src/main/java/li/cil/scannable/client/renderer/ScannerRenderer.java
@OnlyIn(Dist.CLIENT)
public enum ScannerRenderer {
	INSTANCE;

	private int depthCopyFbo;
	private int depthCopyColorBuffer;
	private int depthCopyDepthBuffer;

	private static final JSONBlendingMode RESET_BLEND_STATE = new JSONBlendingMode();

	private long currentStart;

	public void ping(final Vector3d pos) {
		currentStart = System.currentTimeMillis();
		ShaderHandler.SCANNER.setUniform("center", pos);
	}

	public static void render(final MatrixStack matrixStack, final Matrix4f projectionMatrix) {
		INSTANCE.doRender(matrixStack, projectionMatrix);
	}

	@SuppressWarnings("resource")
	private static float computeTargetRadius() {
		return Minecraft.getInstance().gameRenderer.getRenderDistance();
	}

	public static final int SCAN_INITIAL_RADIUS = 10;
	public static final int SCAN_TIME_OFFSET = 200;
	public static final int SCAN_GROWTH_DURATION = 2000;
	public static final int REFERENCE_RENDER_DISTANCE = 12;

	public static float computeRadius(final long start, final float duration) {

		final float r1 = computeTargetRadius();
		final float t1 = duration;
		final float b = SCAN_TIME_OFFSET;
		final float n = 1f / ((t1 + b) * (t1 + b) - b * b);
		final float a = -r1 * b * b * n;
		final float c = r1 * n;

		final float t = (float) (System.currentTimeMillis() - start);

		return SCAN_INITIAL_RADIUS + a + (t + b) * (t + b) * c;
	}

	@SuppressWarnings("resource")
	public static int computeScanGrowthDuration() {
		return SCAN_GROWTH_DURATION * Minecraft.getInstance().options.renderDistance / REFERENCE_RENDER_DISTANCE;
	}

	public void doRender(final MatrixStack matrixStack, final Matrix4f projectionMatrix) {
		final int adjustedDuration = computeScanGrowthDuration();
		final boolean shouldRender = currentStart > 0
				&& adjustedDuration > (int) (System.currentTimeMillis() - currentStart);
		if (shouldRender) {
			if (depthCopyFbo == 0) {
				createDepthCopyFramebuffer();
			}

			render(matrixStack.last().pose(), projectionMatrix);
		} else {
			if (depthCopyFbo != 0) {
				deleteDepthCopyFramebuffer();
			}

			currentStart = 0;
		}
	}

	private void render(final Matrix4f viewMatrix, final Matrix4f projectionMatrix) {
		final Minecraft mc = Minecraft.getInstance();
		final Framebuffer framebuffer = mc.getMainRenderTarget();

		updateDepthTexture(framebuffer);

		final Matrix4f invertedViewMatrix = new Matrix4f(viewMatrix);
		invertedViewMatrix.invert();
		ShaderHandler.SCANNER.setUniform("invViewMat", invertedViewMatrix);

		final Matrix4f invertedProjectionMatrix = new Matrix4f(projectionMatrix);
		invertedProjectionMatrix.invert();
		ShaderHandler.SCANNER.setUniform("invProjMat", invertedProjectionMatrix);

		final Vector3d position = mc.gameRenderer.getMainCamera().getPosition();
		ShaderHandler.SCANNER.setUniform("pos", position);

		final int adjustedDuration = computeScanGrowthDuration();
		final float radius = computeRadius(currentStart, (float) adjustedDuration);
		ShaderHandler.SCANNER.setUniform("radius", radius);
		ShaderHandler.SCANNER.setUniform("target", computeTargetRadius());

		// This is a bit of a hack; blend state is changed from many places in MC, and
		// if there's
		// no other shader active at all, our shader will not properly apply its blend
		// settings.
		// So we use a dummy blend state to change the reference to the last applied one
		// to force it.
		RESET_BLEND_STATE.apply();
		ShaderHandler.SCANNER.bind();
		blit(framebuffer);
		ShaderHandler.SCANNER.unbind();
	}

	private void updateDepthTexture(final Framebuffer framebuffer) {
		GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, framebuffer.frameBufferId);
		GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, depthCopyFbo);
		GL30.glBlitFramebuffer(0, 0, framebuffer.width, framebuffer.height, 0, 0, framebuffer.width, framebuffer.height,
				GL30.GL_DEPTH_BUFFER_BIT, GL30.GL_NEAREST);
	}

	private void createDepthCopyFramebuffer() {
		final Framebuffer framebuffer = Minecraft.getInstance().getMainRenderTarget();

		depthCopyFbo = GlStateManager.glGenFramebuffers();

		// We don't use the color attachment on this FBO, but it's required for a
		// complete FBO.
		depthCopyColorBuffer = createTexture(framebuffer.width, framebuffer.height, GL11.GL_RGBA8, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE);

		// Main reason why we create this FBO: readable depth buffer into which we can
		// copy the MC one.
		depthCopyDepthBuffer = createTexture(framebuffer.width, framebuffer.height, GL30.GL_DEPTH_COMPONENT,
				GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT);

		GlStateManager._glBindFramebuffer(FramebufferConstants.GL_FRAMEBUFFER, depthCopyFbo);
		GlStateManager._glFramebufferTexture2D(FramebufferConstants.GL_FRAMEBUFFER,
				FramebufferConstants.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, depthCopyColorBuffer, 0);
		GlStateManager._glFramebufferTexture2D(FramebufferConstants.GL_FRAMEBUFFER,
				FramebufferConstants.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthCopyDepthBuffer, 0);
		GlStateManager._glBindFramebuffer(FramebufferConstants.GL_FRAMEBUFFER, 0);

		ShaderHandler.SCANNER.setSampler("depthTex", depthCopyDepthBuffer);
	}

	private void deleteDepthCopyFramebuffer() {
		ShaderHandler.SCANNER.setSampler("depthTex", 0);

		GlStateManager._glDeleteFramebuffers(depthCopyFbo);
		depthCopyFbo = 0;

		TextureUtil.releaseTextureId(depthCopyColorBuffer);
		depthCopyColorBuffer = 0;

		TextureUtil.releaseTextureId(depthCopyDepthBuffer);
		depthCopyDepthBuffer = 0;
	}

	private int createTexture(final int width, final int height, final int internalFormat, final int format,
			final int type) {
		final int texture = TextureUtil.generateTextureId();
		GlStateManager._bindTexture(texture);
		GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL14.GL_DEPTH_TEXTURE_MODE, GL11.GL_LUMINANCE);
		GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL14.GL_NONE);
		GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_FUNC, GL11.GL_LEQUAL);
		GlStateManager._texImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, type, null);
		GlStateManager._bindTexture(0);
		return texture;
	}

	private void blit(final Framebuffer framebuffer) {
		final int width = framebuffer.width;
		final int height = framebuffer.height;

		RenderSystem.depthMask(false);
		RenderSystem.disableDepthTest();

		setupMatrices(width, height);

		framebuffer.bindWrite(false);

		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder buffer = tessellator.getBuilder();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.vertex(0, height, 0).uv(0, 0).endVertex();
		buffer.vertex(width, height, 0).uv(1, 0).endVertex();
		buffer.vertex(width, 0, 0).uv(1, 1).endVertex();
		buffer.vertex(0, 0, 0).uv(0, 1).endVertex();
		tessellator.end();

		restoreMatrices();

		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
	}

	private void setupMatrices(final int width, final int height) {
		RenderSystem.matrixMode(GL11.GL_PROJECTION);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		RenderSystem.ortho(0, width, height, 0, 1000, 3000);
		RenderSystem.matrixMode(GL11.GL_MODELVIEW);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		RenderSystem.translated(0, 0, -2000);
		RenderSystem.viewport(0, 0, width, height);
	}

	private void restoreMatrices() {
		RenderSystem.matrixMode(GL11.GL_PROJECTION);
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(GL11.GL_MODELVIEW);
		RenderSystem.popMatrix();
	}
}