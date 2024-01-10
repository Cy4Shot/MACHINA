package com.machina.client.screen;

import java.util.Arrays;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import com.machina.api.client.UIHelper;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.client.model.celestial.CelestialModel;
import com.machina.client.model.celestial.StarModel;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

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

		// Render Starchart
		gui.pose().pushPose();
		gui.pose().scale(zoom, zoom, zoom);

		Vector3f cameraPos = new Vector3f(0, 0, -15f);

		Vector3f focusPoint = new Vector3f(0, 0, 0);

		// Calculate camera rotation based on mouse positions
		float nX = (2.0f * mX / width) - 1.0f;
		float nY = 1.0f - (2.0f * mY / height);
		Vector3f cameraRot = calculateCameraRotation(nX * 30, 0);

		Vector4f[] stars = new Vector4f[] { new Vector4f(0, 0, 0, 0.1f), new Vector4f(10, 0, 0, 0.1f) };
		Vector4f[] newStars = new Vector4f[stars.length];
		for (int i = 0; i < stars.length; i++) {
			newStars[i] = calculateNewPosition(stars[i].x, stars[i].y, stars[i].z, stars[i].w, cameraPos, cameraRot,
					focusPoint);
		}

		Vector4f[] sortedStars = Arrays.stream(newStars).sorted((a, b) -> Float.compare(b.w, a.w))
				.toArray(Vector4f[]::new);

		System.out.println(sortedStars[1].w);

		CelestialModel[] models = new CelestialModel[sortedStars.length];
		for (int i = 0; i < sortedStars.length; i++) {
			models[i] = new StarModel().offset(sortedStars[i].x, sortedStars[i].y, sortedStars[i].z);
		}

		UIHelper.drawCelestials(gui, models, 0, 0, 0, .1f);

//		UIHelper.drawLines(gui,
//				UIHelper.orbit(0, 0, 0.1f, 0f, 0, 100),
//				0xFF_00fefe, 1, 0, 0, 1f);

		gui.pose().popPose();
	}

	private static Vector3f calculateCameraRotation(float mouseX, float mouseY) {
		float sensitivity = 0.1f;
		float maxVerticalAngle = 89.0f;
		float yaw = mouseX * sensitivity;
		float pitch = mouseY * sensitivity;
		pitch = Math.min(Math.max(pitch, -maxVerticalAngle), maxVerticalAngle);
		return new Vector3f(-pitch, -yaw, 0);
	}

	private static Vector4f calculateNewPosition(float x, float y, float z, float scale, Vector3f cameraPos,
			Vector3f cameraRot, Vector3f focusPoint) {
		Matrix4f rotationMatrix = new Matrix4f().rotate(cameraRot.x, new Vector3f(1, 0, 0))
				.rotate(cameraRot.y, new Vector3f(0, 1, 0)).rotate(cameraRot.z, new Vector3f(0, 0, 1));
		Vector4f newPos = new Vector4f(x, y, z, 1).mul(rotationMatrix).add(-cameraPos.x, -cameraPos.y, -cameraPos.z, 0);
		float distance = new Vector3f(newPos.x, newPos.y, newPos.z).distance(cameraPos);
		newPos = newPos.mul(scale * distance);
		return new Vector4f(newPos.x, newPos.y, newPos.z, distance);

	}

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

		// Rotate - Right Click
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_2) {
			this.rotX -= (float) pDragY / (float) height * 80f;
			this.rotY -= (float) pDragX / (float) width * 180f;
		}

		return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
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
