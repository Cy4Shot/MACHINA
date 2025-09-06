package com.machina.block.menu.connector;

import com.machina.api.block.menu.ConnectorMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.block.entity.connector.ItemConduitBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ItemConduitMenu extends ConnectorMenu<ItemConduitBlockEntity> {
    public ItemConduitMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, clientLevel(), buf.readBlockPos(), inv, buf.readEnum(Direction.class));
    }

    public ItemConduitMenu(int id, Level level, BlockPos pos, Inventory inv, Direction d) {
        super(MenuTypeInit.ITEM_CONDUIT.get(), level, pos, id, d);

        invSlots(inv, 0);

        this.addSlot(new AcceptSlot(be, id(0), 108, 5,
                s -> s.is(ItemInit.ITEM_FILTER.get()) || s.is(ItemInit.ADVANCED_ITEM_FILTER.get())));
    }

    @Override
    protected Block getBlock() {
        return BlockInit.ITEM_CONDUIT.get();
    }
}
