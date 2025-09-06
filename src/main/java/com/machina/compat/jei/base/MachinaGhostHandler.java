package com.machina.compat.jei.base;

import com.machina.api.block.menu.MachinaAnyMenu;
import com.machina.api.client.screen.IFilteredScreen;
import com.machina.api.client.screen.MachinaMenuScreen;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class MachinaGhostHandler<M extends MachinaAnyMenu, T extends MachinaMenuScreen<M> & IFilteredScreen>
        implements IGhostIngredientHandler<T> {

    @Override
    public <I> @NotNull List<Target<I>> getTargetsTyped(T gui, @NotNull ITypedIngredient<I> ingredient, boolean doStart) {
        return gui.getFilterSlots().stream().map(slot -> new Target<I>() {
            @Override
            public @NotNull Rect2i getArea() {
                return slot.getRect2i();
            }

            @Override
            public void accept(@NotNull I i) {
                if (i instanceof ItemStack stack) {
                    slot.onSet().accept(stack);
                }
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void onComplete() {
        // Do Nothing.
    }

}
