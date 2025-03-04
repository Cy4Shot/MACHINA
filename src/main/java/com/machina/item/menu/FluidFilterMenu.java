package com.machina.item.menu;

import com.machina.api.item.menu.ItemMenu;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.world.item.ItemStack;

public class FluidFilterMenu extends ItemMenu {

	public FluidFilterMenu(int id) {
		super(MenuTypeInit.FLUID_FILTER.get(), id);
	}

	@Override
	public ItemStack getItem() {
		return ItemInit.FLUID_FILTER.get().getDefaultInstance();
	}
}