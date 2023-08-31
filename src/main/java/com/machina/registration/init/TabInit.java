package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TabInit {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB, Machina.MOD_ID);

	public static final RegistryObject<CreativeModeTab> MACHINA_RESOURCES = create("machina",
			() -> new ItemStack(ItemInit.TRANSISTOR.get()), (pParameters, add) -> {
				for (RegistryObject<Item> e : ItemInit.ITEMS.getEntries()) {
					add.accept(e.get());
				}
			});

	public static final RegistryObject<CreativeModeTab> create(String name, Supplier<ItemStack> item,
			DisplayItemsGenerator gen) {
		return CREATIVE_MODE_TABS.register(name, () -> CreativeModeTab.builder().icon(item)
				.title(Component.translatable(Machina.MOD_ID + ".creativemodetab." + name)).displayItems(gen).build());
	}
}
