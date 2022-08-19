package com.machina.client.screen;

import com.machina.client.ClientStarchart;
import com.machina.client.util.IStarchartSelector;
import com.machina.client.util.UIHelper;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;

public class StarchartSelectScreen extends StarchartScreen {

	IStarchartSelector selector;

	public static void select(IStarchartSelector selector) {
		Minecraft.getInstance().setScreen(new StarchartSelectScreen(selector));
	}

	public StarchartSelectScreen(IStarchartSelector s) {
		this.selector = s;
	}

	@Override
	public void renderAdditional(MatrixStack stack, int pX, int pY, float pPartialTicks) {
		if (selected != null) {
			UIHelper.bindTrmnl();

			float x = this.width / 2 - 50;
			float y = this.height / 2 - 50;
			int mX = (int) (pX - this.width / 2);
			int mY = (int) (pY - this.height / 2);
			if (mX > x - 46 && mX < x - 46 + 17 && mY > y && mY < y + 17) {
				UIHelper.betterBlit(stack, x - 46, y, 237, 73, 17, 17, 256);
			} else {
				UIHelper.betterBlit(stack, x - 46, y, 237, 56, 17, 17, 256);
			}
		}

	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pButton) {
		if (pButton == 0) {
			int x = this.width / 2 - 50;
			int y = this.height / 2 - 50;
			int mX = (int) (pX - this.width / 2);
			int mY = (int) (pY - this.height / 2);
			if (mX > x - 46 && mX < x - 46 + 17 && mY > y && mY < y + 17 && this.selected != null) {
				onSelected();
				UIHelper.click();
				return true;
			}
		}
		return super.mouseReleased(pX, pY, pButton);
	}

	public void onSelected() {
		if (this.selected != null) {
			selector.accept(ClientStarchart.getId(this.selected.data));
		}
	}
}