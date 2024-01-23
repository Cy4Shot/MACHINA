package com.machina.client.screen;

import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;

import com.machina.api.client.UIHelper;
import com.machina.api.client.planet.CelestialRenderInfo;
import com.machina.api.client.planet.CelestialRenderer;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.SolarSystem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class StarchartScreen extends Screen {

	SolarSystem system;

	float rotX = 0;
	float rotY = 0;
	float posX = 0;
	float posY = 0;
	float zoom = 1f;

	public StarchartScreen(SolarSystem s) {
		super(Component.empty());
		this.system = s;
	}

	@Override
	public void render(GuiGraphics gui, int mX, int mY, float partial) {
		UIHelper.renderOverflowHidden(gui, this::renderBackground);

		// Calculate Camera Rotation
		float yaw = Mth.DEG_TO_RAD * rotY;
		float pitch = Mth.DEG_TO_RAD * rotX;
		float halfYaw = yaw * 0.5F;
		float halfPitch = pitch * 0.5F;
		float qw = Mth.cos(halfYaw) * Mth.cos(halfPitch);
		float qx = Mth.sin(halfYaw) * Mth.cos(halfPitch);
		float qy = Mth.cos(halfYaw) * Mth.sin(halfPitch);
		float qz = Mth.sin(halfYaw) * Mth.sin(halfPitch);
		Quaternionf rot = new Quaternionf(qx, qy, qz, qw);

		// Calculate Time
		float time = (float) (minecraft.level.getGameTime() % 2400000L) + minecraft.getFrameTime();

		setupAndRenderCelestials(width / 2, height / 2, Vec3.ZERO, rot, time);

	}

	static CelestialRenderer renderer = (CelestialRenderer) (new CelestialRenderer()
			.setPosColorTexLightmapDefaultFormat());

	protected void setupAndRenderCelestials(int x, int y, Vec3 pos, Quaternionf rot, double t) {
		// Configure Render System
		RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableCull();

		// Transform Matrix Stack
		PoseStack matrixStack = RenderSystem.getModelViewStack();
		matrixStack.pushPose();
		matrixStack.translate(x, y, 300.0D);
		matrixStack.translate(8.0D, 8.0D, 0.0D);
		matrixStack.scale(1.0F, -1.0F, 1.0F);
		matrixStack.scale(32.0F, 32.0F, 32.0F);
		RenderSystem.applyModelViewMatrix();

		// Render
		MultiBufferSource.BufferSource vcp = minecraft.renderBuffers().bufferSource();
		renderCelestials(pos, rot, vcp, t);
		vcp.endBatch();

		// Reset
		matrixStack.popPose();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}

	protected void renderCelestials(Vec3 pos, Quaternionf rot, MultiBufferSource c, double t) {
		PoseStack matrices = new PoseStack();
		matrices.scale(1.0F, 1.0F, 0.1F);
		matrices.translate(0.0D, -0.925000011920929D, 0.0D);
		matrices.translate(pos.x, pos.y, pos.z);
		matrices.mulPose(rot);

		// Render star
		renderer.drawCelestial(c, matrices, 20, CelestialRenderInfo.star(system.star()), t);

		// Render Planets
		for (Planet p : system.planets()) {
			renderer.drawCelestial(c, matrices, 20, CelestialRenderInfo.planet(p), t);
		}
	}

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

		// Rotate - Right Click
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_2) {

			float rotSpeed = 100;
			float maxYAng = 45;

			this.rotX += (float) pDragX / (float) width * rotSpeed;
			this.rotY += (float) pDragY / (float) height * rotSpeed;

			this.rotY = Math.min(this.rotY, maxYAng);
			this.rotY = Math.max(this.rotY, -maxYAng);
		}

		return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
	}

	@Override
	public boolean mouseScrolled(double mX, double mY, double delta) {
		this.zoom *= Math.pow(1.1, -delta);
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
