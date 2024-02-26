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
import com.machina.api.util.math.MathUtil;
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

	float rotX = 90;
	float rotY = 0;
	float posX = 0;
	float posY = 0;
	float zoom;

	public StarchartScreen(SolarSystem s) {
		super(Component.empty());
		this.system = s;
	}
	
	@Override
	protected void init() {
		super.init();

		zoom = calculateZoom(system.maxAphelion(), height, 40);
	}
	
	public float calculateZoom(double targetAphelion, int target, int padding) {
		float max = (float) (targetAphelion * 2);
		
		return (float) MathUtil.binarySearch(0.001D, 100D, (target - padding) / 2f, zoom -> {
			float z = (float) zoom;
			PoseStack matrixStack = RenderSystem.getModelViewStack();
			matrixStack.pushPose();
			matrixStack.translate(width / 2, height / 2, 300.0D);
			matrixStack.scale(1.0F, -1.0F, 1.0F);
			matrixStack.scale(32.0F, 32.0F, 32.0F);
			
			PoseStack matrices = new PoseStack();
			matrices.scale(1.0F, 1.0F, 0.1F);
			matrices.scale(z, z, z);
			matrices.mulPose(createRotQuat(90, 0));
			matrices.pushPose();
			
			Vector4f spos = new Vector4f(max, max, 0, 1);
			Matrix4f stm = new Matrix4f(matrices.last().pose());
			Matrix4f mvp = new Matrix4f(RenderSystem.getProjectionMatrix()).mul(matrixStack.last().pose());

			stm.transform(spos);
			mvp.transform(spos);

			Vector4f norm = spos.div(spos.w);
			float x = (1.0f + norm.x) * 0.5f * target;
			float y = (1.0f - norm.y) * 0.5f * target;
			
			matrices.popPose();
			matrixStack.popPose();
			
			return (double) Math.max(x, y);
		}, 0.01);
	}

	public Quaternionf createRotQuat(float x, float y) {
		float hy = Mth.DEG_TO_RAD * x * 0.5F;
		float hp = Mth.DEG_TO_RAD * y * 0.5F;
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

		setupAndRenderCelestials(gui, width / 2, height / 2, createRotQuat(rotX, rotY), time);

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

			this.rotX += (float) pDragY / (float) width * rotSpeed;
			this.rotY += (float) pDragX / (float) height * rotSpeed;

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
