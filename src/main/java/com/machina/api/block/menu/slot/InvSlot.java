package com.machina.api.block.menu.slot;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class InvSlot extends SlotItemHandler {

	public InvSlot(IItemHandler container, int id, int x, int y) {
		super(container, id, x, y);
	}

	@Override
	public boolean isHighlightable() {
		return false;
	}
}
