package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.entity.machine.AtmosphericSeparatorBlockEntity;
import com.machina.block.menu.AtmosphericSeparatorMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AtmosphericSeparatorScreen extends MachinaMenuScreen<AtmosphericSeparatorMenu> {

	public AtmosphericSeparatorScreen(AtmosphericSeparatorMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {

		AtmosphericSeparatorBlockEntity entity = this.entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawEnergyBar(gui, 0, 30, entity.getEnergy() > 0, "atmospheric_separator.no_power");

		// Fluid Bars
		drawFluidBarVert(gui, 50, -42, 0);
		drawFluidBarVert(gui, 80, -42, 1);
		drawFluidBarVert(gui, 110, -42, 2);
		drawFluidBarVert(gui, 140, -42, 3);
		drawFluidBarVert(gui, 170, -42, 4);

		int i = midWidth();
		int j = midHeight();
		MUI.blitCommon(gui, i + 56, j + 7, 508, 0, 4, 14);
		MUI.blitCommon(gui, i + 86, j + 7, 508, 0, 4, 14);
		MUI.blitCommon(gui, i + 116, j + 7, 508, 0, 4, 14);
		MUI.blitCommon(gui, i + 146, j + 7, 508, 0, 4, 14);
		MUI.blitCommon(gui, i + 176, j + 7, 508, 0, 4, 14);

		MUI.blitCommon(gui, i + 55, j - 60, 491, 113, 6, 11);
		MUI.blitCommon(gui, i + 85, j - 60, 491, 113, 6, 11);
		MUI.blitCommon(gui, i + 115, j - 60, 491, 113, 6, 11);
		MUI.blitCommon(gui, i + 145, j - 60, 491, 113, 6, 11);
		MUI.blitCommon(gui, i + 175, j - 60, 491, 113, 6, 11);

		// Overlay
		drawOverlay(gui);
	}
}
