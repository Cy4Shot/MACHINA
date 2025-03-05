package com.machina.client.screen.menu.item;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.item.menu.FluidFilterMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;

public class FluidFilterScreen extends MachinaMenuScreen<FluidFilterMenu> {

	public FluidFilterScreen(FluidFilterMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void init() {
		super.init();

		int i = midWidth();
		int j = midHeight();

		clickAndHoverItem(i + 107, j + 20, i + 107 + 17, j + 20 + 17, () -> true,
				() -> Component.translatable("fluid_filter.insert"), (item) -> {
					menu.insertFluidFilter(item);
				});
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawMiniBackground(gui);

		drawGhostSlot(gui, () -> false, mx, my, 107, 20, SpecialSlot.DROP, "fluid_filter.filter", (i, j) -> {
			renderFluid(gui, new FluidStack(menu.getCurrentFilter(), 1), i + 1, j + 17, 16, 16, 0);
		});

		drawOverlay(gui);
	}
}
