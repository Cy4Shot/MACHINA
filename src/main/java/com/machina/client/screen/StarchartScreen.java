package com.machina.client.screen;

import java.util.List;
import java.util.stream.Collectors;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import com.machina.api.client.ClientStarchart;
import com.machina.api.client.UIHelper;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.api.util.math.VecUtil;
import com.machina.client.model.celestial.CelestialModel;
import com.machina.client.model.celestial.PlanetModel;
import com.machina.client.model.celestial.StarModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;

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

		SolarSystem system = ClientStarchart.system;
//		double radius = system.star().radius() * zoom;
		double radius = zoom;

		// Calculate Planet Pos
		List<Vector4f> planets = system.planets().stream().map(p -> {
			float x = (float) (Math.cos(p.where_in_orbit()) * p.a());
			float z = (float) (Math.sin(p.where_in_orbit()) * p.a());
			return new Vector4f(x, 0, z, zoom);
		}).collect(Collectors.toList());

		// Render Starchart
		Vector3f cameraPos = new Vector3f(0, 0, -15f);
		Vector3f focusPoint = new Vector3f(0, 0, 0);
		Quaternionf cameraRot = eulerToQuaternion(rotX, rotY);

		// Add Planets
		List<Pair<Float, CelestialModel>> l = planets.stream()
				.map(s -> calculateNewPosition(s.x, s.y, s.z, s.w, cameraPos, cameraRot, focusPoint))
				.map(s -> Pair.of(s.w, new PlanetModel().offset(s.x, s.y, s.z))).collect(Collectors.toList());

		// Add Star
		Vector4f sp = calculateNewPosition(0, 0, 0, (float) radius, cameraPos, cameraRot, focusPoint);
		l.add(Pair.of(sp.w, new StarModel().offset(sp.x, sp.y, sp.z)));

		// Z-Sort
		CelestialModel[] models = l.stream().sorted((a, b) -> Float.compare(b.getFirst(), a.getFirst()))
				.map(Pair::getSecond).toArray(CelestialModel[]::new);

		// Draw
		for (CelestialModel model : models) {
			UIHelper.drawCelestial(gui, model, 0, 0, 0, .1f);
		}

		UIHelper.drawLines(gui,
				UIHelper.orbit(0, 0, zoom, 0f, 0, 100),
				0xFF_00fefe, 0, 0, 0, 0.1f);
	}

	private static Quaternionf eulerToQuaternion(float yaw, float pitch) {
		return new Quaternionf().rotationYXZ((float) Math.toRadians(yaw), 0.0f, (float) Math.toRadians(pitch));
	}

	private static Vector4f calculateNewPosition(float x, float y, float z, float scale, Vector3f cameraPos,
			Quaternionf cameraRot, Vector3f focusPoint) {
		Matrix4f rotationMatrix = new Matrix4f().rotate(cameraRot);
		Vector4f newPos = new Vector4f(x, y, z, 1).mul(rotationMatrix).add(-cameraPos.x, -cameraPos.y, -cameraPos.z, 0);
		float distance = new Vector3f(newPos.x, newPos.y, newPos.z).distance(cameraPos);
		newPos = newPos.mul(scale * distance);
		return new Vector4f(newPos.x, newPos.y, newPos.z, distance);
	}

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

		// Rotate - Right Click
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_2) {

			float rotSpeed = 100;
			float maxYAng = 45;

			this.rotX -= (float) pDragX / (float) width * rotSpeed;
			this.rotY -= (float) pDragY / (float) height * rotSpeed;

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
