package com.machina.api.client.screen;

import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.function.Consumer;

public interface IFilteredScreen {

    record FilterSlot(int x, int y, int sx, int sy, Consumer<ItemStack> onSet) {

        public FilterSlot(int x, int y, Consumer<ItemStack> onSet) {
            this(x, y, 16, 16, onSet);
        }

        public Rect2i getRect2i() {
            return new Rect2i(x, y, sx, sy);
        }
    }

    Collection<FilterSlot> getFilterSlots();

}
