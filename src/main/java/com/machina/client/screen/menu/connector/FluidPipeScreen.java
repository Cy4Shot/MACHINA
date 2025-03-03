package com.machina.client.screen.menu.connector;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.entity.connector.FluidPipeBlockEntity;
import com.machina.block.menu.connector.FluidPipeMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FluidPipeScreen extends MachinaMenuScreen<FluidPipeBlockEntity, FluidPipeMenu> {

	public FluidPipeScreen(FluidPipeMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawUpFacingSlot(gui, 1, mx, my, 20, SpecialSlot.PLUS, "battery.input");

		drawOverlay(gui);
	}
}
