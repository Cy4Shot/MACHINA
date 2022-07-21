package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.item.CatalystItem;
import com.machina.item.ScannerItem;
import com.machina.item.ShipComponentItem;
import com.machina.item.WrenchItem;
import com.machina.registration.builder.ItemBuilder;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Machina.MOD_ID);

	//@formatter:off
	public static final RegistryObject<WrenchItem> WRENCH = basic("wrench", WrenchItem::new);
	public static final RegistryObject<ShipComponentItem> SHIP_COMPONENT = basic("ship_component", ShipComponentItem::new);
	public static final RegistryObject<ScannerItem> SCANNER = basic("scanner", ScannerItem::new);
	public static final RegistryObject<CatalystItem> IRON_CATALYST = basic("iron_catalyst", p -> new CatalystItem(p, 500));
	public static final RegistryObject<Item> REINFORCED_STICK = basic("reinforced_stick");
	public static final RegistryObject<Item> STEEL_INGOT = basic("steel_ingot");
	public static final RegistryObject<Item> STEEL_NUGGET = basic("steel_nugget");
	public static final RegistryObject<Item> ALUMINUM_INGOT = basic("aluminum_ingot");
	public static final RegistryObject<Item> ALUMINUM_NUGGET = basic("aluminum_nugget");
	public static final RegistryObject<Item> PROCESSOR = basic("processor");
	public static final RegistryObject<Item> SILICON = basic("silicon");
	public static final RegistryObject<Item> TRANSISTOR = basic("transistor");
	public static final RegistryObject<Item> AMMONIUM_NITRATE = basic("ammonium_nitrate");
	//@formatter:on

	public static RegistryObject<Item> basic(String name) {
		return register(name, () -> ItemBuilder.basicItem());
	}

	public static <T extends Item> RegistryObject<T> basic(String name, NonNullFunction<Item.Properties, T> factory) {
		return register(name, () -> ItemBuilder.basicItem(factory));
	}

	public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
		return ITEMS.register(name, item);
	}
}
