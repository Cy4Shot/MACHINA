package com.cy4.machina.client.gui.element;

import com.cy4.machina.client.gui.StarchartScreen;
import com.cy4.machina.client.util.UIHelper;
import com.cy4.machina.client.util.UIHelper.StippleType;
import com.cy4.machina.starchart.PlanetData;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.StringTextComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlanetNodeElement extends Widget {

	StarchartScreen screen;
	PlanetData data;

	public PlanetNodeElement(float pX, float pY, StarchartScreen screen, PlanetData data) {
		super((int) pX, (int) pY, 3, 3, new StringTextComponent(""));
		this.screen = screen;
		this.active = true;
		this.visible = true;
		this.data = data;
	}
	
	public PlanetData getData() {
		return data;
	}

	@Override
	public void onClick(double pMouseX, double pMouseY) {
		super.onClick(pMouseX, pMouseY);

		screen.selected = this;
	}

	@Override
	public void render(MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		Minecraft mc = Minecraft.getInstance();
		mc.getTextureManager().bind(StarchartScreen.SC_RS);
		Vector2f centre = screen.getNewCentre();

		float offsetC = (float) (8 * Math.cos(mc.levelRenderer.ticks / 10f) + 8);
		int textureSize = 3;

		if (true) {
			UIHelper.line(matrixStack, centre.x, centre.y, x + textureSize / 2f, y + textureSize / 2f, 0xFFFFFFFF, 3f,
					StippleType.DASHED);
		}
		UIHelper.betterBlit(matrixStack, x, y, textureSize * (int) offsetC, 0, textureSize, textureSize, 128);
		
		if (screen.selected != null && screen.selected.equals(this)) {
			UIHelper.box(matrixStack, x - 2, y - 2, x + textureSize + 2, y + textureSize + 2,  0xFFFFFFFF, 4f, StippleType.FULL);
		}
	}

}
