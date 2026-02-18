package com.machina.api.block.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class InvSlot extends Slot {

	public InvSlot(Container container, int id, int x, int y) {
		super(container, id, x, y);
	}

	@Override
	public boolean isHighlightable() {
		return false;
	}
}
