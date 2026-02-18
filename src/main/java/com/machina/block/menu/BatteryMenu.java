package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.api.util.ItemStackUtil;
import com.machina.block.entity.machine.BatteryBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class BatteryMenu extends MachinaContainerMenu<BatteryBlockEntity> {
	public BatteryMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, client(buf), new ItemStackHandler(3));
	}

	public BatteryMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.BATTERY.get(), id, level);

		this.addSlot(new AcceptSlot(container, 0, 108, -57, ItemStackUtil::isCapacitor));
		this.addSlot(new AcceptSlot(container, 1, 21, 31, ItemStackUtil::hasEnergy));
		this.addSlot(new AcceptSlot(container, 2, 198, 31, ItemStackUtil::hasEnergy));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.BATTERY.get();
	}
}
