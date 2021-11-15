package com.cy4.machina.client.gui.element;

import com.cy4.machina.client.gui.StarchartScreen;
import com.cy4.machina.client.util.UIHelper;
import com.cy4.machina.client.util.UIHelper.StippleType;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.StringTextComponent;

public class PlanetNodeElement extends Widget {

	StarchartScreen screen;

	public PlanetNodeElement(float pX, float pY, StarchartScreen screen) {
		super((int) pX, (int) pY, 3, 3, new StringTextComponent(""));
		this.screen = screen;
		this.active = true;
		this.visible = true;
	}

	@Override
	public void onClick(double pMouseX, double pMouseY) {
		super.onClick(pMouseX, pMouseY);
		
		this.visible = false;
	}

	@Override
	public void render(MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		if (this.visible) {
			Minecraft mc = Minecraft.getInstance();
			mc.getTextureManager().bind(StarchartScreen.SC_RS);
			Vector2f centre = screen.getCentre();

			float offsetC = (float) (8 * Math.cos(mc.levelRenderer.ticks / 10f) + 8);
			int textureSize = 3;

			if (true) {
				UIHelper.line(matrixStack, centre.x, centre.y, x + textureSize / 2f, y + textureSize / 2f, 0xFFFFFFFF, 3f,
						StippleType.DASHED);
			}
			UIHelper.betterBlit(matrixStack, x, y, textureSize * (int) offsetC, 0, textureSize, textureSize, 128);
		}
		
	}

}
