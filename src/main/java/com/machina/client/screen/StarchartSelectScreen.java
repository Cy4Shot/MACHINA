package com.machina.client.screen;

import com.machina.client.ClientStarchart;
import com.machina.client.util.IStarchartSelector;
import com.machina.client.util.UIHelper;
import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.init.PlanetAttributeTypesInit;
import com.machina.util.text.StringUtils;
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
	public void render(MatrixStack stack, int pX, int pY, float pPartialTicks) {
		super.render(stack, pX, pY, pPartialTicks);

		int x = this.width - 50;
		int y = this.height - 50;
		if (this.selected == null) {
			UIHelper.drawCenteredStringWithBorder(stack,
					StringUtils.translate("machina.screen.starchart_select.noselect"), this.width / 2,
					this.height / 2 + 30, 0xFF_ff0000, 0xFF_0e0e0e);
		} else {
			UIHelper.bindScifi();
			this.blit(stack, x - 49, y - 62, 151, 103, 88, 81);
			this.blit(stack, x - 37, y - 42, 4, 130, 69, 1);
			UIHelper.drawCenteredStringWithBorder(stack,
					selected.getData().getAttributeFormatted(PlanetAttributeTypesInit.PLANET_NAME), x - 2, y - 53,
					0xFF_00fefe, 0xFF_0e0e0e);
			int i = 0;
			for (PlanetTrait t : selected.getData().getTraits()) {
				UIHelper.drawCenteredStringWithBorder(stack, t.toString(), x - 2, y - 38 + i * 10, t.getColor(),
						0xFF_0e0e0e);
				i++;
			}

			UIHelper.bindTrmnl();
			if (pX > x - 46 && pX < x - 46 + 17 && pY > y && pY < y + 17) {
				this.blit(stack, x - 46, y, 237, 73, 17, 17);
			} else {
				this.blit(stack, x - 46, y, 237, 56, 17, 17);
			}
		}

	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pButton) {
		if (pButton == 0) {
			int x = this.width - 50;
			int y = this.height - 50;

			if (pX > x - 46 && pX < x - 46 + 17 && pY > y && pY < y + 17 && this.selected != null) {
				onSelected();
				UIHelper.click();
			}
		}
		return super.mouseReleased(pX, pY, pButton);
	}

	public void onSelected() {
		if (this.selected != null) {
			selector.accept(ClientStarchart.getId(this.selected.getData()));
		}
	}

}
