package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaMachineMenu;
import com.machina.api.block.menu.slot.InvSlot;
import com.machina.api.block.menu.slot.ResultSlot;
import com.machina.block.entity.machine.ElectrolyzerBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ElectrolyzerMenu extends MachinaMachineMenu<ElectrolyzerBlockEntity> {

	public ElectrolyzerMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, client(buf), new ItemStackHandler(3));
	}

	public ElectrolyzerMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.ELECTROLYZER.get(), id, level);

		this.addSlot(new InvSlot(container, 0, 25, 30));
		this.addSlot(new InvSlot(container, 1, 109, -19));
		this.addSlot(new ResultSlot(container, 2, 194, 30));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.ELECTROLYZER.get();
	}
}
