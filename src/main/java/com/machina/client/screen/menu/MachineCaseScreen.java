package com.machina.client.screen.menu;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.entity.machine.MachineCaseBlockEntity;
import com.machina.block.menu.MachineCaseMenu;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class MachineCaseScreen extends MachinaMenuScreen<MachineCaseBlockEntity, MachineCaseMenu> {

	public MachineCaseScreen(MachineCaseMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		this.imageWidth = 230;
		this.imageHeight = 219;
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawOverlay(gui);
		drawMultiblock(gui, MultiblockInit.HABER, this.imageWidth / 2, this.imageHeight / 2, 1, pt);
	}
}
