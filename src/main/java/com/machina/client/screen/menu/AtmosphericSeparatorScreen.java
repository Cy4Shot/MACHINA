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

		System.out.println("akshdkjas");
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {

		AtmosphericSeparatorBlockEntity entity = this.entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawEnergyBar(gui, 0, 30, entity.getEnergy() > 0, "atmospheric_separator.no_power");

		// Fluid Bars
		drawFluidBarVert(gui, 65, -42, 0);
		drawFluidBarVert(gui, 95, -42, 1);
		drawFluidBarVert(gui, 124, -42, 2);
		drawFluidBarVert(gui, 154, -42, 3);

		int i = midWidth();
		int j = midHeight();
		MUI.blitCommon(gui, i + 71, j + 7, 508, 0, 4, 14);
		MUI.blitCommon(gui, i + 101, j + 7, 508, 0, 4, 14);
		MUI.blitCommon(gui, i + 130, j + 7, 508, 0, 4, 14);
		MUI.blitCommon(gui, i + 160, j + 7, 508, 0, 4, 14);

		MUI.blitCommon(gui, i + 70, j - 60, 491, 113, 6, 11);
		MUI.blitCommon(gui, i + 100, j - 60, 491, 113, 6, 11);
		MUI.blitCommon(gui, i + 129, j - 60, 491, 113, 6, 11);
		MUI.blitCommon(gui, i + 159, j - 60, 491, 113, 6, 11);

		// Overlay
		drawOverlay(gui);
	}
}
