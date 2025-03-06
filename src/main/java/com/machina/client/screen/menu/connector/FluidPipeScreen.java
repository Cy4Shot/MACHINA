package com.machina.client.screen.menu.connector;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.menu.connector.FluidPipeMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FluidPipeScreen extends MachinaMenuScreen<FluidPipeMenu> {

	public FluidPipeScreen(FluidPipeMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawMiniBackground(gui);

		drawNoFacingSlot(gui, menu.id(0), mx, my, 107, 20, SpecialSlot.CROSS, "fluid_pipe.filter");

		drawOverlay(gui);
	}
}
