package com.machina.item.menu;

import java.util.Optional;

import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.api.item.menu.ItemMenu;
import com.machina.item.filter.FluidFilterItem;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.MenuTypeInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class FluidFilterMenu extends ItemMenu {

    public FluidFilterMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, hand(buf));
    }

    public FluidFilterMenu(int id, Inventory inv, InteractionHand hand) {
        super(MenuTypeInit.FLUID_FILTER.get(), id, inv, hand);

        invSlots(inv, 0);
    }

    public void insertFluidFilter(ItemStack stack) {
        Optional<IFluidHandlerItem> handler = FluidUtil.getFluidHandler(stack.copyWithCount(1));
        handler.ifPresent(f -> {
            FluidStack fluid = f.getFluidInTank(0);
            if (!fluid.isEmpty()) {
                this.stack = FluidFilterItem.set(this.stack, fluid.getFluid(), null);
                this.containerChanged();
            }
        });
    }

    public void toggleMode() {
        Mode mode = FluidFilterItem.getMode(stack);
        FluidFilterItem.set(stack, null, mode.opposite());
        this.containerChanged();
    }

    public Fluid getCurrentFilter() {
        return FluidFilterItem.getFluid(stack);
    }

    @Override
    public ItemStack getItem() {
        return ItemInit.FLUID_FILTER.get().getDefaultInstance();
    }

    @Override
    protected int getContainerSize() {
        return 0;
    }
}