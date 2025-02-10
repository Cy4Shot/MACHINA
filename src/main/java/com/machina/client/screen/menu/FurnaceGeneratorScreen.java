package com.machina.client.screen.menu;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.FurnaceGeneratorBlockEntity;
import com.machina.block.menu.FurnaceGeneratorMenu;
import com.machina.config.CommonConfig;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class FurnaceGeneratorScreen extends MachinaMenuScreen<FurnaceGeneratorBlockEntity, FurnaceGeneratorMenu> {

	public FurnaceGeneratorScreen(FurnaceGeneratorMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawDownFacingSlot(gui, 0, mx, my, 107, -28, SpecialSlot.COAL, "furnace_generator.input");
		drawEnergyBar(gui, 20, true, "");

		int i = midWidth();
		int j = midHeight();
		blitCommon(gui, i + 81, j - 18, 369, 80, 17, 8);
		blitCommon(gui, i + 81, j - 10, 508, 0, 4, 21);

		blitCommon(gui, i + 134, j - 18, 390, 80, 17, 8);
		blitCommon(gui, i + 147, j - 10, 508, 0, 4, 21);

		Component text = this.entity.isLit()
				? Component.literal(uistrs("furnace_generator.progress") + ": "
						+ StringUtils.formatPercent(this.entity.getProgress()) + " ("
						+ StringUtils.formatTicks(this.entity.ticksRemaining()) + ")")
				: uistr("furnace_generator.no_input");
		int color = this.entity.isLit() ? 0x00FEFE : 0xFE0000;
		gui.drawCenteredString(font, text, i + 117, j - 54, color);

		if (this.entity.isLit())
			gui.drawCenteredString(font,
					Component.literal(uistrs("furnace_generator.generating") + ": "
							+ StringUtils.formatPower(CommonConfig.furnaceGeneratorRate.get()) + "/t"),
					i + 117, j - 42, 0x00FEFE);

		drawOverlay(gui);
	}
}
