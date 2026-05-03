package com.machina.client.screen;

import org.lwjgl.glfw.GLFW;

import com.machina.api.client.screen.MUI;
import com.machina.api.util.math.MathUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MultiblockHousingScreen extends Screen {

	private final ResourceLocation mb;
	private final int imageWidth;
	private final int imageHeight;

	protected long aliveTicks = 0;
	protected float rotX, rotY;

	public MultiblockHousingScreen(ResourceLocation multiblock, Component title) {
		super(title);
		this.mb = multiblock;
		this.imageWidth = 235;
		this.imageHeight = 146;
	}

	protected int midWidth() {
		return (this.width - this.imageWidth) / 2;
	}

	protected int midHeight() {
		return (this.height - this.imageHeight) / 2;
	}

	@Override
	public void tick() {
		super.tick();
		this.aliveTicks++;
	}

	@Override
	public void render(GuiGraphics gui, int mouseX, int mouseY, float pt) {
		super.render(gui, mouseX, mouseY, pt);
		int i = midWidth();
		int j = midHeight();

		MUI.blitCommon(gui, i, j, 179, 94, 235, 146);
		MUI.drawStringVertical(gui, this.getTitle(), i + 245, j + 2);
		MUI.drawMultiblock(gui, mb, this.width / 2, this.height / 2 - 4, rotX, rotY, 1, pt);
		MUI.drawOverlay(gui, this.width, this.height, this.aliveTicks);
	}

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			this.rotX -= (float) pDragY / (float) height * 80f;
			this.rotY -= (float) pDragX / (float) width * 180f;
			this.rotX = MathUtil.clamp(this.rotX, 0f, 60f);
		}

		return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		int i = midWidth();
		int j = midHeight();

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && mx > i && mx < i + 235 && my > j - 73 && my < j + 78) {
			Minecraft.getInstance().setScreen(null);
		}

		return super.mouseClicked(mx, my, button);
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
}