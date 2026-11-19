package com.machina.block.menu.connector;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.block.menu.ConnectorMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ItemConduitMenu extends ConnectorMenu<ConnectorBlockEntity<?, ?>> {
	public ItemConduitMenu(int id, Inventory inv, FriendlyByteBuf extra) {
		this(id, inv, client(extra), new ItemStackHandler(1), extra.readEnum(Direction.class));
	}

	public ItemConduitMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container, Direction d) {
		super(MenuTypeInit.ITEM_CONDUIT.get(), id, level, d);

		invSlots(inv, 0);

		this.addSlot(new AcceptSlot(container, 0, 108, 5,
				s -> s.is(ItemInit.ITEM_FILTER.get()) || s.is(ItemInit.ADVANCED_ITEM_FILTER.get())));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.BASIC_ITEM_CONDUIT.get();
	}

	@Override
	public boolean stillValid(Player player) {
		return this.access.evaluate((level, pos) -> {
			Block block = level.getBlockState(pos).getBlock();
			return (block == BlockInit.BASIC_ITEM_CONDUIT.get() ||
					block == BlockInit.ADVANCED_ITEM_CONDUIT.get() ||
					block == BlockInit.SUPREME_ITEM_CONDUIT.get()) &&
					player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
		}, true);
	}
}
