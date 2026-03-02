package com.machina.api.block.menu.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class AcceptSlot extends SlotItemHandler {

	private final Predicate<ItemStack> acceptor;

	public AcceptSlot(IItemHandler container, int id, int x, int y, Predicate<ItemStack> acceptor) {
		super(container, id, x, y);

		this.acceptor = acceptor;
	}

	@Override
	public boolean isHighlightable() {
		return false;
	}

	@Override
	public boolean mayPlace(@NotNull ItemStack stack) {
		return this.acceptor.test(stack);
	}
}
