package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.rocket.RocketPartType;
import com.machina.block.menu.RocketPartBenchMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class RocketPartBenchScreen extends MachinaMenuScreen<RocketPartBenchMenu> {

	public RocketPartBenchScreen(RocketPartBenchMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	private int selected = 0;

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
//		RocketPartBenchBlockEntity entity = this.<RocketPartBenchBlockEntity>entity();

		drawInventory(gui, mx, my);
		drawRocketBackground(gui);
		drawEnergyBarSmall(gui, -77, 53, true, "");

		int i = midWidth();
		int j = midHeight();

		// Slots
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 4; y++) {
				MUI.drawSlot(gui, i + 10 + x * 22, j - 50 + y * 22, mx, my, false, false);
			}
		}

		// Tabs
		MUI.blitRocket(gui, i + 96, j - 72, 253, 0, 121, 26);
		for (int x = 0; x < 5; x++) {
			RocketPartType type = RocketPartType.values()[x];
			if (selected != x
					&& (mx < i + 105 + x * 21 || mx > i + 105 + x * 21 + 18 || my < j - 71 || my > j - 71 + 18)) {
				MUI.blitRocket(gui, i + 105 + x * 21, j - 71, 235, 0, 18, 18);
			} else {
				MUI.blitRocket(gui, i + 105 + x * 21, j - 71, 235, 18, 18, 18);
			}

			MUI.blitRocket(gui, i + 106 + x * 21, j - 70, type.getX(), 0, 16, 16);

			final int x1 = x;
			clickAndHover("tab_" + x, i + 105 + x * 21, j - 71, i + 123 + x * 21, j - 53, () -> true,
					() -> type.getName(), () -> {
						this.selected = x1;
					});
		}

		// Moving Decorators
		int k = 6 - usy.intValue() % 6;
		MUI.blitCommon(gui, i + 80, j - 72, 431, 136 + k, 2, 142);

		MUI.drawCenteredString(gui, MUI.uistr("rocket_part_bench.inv"), i + 42, j - 65, MUI.CYAN);

		drawOverlay(gui);
	}
}
