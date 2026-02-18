package com.machina.api.block.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.machina.api.block.entity.ContainerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachinaContainerMenu<T extends WorldlyContainer> extends MachinaAnyMenu {

	protected final ContainerLevelAccess access;

	public MachinaContainerMenu(MenuType<?> type, int id, ContainerLevelAccess access) {
		super(type, id);
		this.access = access;
	}

	protected abstract Block getBlock();

	@Override
	public boolean stillValid(@NotNull Player player) {
		return stillValid(this.access, player, getBlock());
	}

	@Override
	public Component getName() {
		return this.getBlock().getName();
	}

	@Override
	public @Nullable BlockState getDefaultState() {
		return this.getBlock().defaultBlockState();
	}

	@Override
	public ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
		ItemStack quickMovedStack = ItemStack.EMPTY;
		Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);

		if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
			ItemStack rawStack = quickMovedSlot.getItem();
			quickMovedStack = rawStack.copy();

			if (quickMovedSlotIndex == 0) {
				if (!this.moveItemStackTo(rawStack, 5, 41, true)) {
					return ItemStack.EMPTY;
				}
			} else if (quickMovedSlotIndex >= 5 && quickMovedSlotIndex < 41) {
				if (!this.moveItemStackTo(rawStack, 1, 5, false)) {
					if (quickMovedSlotIndex < 32) {
						if (!this.moveItemStackTo(rawStack, 32, 41, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.moveItemStackTo(rawStack, 5, 32, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.moveItemStackTo(rawStack, 5, 41, false)) {
				return ItemStack.EMPTY;
			}

			if (rawStack.isEmpty()) {
				quickMovedSlot.set(ItemStack.EMPTY);
			} else {
				quickMovedSlot.setChanged();
			}
		}

		return quickMovedStack;
	}

	@Override
	public ContainerBlockEntity getBlockEntity() {
		return this.access.evaluate((level, pos) -> level.getBlockEntity(pos)).map(e -> {
			if (e instanceof ContainerBlockEntity cbe) {
				return cbe;
			}
			return null;
		}).orElse(null);
	}

	public BlockPos getBlockPos() {
		return this.access.evaluate((level, pos) -> pos).get();
	}
}
