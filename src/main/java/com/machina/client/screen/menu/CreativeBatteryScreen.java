package com.machina.client.screen.menu;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.entity.machine.CreativeBatteryBlockEntity;
import com.machina.block.menu.CreativeBatteryMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CreativeBatteryScreen extends MachinaMenuScreen<CreativeBatteryBlockEntity, CreativeBatteryMenu> {

	public CreativeBatteryScreen(CreativeBatteryMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawEnergyBar(gui, 0, true, "");
		drawOverlay(gui);
	}
}
