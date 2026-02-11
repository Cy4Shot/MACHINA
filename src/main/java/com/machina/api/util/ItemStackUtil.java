package com.machina.api.util;

import com.machina.api.item.RocketPartItem;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TagInit.ItemTagInit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ItemStackUtil {

    public static Predicate<ItemStack> is(Item item) {
        return stack -> stack.is(item);
    }

    public static boolean hasEnergy(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }

    public static boolean hasFluid(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
    }

    public static boolean hasFluid(ItemStack stack, Fluid fluid) {
        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                .map(h ->
                        IntStream.range(0, h.getTanks())
                                .anyMatch(i -> h.getFluidInTank(i).getFluid().isSame(fluid)))
                .orElse(false);
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

    public static boolean isRocketPart(ItemStack stack, RocketPartType type) {
        if (stack.getItem() instanceof RocketPartItem rpi) {
            return rpi.getRocketPart().getType().equals(type);
        }
        return false;
    }
}
