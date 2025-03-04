package com.machina.api.util;

import java.util.function.Predicate;

import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TagInit.ItemTagInit;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class ItemStackUtil {

	public static Predicate<ItemStack> is(Item item) {
		return stack -> stack.is(item);
	}

	public static boolean hasEnergy(ItemStack stack) {
		return stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
	}

	public static boolean isCapacitor(ItemStack stack) {
		return stack.is(ItemTagInit.CAPACITOR);
	}

	public static boolean isBurnable(ItemStack stack) {
		return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
	}

	public static boolean isBlueprint(ItemStack stack) {
		return stack.getItem().equals(ItemInit.BLUEPRINT.get());
	}
}
