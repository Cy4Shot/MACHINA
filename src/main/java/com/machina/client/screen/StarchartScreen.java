package com.machina.client.screen;

import org.joml.Matrix4f;

import com.machina.api.client.UIHelper;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.api.util.math.VecUtil;
import com.machina.client.model.celestial.CelestialModel;
import com.machina.client.model.celestial.StarModel;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

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

	@Override
	public void render(GuiGraphics gui, int p_281550_, int p_282878_, float p_282465_) {
		UIHelper.renderOverflowHidden(gui, this::renderBackground);

		// Circle
		double[] buf = UIHelper.ellipse(0, 0, 50f, 50f, 100, 0);
		UIHelper.drawLines(gui, buf, 0xFF_00fefe);

		// Star
		CelestialModel model = new StarModel();
		gui.pose().pushPose();
		gui.pose().mulPoseMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
		gui.pose().scale(.3f, .3f, .3f);
		gui.pose().mulPose(VecUtil.rotationDegrees(VecUtil.YN, p_281550_));
		UIHelper.drawCelestial(gui, model);
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
