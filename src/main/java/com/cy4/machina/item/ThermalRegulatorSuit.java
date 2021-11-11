package com.cy4.machina.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * The thermal regulator suit that prevents damage from traits that include temperature change
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
	public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
		return armorType == slot;
	}
	
	/**
	 * Inefficient way but it works... a loop was a bit redundant here
	 * @param player
	 * @return
	 */
	public static boolean isFullSuit(PlayerEntity player) {
		NonNullList<ItemStack> armourSlots = player.inventory.armor;
		return armourSlots.get(0).getItem() instanceof ThermalRegulatorSuit && armourSlots.get(1).getItem() instanceof ThermalRegulatorSuit
				&& armourSlots.get(2).getItem() instanceof ThermalRegulatorSuit && armourSlots.get(3).getItem() instanceof ThermalRegulatorSuit;
	}

}
