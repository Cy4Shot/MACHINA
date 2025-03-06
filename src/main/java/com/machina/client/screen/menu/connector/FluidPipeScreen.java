package com.machina.client.screen.menu.connector;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.block.menu.connector.FluidPipeMenu;
import com.machina.item.FluidFilterItem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class FluidPipeScreen extends MachinaMenuScreen<FluidPipeMenu> {

	public FluidPipeScreen(FluidPipeMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {

		int id = menu.id(0);

		drawInventory(gui, mx, my);
		drawMiniBackground(gui);

		drawNoFacingSlot(gui, id, mx, my, 107, 0, SpecialSlot.DROP, "fluid_pipe.filter");

		drawToggle(gui, mx, my, 0, 0, FluidFilterItem.getMode(menu.getBlockEntity().getItem(id)) == Mode.BLACKLIST,
				SpecialSlot.BLACKLIST, SpecialSlot.WHITELIST, (val) -> {
					ItemStack stack = FluidFilterItem.set(menu.getBlockEntity().getItem(id), null,
							val ? Mode.BLACKLIST : Mode.WHITELIST);
					this.menu.setItem(id, stack);
				});

		int i = midWidth();
		int j = midHeight();
		blitCommon(gui, i + 133, j + 6, 405, 13, 17, 6);
		blitCommon(gui, i + 82, j + 6, 422, 13, 17, 6);

		drawOverlay(gui);
	}
}
