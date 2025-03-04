package com.machina.api.item.menu;

import org.jetbrains.annotations.Nullable;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.block.menu.MachinaAnyMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ItemMenu extends MachinaAnyMenu {

	protected ItemMenu(MenuType<?> t, int w) {
		super(t, w);
	}

	@Override
	public boolean stillValid(Player p) {
		return true;
	}

	public abstract ItemStack getItem();

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public @Nullable BlockState getDefaultState() {
		return null;
	}

	@Override
	public @Nullable MachinaBlockEntity getBlockEntity() {
		return null;
	}

	@Override
	public Component getName() {
		return getItem().getDisplayName();
	}
}
