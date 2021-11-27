/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.init;

import static com.cy4.machina.Machina.MACHINA_ITEM_GROUP;

import com.cy4.machina.item.ThermalRegulatorSuit;
import com.cy4.machina.item.WrenchItem;
import com.machina.api.registry.annotation.RegisterItem;
import com.machina.api.registry.annotation.RegistryHolder;
import com.machina.api.registry.builder.ItemBuilder;

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
	public static final WrenchItem WRENCH = ItemBuilder.create(WrenchItem::new).tab(MACHINA_ITEM_GROUP).build();
}
