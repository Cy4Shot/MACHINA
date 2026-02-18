package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.InvSlot;
import com.machina.api.block.menu.slot.ResultSlot;
import com.machina.block.entity.machine.CompressorBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CompressorMenu extends MachinaContainerMenu<CompressorBlockEntity> {

	public CompressorMenu(int id, Inventory inv) {
		this(id, inv, ContainerLevelAccess.NULL, new ItemStackHandler(3));
	}

	public CompressorMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.COMPRESSOR.get(), id, level);

		this.addSlot(new InvSlot(container, 0, 62, -29));
		this.addSlot(new InvSlot(container, 1, 108, -5));
		this.addSlot(new ResultSlot(container, 2, 154, -29));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.COMPRESSOR.get();
	}
}
