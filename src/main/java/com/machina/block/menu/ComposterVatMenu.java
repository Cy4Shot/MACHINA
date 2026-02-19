package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaMachineMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.block.entity.machine.ComposterVatBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public class ComposterVatMenu extends MachinaMachineMenu<ComposterVatBlockEntity> {

	public ComposterVatMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, client(buf), new ItemStackHandler(1));
	}

	public ComposterVatMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.COMPOSTER_VAT.get(), id, level);

		this.addSlot(new AcceptSlot(container, 0, 25, -14,
				stack -> stack.getItemHolder().getData(NeoForgeDataMaps.COMPOSTABLES) != null));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.COMPOSTER_VAT.get();
	}
}
