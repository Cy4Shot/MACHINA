package com.machina.client.screen;

import com.machina.client.util.UIHelper;
import com.machina.util.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;

public class GoBeyondScreen extends Screen {

	public GoBeyondScreen() {
		super(StringUtils.EMPTY);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void render(MatrixStack stack, int pMouseX, int pMouseY, float pPartialTicks) {
		UIHelper.resetColor();
		this.renderBackground(stack);
		super.render(stack, pMouseX, pMouseY, pPartialTicks);
	}

	@Override
	public void renderBackground(MatrixStack stack) {
		this.fillGradient(stack, 0, 0, this.width, this.height, -1072689136, -804253680);
		this.fillGradient(stack, 0, 0, this.width, this.height, -1072689136, -804253680);
		
		UIHelper.bindScifi();

		// Back
		int xSize = 236, ySize = 99;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 2, 3, xSize, ySize);
		this.blit(stack, x + 50, y + 10, 3, 130, 135, 18);

		// Data
		draw(stack, StringUtils.translateScreen("go_beyond.title"), x + 117, y + 14, 0xFF_00fefe, true);
		draw(stack, StringUtils.translateScreen("go_beyond.desc1"), x + 117, y + 40, 0xFF_00fefe, true);
		draw(stack, StringUtils.translateScreen("go_beyond.desc2"), x + 117, y + 50, 0xFF_00fefe, true);

		// Footer
		draw(stack, "MACHINA://GO_BEYOND/", x + 8, y + 82, 0xFF_00fefe, false);
	}

	private static void draw(MatrixStack stack, String title, int x, int y, int col, boolean centered) {
		if (centered)
			UIHelper.drawCenteredStringWithBorder(stack, title, x, y, col, 0xFF_0e0e0e);
		else
			UIHelper.drawStringWithBorder(stack, title, x, y, col, 0xFF_0e0e0e);
	}
}
