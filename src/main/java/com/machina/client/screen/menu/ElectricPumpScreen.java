package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.entity.machine.ElectricPumpBlockEntity;
import com.machina.block.menu.ElectricPumpMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class ElectricPumpScreen extends MachinaMenuScreen<ElectricPumpMenu> {

	public ElectricPumpScreen(ElectricPumpMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawBackground(gui);

		drawFluidBar(gui, 0, 0, 0);

		int i = midWidth();
		int j = midHeight();

		FluidStack fluid = this.<ElectricPumpBlockEntity>entity().getFluid(0);
		if (fluid.isEmpty()) {
			MUI.drawCenteredString(gui, MUI.uistr("elecric_pump.empty"), i + 118, j - 30, MUI.RED);
		} else {
			int col = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor();
			MUI.drawCenteredString(gui, fluid.getDisplayName().copy().withStyle(Style.EMPTY.withBold(true)), i + 118,
					j - 30, col);
		}

		drawOverlay(gui);
	}
}
