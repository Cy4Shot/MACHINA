package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.block.entity.machine.MachineCaseBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class MachineCaseMenu extends MachinaContainerMenu<MachineCaseBlockEntity> {

	public MachineCaseMenu(int id, Inventory inv) {
		this(id, inv, ContainerLevelAccess.NULL, new ItemStackHandler(0));
	}

	public MachineCaseMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.MACHINE_CASE.get(), id, level);

//        this.addSlot(new AcceptSlot(container, 0, -2, 74, ItemStackUtil::isBlueprint));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.BASIC_MACHINE_CASE.get();
	}
}
