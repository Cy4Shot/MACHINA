package com.machina.client.screen.element;

import com.machina.client.screen.StarchartScreen;
import com.machina.client.util.UIHelper;
import com.machina.client.util.UIHelper.StippleType;
import com.machina.registration.init.AttributeInit;
import com.machina.util.Color;
import com.machina.util.math.MathUtil;
import com.machina.util.text.StringUtils;
import com.machina.world.data.PlanetData;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlanetNodeElement extends Widget {

	StarchartScreen screen;
	PlanetData data;
	private int icon;

	private static int tex = 16;

	public PlanetNodeElement(float pX, float pY, StarchartScreen screen, PlanetData data) {
		super((int) pX, (int) pY, tex, tex, StringUtils.EMPTY);
		this.screen = screen;
		active = true;
		visible = true;
		this.data = data;
		this.icon = data.getAttribute(AttributeInit.PLANET_ICON);
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
		UIHelper.bindStcht();
		Vector2f c = screen.getNewCentre();
		double d = MathUtil.distance(c, new Vector2f(x + tex / 2f, y + tex / 2f));
		int col = Color.getIntFromColor(0, 254, 254,
				128 + (int) ((Math.cos(UIHelper.levelTicks() / 4f) + Math.random() * 1.5D) * 25));
		UIHelper.polygon(matrixStack, c.x, c.y, (float) d + 1, 100, col, 1f, StippleType.FULL);
		if (screen.selected != null && screen.selected.equals(this)) {
			UIHelper.line(matrixStack, c.x, c.y, x + tex / 2f, y + tex / 2f, col, 1f, StippleType.DASHED);
		}
	}

	public void renderFront(MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		UIHelper.bindStcht();
		UIHelper.blitTransp(matrixStack, x - 12 + tex / 2f, y - 12 + tex / 2f, 0, 48, 24, 24, 128);
		UIHelper.betterBlit(matrixStack, x, y, 48 + (icon % 5) * 16, Math.floorDiv(icon, 5) * 16, tex, tex, 128);

		if (screen.selected != null && screen.selected.equals(this)) {
			float o = (int) (2 * Math.sin(UIHelper.levelTicks() / 4f));
			UIHelper.box(matrixStack, x - o, y - o, x + tex + o, y + tex + o, 0xFFFFFFFF, 4f, StippleType.FULL);
		}
	}
}
