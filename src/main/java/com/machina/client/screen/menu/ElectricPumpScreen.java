package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.entity.machine.ElectricPumpBlockEntity;
import com.machina.block.menu.ElectricPumpMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

public class ElectricPumpScreen extends MachinaMenuScreen<ElectricPumpMenu> {

	public ElectricPumpScreen(ElectricPumpMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		ElectricPumpBlockEntity entity = this.entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);

		drawFluidBar(gui, 0, -20, 0);

		// Config
		drawEnergyBar(gui, 0, 20, true, "");

		int i = midWidth();
		int j = midHeight();
		MUI.blitCommon(gui, i + 61, j - 11, 508, 0, 4, 22);
		MUI.blitCommon(gui, i + 167, j - 11, 508, 0, 4, 22);

		FluidStack fluid = entity.getFluid(0);
		if (entity.getTankCapacity(0) == fluid.getAmount()) {
			MUI.drawCenteredString(gui, MUI.uistr("electric_pump.full"), i + 118, j - 46, MUI.RED);
		} else {
			// TODO: Config
			if (entity.getEnergy() > 2000) {
				MUI.drawCenteredString(gui, MUI.uistr("electric_pump.scanning"), i + 118, j - 46, MUI.CYAN);
			} else {
				MUI.drawCenteredString(gui, MUI.uistr("electric_pump.no_power"), i + 118, j - 46, MUI.RED);
			}
		}

		drawOverlay(gui);
	}
}
