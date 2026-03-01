package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.menu.MultiblockHousingMenu;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class MultiblockHousingScreen extends MachinaMenuScreen<MultiblockHousingMenu> {

	public MultiblockHousingScreen(MultiblockHousingMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		this.imageWidth = 230;
		this.imageHeight = 219;
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawOverlay(gui);
		MUI.drawMultiblock(gui, MultiblockInit.HABER, this.imageWidth / 2, this.imageHeight / 2, rotX, rotY, 1, pt);
	}
}
