package com.machina.item.menu;

import com.machina.api.item.menu.ItemMenu;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class FluidFilterMenu extends ItemMenu {

	public FluidFilterMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, hand(inv, buf));
	}

	public FluidFilterMenu(int id, Inventory inv, ItemStack stack) {
		super(MenuTypeInit.FLUID_FILTER.get(), id, stack);

		invSlots(inv, 0);
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