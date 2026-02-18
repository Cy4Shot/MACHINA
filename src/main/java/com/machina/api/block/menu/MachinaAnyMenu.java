package com.machina.api.block.menu;

import org.jetbrains.annotations.Nullable;

import com.machina.api.block.entity.ContainerBlockEntity;
import com.machina.api.block.menu.slot.InvSlot;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachinaAnyMenu extends AbstractContainerMenu {

	protected MachinaAnyMenu(MenuType<?> menu, int id) {
		super(menu, id);
	}

	public void invSlots(Inventory inv, int offset) {
		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new InvSlot(inv, j1 + l * 9 + 9, 28 + j1 * 20, 83 + l * 20 + offset));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new InvSlot(inv, i1, 28 + i1 * 20, 148 + offset));
		}
	}

	public abstract Component getName();

	@Nullable
	public abstract ContainerBlockEntity getBlockEntity();

	@Nullable
	public abstract BlockState getDefaultState();
}
