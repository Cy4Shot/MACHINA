package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.CompressorBlockEntity;
import com.machina.block.menu.CompressorMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CompressorScreen extends MachinaMenuScreen<CompressorMenu> {

	public CompressorScreen(CompressorMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		CompressorBlockEntity entity = this.entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawEnergyBar(gui, 0, 30, entity.getEnergy() > 0, "compressor.no_power");

		drawUpFacingSlot(gui, 0, mx, my, 61, -30, MuiSlot.PLUS, "compressor.input");
		drawUpFacingSlot(gui, 2, mx, my, 153, -30, MuiSlot.DUST, "compressor.output");

		drawDownFacingSlot(gui, 1, mx, my, 107, -8, MuiSlot.PLATE, "compressor.mould");

		int i = midWidth();
		int j = midHeight();
		MUI.blitCommon(gui, i + 114, j + 13, 508, 0, 4, 8);

		MUI.blitCommon(gui, i + 68, j - 7, 399, 0, 19, 13);
		MUI.blitCommon(gui, i + 145, j - 7, 418, 0, 19, 13);

		MUI.blitCommon(gui, i + 87, j + 2, 405, 9, 12, 4);
		MUI.blitCommon(gui, i + 133, j + 2, 405, 9, 12, 4);

		MuiSlot.RIGHT.draw(gui, i + 90, j - 26, this.aliveTicks);
		MuiSlot.RIGHT.draw(gui, i + 111, j - 26, this.aliveTicks);
		MuiSlot.RIGHT.draw(gui, i + 132, j - 26, this.aliveTicks);

		Component text;
		Component text2 = Component.empty();
		if (entity.isLit()) {
			text = Component
					.literal(MUI.uistrs("compressor.progress") + ": " + StringUtils.formatPercent(entity.getProgress())
							+ " (" + StringUtils.formatTicks(entity.ticksRemaining()) + ")");
			text2 = Component.literal(
					MUI.uistrs("compressor.usage") + ": " + StringUtils.formatPower(entity.getPowerRate()) + "/t");
		} else {
			if (!entity.hasRecipe()) {
				text = MUI.uistr("compressor.no_input");
			} else if (!entity.meetsRequirements()) {
				text = Component.literal(MUI.uistrs("compressor.no_power") + " ("
						+ StringUtils.formatPercent(entity.getProgress()) + ")");
				text2 = Component.literal(MUI.uistrs("compressor.requires") + ": "
						+ StringUtils.formatPower(entity.getPowerRate()) + "/t");
			} else if (!entity.hasSpace()) {
				text = MUI.uistr("compressor.no_space");
			} else {
				// This should never happen. Who knows? Maybe it will.
				text = MUI.uistr("compressor.no_input");
			}
		}
		int color = entity.isLit() ? MUI.CYAN : MUI.RED;
		MUI.drawCenteredString(gui, text, i + 117, j - 58, color);
		MUI.drawCenteredString(gui, text2, i + 117, j - 44, color);

		drawOverlay(gui);
	}
}
