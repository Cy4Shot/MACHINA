package com.machina.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import net.minecraftforge.common.crafting.StackList;

public class InventoryUtils {
	private InventoryUtils() {
		throw new IllegalAccessError("Utility class");
	}

	/**
	 * Creates slots for the player's inventory for a {@link Container}. Convenience
	 * method to improve readability of Container code.
	 *
	 * @param playerInventory Player's inventory
	 * @param startX          X-position of top-left slot
	 * @param startY          Y-position of top-left slot
	 * @return A collection of slots to be added
	 * @since 4.1.1
	 */
	public static Collection<Slot> createPlayerSlots(PlayerInventory playerInventory, int startX, int startY) {
		Collection<Slot> list = new ArrayList<>();
		// Backpack
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				list.add(new Slot(playerInventory, x + y * 9 + 9, startX + x * 18, startY + y * 18));
			}
		}
		// Hotbar
		for (int x = 0; x < 9; ++x) {
			list.add(new Slot(playerInventory, x, 8 + x * 18, startY + 58));
		}
		return list;
	}
	
	/**
     * Consumes (removes) items from the inventory. This is useful for machines, which may have
     * multiple input slots and recipes that consume multiple of one item.
     *
     * @param inventory The inventory
     * @param ingredient The items to match ({@link net.minecraft.item.crafting.Ingredient}, etc.)
     * @param amount The total number of items to remove
     */
    public static void consumeItems(IInventory inventory, Predicate<ItemStack> ingredient, int amount) {
        int amountLeft = amount;
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                int toRemove = Math.min(amountLeft, stack.getCount());

                stack.shrink(toRemove);
                if (stack.isEmpty()) {
                    inventory.setItem(i, ItemStack.EMPTY);
                }

                amountLeft -= toRemove;
                if (amountLeft == 0) {
                    return;
                }
            }
        }
    }
    
    /**
     * Gets the total number of matching items in all slots in the inventory.
     *
     * @param inventory  The inventory
     * @param ingredient The items to match ({@link net.minecraft.item.crafting.Ingredient}, etc.)
     * @return The number of items in all matching item stacks
     */
    public static int getTotalCount(IInventory inventory, Predicate<ItemStack> ingredient) {
        int total = 0;
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                total += stack.getCount();
            }
        }
        return total;
    }

	public static boolean canItemsStack(ItemStack a, ItemStack b) {
		if (a.getItem() != Items.AIR && b.getItem() != Items.AIR && a.getItem() != b.getItem())
			return false;
		if (a.getTag() == null && b.getTag() != null)
			return false;
		return (a.getTag() == null || a.getTag().equals(b.getTag())) && a.areCapsCompatible(b);
	}

	/**
	 * Obtain the first matching stack. {@link StackList} has a similar method, but
	 * this avoids creating the entire list when it isn't needed.
	 *
	 * @param inv       The inventory to search
	 * @param predicate Condition to match
	 * @return The first matching stack, or {@link ItemStack#EMPTY} if there is none
	 * @since 3.1.0 (was in StackHelper from 3.0.6)
	 */
	public static ItemStack firstMatch(IInventory inv, Predicate<ItemStack> predicate) {
		for (int i = 0; i < inv.getContainerSize(); ++i) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty() && predicate.test(stack)) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
}
