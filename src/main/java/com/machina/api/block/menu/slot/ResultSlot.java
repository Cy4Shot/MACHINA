package com.machina.api.block.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ResultSlot extends Slot {

    public ResultSlot(Container container, int id, int x, int y) {
        super(container, id, x, y);
    }

    @Override
    public boolean isHighlightable() {
        return false;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}
