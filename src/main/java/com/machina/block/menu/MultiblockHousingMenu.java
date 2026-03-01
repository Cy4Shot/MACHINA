package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaMachineMenu;
import com.machina.block.entity.machine.MultiblockHousingBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class MultiblockHousingMenu extends MachinaMachineMenu<MultiblockHousingBlockEntity> {

	public MultiblockHousingMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, client(buf), new ItemStackHandler(0));
	}

	public MultiblockHousingMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.MULTIBLOCK_HOUSING.get(), id, level);

//        this.addSlot(new AcceptSlot(container, 0, -2, 74, ItemStackUtil::isBlueprint));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.MULTIBLOCK_HOUSING.get();
	}
}
