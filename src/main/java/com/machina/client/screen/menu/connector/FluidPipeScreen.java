package com.machina.client.screen.menu.connector;

import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.machina.api.cap.sided.ConnectionSide;
import com.machina.api.cap.sided.Side;
import com.machina.api.client.screen.IFilteredScreen;
import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.SpecialSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.api.network.PacketSender;
import com.machina.api.network.c2s.C2SMenuSetItem;
import com.machina.api.network.c2s.C2SMenuToggleConnector;
import com.machina.block.menu.connector.FluidPipeMenu;
import com.machina.item.FluidFilterItem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class FluidPipeScreen extends MachinaMenuScreen<FluidPipeMenu> implements IFilteredScreen {

	public FluidPipeScreen(FluidPipeMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	private void setFluidStack(ItemStack stack) {
		LazyOptional<IFluidHandlerItem> handler = FluidUtil.getFluidHandler(stack.copyWithCount(1));
		if (handler.isPresent()) {
			IFluidHandlerItem f = handler.resolve().get();
			FluidStack fluid = f.getFluidInTank(0);
			if (!fluid.isEmpty()) {
				ItemStack newStack = FluidFilterItem.set(menu.getBlockEntity().getItem(menu.id(0)), fluid.getFluid(),
						null);
				PacketSender
						.sendToServer(new C2SMenuSetItem(menu.id(0), newStack, menu.getBlockEntity().getBlockPos()));
			}
		}
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		int id = menu.id(0);
		int i = midWidth();
		int j = midHeight();

		drawInventory(gui, mx, my);
		drawMiniBackground(gui);

		// Top slot
		drawNoFacingSlot(gui, id, mx, my, 107, 4, SpecialSlot.DROP, "fluid_pipe.filter");
		MUI.blitCommon(gui, i + 133, j + 10, 405, 13, 17, 6);
		MUI.blitCommon(gui, i + 82, j + 10, 422, 13, 17, 6);

		// Fluid Slot
		drawGhostSlot(gui, () -> false, mx, my, 74, 34, SpecialSlot.DROP, "", (i1, j1) -> {
			renderFluid(gui, new FluidStack(FluidFilterItem.getFluid(menu.getBlockEntity().getItem(id)), 1), i1 + 1,
					j1 + 17, 16, 16, 0);
		});
		clickAndHoverItem(i + 74, j + 34, i + 74 + 17, j + 34 + 17, () -> true, () -> MUI.uistr("fluid_pipe.insert"),
				this::setFluidStack);

		// Mode Slot
		drawToggle(gui, mx, my, 107, 34, FluidFilterItem.getMode(menu.getBlockEntity().getItem(id)) == Mode.BLACKLIST,
				SpecialSlot.BLACKLIST, SpecialSlot.WHITELIST, (val) -> {
					ItemStack stack = FluidFilterItem.set(menu.getBlockEntity().getItem(id), null,
							val ? Mode.BLACKLIST : Mode.WHITELIST);
					PacketSender
							.sendToServer(new C2SMenuSetItem(menu.id(0), stack, menu.getBlockEntity().getBlockPos()));
				}, () -> FluidFilterItem.getMode(menu.getBlockEntity().getItem(id)).comp());

		// IO Slot
		ConnectionSide side = menu.be.getConnection(menu.dir);
		if (side.isIO()) {
			drawToggleIO(gui, mx, my, 140, 34, side.isInput() ? Side.INPUT : Side.OUTPUT,
					() -> menu.be.getConnection(menu.dir).comp()
							.setStyle(Style.EMPTY.withColor(
									menu.be.getConnection(menu.dir) == ConnectionSide.INPUT ? 0x0377fc : 0xfc9003)),
					() -> {
						PacketSender.sendToServer(new C2SMenuToggleConnector(menu.dir, menu.be.getBlockPos()));
					});
		}

		drawOverlay(gui);
	}

	@Override
	public Collection<FilterSlot> getFilterSlots() {
		int i = midWidth();
		int j = midHeight();
		return List.of(new FilterSlot(i + 75, j + 35, this::setFluidStack));
	}
}
