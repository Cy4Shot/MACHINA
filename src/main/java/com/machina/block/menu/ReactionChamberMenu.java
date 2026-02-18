package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.InvSlot;
import com.machina.api.block.menu.slot.ResultSlot;
import com.machina.block.entity.machine.ReactionChamberBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ReactionChamberMenu extends MachinaContainerMenu<ReactionChamberBlockEntity> {

	public ReactionChamberMenu(int id, Inventory inv) {
		this(id, inv, ContainerLevelAccess.NULL, new ItemStackHandler(4));
	}

	public ReactionChamberMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.REACTION_CHAMBER.get(), id, level);

		this.addSlot(new InvSlot(container, 0, 5, 30));
		this.addSlot(new InvSlot(container, 1, 25, 30));
		this.addSlot(new ResultSlot(container, 2, 194, 30));
		this.addSlot(new ResultSlot(container, 3, 214, 30));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.REACTION_CHAMBER.get();
	}
}
