package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.InvSlot;
import com.machina.api.block.menu.slot.ResultSlot;
import com.machina.block.entity.machine.ElectrolyzerBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class ElectrolyzerMenu extends MachinaContainerMenu<ElectrolyzerBlockEntity> {

	public ElectrolyzerMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, clientLevel(), buf.readBlockPos(), inv);
	}

	public ElectrolyzerMenu(int id, Level level, BlockPos pos, Inventory inv) {
		super(MenuTypeInit.ELECTROLYZER.get(), level, pos, id);

		this.addSlot(new InvSlot(be, 0, 25, 30));
		this.addSlot(new InvSlot(be, 1, 109, -19));
		this.addSlot(new ResultSlot(be, 2, 194, 30));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.ELECTROLYZER.get();
	}
}
