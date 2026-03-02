package com.machina.client.screen.menu.connector;

import com.machina.api.cap.sided.ConnectionSide;
import com.machina.api.cap.sided.Side;
import com.machina.api.client.screen.IFilteredScreen;
import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.api.network.c2s.C2SMenuSetItem;
import com.machina.api.network.c2s.C2SMenuToggleConnector;
import com.machina.block.menu.connector.FluidPipeMenu;
import com.machina.item.filter.FluidFilterItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class FluidPipeScreen extends MachinaMenuScreen<FluidPipeMenu> implements IFilteredScreen {

	public FluidPipeScreen(FluidPipeMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	private void setFluidStack(ItemStack stack) {
		Optional<IFluidHandlerItem> handler = FluidUtil.getFluidHandler(stack.copyWithCount(1));
		if (handler.isPresent()) {
			IFluidHandlerItem f = handler.get();
			FluidStack fluid = f.getFluidInTank(0);
			if (!fluid.isEmpty() && menu.getBlockEntity() != null) {
				ItemStack newStack = FluidFilterItem.set(menu.getSlot(menu.id(0)).getItem(), fluid.getFluid(), null);
				PacketDistributor.sendToServer(new C2SMenuSetItem(menu.id(0), newStack, menu.getBlockPos()));
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

        if (menu.getBlockEntity() == null) {
            return;
        }

        // Top slot
        drawNoFacingSlot(gui, id, mx, my, 107, 4, MuiSlot.FLUID, "fluid_pipe.filter");
        MUI.blitCommon(gui, i + 133, j + 10, 405, 13, 17, 6);
        MUI.blitCommon(gui, i + 82, j + 10, 422, 13, 17, 6);

        // Fluid Slot
        drawGhostSlot(gui, () -> false, mx, my, 74, 34, MuiSlot.FLUID, "",
                (i1, j1) -> MUI.renderFluid(gui,
                        new FluidStack(FluidFilterItem.getFluid(menu.getBlockEntity().getItem(id)), 1), i1 + 1, j1 + 17,
                        16, 16, 0));
        clickAndHoverItem(i + 74, j + 34, i + 74 + 17, j + 34 + 17, () -> true, () -> MUI.uistr("fluid_pipe.insert"),
                this::setFluidStack);

        // Mode Slot
        Mode mode = FluidFilterItem.getMode(menu.getBlockEntity().getItem(id));

        drawToggle(gui, mx, my, 107, 34, mode == Mode.BLACKLIST,
                MuiSlot.BLACKLIST, MuiSlot.WHITELIST, (val) -> {
                    ItemStack stack = FluidFilterItem.set(menu.getBlockEntity().getItem(id), null,
                            val ? Mode.BLACKLIST : Mode.WHITELIST);
                    PacketDistributor
                            .sendToServer(new C2SMenuSetItem(menu.id(0), stack, menu.getBlockEntity().getBlockPos()));
                }, mode::comp);

        // IO Slot
        ConnectionSide side = menu.getConnection();
        if (side.isIO()) {
            drawToggleIO(gui, mx, my, 140, 34, side.isInput() ? Side.INPUT : Side.OUTPUT,
                    () -> menu.getConnection().comp().setStyle(
                            Style.EMPTY.withColor(menu.getConnection() == ConnectionSide.INPUT ? 0x0377fc : 0xfc9003)),
                    () -> PacketDistributor.sendToServer(new C2SMenuToggleConnector(menu.dir, menu.getBlockPos())));
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
