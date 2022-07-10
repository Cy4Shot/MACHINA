package com.machina.client.screen;

import com.machina.client.ClientStarchart;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.item.container.ScannerContainer;
import com.machina.planet.attribute.PlanetAttributeType;
import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.init.PlanetAttributeTypesInit;
import com.machina.util.color.Color;
import com.machina.util.server.PlanetUtils;
import com.machina.util.text.StringUtils;
import com.machina.world.data.PlanetData;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class ScannerScreen extends NoJeiContainerScreen<ScannerContainer> {
	private static int tab = 0;

	public ScannerScreen(ScannerContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - 176) / 2;
	}

	@Override
	public void render(MatrixStack stack, int pMouseX, int pMouseY, float pPartialTicks) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);

		// Darker background
		this.renderBackground(stack);

		// Render
		super.render(stack, pMouseX, pMouseY, pPartialTicks);
		this.renderTooltip(stack, pMouseX, pMouseY);
	}

	@Override
	protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {
	}

	@Override
	protected void renderBg(MatrixStack stack, float pPartialTicks, int pX, int pY) {
		UIHelper.bindScifi();

		// Back
		int xSize = 236, ySize = 99;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 2, 3, xSize, ySize);
		this.blit(stack, x + 50, y - 22, 3, 130, 135, 18);
		this.blit(stack, x + 24, y - 26, 3, 150, 23, 23);
		this.blit(stack, x + 187, y - 26, 29, 150, 23, 23);

		// Buttons
		if (pX > x + 24 && pX < x + 24 + 23 && pY > y - 26 && pY < y - 26 + 23) {
			this.blit(stack, x + 31, y - 20, 216, 196, 8, 12);
		} else {
			this.blit(stack, x + 31, y - 20, 216, 184, 8, 12);
		}
		if (pX > x + 187 && pX < x + 187 + 23 && pY > y - 26 && pY < y - 26 + 23) {
			this.blit(stack, x + 194, y - 20, 208, 196, 8, 12);
		} else {
			this.blit(stack, x + 194, y - 20, 208, 184, 8, 12);
		}

		// Data
		RegistryKey<World> dim = this.menu.getDim();
		if (PlanetUtils.isDimensionPlanet(dim)) {
			PlanetData data = ClientStarchart.getPlanetData(dim);
			draw(stack, StringUtils.translate("machina.screen.scanner.tab" + tab), x + 120, y + 6, 0xFF_00fefe, true);
			switch (tab) {
			case 0:
				int i = 0;
				for (PlanetTrait t : data.getTraits()) {
					draw(stack, t.toString(), x + 120, y + 20 + i * 10, t.getColor(), true);
					i++;
				}
				break;
			case 1:
				drawAttribute(stack, PlanetAttributeTypesInit.GRAVITY, x, y + 20);
				drawAttribute(stack, PlanetAttributeTypesInit.ATMOSPHERIC_PRESSURE, x, y + 30);
				drawAttribute(stack, PlanetAttributeTypesInit.TEMPERATURE, x, y + 40);
				drawAttribute(stack, PlanetAttributeTypesInit.FOG_DENSITY, x, y + 50);
				break;
			case 2:
				if (data.getAttribute(PlanetAttributeTypesInit.CAVES_EXIST) == 1) {
					drawAttribute(stack, PlanetAttributeTypesInit.CAVE_CHANCE, x, y + 20);
					drawAttribute(stack, PlanetAttributeTypesInit.CAVE_THICKNESS, x, y + 30);
					drawAttribute(stack, PlanetAttributeTypesInit.CAVE_LENGTH, x, y + 40);
					break;
				}
				draw(stack, StringUtils.translate("machina.screen.scanner.nocave"), x + 120, y + 20, 0xFF_FF0000, true);
				break;
			case 3:
				drawAttribute(stack, PlanetAttributeTypesInit.DISTANCE, x, y + 20);
				break;
			}
			draw(stack,
					PlanetAttributeTypesInit.PLANET_NAME.getName() + ": "
							+ data.getAttributeFormatted(PlanetAttributeTypesInit.PLANET_NAME),
					x + 117, y - 18, 0xFF_00fefe, true);
		} else {
			draw(stack,
					StringUtils.translate("machina.screen.scanner.location")
							+ StringUtils.capitalizeWord(dim.location().getPath().replace("_", " ")),
					x + 117, y - 18, 0xFF_00fefe, true);
			draw(stack, StringUtils.translate("machina.screen.scanner.nodata"), x + 120, y + 6, 0xFF_00fefe, true);
		}

		// Footer
		draw(stack, "MACHINA://SCANNER-" + (tab + 1) + "/", x + 8, y + 82, 0xFF_00fefe, false);
	}

	private void drawAttribute(MatrixStack stack, PlanetAttributeType<?> type, int x, int y) {
		PlanetData data = ClientStarchart.getPlanetData(this.menu.getDim());
		String title = type.getName() + ": ";
		String value = data.getAttributeFormatted(type);
		Color[] colors = data.getAttribute(PlanetAttributeTypesInit.PALETTE);
		draw(stack, title, x + 120 - UIHelper.getWidth(value) / 2, y, colors[0].maxBrightness().toInt(), true);
		draw(stack, value, x + 120 + UIHelper.getWidth(title) / 2, y, colors[4].maxBrightness().toInt(), true);
	}

	private static void draw(MatrixStack stack, String title, int x, int y, int col, boolean centered) {
		if (centered)
			UIHelper.drawCenteredStringWithBorder(stack, title, x, y, col, 0xFF_0e0e0e);
		else
			UIHelper.drawStringWithBorder(stack, title, x, y, col, 0xFF_0e0e0e);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pButton) {
		if (pButton == 0) {
			int xSize = 236, ySize = 99;
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;
			if (pX > x + 24 && pX < x + 24 + 23 && pY > y - 26 && pY < y - 26 + 23) {
				if (tab > 0) {
					tab--;
					UIHelper.click();
				}
			}
			if (pX > x + 187 && pX < x + 187 + 23 && pY > y - 26 && pY < y - 26 + 23) {
				if (tab < 5) {
					tab++;
					UIHelper.click();
				}
			}
		}
		return super.mouseReleased(pX, pY, pButton);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
