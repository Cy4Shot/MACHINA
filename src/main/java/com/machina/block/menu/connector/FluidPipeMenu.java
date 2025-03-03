package com.machina.block.menu.connector;

import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.block.entity.connector.FluidPipeBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class FluidPipeMenu extends MachinaContainerMenu<FluidPipeBlockEntity> {
	public FluidPipeMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, clientLevel(), buf.readBlockPos(), inv);
	}

	public FluidPipeMenu(int id, Level level, BlockPos pos, Inventory inv) {
		super(MenuTypeInit.FLUID_PIPE.get(), level, pos, id);

		invSlots(inv, 0);
	}

	@Override
	protected Block getBlock() {
		return BlockInit.FLUID_PIPE.get();
	}
}
