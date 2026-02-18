package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.MelterBlockEntity;
import com.machina.block.menu.MelterMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class MelterScreen extends MachinaMenuScreen<MelterMenu> {

	public MelterScreen(MelterMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		MelterBlockEntity entity = this.entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawEnergyBar(gui, 0, 30, entity.getEnergy() > 0, "melter.no_power");

		drawDownFacingSlot(gui, 0, mx, my, 24, -17, MuiSlot.PLUS, "melter.input");

		drawFluidBar(gui, 28, -6, 0);

		int i = midWidth();
		int j = midHeight();
		MuiSlot.RIGHT.draw(gui, i + 56, j - 10);

		Component text;
		Component text2 = Component.empty();
		if (entity.isLit()) {
			text = Component
					.literal(MUI.uistrs("melter.progress") + ": " + StringUtils.formatPercent(entity.getProgress())
							+ " (" + StringUtils.formatTicks(entity.ticksRemaining()) + ")");
			text2 = Component
					.literal(MUI.uistrs("melter.usage") + ": " + StringUtils.formatPower(entity.getPowerRate()) + "/t");
		} else {
			if (!entity.hasRecipe()) {
				text = MUI.uistr("melter.no_input");
			} else if (!entity.meetsRequirements()) {
				text = Component.literal(
						MUI.uistrs("melter.no_power") + " (" + StringUtils.formatPercent(entity.getProgress()) + ")");
				text2 = Component.literal(
						MUI.uistrs("melter.requires") + ": " + StringUtils.formatPower(entity.getPowerRate()) + "/t");
			} else if (!entity.hasSpace()) {
				text = MUI.uistr("melter.no_space");
			} else {
				// This should never happen. Who knows? Maybe it will.
				text = MUI.uistr("melter.no_input");
			}
		}
		int color = entity.isLit() ? MUI.CYAN : MUI.RED;
		MUI.drawCenteredString(gui, text, i + 117, j - 54, color);
		MUI.drawCenteredString(gui, text2, i + 117, j - 38, color);

		drawOverlay(gui);
	}
}
