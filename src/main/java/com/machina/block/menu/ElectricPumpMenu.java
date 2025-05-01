package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.block.entity.machine.ElectricPumpBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class ElectricPumpMenu extends MachinaContainerMenu<ElectricPumpBlockEntity> {

	public ElectricPumpMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, clientLevel(), buf.readBlockPos(), inv);
	}

	public ElectricPumpMenu(int id, Level level, BlockPos pos, Inventory inv) {
		super(MenuTypeInit.ELECTRIC_PUMP.get(), level, pos, id);

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.ELECTRIC_PUMP.get();
	}
}
