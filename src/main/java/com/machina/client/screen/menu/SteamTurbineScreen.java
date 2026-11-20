package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.steam_turbine.SteamTurbineControllerBlockEntity;
import com.machina.block.menu.SteamTurbineMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SteamTurbineScreen extends MachinaMenuScreen<SteamTurbineMenu> {

	public SteamTurbineScreen(SteamTurbineMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		SteamTurbineControllerBlockEntity entity = this.entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);

		int i = midWidth();
		int j = midHeight();

		if (entity == null || !entity.formed) {
			MUI.drawCenteredString(gui, Component.translatable("gui.machina.turbine.not_formed"), i + 90, j - 30, MUI.RED);
			MUI.drawCenteredString(gui, Component.translatable("gui.machina.turbine.build_structure"), i + 90, j - 15, MUI.WHITE);
			drawOverlay(gui);
			return;
		}

		drawStatusPanel(gui, i + 115, j - 55);
		drawSteamRateGauge(gui, i + 35, j + 25, mx, my);
		drawEfficiencyGauge(gui, i + 125, j + 25, mx, my);
		drawEnergyBar(gui, 0, -20, true, "");

		drawOverlay(gui);
	}

	private void drawStatusPanel(GuiGraphics gui, int x, int y) {
		float efficiency = this.menu.getEfficiency();
		int steamRate = this.menu.getLastSteamRate();
		int energyProduced = this.menu.getLastEnergyProduced();
		boolean bufferFull = this.menu.isBufferFull();

		Component statusText;
		int statusColor;

		if (bufferFull) {
			statusText = Component.translatable("gui.machina.turbine.status.output_full");
			statusColor = MUI.RED;
		} else if (efficiency > 0.9f) {
			statusText = Component.translatable("gui.machina.turbine.status.running");
			statusColor = MUI.GREEN;
		} else if (efficiency > 0) {
			statusText = Component.translatable("gui.machina.turbine.status.starting");
			statusColor = MUI.ACC_1;
		} else {
			statusText = Component.translatable("gui.machina.turbine.status.idle");
			statusColor = MUI.CYAN;
		}

		MUI.drawCenteredString(gui, statusText, x, y, statusColor);

		String rateStr = steamRate + " mB/t -> " + StringUtils.formatPower(energyProduced) + "/t";
		MUI.drawCenteredString(gui, Component.literal(rateStr), x, y + 12, MUI.WHITE);
	}

	private void drawSteamRateGauge(GuiGraphics gui, int x, int y, int mx, int my) {
		MUI.drawString(gui, Component.translatable("gui.machina.turbine.steam_input"), x, y - 8, MUI.CYAN);

		gui.fill(x, y, x + 75, y + 12, 0xFF101010);
		gui.fill(x + 1, y + 1, x + 74, y + 11, 0xFF202020);

		int steamRate = this.menu.getLastSteamRate();
		int maxVisualRate = 2000;
		float percent = Math.min(1.0f, (float) steamRate / maxVisualRate);
		int fillWidth = (int) (percent * 71);
		gui.fill(x + 2, y + 2, x + 2 + fillWidth, y + 10, 0xFF555555);

		String steamStr = steamRate + " mB/t";
		MUI.drawString(gui, Component.literal(steamStr), x, y + 14, steamRate > 0 ? MUI.GREEN : MUI.WHITE);

		registerHoverable("steam_gauge", x, y, x + 75, y + 12,
				() -> Component.translatable("gui.machina.turbine.steam_rate_tooltip", this.menu.getLastSteamRate()));
	}

	private void drawEfficiencyGauge(GuiGraphics gui, int x, int y, int mx, int my) {
		MUI.drawString(gui, Component.translatable("gui.machina.turbine.efficiency"), x, y - 8, MUI.CYAN);

		gui.fill(x, y, x + 75, y + 12, 0xFF101010);
		gui.fill(x + 1, y + 1, x + 74, y + 11, 0xFF202020);

		float spinEfficiency = this.menu.getEfficiency();
		float bufferEfficiency = this.menu.getBufferEfficiency();
		float totalEfficiency = spinEfficiency * bufferEfficiency;

		int fillWidth = (int) (totalEfficiency * 71);
		int color = totalEfficiency >= 0.9f ? 0xFF00AA00 : (totalEfficiency >= 0.5f ? 0xFFAAAA00 : 0xFFAA5500);
		gui.fill(x + 2, y + 2, x + 2 + fillWidth, y + 10, color);

		String effStr = StringUtils.formatPercent(totalEfficiency);
		MUI.drawString(gui, Component.literal(effStr), x, y + 14, totalEfficiency >= 0.9f ? MUI.GREEN : MUI.ACC_1);

		registerHoverable("efficiency_gauge", x, y, x + 75, y + 12,
				() -> Component.translatable("gui.machina.turbine.efficiency_tooltip",
						StringUtils.formatPercent(this.menu.getEfficiency() * this.menu.getBufferEfficiency())));
	}
}
