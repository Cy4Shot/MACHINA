package com.machina.client.screen.menu.item;

import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.IFilteredScreen;
import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.item.filter.FluidFilterItem;
import com.machina.item.menu.FluidFilterMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class FluidFilterScreen extends MachinaMenuScreen<FluidFilterMenu> implements IFilteredScreen {

	public FluidFilterScreen(FluidFilterMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void init() {
		super.init();

		int i = midWidth();
		int j = midHeight();

		clickAndHoverItem(i + 89, j + 34, i + 89 + 17, j + 34 + 17, () -> true, () -> MUI.uistr("fluid_filter.insert"),
				menu::insertFluidFilter);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawMiniBackground(gui);

		int i1 = midWidth();
		int j1 = midHeight();
		Mode mode = FluidFilterItem.getMode(menu.stack);
		Fluid fluid = FluidFilterItem.getFluid(menu.stack);
		MUI.drawCenteredString(gui,
				mode.comp().setStyle(Style.EMPTY.withColor(mode == Mode.BLACKLIST ? 0xFF0000 : 0x00FF00).withBold(true))
						.append(MUI.uistr("fluid_filter.for")
								.withStyle(Style.EMPTY.withColor(MUI.CYAN).withBold(false))),
				i1 + 117, j1 + 4);
		MUI.drawCenteredString(gui, Component.translatable(fluid.getFluidType().getDescriptionId()).setStyle(Style.EMPTY
				.withColor(IClientFluidTypeExtensions.of(fluid).getTintColor(new FluidStack(fluid, 1))).withBold(true)),
				i1 + 117, j1 + 6 + font.lineHeight);

		drawGhostSlot(gui, () -> false, mx, my, 89, 34, MuiSlot.FLUID, "", (i, j) -> {
			MUI.renderFluid(gui, new FluidStack(menu.getCurrentFilter(), 1), i + 1, j + 17, 16, 16, 0);
		});

		drawToggle(gui, mx, my, 125, 34, mode == Mode.BLACKLIST, MuiSlot.BLACKLIST, MuiSlot.WHITELIST,
				x -> menu.toggleMode(), () -> FluidFilterItem.getMode(menu.stack).comp());

		MUI.blitCommon(gui, i1 + 151, j1 + 40, 405, 13, 17, 6);
		MUI.blitCommon(gui, i1 + 64, j1 + 40, 422, 13, 17, 6);

		drawOverlay(gui);
	}

	@Override
	public Collection<FilterSlot> getFilterSlots() {
		int i = midWidth();
		int j = midHeight();
		return List.of(new FilterSlot(i + 90, j + 35, menu::insertFluidFilter));
	}
}
