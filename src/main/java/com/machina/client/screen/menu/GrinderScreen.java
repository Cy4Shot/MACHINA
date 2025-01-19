package com.machina.client.screen.menu;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.entity.machine.GrinderBlockEntity;
import com.machina.block.menu.GrinderMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GrinderScreen extends MachinaMenuScreen<GrinderBlockEntity, GrinderMenu> {

	public GrinderScreen(GrinderMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawBackground(gui);
		drawEnergyBar(gui, 117, 30, this.entity.getEnergy() > 0, true, "grinder.no_power");

		drawUpFacingSlot(gui, 0, mx, my, 20, 30, SpecialSlot.PLUS, "grinder.input");
		drawUpFacingSlot(gui, 1, mx, my, 120, 30, SpecialSlot.DUST, "grinder.output");

		drawOverlay(gui);
	}
}
