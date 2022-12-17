package com.machina.client.screen;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.machina.client.ClientResearch;
import com.machina.client.util.UIHelper;
import com.machina.client.util.UIHelper.StippleType;
import com.machina.registration.init.ResearchInit;
import com.machina.research.Research;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

public class ResearchScreen extends Screen {

	public ResearchScreen(ITextComponent pTitle) {
		super(StringUtils.EMPTY);
	}

	float posX = 0;
	float posY = 0;
	float zoom = 1f;

	@Override
	public void render(MatrixStack stack, int pMouseX, int pMouseY, float pPartialTicks) {
		super.render(stack, pMouseX, pMouseY, pPartialTicks);

		// Background
		UIHelper.renderOverflowHidden(stack, this::renderContainerBackground, MatrixStack::toString);

		// Setup View
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
		GL11.glPopMatrix();

		// Draw the tree
		UIHelper.bindScifi();
		List<String> researched = ClientResearch.getResearch().getResearched();
		for (Research res : ResearchInit.RESEARCHES.values()) {
			if (res.getParent() == null || researched.contains(res.getParent().getId()))
				renderLines(stack, res);
		}
		for (Research res : ResearchInit.RESEARCHES.values()) {
			if (res.getParent() == null || researched.contains(res.getParent().getId()))
				render(stack, res);
		}

		// Help UI
		UIHelper.bindStcht();
		float x = this.width / 2 - 50;
		float y = this.height / 2 - 50;
		UIHelper.betterBlit(stack, x - 19, -y - 40, 0, 112, 16, 16, 128);
		UIHelper.betterBlit(stack, x - 19, -y - 20, 32, 112, 16, 16, 128);
		UIHelper.betterBlit(stack, x - 19, -y - 0, 48, 112, 16, 16, 128);
		UIHelper.betterBlit(stack, x - 19, -y + 20, 64, 112, 16, 16, 128);
		UIHelper.drawStringWithBorder(stack, StringUtils.translateScreen("research.sel"), x, -y - 36, 0xFF_00fefe,
				0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, StringUtils.translateScreen("research.pan"), x, -y - 16, 0xFF_00fefe,
				0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, StringUtils.translateScreen("research.zoo"), x, -y + 04, 0xFF_00fefe,
				0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, StringUtils.translateScreen("research.cls"), x, -y + 24, 0xFF_00fefe,
				0xFF_0e0e0e);
	}

	private void renderLines(MatrixStack stack, Research res) {
		if (res.getParent() != null) {
			Vector2f c = getResPos(res);
			Vector2f o = getResPos(res.getParent());
			UIHelper.line(stack, c.x + 9 * zoom, c.y + zoom, o.x + 9 * zoom, o.y + 18 * zoom, 0xFF_00fefe, 4 * zoom,
					StippleType.FULL);
		}
	}

	private void render(MatrixStack stack, Research res) {
		Vector2f c = getResPos(res);
		UIHelper.sizedBlitTransp(stack, c.x, c.y, 19 * zoom, 19 * zoom, 228, 184, 19, 19, 256);
	}

	private Vector2f getResPos(Research res) {
		Vector2f l = res.getLocation();
		return new Vector2f(posX - 9 * zoom + l.y * 30 * zoom, posY - 9 * zoom + l.x * 30 * zoom);
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

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
		// Pan - Middle Click
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_3) {

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
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
		zoom += pDelta / 10;
		if (zoom > 3)
			zoom = 3;
		if (zoom < 0.5f)
			zoom = 0.5f;
		return super.mouseScrolled(pMouseX, pMouseY, pDelta);
	}
}
