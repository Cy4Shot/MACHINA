package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.ElectrolyzerBlockEntity;
import com.machina.block.menu.ElectrolyzerMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ElectrolyzerScreen extends MachinaMenuScreen<ElectrolyzerMenu> {

	public ElectrolyzerScreen(ElectrolyzerMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		ElectrolyzerBlockEntity entity = this.<ElectrolyzerBlockEntity>entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawEnergyBar(gui, 0, 30, entity.getEnergy() > 0, "electrolyzer.no_power");

		// Fluid Bars
		drawFluidBarVert(gui, 5, -62, 0);
		drawFluidBarVert(gui, 25, -62, 1);
		drawFluidBarVert(gui, 45, -62, 2);
		drawFluidBarVert(gui, 174, -62, 3);
		drawFluidBarVert(gui, 194, -62, 4);
		drawFluidBarVert(gui, 214, -62, 5);

		int i = midWidth();
		int j = midHeight();

		// Slots
		MUI.drawSlot(gui, i + 108, j - 20, mx, my, false, true);
		MUI.drawSlot(gui, i + 24, j + 29, mx, my, false, true);
		MUI.drawSlot(gui, i + 193, j + 29, mx, my, false, true);

		// Deco
		MUI.blitCommon(gui, i + 11, j - 13, 486, 94, 4, 18);
		MUI.blitCommon(gui, i + 11, j + 4, 486, 94, 4, 4);
		MUI.blitCommon(gui, i + 10, j + 8, 437, 80, 6, 6);
		MUI.blitCommon(gui, i + 10, j + 8, 449, 80, 6, 6);

		MUI.blitCommon(gui, i + 31, j - 13, 486, 94, 4, 18);
		MUI.blitCommon(gui, i + 31, j + 4, 486, 94, 4, 4);
		MUI.blitCommon(gui, i + 31, j + 14, 486, 94, 4, 8);
		MUI.blitCommon(gui, i + 30, j + 8, 443, 80, 6, 6);

		MUI.blitCommon(gui, i + 51, j - 13, 486, 94, 4, 18);
		MUI.blitCommon(gui, i + 51, j + 4, 486, 94, 4, 4);
		MUI.blitCommon(gui, i + 50, j + 8, 461, 80, 6, 6);

		MUI.blitCommon(gui, i + 180, j - 13, 486, 94, 4, 18);
		MUI.blitCommon(gui, i + 180, j + 4, 486, 94, 4, 4);
		MUI.blitCommon(gui, i + 179, j + 8, 461, 80, 6, 6);

		MUI.blitCommon(gui, i + 200, j - 13, 486, 94, 4, 18);
		MUI.blitCommon(gui, i + 200, j + 4, 486, 94, 4, 4);
		MUI.blitCommon(gui, i + 200, j + 14, 486, 94, 4, 8);
		MUI.blitCommon(gui, i + 199, j + 8, 443, 80, 6, 6);

		MUI.blitCommon(gui, i + 220, j - 13, 486, 94, 4, 18);
		MUI.blitCommon(gui, i + 220, j + 4, 486, 94, 4, 4);
		MUI.blitCommon(gui, i + 219, j + 8, 455, 80, 6, 6);

		MUI.blitCommon(gui, i + 16, j + 9, 428, 14, 14, 4);
		MUI.blitCommon(gui, i + 205, j + 9, 428, 14, 14, 4);

		MUI.blitCommon(gui, i + 36, j + 9, 428, 14, 14, 4);
		MUI.blitCommon(gui, i + 185, j + 9, 428, 14, 14, 4);

		MUI.blitCommon(gui, i + 56, j + 9, 405, 9, 6, 4);
		MUI.blitCommon(gui, i + 62, j + 9, 405, 9, 26, 4);
		MUI.blitCommon(gui, i + 88, j + 9, 405, 9, 26, 4);
		MUI.blitCommon(gui, i + 120, j + 9, 405, 9, 26, 4);
		MUI.blitCommon(gui, i + 146, j + 9, 405, 9, 26, 4);
		MUI.blitCommon(gui, i + 172, j + 9, 405, 9, 7, 4);

		MUI.blitCommon(gui, i + 114, j + 8, 399, 13, 6, 6);
		MUI.blitCommon(gui, i + 115, j + 14, 508, 0, 4, 7);

		MuiSlot.RIGHT.draw(gui, i + 80, j - 16, this.aliveTicks);
		MuiSlot.RIGHT.draw(gui, i + 145, j - 16, this.aliveTicks);

		// Text
		Component text;
		Component text2 = Component.empty();
		if (entity.isLit()) {
			text = Component.literal(
					MUI.uistrs("electrolyzer.progress") + ": " + StringUtils.formatPercent(entity.getProgress()));
			text2 = Component.literal(
					MUI.uistrs("electrolyzer.usage") + ": " + StringUtils.formatPower(entity.getPowerRate()) + "/t");
		} else {
			if (!entity.hasRecipe()) {
				text = MUI.uistr("electrolyzer.no_input");
			} else if (!entity.meetsRequirements()) {
				text = Component.literal(MUI.uistrs("electrolyzer.no_power") + " ("
						+ StringUtils.formatPercent(entity.getProgress()) + ")");
				text2 = Component.literal(MUI.uistrs("electrolyzer.requires") + ": "
						+ StringUtils.formatPower(entity.getPowerRate()) + "/t");
			} else if (!entity.hasSpace()) {
				text = MUI.uistr("electrolyzer.no_space");
			} else {
				// This should never happen. Who knows? Maybe it will.
				text = MUI.uistr("electrolyzer.no_input");
			}
		}
		int color = entity.isLit() ? MUI.CYAN : MUI.RED;
		MUI.drawCenteredString(gui, text, i + 117, j - 54, color);
		MUI.drawCenteredString(gui, text2, i + 117, j - 38, color);

		// Overlay
		drawOverlay(gui);
	}
}
