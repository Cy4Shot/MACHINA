package com.machina.client.screen.element;

import com.machina.client.screen.StarchartScreen;
import com.machina.client.util.UIHelper;
import com.machina.client.util.UIHelper.StippleType;
import com.machina.util.math.MathUtil;
import com.machina.world.data.PlanetData;
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

	private static int textureSize = 5;

	public PlanetNodeElement(float pX, float pY, StarchartScreen screen, PlanetData data) {
		super((int) pX, (int) pY, textureSize, textureSize, new StringTextComponent(""));
		this.screen = screen;
		active = true;
		visible = true;
		this.data = data;
	}

	public PlanetData getData() {
		return data;
	}

	@Override
	public void onClick(double pMouseX, double pMouseY) {
		super.onClick(pMouseX, pMouseY);
		UIHelper.click();
		screen.selected = this;
	}

	@Override
	public void render(MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		Minecraft mc = Minecraft.getInstance();
		mc.getTextureManager().bind(StarchartScreen.SC_RS);
		Vector2f centre = screen.getNewCentre();

		float offsetC = (float) (8 * Math.cos(mc.levelRenderer.ticks / 10f) + 8);

		double distFromCentre = MathUtil.distance(centre, new Vector2f(x, y));

		if (true) {
//			UIHelper.line(matrixStack, centre.x, centre.y, x + textureSize / 2f, y + textureSize / 2f, 0xFFFFFFFF, 3f,
//					StippleType.DASHED);
			UIHelper.polygon(matrixStack, centre.x, centre.y, (float) distFromCentre + 1, 100, 0x55_FFFFFF, 2f,
					StippleType.FULL);
		}
		UIHelper.betterBlit(matrixStack, x, y, textureSize * (int) offsetC, 0, textureSize, textureSize, 128);

		if (screen.selected != null && screen.selected.equals(this)) {
			UIHelper.box(matrixStack, x - 2, y - 2, x + textureSize + 2, y + textureSize + 2, 0xFFFFFFFF, 4f,
					StippleType.FULL);
		}
	}

}
