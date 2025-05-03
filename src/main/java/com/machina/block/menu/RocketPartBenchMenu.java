package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.InvSlot;
import com.machina.block.entity.machine.RocketPartBenchBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class RocketPartBenchMenu extends MachinaContainerMenu<RocketPartBenchBlockEntity> {

	public RocketPartBenchMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, clientLevel(), buf.readBlockPos(), inv);
	}

	public RocketPartBenchMenu(int id, Level level, BlockPos pos, Inventory inv) {
		super(MenuTypeInit.ROCKET_PART_BENCH.get(), level, pos, id);

		this.addSlot(new InvSlot(be, 0, 25, -14));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.ROCKET_PART_BENCH.get();
	}
}
