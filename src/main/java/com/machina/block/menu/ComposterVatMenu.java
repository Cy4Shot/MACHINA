package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.block.entity.machine.ComposterVatBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public class ComposterVatMenu extends MachinaContainerMenu<ComposterVatBlockEntity> {

	public ComposterVatMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, clientLevel(), buf.readBlockPos(), inv);
	}

	public ComposterVatMenu(int id, Level level, BlockPos pos, Inventory inv) {
		super(MenuTypeInit.COMPOSTER_VAT.get(), level, pos, id);

		this.addSlot(new AcceptSlot(be, 0, 25, -14,
				stack -> stack.getItemHolder().getData(NeoForgeDataMaps.COMPOSTABLES) != null));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.COMPOSTER_VAT.get();
	}
}
