package com.machina.rocket;

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
        this(id, inv, inv, null);
    }

    public RocketMenu(int w, Inventory playerInv, Container container, RocketEntity entity) {
        super(MenuTypeInit.ROCKET.get(), w);

        this.entity = entity;
        this.container = container;

        container.startOpen(playerInv.player);
    }

    @Override
    public boolean stillValid(Player p) {
        return p.distanceTo(this.entity) < 10f;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            int size = this.container.getContainerSize();
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();
            if (index < size
                    && !this.moveItemStackTo(stack1, size, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
            if (!this.moveItemStackTo(stack1, 0, size, false)) {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack;
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

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}