package com.machina.client.screen;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.machina.client.ClientStarchart;
import com.machina.client.util.ClientTimer;
import com.machina.client.util.UIHelper;
import com.machina.client.util.UIHelper.StippleType;
import com.machina.planet.PlanetData;
import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.init.AttributeInit;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class StarchartScreen extends Screen {

	public List<Planet> planets = new ArrayList<Planet>();
	public Planet selected;
	float rotX = -45;
	float rotY = 60;
	float posX = 0;
	float posY = 0;
	float zoom = 1f;
	boolean followPos = false;
	boolean followZoom = false;
	boolean hasTopButtons = true;

	public StarchartScreen() {
		super(StringUtils.EMPTY);
	}

	@Override
	protected void init() {
		super.init();

		planets.clear();

		for (int i = 0; i < ClientStarchart.getStarchart().size(); i++) {
			PlanetData data = ClientStarchart.getPlanetData(i);
			if (!data.equals(PlanetData.NONE))
				planets.add(new Planet(data));
		}
	}

	@Override
	public void render(MatrixStack stack, int pMouseX, int pMouseY, float pPartialTicks) {
		super.render(stack, pMouseX, pMouseY, pPartialTicks);

		// Follow
		if (selected != null && followPos) {
			double smoothing = 0.01D;
			double time = selected.getAngle();
			double rX = (selected.dist * Math.cos(rotY) * zoom) / 2;
			double rY = selected.dist * Math.sin(rotY) * zoom;
			double x = Math.sin(time + Math.PI) * rX;
			double y = Math.cos(time + Math.PI) * rY;

			posX += (x - posX) * smoothing;
			posY += (y - posY) * smoothing;
		}

		if (selected != null && followZoom) {
			double smoothing = 0.01D;
			zoom += (2.5D - zoom) * smoothing;
		}

		// Background
		UIHelper.renderOverflowHidden(stack, this::renderContainerBackground, MatrixStack::toString);

		// Background Grid.
		GL11.glPushMatrix();
		Matrix4f cam = Matrix4f.createScaleMatrix(1, 1, 1);
		Matrix4f view = Matrix4f.orthographic(width, -height, -9999, 9999);
		view.translate(new Vector3f(1, 1, 0));
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		FloatBuffer fb = BufferUtils.createFloatBuffer(16 * Float.SIZE);
		fb.rewind();
		view.store(fb);
		fb.flip();
		fb.clear();
		GL11.glMultMatrixf(fb);
		fb.clear();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		fb.rewind();
		cam.store(fb);
		fb.flip();
		fb.clear();
		GL11.glMultMatrixf(fb);
		GL11.glPushMatrix();

		float gridScale = height / 3 / 3.5F;
		float gridSize = 7000;

		Matrix4f mat = stack.last().pose().copy();
		mat.translate(new Vector3f(posX / 2, posY / 2, 0));
		mat.multiply(new Quaternion(new Vector3f(1, 0, 0), rotY, true));
		mat.multiply(new Quaternion(new Vector3f(0, 0, 1), rotX, true));
		mat.multiply(zoom);

		gridSize += gridScale / 2;
		for (float v = -gridSize; v <= gridSize; v += gridScale) {

			int col = 0x44_00fefe;
			float f3 = (col >> 24 & 255) / 255.0F;
			float f = (col >> 16 & 255) / 255.0F;
			float f1 = (col >> 8 & 255) / 255.0F;
			float f2 = (col & 255) / 255.0F;
			BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();

			RenderSystem.enableBlend();
			RenderSystem.disableTexture();
			RenderSystem.defaultBlendFunc();
			RenderSystem.lineWidth(1);
			bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.vertex(mat, v, gridSize, 0.0F).color(f, f1, f2, f3).endVertex();
			bufferbuilder.vertex(mat, v, -gridSize, 0.0F).color(f, f1, f2, f3).endVertex();
			bufferbuilder.vertex(mat, gridSize, v, 0.0F).color(f, f1, f2, f3).endVertex();
			bufferbuilder.vertex(mat, -gridSize, v, 0.0F).color(f, f1, f2, f3).endVertex();
			bufferbuilder.end();
			WorldVertexBufferUploader.end(bufferbuilder);
			RenderSystem.lineWidth(1f);
			RenderSystem.enableTexture();
			RenderSystem.disableBlend();
		}

		// Planet Rings
		for (Planet planet : planets) {
			float x = (float) (planet.dist * Math.cos(rotY) * zoom) / 2;
			float y = (float) (planet.dist * Math.sin(rotY) * zoom);
			UIHelper.ellipse(stack, posX, posY, x, y, 100, 0, 0xFF_00fefe, 1, StippleType.FULL);

			if (selected != null && selected.equals(planet)) {
				double time = planet.getAngle();
				UIHelper.line(stack, posX, posY, (int) (posX + Math.sin(time) * x), (int) (posY + Math.cos(time) * y),
						0xFF_00fefe, 1f, StippleType.DASHED);
			}
		}

		// Planets
		for (Planet planet : planets) {

			double time = planet.getAngle();
			float rX = (float) (planet.dist * Math.cos(rotY) * zoom) / 2;
			float rY = (float) (planet.dist * Math.sin(rotY) * zoom);
			float x = (float) (posX + Math.sin(time) * rX);
			float y = (float) (posY + Math.cos(time) * rY);

			float s0 = 8 * zoom / 1.5f;
			float s1 = 24 * zoom / 1.5f;

			UIHelper.bindStcht();
			UIHelper.sizedBlitTransp(stack, x - s1 / 2, y - s1 / 2, s1, s1, 104, 104, 24, 24, 128);
			UIHelper.sizedBlit(stack, x - s0, y - s0, s0 * 2, s0 * 2, 48 + (planet.icon % 5) * 16,
					Math.floorDiv(planet.icon, 5) * 16, 16, 16, 128);

			if (selected != null && selected.equals(planet)) {
				float o = (int) (2 * Math.sin(UIHelper.levelTicks() / 4f));
				UIHelper.box(stack, x - s0 - o, y - s0 - o, x + s0 + o, y + s0 + o, 0xFF_00fefe, 4f, StippleType.FULL);
			}
		}

		GL11.glPopMatrix();
		GL11.glPopMatrix();

		// Sun
		UIHelper.bindStcht();
		float s0 = 64 * zoom;
		float s1 = 48 * zoom;
		UIHelper.sizedBlitTransp(stack, posX - s0 / 2, posY - s0 / 2, s0, s0, 0, 48, 64, 64, 128);
		UIHelper.sizedBlit(stack, posX - s1 / 2, posY - s1 / 2, s1, s1, 0, 0, 48, 48, 128);

		// Help UI
		float x = this.width / 2 - 50;
		float y = this.height / 2 - 50;
		UIHelper.betterBlit(stack, x - 19, -y - 40, 0, 112, 16, 16, 128);
		UIHelper.betterBlit(stack, x - 19, -y - 20, 16, 112, 16, 16, 128);
		UIHelper.betterBlit(stack, x - 19, -y - 0, 32, 112, 16, 16, 128);
		UIHelper.betterBlit(stack, x - 19, -y + 20, 48, 112, 16, 16, 128);
		UIHelper.betterBlit(stack, x - 19, -y + 40, 64, 112, 16, 16, 128);
		UIHelper.drawStringWithBorder(stack, StringUtils.translateScreen("starchart.sel"), x, -y - 36, 0xFF_00fefe,
				0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, StringUtils.translateScreen("starchart.rot"), x, -y - 16, 0xFF_00fefe,
				0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, StringUtils.translateScreen("starchart.pan"), x, -y + 04, 0xFF_00fefe,
				0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, StringUtils.translateScreen("starchart.zoo"), x, -y + 24, 0xFF_00fefe,
				0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, StringUtils.translateScreen("starchart.cls"), x, -y + 44, 0xFF_00fefe,
				0xFF_0e0e0e);

		// Select UI
		if (selected != null) {
			UIHelper.bindScifi();
			UIHelper.betterBlit(stack, x - 49, y - 62, 151, 103, 88, 81, 256);
			UIHelper.betterBlit(stack, x - 37, y - 42, 4, 130, 69, 1, 256);
			UIHelper.drawCenteredStringWithBorder(stack, selected.name, x - 2, y - 53, 0xFF_00fefe, 0xFF_0e0e0e);
			int i = 0;
			for (PlanetTrait t : selected.data.getTraits()) {
				UIHelper.drawCenteredStringWithBorder(stack, t.toString(), x - 2, y - 38 + i * 10, t.getColor(),
						0xFF_0e0e0e);
				i++;
			}
		}

		renderAdditional(stack, pMouseX, pMouseY, pPartialTicks);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

	}

	public void renderAdditional(MatrixStack stack, int mX, int mY, float par) {
		float y = this.height / 2 - 50;
		// Switch Tab Buttons
		UIHelper.bindScifi();
		UIHelper.betterBlit(stack, -9 + 15, -y - 40, 228, 184, 19, 19, 256);
		UIHelper.betterBlit(stack, -9 - 15, -y - 40, 162, 230, 19, 19, 256);
		UIHelper.betterBlit(stack, -9 - 14, -y - 39, 112, 240, 16, 16, 256);
		UIHelper.betterBlit(stack, -9 + 16, -y - 39, 128, 240, 16, 16, 256);
		UIHelper.bindPrgrs();
		UIHelper.betterBlit(stack, -9 + 10, -y - 39, 0, 239, 29, 17, 256);
		UIHelper.betterBlit(stack, -9 - 20, -y - 39, 0, 222, 29, 17, 256);
		UIHelper.betterBlit(stack, -9 - 41, -y - 42, 29, 243, 19, 13, 256);
		UIHelper.betterBlit(stack, -9 + 40, -y - 42, 78, 243, 19, 13, 256);
	}

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

		// Rotate - Right Click
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_2) {
			followPos = false;
			followZoom = false;
			this.rotX -= (float) pDragX / (float) width * 180f;
		}

		// Pan - Middle Click
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_3) {

			followPos = false;
			followZoom = false;

			this.posX += pDragX / 2;
			this.posY += pDragY / 2;

			if (posX > width / 3)
				posX = width / 3;
			if (posX < -width / 3)
				posX = -width / 3;

			if (posY > height / 3)
				posY = height / 3;
			if (posY < -height / 3)
				posY = -height / 3;
		}

		return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pButton) {

		if (pButton == GLFW.GLFW_MOUSE_BUTTON_1) {
			Collections.shuffle(planets);
			for (Planet planet : planets) {

				if (planet == selected)
					continue;

				double time = planet.getAngle();
				double rX = (planet.dist * Math.cos(rotY) * zoom) / 2;
				double rY = (planet.dist * Math.sin(rotY) * zoom);
				double x = (posX + Math.sin(time) * rX) + width / 2;
				double y = (posY + Math.cos(time) * rY) + height / 2;

				double s = 12 * zoom;

				if (pX > x - s && pX < x + s && pY > y - s && pY < y + s) {
					UIHelper.click();
					selected = planet;
					followPos = true;
					followZoom = true;
					return true;
				}
			}

			if (selected != null) {
				UIHelper.click();
				selected = null;
				followPos = false;
				followZoom = false;
				return true;
			}
		}

		float x = this.width / 2;
		if (pButton == 0 && hasTopButtons && pX > x + 6 && pX < x + 25 && pY > 10 && pY < 29) {
			UIHelper.click();
			Minecraft.getInstance().setScreen(new ResearchScreen());
			return true;
		}

		return super.mouseReleased(pX, pY, pButton);
	}

	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
		followPos = false;
		followZoom = false;
		zoom += pDelta / 10;
		if (zoom > 3)
			zoom = 3;
		if (zoom < 0.5f)
			zoom = 0.5f;
		return super.mouseScrolled(pMouseX, pMouseY, pDelta);
	}

	private void renderContainerBackground(MatrixStack matrixStack) {
		assert minecraft != null;

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

			// Decrement
			uncoveredWidth -= textureSize;
			currentX += textureSize;

			// Reset
			uncoveredHeight = this.height;
			currentY = 0;
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	class Planet {

		public PlanetData data;
		public int dist, icon;
		public float phase, speed;
		public String name;

		public Planet(PlanetData data) {
			this.data = data;
			this.dist = (int) (data.getAttribute(AttributeInit.DISTANCE) * ((height - 150) / 20) + 150);
			this.icon = data.getAttribute(AttributeInit.PLANET_ICON);
			this.phase = data.getAttribute(AttributeInit.PHASE_SHIFT);
			this.speed = data.getAttribute(AttributeInit.ORBITAL_SPEED);
			this.name = data.getAttribute(AttributeInit.PLANET_NAME);
		}

		public double getAngle() {
			return Math.toRadians(ClientTimer.totalTick / 2 * speed + phase - rotX);
		}
	}
}