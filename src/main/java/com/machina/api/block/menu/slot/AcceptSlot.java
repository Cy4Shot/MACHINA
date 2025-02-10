package com.machina.api.block.menu.slot;

import java.util.function.Predicate;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AcceptSlot extends Slot {

	private final Predicate<ItemStack> acceptor;

	public AcceptSlot(Container container, int id, int x, int y, Predicate<ItemStack> acceptor) {
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
