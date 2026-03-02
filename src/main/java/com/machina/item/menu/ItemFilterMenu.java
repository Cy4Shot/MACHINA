package com.machina.item.menu;

import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.api.item.menu.ItemMenu;
import com.machina.item.filter.ItemFilterItem;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemFilterMenu extends ItemMenu {

	public ItemFilterMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, hand(buf));
	}

	public ItemFilterMenu(int id, Inventory inv, InteractionHand hand) {
		super(MenuTypeInit.ITEM_FILTER.get(), id, inv, hand);

		invSlots(inv, 0);
	}

	public void insertItemFilter(ItemStack stack) {
		this.stack = ItemFilterItem.set(this.stack, stack.getItem(), null);
		this.containerChanged();
	}

	public void toggleMode() {
        Mode mode = ItemFilterItem.getMode(stack);
        if (mode != null) {
            ItemFilterItem.set(stack, null, mode.opposite());
            this.containerChanged();
        }
    }

	public Item getCurrentFilter() {
		return ItemFilterItem.getItem(stack);
	}

	@Override
	public ItemStack getItem() {
		return ItemInit.ITEM_FILTER.get().getDefaultInstance();
	}

	@Override
	protected int getContainerSize() {
		return 0;
	}
}