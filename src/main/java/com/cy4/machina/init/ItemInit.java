package com.cy4.machina.init;


import com.cy4.machina.Machina;
import com.cy4.machina.api.annotation.registries.RegisterItem;
import com.cy4.machina.api.annotation.registries.RegistryHolder;

import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@RegistryHolder
public class ItemInit {
	@RegisterItem("test_item")
	public static final Item TEST_ITEM = new Item(new Item.Properties());

	
}
