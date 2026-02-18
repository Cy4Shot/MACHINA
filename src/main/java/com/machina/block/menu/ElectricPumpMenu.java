package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.block.entity.machine.ElectricPumpBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ElectricPumpMenu extends MachinaContainerMenu<ElectricPumpBlockEntity> {

	public ElectricPumpMenu(int id, Inventory inv) {
		this(id, inv, ContainerLevelAccess.NULL, new ItemStackHandler(0));
	}

	public ElectricPumpMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.ELECTRIC_PUMP.get(), id, level);

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.ELECTRIC_PUMP.get();
	}
}
