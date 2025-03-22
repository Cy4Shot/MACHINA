package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI.SpecialSlot;
import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.FurnaceGeneratorBlockEntity;
import com.machina.block.menu.FurnaceGeneratorMenu;
import com.machina.config.CommonConfig;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FurnaceGeneratorScreen extends MachinaMenuScreen<FurnaceGeneratorMenu> {

	public FurnaceGeneratorScreen(FurnaceGeneratorMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {

		FurnaceGeneratorBlockEntity entity = this.<FurnaceGeneratorBlockEntity>entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawDownFacingSlot(gui, 0, mx, my, 107, -28, SpecialSlot.COAL, "furnace_generator.input");
		drawEnergyBar(gui, 0, 20, true, "");

		int i = midWidth();
		int j = midHeight();
		MUI.blitCommon(gui, i + 81, j - 18, 369, 80, 17, 8);
		MUI.blitCommon(gui, i + 81, j - 10, 508, 0, 4, 21);

		MUI.blitCommon(gui, i + 134, j - 18, 390, 80, 17, 8);
		MUI.blitCommon(gui, i + 147, j - 10, 508, 0, 4, 21);

		Component text = entity.isLit()
				? Component.literal(MUI.uistrs("furnace_generator.progress") + ": "
						+ StringUtils.formatPercent(entity.getProgress()) + " ("
						+ StringUtils.formatTicks(entity.ticksRemaining()) + ")")
				: MUI.uistr("furnace_generator.no_input");
		int color = entity.isLit() ? 0x00FEFE : 0xFE0000;
		gui.drawCenteredString(font, text, i + 117, j - 54, color);

		if (entity.isLit())
			gui.drawCenteredString(font,
					Component.literal(MUI.uistrs("furnace_generator.generating") + ": "
							+ StringUtils.formatPower(CommonConfig.furnaceGeneratorRate.get()) + "/t"),
					i + 117, j - 42, 0x00FEFE);

		drawOverlay(gui);
	}
}
