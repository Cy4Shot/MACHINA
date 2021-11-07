package com.cy4.machina.init;

import static com.cy4.machina.Machina.MACHINA_ITEM_GROUP;

import com.cy4.machina.api.annotation.registries.RegisterItem;
import com.cy4.machina.api.annotation.registries.RegistryHolder;

import net.minecraft.item.Item;

@RegistryHolder
public class ItemInit {

	@RegisterItem("test_item")
	public static final Item TEST_ITEM = new Item(new Item.Properties().tab(MACHINA_ITEM_GROUP));
	
}
