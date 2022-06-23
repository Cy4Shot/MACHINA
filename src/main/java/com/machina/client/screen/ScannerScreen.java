package com.machina.client.screen;

import com.machina.client.ClientStarchart;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.item.container.ScannerContainer;
import com.machina.planet.trait.PlanetTrait;
import com.machina.planet.trait.PlanetTraitList;
import com.machina.registration.init.PlanetAttributeTypesInit;
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
		String title, gravity, atmo, temp, fog, cave_chance, cave_thickness, cave_length;

		RegistryKey<World> dim = this.menu.getDim();
		PlanetTraitList traits = new PlanetTraitList();
		if (PlanetUtils.isDimensionPlanet(dim)) {
			

			PlanetData data = ClientStarchart.getPlanetData(dim);
			traits = data.getTraits();

			title = data.getAttributeFormatted(PlanetAttributeTypesInit.PLANET_NAME);

			gravity = data.getAttributeFormatted(PlanetAttributeTypesInit.GRAVITY);
			atmo = data.getAttributeFormatted(PlanetAttributeTypesInit.ATMOSPHERIC_PRESSURE);
			temp = data.getAttributeFormatted(PlanetAttributeTypesInit.TEMPERATURE);
			fog = data.getAttributeFormatted(PlanetAttributeTypesInit.FOG_DENSITY);

			int caves = data.getAttribute(PlanetAttributeTypesInit.CAVES_EXIST);
			cave_chance = String
					.valueOf((float) caves * data.getAttribute(PlanetAttributeTypesInit.CAVE_CHANCE) * 100 * 50) + "%";
			if (caves == 1) {
				cave_thickness = String.valueOf(data.getAttribute(PlanetAttributeTypesInit.CAVE_THICKNESS) * 250) + "m";
				cave_length = String.valueOf(data.getAttribute(PlanetAttributeTypesInit.CAVE_LENGTH) * 16) + "m";
			} else {
				cave_thickness = "0m";
				cave_length = "0m";
			}
		} else {
//			if (World.OVERWORLD.equals(dim)) {
			title = "Overworld";
			gravity = "1G";
			atmo = "1 atm";
			temp = "287 K";
			fog = "0.2";
			cave_chance = "50%";
			cave_thickness = "5m";
			cave_length = "48m";
//			}
		}

		if (tab == 0) {
			draw(stack, StringUtils.translate("machina.screen.scanner.tab0"), x + 120, y + 6, 0xFF_00fefe, true);
			for (int i = 0; i < traits.size(); i++) {
				PlanetTrait t = traits.get(i);
				draw(stack, t.toString(), x + 120, y + 20 + i * 10, t.getColor(), true);
			}
		} else if (tab == 1) {
			draw(stack, StringUtils.translate("machina.screen.scanner.tab1"), x + 120, y + 6, 0xFF_00fefe, true);
			draw(stack, PlanetAttributeTypesInit.GRAVITY.getName() + ": " + gravity, x + 20, y + 20, 0xFF_00fefe, false);
			draw(stack, PlanetAttributeTypesInit.ATMOSPHERIC_PRESSURE.getName() + ": " + atmo, x + 20, y + 30, 0xFF_00fefe, false);
			draw(stack, PlanetAttributeTypesInit.TEMPERATURE.getName() + ": " + temp, x + 20, y + 40, 0xFF_00fefe, false);
			draw(stack, PlanetAttributeTypesInit.FOG_DENSITY.getName() + ": " + fog, x + 20, y + 50, 0xFF_00fefe, false);
		} else if (tab == 2) {
			draw(stack, StringUtils.translate("machina.screen.scanner.tab2"), x + 120, y + 6, 0xFF_00fefe, true);
			draw(stack, PlanetAttributeTypesInit.CAVE_CHANCE.getName() + ": " + cave_chance, x + 20, y + 20, 0xFF_00fefe, false);
			draw(stack, PlanetAttributeTypesInit.CAVE_THICKNESS.getName() + ": " + cave_thickness, x + 20, y + 30, 0xFF_00fefe, false);
			draw(stack, PlanetAttributeTypesInit.CAVE_LENGTH.getName() + ": " + cave_length, x + 20, y + 40, 0xFF_00fefe, false);
		}

		draw(stack, PlanetAttributeTypesInit.PLANET_NAME.getName() + " - " + title, x + 117, y - 18, 0xFF_00fefe, true);
		draw(stack, "MACHINA://SCANNER-" + (tab + 1) + "/", x + 8, y + 82, 0xFF_00fefe, false);
	}

	private static void draw(MatrixStack stack, String title, int x, int y, int col, boolean centered) {
		if (centered) {
			UIHelper.drawCenteredStringWithBorder(stack, title, x, y, col, 0xFF_0e0e0e);
		} else {
			UIHelper.drawStringWithBorder(stack, title, x, y, col, 0xFF_0e0e0e);
		}
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
