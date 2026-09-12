package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.client.screen.ProgressBar;
import com.machina.block.entity.machine.fission_reactor.FissionReactorControllerBlockEntity;
import com.machina.block.menu.FissionReactorMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FissionReactorScreen extends MachinaMenuScreen<FissionReactorMenu> {

	public FissionReactorScreen(FissionReactorMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {

		FissionReactorControllerBlockEntity entity = this.entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);

		int i = midWidth();
		int j = midHeight();

		// Steam Rate Bar (top left)
		drawRGBar(gui, 0, 0, this.menu.getSteamRateBar());

		// Reactivity Bar (center)
		drawRGBar(gui, 0, 30, this.menu.getReactivityProgress());

		// Water Bar (top left, vertical)
		drawFluidBarVert(gui, 5, -62, 0);

		// Steam Rate Display (next to water)
		int currentRate = this.menu.getCurrentSteamRate();
		int maxRate = this.menu.getMaxSteamRate();
		String steamText = maxRate > 0
				? String.format("Steam: %d/%d mB/t", currentRate, maxRate)
				: String.format("Steam: %d mB/t", currentRate);
		MUI.drawString(gui, Component.literal(steamText), i + 30, j - 60);

		// Status Text
		Component statusText;
		Component detailText = Component.empty();
		int color;

		if (!entity.formed) {
			statusText = Component.literal("Not Formed");
			color = MUI.RED;
		} else if (entity.getBurnTimeRemaining() > 0) {
			statusText = Component.literal("Running");
			int burnPercent = (int) (entity.getBurnProgress() * 100);
			detailText = Component.literal("Fuel: " + burnPercent + "%");
			color = MUI.GREEN;
		} else {
			int waterMB = entity.getFluidMB(0);
			boolean hasWater = waterMB > 0;
			boolean hasFuel = !entity.getItem(0).isEmpty();

			if (!hasFuel) {
				statusText = Component.literal("No Fuel");
				color = MUI.RED;
			} else if (!hasWater) {
				statusText = Component.literal("No Water");
				color = MUI.RED;
			} else {
				statusText = Component.literal("Idle");
				color = MUI.CYAN;
			}
		}

		MUI.drawCenteredString(gui, statusText, i + 117, j - 54, color);
		if (!detailText.getString().isEmpty()) {
			MUI.drawCenteredString(gui, detailText, i + 117, j - 42, color);
		}

		// Reactivity value
		float reactivity = this.menu.getReactivity();
		Component reactivityText = Component.literal(
				"Reactivity: " + String.format("%.3f", reactivity));
		MUI.drawCenteredString(gui, reactivityText, i + 117, j + 12, MUI.CYAN);

		drawOverlay(gui);
	}
}
