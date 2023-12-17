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
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

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
		RenderSystem.setShaderTexture(0, new MachinaRL("textures/gui/starchart/star_bg.png"));

		// Setup Pose
		gui.pose().pushPose();
		gui.pose().mulPoseMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
		gui.pose().scale(.3f, .3f, .3f);
		gui.pose().mulPose(VecUtil.rotationDegrees(VecUtil.YN, p_281550_));

		gui.pose().translate(-.5f, -.5f, -.5f);

		// Create Buffer
		BufferBuilder buffer = Tesselator.getInstance().getBuilder();
		VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
		buffer.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		for (BakedQuad quad : model.getQuads(null, null, new LegacyRandomSource(0L))) {
			buffer.putBulkData(gui.pose().last(), quad, 1f, 1f, 1f, 1.0F, 15728880, OverlayTexture.NO_OVERLAY, true);
		}

		// Draw
		vertexBuffer.bind();
		vertexBuffer.upload(buffer.end());
		vertexBuffer.drawWithShader(gui.pose().last().pose(),
				VecUtil.orthographic(1f, (float) gui.guiHeight() / gui.guiWidth(), 0.0001f, 1000f),
//				RenderSystem.getProjectionMatrix(),
				GameRenderer.getPositionTexShader());
		VertexBuffer.unbind();

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
