package com.machina.client.screen;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import com.machina.api.client.UIHelper;
import com.machina.api.client.planet.CelestialRenderInfo;
import com.machina.api.client.planet.CelestialRenderer;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.SolarSystem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;

public class StarchartScreen extends Screen {

	SolarSystem system;

	float rotX = 0;
	float rotY = 45;
	float posX = 0;
	float posY = 0;
	float zoom;

	public StarchartScreen(SolarSystem s) {
		super(Component.empty());
		this.system = s;

		// Init Zoom
		System.out.println(system.maxAphelion());
		zoom = calculateZoomForMaxRange(system.maxAphelion() * 2);
	}

	public float calculateZoomForMaxRange(double desiredMaxRange) {

		float orx = rotX;
		float ory = rotY;
		rotX = 0;
		rotY = 89.99999F;

		// Create a temporary PoseStack to compute the range without altering the
		// original one
		PoseStack tempMatrices = new PoseStack();

		// Assume createRotQuat() returns the rotation quaternion
		Quaternionf rotQuat = createRotQuat();

		// Start with a reasonable initial guess for the zoom factor
		float zoom = 10f;

		// Set the initial zoom and rotation in the temporary PoseStack
		tempMatrices.scale(1.0F, 1.0F, 0.1F);
		tempMatrices.scale(zoom, zoom, zoom);
		tempMatrices.mulPose(rotQuat);

		// Calculate the maximum range using the temporary PoseStack
		float[] maxRange = calculateViewableRange(tempMatrices);

		// Assuming maxRange is an array containing range along x, y, and z axes
		float max = Math.max(maxRange[0], maxRange[1]);

		// If the initial range is already larger than the desired max range, reduce the
		// zoom
		if (true) {
			// Perform binary search to find the appropriate zoom level
			float low = 0.001f;
			float high = zoom;
			while (high - low > 0.001f) { // Adjust the epsilon as needed
				zoom = (low + high) / 2;
				tempMatrices = new PoseStack();
				tempMatrices.scale(1.0F, 1.0F, 0.1F);
				tempMatrices.scale(zoom, zoom, zoom);
				tempMatrices.mulPose(rotQuat);
				maxRange = calculateViewableRange(tempMatrices);
				max = Math.max(maxRange[0], maxRange[1]);
				System.out.println(maxRange[1]);
				if (max < desiredMaxRange) {
					low = zoom;
				} else {
					high = zoom;
				}
			}
		}

		rotX = orx;
		rotY = ory;
		return zoom;
	}

	public float[] calculateViewableRange(PoseStack stack) {
		Matrix4f projectionMatrix = RenderSystem.getProjectionMatrix();
		Matrix4f modelViewMatrix = RenderSystem.getModelViewMatrix();

		// Calculate the corners of the view frustum in clip space
		Vector4f[] clipSpaceCorners = { new Vector4f(-1, -1, -1, 1), // near bottom left
				new Vector4f(1, -1, -1, 1), // near bottom right
				new Vector4f(1, 1, -1, 1), // near top right
				new Vector4f(-1, 1, -1, 1), // near top left
				new Vector4f(-1, -1, 1, 1), // far bottom left
				new Vector4f(1, -1, 1, 1), // far bottom right
				new Vector4f(1, 1, 1, 1), // far top right
				new Vector4f(-1, 1, 1, 1) // far top left
		};

		// Transform the corners to world space
		Matrix4f projectionMatrixInverse = new Matrix4f(projectionMatrix).invert();
		Matrix4f modelViewMatrixInverse = new Matrix4f(modelViewMatrix).invert();
		Matrix4f stackInverse = new Matrix4f(stack.last().pose()).invert();

		for (Vector4f corner : clipSpaceCorners) {
			corner.mul(1.0f / corner.w); // Perspective division
			projectionMatrixInverse.transform(corner);
			modelViewMatrixInverse.transform(corner);
			stackInverse.transform(corner);
		}

		// Find the minimum and maximum coordinates
		float minX = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE, maxZ = Float.MIN_VALUE;

		for (Vector4f corner : clipSpaceCorners) {
			minX = Math.min(minX, corner.x);
			minZ = Math.min(minZ, corner.z);
			maxX = Math.max(maxX, corner.x);
			maxZ = Math.max(maxZ, corner.z);
		}

		// Calculate the range in each dimension
		float rangeX = maxX - minX;
		float rangeZ = maxZ - minZ;
		
		return new float[] { rangeX / (width * 2f), rangeZ / (height * 2f)};
	}

	public Quaternionf createRotQuat() {
		float hy = Mth.DEG_TO_RAD * rotY * 0.5F;
		float hp = Mth.DEG_TO_RAD * rotX * 0.5F;
		float shy = Mth.sin(hy);
		float chy = Mth.cos(hy);
		float shp = Mth.sin(hp);
		float chp = Mth.cos(hp);
		float qw = chy * chp;
		float qx = shy * chp;
		float qy = chy * shp;
		float qz = shy * shp;
		return new Quaternionf(qx, qy, qz, qw);
	}

	private ScreenParticleHolder p_target = new ScreenParticleHolder();

	@Override
	public void render(GuiGraphics gui, int mX, int mY, float partial) {
		UIHelper.renderOverflowHidden(gui, this::renderBackground);

		// Calculate Time
		float time = (float) (minecraft.level.getGameTime() % 2400000L) + minecraft.getFrameTime();

		setupAndRenderCelestials(gui, width / 2, height / 2, createRotQuat(), time);

		ScreenParticleHandler.renderParticles(p_target);
		p_target.tick();
	}

	protected void setupAndRenderCelestials(GuiGraphics gui, int x, int y, Quaternionf rot, double t) {

		// Configure Render System
		RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableCull();

		// Apply to model view matrix
		PoseStack matrixStack = RenderSystem.getModelViewStack();
		matrixStack.pushPose();
		matrixStack.translate(x, y, 300.0D);
		matrixStack.translate(posX, posY, 0);
		matrixStack.scale(1.0F, -1.0F, 1.0F);
		matrixStack.scale(32.0F, 32.0F, 32.0F);
		RenderSystem.applyModelViewMatrix();

		// Render
		MultiBufferSource.BufferSource vcp = minecraft.renderBuffers().bufferSource();
		renderCelestials(gui, rot, vcp, t);
		vcp.endBatch();

		// Reset
		matrixStack.popPose();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}

	@Override
	public void resize(Minecraft p_96575_, int p_96576_, int p_96577_) {
		p_target.particles.clear();
		super.resize(p_96575_, p_96576_, p_96577_);
	}

	CelestialRenderer renderer = new CelestialRenderer();

	protected void renderCelestials(GuiGraphics gui, Quaternionf rot, MultiBufferSource c, double t) {
		PoseStack matrices = new PoseStack();
		matrices.scale(1.0F, 1.0F, 0.1F);
		matrices.scale(zoom, zoom, zoom);
		matrices.mulPose(rot);

		// Render star
		renderer.drawCelestial(c, matrices, 20, CelestialRenderInfo.from(system.star(), gui), t, p_target);

		// Render Planets
		for (Planet p : system.planets()) {
			renderer.drawCelestial(c, matrices, 20, CelestialRenderInfo.from(p, gui), t, p_target);
		}
	}

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

		// Rotate - Right Click
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_2) {

			float rotSpeed = 100;
			float maxYAng = 89.9f;

			this.rotX += (float) pDragX / (float) width * rotSpeed;
			this.rotY += (float) pDragY / (float) height * rotSpeed;

			this.rotY = Math.min(this.rotY, maxYAng);
			this.rotY = Math.max(this.rotY, -maxYAng);
		}

		// Pan - Middle Click or Left Click
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_1 || pButton == GLFW.GLFW_MOUSE_BUTTON_3) {
			this.posX += pDragX / 2;
			this.posY += pDragY / 2;
		}

		return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
	}

	@Override
	public boolean mouseScrolled(double mX, double mY, double delta) {
		this.zoom *= Math.pow(1.1, delta);
		System.out.println(zoom);
		return super.mouseScrolled(mX, mY, delta);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void renderBackground(PoseStack matrixStack) {
		UIHelper.bindStars();

		int textureSize = 1536;
		int currentX = 0;
		int currentY = 0;
		int uncoveredWidth = this.width;
		int uncoveredHeight = this.height;
		while (uncoveredWidth > 0) {
			while (uncoveredHeight > 0) {
				UIHelper.blit(matrixStack, currentX, currentY, textureSize, 0, Math.min(textureSize, uncoveredWidth),
						Math.min(textureSize, uncoveredHeight));
				uncoveredHeight -= textureSize;
				currentY += textureSize;
			}
			uncoveredWidth -= textureSize;
			currentX += textureSize;
			uncoveredHeight = this.height;
			currentY = 0;
		}
	}
}
