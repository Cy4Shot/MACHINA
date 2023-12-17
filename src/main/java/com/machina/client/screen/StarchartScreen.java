package com.machina.client.screen;

import org.joml.Matrix4f;

import com.machina.api.client.UIHelper;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.math.VecUtil;
import com.machina.client.model.StarModel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.LegacyRandomSource;

public class StarchartScreen extends Screen {

	SolarSystem system;

	float rotX = -45;
	float rotY = 60;
	float posX = 0;
	float posY = 0;
	float zoom = 1f;

	public StarchartScreen(SolarSystem s) {
		super(Component.empty());
		this.system = s;
	}

	@SuppressWarnings("resource")
	@Override
	public void render(GuiGraphics gui, int p_281550_, int p_282878_, float p_282465_) {
		UIHelper.renderOverflowHidden(gui, this::renderBackground);

		double[] buf = UIHelper.ellipse(0, 0, 50f, 50f, 100, 0);
		UIHelper.drawLines(gui, buf, 0xFF_00fefe);

		// Create Model
		StarModel model = new StarModel();
//		RenderSystem.setShaderTexture(0, new MachinaRL("textures/gui/starchart/star_bg.png"));
//
//		// Setup Pose
//		gui.pose().pushPose();
//		gui.pose().mulPoseMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
//		gui.pose().scale(.3f, .3f, .3f);
//		gui.pose().mulPose(VecUtil.rotationDegrees(VecUtil.YN, p_281550_));
//
//		// Create Buffer
//		BufferBuilder buffer = Tesselator.getInstance().getBuilder();
//		VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
//		buffer.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
//		for (BakedQuad quad : model.getQuads(null, null, new LegacyRandomSource(0L))) {
//			buffer.putBulkData(gui.pose().last(), quad, 1f, 1f, 1f, 1.0F, 15728880, OverlayTexture.NO_OVERLAY, true);
//		}
//
//		// Draw
//		vertexBuffer.bind();
//		vertexBuffer.upload(buffer.end());
//		vertexBuffer.drawWithShader(gui.pose().last().pose(),
//				VecUtil.orthographic(1f, (float) gui.guiHeight() / gui.guiWidth(), 0.01f, 1000f),
//				GameRenderer.getPositionTexColorShader());
//		VertexBuffer.unbind();

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tesselator.getBuilder();
		Matrix4f matrix4f = (new Matrix4f()).setPerspective(1.4835298F,
				(float) gui.guiWidth() / (float) gui.guiHeight(), 0.05F, 10.0F);
		RenderSystem.backupProjectionMatrix();
		RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.DISTANCE_TO_ORIGIN);
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.setIdentity();
		posestack.mulPose(Axis.XP.rotationDegrees(180.0F));
		posestack.scale(.3f, .3f, .3f);
		posestack.mulPose(VecUtil.rotationDegrees(VecUtil.YN, p_281550_));
		RenderSystem.applyModelViewMatrix();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.enableBlend();
		RenderSystem.disableCull();
		RenderSystem.depthMask(false);

		for (int j = 0; j < 4; ++j) {
			posestack.pushPose();
			float f = ((float) (j % 2) / 2.0F - 0.5F) / 256.0F;
			float f1 = ((float) (j / 2) / 2.0F - 0.5F) / 256.0F;
			posestack.translate(f, f1, 0.0F);
			RenderSystem.applyModelViewMatrix();

			for (int k = 0; k < 6; ++k) {
				RenderSystem.setShaderTexture(0, new MachinaRL("textures/gui/starchart/star_bg.png"));
				bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
				for (BakedQuad quad : model.getQuads(null, null, new LegacyRandomSource(0L))) {
					bufferbuilder.putBulkData(gui.pose().last(), quad, 1f, 1f, 1f, 1.0F, 15728880,
							OverlayTexture.NO_OVERLAY, true);
				}

				tesselator.end();
			}

			posestack.popPose();
			RenderSystem.applyModelViewMatrix();
			RenderSystem.colorMask(true, true, true, false);
		}

		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.restoreProjectionMatrix();
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.depthMask(true);
		RenderSystem.enableCull();
		RenderSystem.enableDepthTest();

		// Pop Pose
		gui.pose().popPose();
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
