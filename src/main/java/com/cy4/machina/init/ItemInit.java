package com.cy4.machina.init;

import static com.cy4.machina.Machina.MACHINA_ITEM_GROUP;

import com.cy4.machina.api.annotation.registries.RegisterItem;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.item.ThermalRegulatorSuit;
import com.cy4.machina.item.WrenchItem;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;

@RegistryHolder
public final class ItemInit {

	@RegisterItem("item_group_icon")
	public static final Item ITEM_GROUP_ICON = new Item(new Item.Properties().tab(MACHINA_ITEM_GROUP)) {

		@Override
		public void fillItemCategory(net.minecraft.item.ItemGroup pGroup,
				net.minecraft.util.NonNullList<net.minecraft.item.ItemStack> pItems) {
		}
	};

	@RegisterItem("thermal_regulating_helmet")
	public static final ThermalRegulatorSuit THERMAL_REGULATING_HELMET = new ThermalRegulatorSuit(
			new Item.Properties().tab(MACHINA_ITEM_GROUP).defaultDurability(512), EquipmentSlotType.HEAD);
	@RegisterItem("thermal_regulating_chestplate")
	public static final ThermalRegulatorSuit THERMAL_REGULATING_CHESTPLATE = new ThermalRegulatorSuit(
			new Item.Properties().tab(MACHINA_ITEM_GROUP).defaultDurability(512), EquipmentSlotType.CHEST);
	@RegisterItem("thermal_regulating_leggings")
	public static final ThermalRegulatorSuit THERMAL_REGULATING_LEGGINGS = new ThermalRegulatorSuit(
			new Item.Properties().tab(MACHINA_ITEM_GROUP).defaultDurability(512), EquipmentSlotType.LEGS);
	@RegisterItem("thermal_regulating_boots")
	public static final ThermalRegulatorSuit THERMAL_REGULATING_BOOTS = new ThermalRegulatorSuit(
			new Item.Properties().tab(MACHINA_ITEM_GROUP).defaultDurability(512), EquipmentSlotType.FEET);

	@RegisterItem("wrench")
	public static final WrenchItem WRENCH = new WrenchItem(new Item.Properties().tab(MACHINA_ITEM_GROUP));
}
