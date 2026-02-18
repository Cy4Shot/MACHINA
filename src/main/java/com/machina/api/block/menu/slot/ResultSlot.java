package com.machina.api.block.menu.slot;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ResultSlot extends SlotItemHandler {

	public ResultSlot(IItemHandler container, int id, int x, int y) {
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
