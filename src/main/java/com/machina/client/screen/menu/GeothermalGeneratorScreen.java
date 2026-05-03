package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.geothermal_generator.GeothermalGeneratorControllerBlockEntity;
import com.machina.block.menu.GeothermalGeneratorMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GeothermalGeneratorScreen extends MachinaMenuScreen<GeothermalGeneratorMenu> {

	public GeothermalGeneratorScreen(GeothermalGeneratorMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {

		GeothermalGeneratorControllerBlockEntity entity = this.entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawEnergyBar(gui, 0, 0, true, "");

		int i = midWidth();
		int j = midHeight();

		Component text = entity.isLit() ? MUI.uistr("geothermal_generator.progress")
				: (entity.isEnergyFull() ? MUI.uistr("geothermal_generator.no_space")
						: MUI.uistr("geothermal_generator.no_input"));
		int color = entity.isLit() ? MUI.CYAN : MUI.RED;
		MUI.drawCenteredString(gui, text, i + 117, j - 54, color);

		if (entity.isLit())
			MUI.drawCenteredString(gui, Component.literal(MUI.uistrs("geothermal_generator.generating") + ": "
					+ StringUtils.formatPower(entity.energySource()) + "/t"), i + 117, j - 42);

		drawOverlay(gui);
	}
}
