package com.machina.client.screen;

import org.jetbrains.annotations.NotNull;

import com.machina.api.starchart.obj.SolarSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class StarchartScreen extends Screen {

	private final StarchartRenderable renderable;

	public StarchartScreen(SolarSystem s) {
		super(Component.empty());
		this.renderable = new StarchartRenderable(s, false);
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mX, int mY, float partial) {
		renderable.render(gui, 0, 0, 0, 0, width, height);
	}

	@Override
	public boolean mouseClicked(double mX, double mY, int button) {
		if (renderable.mouseClicked(mX, mY, button)) {
			return true;
		}
		return super.mouseClicked(mX, mY, button);
	}

	@Override
	public boolean mouseDragged(double mX, double mY, int button, double dX, double dY) {
		if (renderable.mouseDragged(button, dX, dY, width, height)) {
			return true;
		}
		return super.mouseDragged(mX, mY, button, dX, dY);
	}

	@Override
	public boolean mouseScrolled(double mX, double mY, double deltaX, double deltaY) {
		if (renderable.mouseScrolled(deltaY)) {
			return true;
		}
		return super.mouseScrolled(mX, mY, deltaX, deltaY);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
