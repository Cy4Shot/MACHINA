package com.cy4.machina.init;

import static com.cy4.machina.Machina.MACHINA_ITEM_GROUP;

import com.cy4.machina.api.annotation.registries.RegisterItem;
import com.cy4.machina.api.annotation.registries.RegistryHolder;

import net.minecraft.item.Item;

@RegistryHolder
public class ItemInit {
	/**
	@RegisterItem("item_group_icon")
	public static final Item ITEM_GROUP_ICON = new Item(new Item.Properties().tab(MACHINA_ITEM_GROUP)) {
		@Override
		public void fillItemCategory(net.minecraft.item.ItemGroup pGroup,
				net.minecraft.util.NonNullList<net.minecraft.item.ItemStack> pItems) {
			// We need to hide the item
		}
	};
	**/

	@RegisterItem("item_group_icon")
	public static final Item ITEM_GROUP_ICON = new Item(new Item.Properties());
}
