package com.machina.client.screen.menu.item;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.item.menu.FluidFilterMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class FluidFilterScreen extends MachinaMenuScreen<FluidFilterMenu> {

	public FluidFilterScreen(FluidFilterMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawMiniBackground(gui);

		drawGhostSlot(gui, () -> false, mx, my, 107, 20, SpecialSlot.DROP, "fluid_filter.filter", (i, j) -> {
			renderFluid(gui, new FluidStack(Fluids.WATER, 1000), i + 1, j + 17, 16, 16, 0);
		});

		drawOverlay(gui);
	}
}
