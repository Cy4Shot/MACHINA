package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.api.util.ItemStackUtil;
import com.machina.block.entity.machine.BatteryBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class BatteryMenu extends MachinaContainerMenu<BatteryBlockEntity> {
	public BatteryMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, clientLevel(), buf.readBlockPos(), inv);
	}

	public BatteryMenu(int id, Level level, BlockPos pos, Inventory inv) {
		super(MenuTypeInit.BATTERY.get(), level, pos, id);

		this.addSlot(new AcceptSlot(be, 0, 108, -57, ItemStackUtil::isCapacitor));
		this.addSlot(new AcceptSlot(be, 1, 21, 31, ItemStackUtil::hasEnergy));
		this.addSlot(new AcceptSlot(be, 2, 198, 31, ItemStackUtil::hasEnergy));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.BATTERY.get();
	}
}
