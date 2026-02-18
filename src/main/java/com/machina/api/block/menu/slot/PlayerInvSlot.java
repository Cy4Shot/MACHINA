package com.machina.api.block.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class PlayerInvSlot extends Slot {

	public PlayerInvSlot(Container container, int id, int x, int y) {
		super(container, id, x, y);
	}

	@Override
	public boolean isHighlightable() {
		return false;
	}
}
