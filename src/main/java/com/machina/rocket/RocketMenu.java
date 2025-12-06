package com.machina.rocket;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.block.menu.MachinaAnyMenu;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class RocketMenu extends MachinaAnyMenu {

    public RocketEntity entity;
    private final Container container;
    
    public RocketMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, null, null);
    }

    public RocketMenu(int w, Inventory playerInv, Container container, RocketEntity entity) {
        super(MenuTypeInit.ROCKET.get(), w);

        this.entity = entity;
        this.container = container;
        
        container.startOpen(playerInv.player);
        
        invSlots(playerInv, 0);
    }

    @Override
    public boolean stillValid(Player p) {
        return p.distanceTo(this.entity) < 10f;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(i);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (i < getContainerSize()) {
                if (!moveItemStackTo(itemstack1, getContainerSize(), getContainerSize() + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                for (int x = 0; x < getContainerSize(); x++) {
                    if (slots.get(x).mayPlace(itemstack) && !moveItemStackTo(itemstack1, x, x + 1, true)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public @Nullable BlockState getDefaultState() {
        return null;
    }

    @Override
    public @Nullable MachinaBlockEntity getBlockEntity() {
        return null;
    }

    @Override
    public Component getName() {
        return this.entity.getDisplayName();
    }

    private int getContainerSize() {
        return this.container.getContainerSize();
    }
    
    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}