package com.machina.api.block.menu;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nullable;

@FunctionalInterface
public interface IDirectionalMenuProvider {
    @Nullable
    AbstractContainerMenu createMenu(int id, Inventory inv, Player player, Direction d);
}
