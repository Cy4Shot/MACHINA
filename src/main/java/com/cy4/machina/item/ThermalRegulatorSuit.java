/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
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
