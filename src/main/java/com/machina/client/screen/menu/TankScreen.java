package com.machina.client.screen.menu;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.entity.machine.TankBlockEntity;
import com.machina.block.menu.TankMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class TankScreen extends MachinaMenuScreen<TankBlockEntity, TankMenu> {

	public TankScreen(TankMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawBackground(gui);

		drawFluidBar(gui, 0, 0, 0);

		drawOverlay(gui);
	}
}
