package com.machina.item.menu;

import java.util.function.Consumer;

import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.api.item.menu.ItemMenu;
import com.machina.item.filter.AdvancedItemFilterItem;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AdvancedItemFilterMenu extends ItemMenu {

	public AdvancedItemFilterMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, hand(buf));
	}

	public AdvancedItemFilterMenu(int id, Inventory inv, InteractionHand hand) {
		super(MenuTypeInit.ADVANCED_ITEM_FILTER.get(), id, inv, hand);

		invSlots(inv, 0);
	}

	public Consumer<ItemStack> insertItemFilter(int slot) {
		return (stack) -> {
			NonNullList<Item> items = AdvancedItemFilterItem.getItems(this.stack);
			items.set(slot, stack.getItem());
			AdvancedItemFilterItem.set(this.stack, items, null);
			this.containerChanged();
		};
	}

	public void toggleMode() {
		Mode mode = AdvancedItemFilterItem.getMode(stack);
		AdvancedItemFilterItem.set(stack, null, mode.opposite());
		this.containerChanged();
	}

	@Override
	public ItemStack getItem() {
		return ItemInit.ADVANCED_ITEM_FILTER.get().getDefaultInstance();
	}

	@Override
	protected int getContainerSize() {
		return 0;
	}
}