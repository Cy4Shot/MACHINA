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

package com.cy4.machina.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * The thermal regulator suit that prevents damage from traits that include
 * temperature change
 * 
 * @author matyrobbrt
 *
 */
public class ThermalRegulatorSuit extends Item {

	public final EquipmentSlotType slot;

	public ThermalRegulatorSuit(Properties pProperties, EquipmentSlotType slot) {
		super(pProperties);
		this.slot = slot;
	}

	@Override
	public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
		return slot;
	}

	/**
	 * Inefficient way but it works... a loop was a bit redundant here
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isFullSuit(PlayerEntity player) {
		NonNullList<ItemStack> armourSlots = player.inventory.armor;
		return armourSlots.get(0).getItem() instanceof ThermalRegulatorSuit
				&& armourSlots.get(1).getItem() instanceof ThermalRegulatorSuit
				&& armourSlots.get(2).getItem() instanceof ThermalRegulatorSuit
				&& armourSlots.get(3).getItem() instanceof ThermalRegulatorSuit;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return super.canApplyAtEnchantingTable(stack, enchantment) && enchantment != Enchantments.MENDING;
	}

}
