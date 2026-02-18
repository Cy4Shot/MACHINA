package com.machina.block.menu.connector;

import com.machina.api.block.menu.ConnectorMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.block.entity.connector.FluidPipeBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class FluidPipeMenu extends ConnectorMenu<FluidPipeBlockEntity> {
	public FluidPipeMenu(int id, Inventory inv, FriendlyByteBuf extra) {
		this(id, inv, ContainerLevelAccess.NULL, new ItemStackHandler(1), extra.readEnum(Direction.class));
	}

	public FluidPipeMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container, Direction d) {
		super(MenuTypeInit.FLUID_PIPE.get(), id, level, d);

		invSlots(inv, 0);

		this.addSlot(new AcceptSlot(container, id(0), 108, 5, s -> s.is(ItemInit.FLUID_FILTER.get())));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.FLUID_PIPE.get();
	}
}
