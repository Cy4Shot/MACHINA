package com.machina.api.item.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public abstract class ItemMenu extends AbstractContainerMenu {

	protected ItemMenu(MenuType<?> t, int w) {
		super(t, w);
	}

	@Override
	public boolean stillValid(Player p) {
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}
}
