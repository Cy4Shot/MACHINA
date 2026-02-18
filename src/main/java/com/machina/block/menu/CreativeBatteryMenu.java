package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.block.entity.machine.CreativeBatteryBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class CreativeBatteryMenu extends MachinaContainerMenu<CreativeBatteryBlockEntity> {
	public CreativeBatteryMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, clientLevel(), buf.readBlockPos(), inv);
	}

	public CreativeBatteryMenu(int id, Level level, BlockPos pos, Inventory inv) {
		super(MenuTypeInit.CREATIVE_BATTERY.get(), level, pos, id);

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.CREATIVE_BATTERY.get();
	}
}
