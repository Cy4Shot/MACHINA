package com.machina.registration.init;

import com.machina.Machina;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TabInit {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB, Machina.MOD_ID);

	public static final RegistryObject<CreativeModeTab> MACHINA_RESOURCES = CREATIVE_MODE_TABS.register("machina_main",
			() -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemInit.TRANSISTOR.get()))
					.title(Component.translatable("creativemodetab.machina_main")).displayItems((pParameters, add) -> {
						for (RegistryObject<Item> e : ItemInit.ITEMS.getEntries()) {
							add.accept(e.get());
						}
					}).build());
}
