package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaMachineMenu;
import com.machina.api.block.menu.slot.InvSlot;
import com.machina.block.entity.machine.SolidifierBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class SolidifierMenu extends MachinaMachineMenu<SolidifierBlockEntity> {

	public SolidifierMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, client(buf), new ItemStackHandler(1));
	}

	public SolidifierMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.SOLIDIFIER.get(), id, level);

		this.addSlot(new InvSlot(container, 0, 193, -14));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.SOLIDIFIER.get();
	}
}
