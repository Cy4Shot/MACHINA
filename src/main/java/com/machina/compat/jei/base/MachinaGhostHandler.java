package com.machina.compat.jei.base;

import java.util.List;
import java.util.stream.Collectors;

import com.machina.api.client.screen.IFilteredScreen;
import com.machina.api.client.screen.MachinaMenuScreen;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

public class MachinaGhostHandler<T extends MachinaMenuScreen<?> & IFilteredScreen>
		implements IGhostIngredientHandler<T> {

	@Override
	public <I> List<Target<I>> getTargetsTyped(T gui, ITypedIngredient<I> ingredient, boolean doStart) {
		return gui.getFilterSlots().stream().map(slot -> new Target<I>() {
			@Override
			public Rect2i getArea() {
				return slot.getRect2i();
			}

			@Override
			public void accept(I i) {
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
