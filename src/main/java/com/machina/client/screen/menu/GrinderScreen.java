package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.GrinderBlockEntity;
import com.machina.block.menu.GrinderMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GrinderScreen extends MachinaMenuScreen<GrinderMenu> {

	public GrinderScreen(GrinderMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		GrinderBlockEntity entity = this.<GrinderBlockEntity>entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawEnergyBar(gui, 0, 30, entity.getEnergy() > 0, "grinder.no_power");

		drawDownFacingSlot(gui, 0, mx, my, 61, -20, SpecialSlot.PLUS, "grinder.input");
		drawDownFacingSlot(gui, 1, mx, my, 153, -20, SpecialSlot.DUST, "grinder.output");

		int i = midWidth();
		int j = midHeight();
		blitCommon(gui, i + 68, j, 399, 0, 19, 13);
		blitCommon(gui, i + 145, j, 418, 0, 19, 13);

		blitCommon(gui, i + 87, j + 9, 405, 9, 26, 4);
		blitCommon(gui, i + 119, j + 9, 405, 9, 26, 4);

		blitCommon(gui, i + 113, j + 8, 399, 13, 6, 6);
		blitCommon(gui, i + 114, j + 14, 508, 0, 4, 7);

		SpecialSlot.RIGHT.draw(gui, i + 90, j - 13, this.aliveTicks);
		SpecialSlot.RIGHT.draw(gui, i + 111, j - 13, this.aliveTicks);
		SpecialSlot.RIGHT.draw(gui, i + 132, j - 13, this.aliveTicks);

		Component text;
		Component text2 = Component.empty();
		if (entity.isLit()) {
			text = Component.literal(uistrs("grinder.progress") + ": " + StringUtils.formatPercent(entity.getProgress())
					+ " (" + StringUtils.formatTicks(entity.ticksRemaining()) + ")");
			text2 = Component
					.literal(uistrs("grinder.usage") + ": " + StringUtils.formatPower(entity.getPowerRate()) + "/t");
		} else {
			if (!entity.hasRecipe()) {
				text = uistr("grinder.no_input");
			} else if (!entity.hasPower()) {
				text = Component.literal(
						uistrs("grinder.no_power") + " (" + StringUtils.formatPercent(entity.getProgress()) + ")");
				text2 = Component.literal(
						uistrs("grinder.requires") + ": " + StringUtils.formatPower(entity.getPowerRate()) + "/t");
			} else if (!entity.hasSpace()) {
				text = uistr("grinder.no_space");
			} else {
				// This should never happen. Who knows? Maybe it will.
				text = uistr("grinder.no_input");
			}
		}
		int color = entity.isLit() ? 0x00FEFE : 0xFE0000;
		gui.drawCenteredString(font, text, i + 117, j - 54, color);
		gui.drawCenteredString(font, text2, i + 117, j - 38, color);

		drawOverlay(gui);
	}
}
