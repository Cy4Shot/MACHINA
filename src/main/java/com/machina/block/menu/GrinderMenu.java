package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.InvSlot;
import com.machina.api.block.menu.slot.ResultSlot;
import com.machina.block.entity.machine.GrinderBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class GrinderMenu extends MachinaContainerMenu<GrinderBlockEntity> {

	public GrinderMenu(int id, Inventory inv) {
		this(id, inv, ContainerLevelAccess.NULL, new ItemStackHandler(2));
	}

	public GrinderMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.GRINDER.get(), id, level);

		this.addSlot(new InvSlot(container, 0, 62, -17));
		this.addSlot(new ResultSlot(container, 1, 154, -17));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.GRINDER.get();
	}
}
