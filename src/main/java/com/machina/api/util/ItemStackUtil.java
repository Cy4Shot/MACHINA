package com.machina.api.util;

import java.util.function.Predicate;
import java.util.stream.IntStream;

import com.machina.api.item.RocketPartItem;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.registration.init.TagInit.ItemTagInit;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class ItemStackUtil {

	public static Predicate<ItemStack> is(Item item) {
		return stack -> stack.is(item);
	}

	public static boolean hasEnergy(ItemStack stack) {
		return stack.getCapability(Capabilities.EnergyStorage.ITEM) != null;
	}

	public static boolean hasFluid(ItemStack stack) {
		return stack.getCapability(Capabilities.FluidHandler.ITEM) != null;
	}

	public static boolean hasFluid(ItemStack stack, Fluid fluid) {
		IFluidHandler handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
		if (handler != null) {
			return IntStream.range(0, handler.getTanks())
					.anyMatch(i -> handler.getFluidInTank(i).getFluid().isSame(fluid));
		}
		return false;
	}

	public static boolean isCapacitor(ItemStack stack) {
		return stack.is(ItemTagInit.CAPACITOR);
	}

	public static boolean isBurnable(ItemStack stack) {
		return stack.getBurnTime(RecipeType.SMELTING) > 0;
	}

	public static boolean isRocketPart(ItemStack stack, RocketPartType type) {
		if (stack.getItem() instanceof RocketPartItem rpi) {
			return rpi.getRocketPart().getType().equals(type);
		}
		return false;
	}
}
