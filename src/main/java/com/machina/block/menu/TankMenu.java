package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.api.util.ItemStackUtil;
import com.machina.block.entity.machine.TankBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TankMenu extends MachinaContainerMenu<TankBlockEntity> {

	public TankMenu(int id, Inventory inv) {
		this(id, inv, ContainerLevelAccess.NULL, new ItemStackHandler(2));
	}

	public TankMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.TANK.get(), id, level);

		invSlots(inv, 0);

		this.addSlot(new AcceptSlot(container, 0, 21, 31, ItemStackUtil::hasFluid));
		this.addSlot(new AcceptSlot(container, 1, 198, 31, ItemStackUtil::hasFluid));
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.TANK.get();
	}
}
