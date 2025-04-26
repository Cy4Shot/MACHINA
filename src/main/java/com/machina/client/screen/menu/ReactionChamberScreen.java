package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.ReactionChamberBlockEntity;
import com.machina.block.menu.ReactionChamberMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ReactionChamberScreen extends MachinaMenuScreen<ReactionChamberMenu> {

	public ReactionChamberScreen(ReactionChamberMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		ReactionChamberBlockEntity entity = this.<ReactionChamberBlockEntity>entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawEnergyBar(gui, 0, 30, entity.getEnergy() > 0, "reaction_chamber.no_power");

		// Fluid Bars
		drawFluidBarVert(gui, 5, -62, 0);
		drawFluidBarVert(gui, 25, -62, 1);
		drawFluidBarVert(gui, 194, -62, 2);
		drawFluidBarVert(gui, 214, -62, 3);

		int i = midWidth();
		int j = midHeight();

		// Slots
		MUI.drawSlot(gui, i + 4, j + 29, mx, my, false, true);
		MUI.drawSlot(gui, i + 24, j + 29, mx, my, false, true);
		MUI.drawSlot(gui, i + 193, j + 29, mx, my, false, true);
		MUI.drawSlot(gui, i + 213, j + 29, mx, my, false, true);

		// Deco
		MUI.blitCommon(gui, i + 11, j - 13, 486, 94, 4, 18);
		MUI.blitCommon(gui, i + 11, j + 4, 486, 94, 4, 4);
		MUI.blitCommon(gui, i + 11, j + 14, 486, 94, 4, 8);
		MUI.blitCommon(gui, i + 10, j + 8, 437, 80, 6, 6);

		MUI.blitCommon(gui, i + 31, j - 13, 486, 94, 4, 18);
		MUI.blitCommon(gui, i + 31, j + 4, 486, 94, 4, 4);
		MUI.blitCommon(gui, i + 31, j + 14, 486, 94, 4, 8);
		MUI.blitCommon(gui, i + 30, j + 8, 443, 80, 6, 6);

		MUI.blitCommon(gui, i + 200, j - 13, 486, 94, 4, 18);
		MUI.blitCommon(gui, i + 200, j + 4, 486, 94, 4, 4);
		MUI.blitCommon(gui, i + 200, j + 14, 486, 94, 4, 8);
		MUI.blitCommon(gui, i + 199, j + 8, 443, 80, 6, 6);

		MUI.blitCommon(gui, i + 220, j - 13, 486, 94, 4, 18);
		MUI.blitCommon(gui, i + 220, j + 4, 486, 94, 4, 4);
		MUI.blitCommon(gui, i + 220, j + 14, 486, 94, 4, 8);
		MUI.blitCommon(gui, i + 219, j + 8, 431, 80, 6, 6);

		MUI.blitCommon(gui, i + 16, j + 9, 428, 14, 14, 4);
		MUI.blitCommon(gui, i + 205, j + 9, 428, 14, 14, 4);

		MUI.blitCommon(gui, i + 36, j + 9, 405, 9, 26, 4);
		MUI.blitCommon(gui, i + 62, j + 9, 405, 9, 26, 4);
		MUI.blitCommon(gui, i + 88, j + 9, 405, 9, 26, 4);
		MUI.blitCommon(gui, i + 120, j + 9, 405, 9, 26, 4);
		MUI.blitCommon(gui, i + 146, j + 9, 405, 9, 26, 4);
		MUI.blitCommon(gui, i + 172, j + 9, 405, 9, 26, 4);
		MUI.blitCommon(gui, i + 198, j + 9, 405, 9, 1, 4);

		MUI.blitCommon(gui, i + 114, j + 8, 399, 13, 6, 6);
		MUI.blitCommon(gui, i + 115, j + 14, 508, 0, 4, 7);

		// Text
		Component text;
		Component text2 = Component.empty();
		if (entity.isLit()) {
			text = Component.literal(
					MUI.uistrs("reaction_chamber.progress") + ": " + StringUtils.formatPercent(entity.getProgress())
							+ " (" + StringUtils.formatTicks(entity.ticksRemaining()) + ")");
			text2 = Component.literal(MUI.uistrs("reaction_chamber.usage") + ": "
					+ StringUtils.formatPower(entity.getPowerRate()) + "/t");
		} else {
			if (!entity.hasRecipe()) {
				text = MUI.uistr("reaction_chamber.no_input");
			} else if (!entity.meetsRequirements()) {
				text = Component.literal(MUI.uistrs("reaction_chamber.no_power") + " ("
						+ StringUtils.formatPercent(entity.getProgress()) + ")");
				text2 = Component.literal(MUI.uistrs("reaction_chamber.requires") + ": "
						+ StringUtils.formatPower(entity.getPowerRate()) + "/t");
			} else if (!entity.hasSpace()) {
				text = MUI.uistr("reaction_chamber.no_space");
			} else {
				// This should never happen. Who knows? Maybe it will.
				text = MUI.uistr("reaction_chamber.no_input");
			}
		}
		int color = entity.isLit() ? MUI.CYAN : MUI.RED;
		MUI.drawCenteredString(gui, text, i + 117, j - 54, color);
		MUI.drawCenteredString(gui, text2, i + 117, j - 38, color);

		// Overlay
		drawOverlay(gui);
	}
}
