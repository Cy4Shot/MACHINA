package com.machina.api.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PlayerHelper {
    public static boolean hasAll(Player player, ItemStack stack) {
        if (stack.isEmpty())
            return true;
        if (stack.getCount() <= 0)
            return true;
        int desired = stack.getCount();
        for (ItemStack item : player.getInventory().items) {
            if (item.isEmpty())
                continue;
            if (item.getItem() == stack.getItem()) {
                desired -= item.getCount();
                if (desired <= 0)
                    return true;
            }
        }
        return false;
    }

    public static boolean hasAll(Player player, List<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            if (!hasAll(player, stack))
                return false;
        }
        return true;
    }

    public static void consumeAll(Player player, ItemStack stack) {
        if (stack.isEmpty())
            return;
        if (stack.getCount() <= 0)
            return;
        int desired = stack.getCount();
        for (ItemStack item : player.getInventory().items) {
            if (item.isEmpty())
                continue;
            if (item.getItem() == stack.getItem()) {
                int count = item.getCount();
                if (desired >= count) {
                    item.shrink(count);
                    desired -= count;
                } else {
                    item.shrink(desired);
                    break;
                }
            }
        }
    }

    public static void consumeAll(Player player, List<ItemStack> stacks) {
        stacks.forEach(s -> consumeAll(player, s));
    }
}
