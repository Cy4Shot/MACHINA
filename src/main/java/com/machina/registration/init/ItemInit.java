package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.item.ShipComponentItem;
import com.machina.item.ThermalRegulatorSuit;
import com.machina.item.WrenchItem;
import com.machina.registration.Registration;
import com.machina.registration.builder.ItemBuilder;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Machina.MOD_ID);

	public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
		return ITEMS.register(name, item);
	}

	public static final RegistryObject<ThermalRegulatorSuit> THERMAL_REGULATING_HELMET = register("thermal_regulating_helmet",
			() -> new ThermalRegulatorSuit(new Item.Properties().tab(Registration.MACHINA_ITEM_GROUP).defaultDurability(512),
					EquipmentSlotType.HEAD));
	public static final RegistryObject<ThermalRegulatorSuit> THERMAL_REGULATING_CHESTPLATE = register("thermal_regulating_chestplate",
			() -> new ThermalRegulatorSuit(new Item.Properties().tab(Registration.MACHINA_ITEM_GROUP).defaultDurability(512),
					EquipmentSlotType.CHEST));
	public static final RegistryObject<ThermalRegulatorSuit> THERMAL_REGULATING_LEGGINGS = register("thermal_regulating_leggings",
			() -> new ThermalRegulatorSuit(new Item.Properties().tab(Registration.MACHINA_ITEM_GROUP).defaultDurability(512),
					EquipmentSlotType.LEGS));
	public static final RegistryObject<ThermalRegulatorSuit> THERMAL_REGULATING_BOOTS = register("thermal_regulating_boots",
			() -> new ThermalRegulatorSuit(new Item.Properties().tab(Registration.MACHINA_ITEM_GROUP).defaultDurability(512),
					EquipmentSlotType.FEET));

	public static final RegistryObject<WrenchItem> WRENCH = register("wrench", () -> ItemBuilder.basicItem(WrenchItem::new));
	
	public static final RegistryObject<ShipComponentItem> SHIP_COMPONENT = register("ship_component", () -> ItemBuilder.basicItem(ShipComponentItem::new));
}
