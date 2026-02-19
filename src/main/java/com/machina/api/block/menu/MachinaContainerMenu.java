package com.machina.api.block.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.machina.api.block.entity.ContainerBlockEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class MachinaContainerMenu<T extends ContainerBlockEntity> extends MachinaAnyMenu {

	protected final ContainerLevelAccess access;

	public MachinaContainerMenu(MenuType<?> type, int id, ContainerLevelAccess access) {
		super(type, id);
		this.access = access;
	}

	protected abstract Block getBlock();

	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	protected static final ContainerLevelAccess client(FriendlyByteBuf buf) {
		return ContainerLevelAccess.create(Minecraft.getInstance().level, buf.readBlockPos());
	}

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
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack empty = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot == null || !slot.hasItem() || this.slots.size() <= 36) {
			return empty;
		}

		ItemStack stackInSlot = slot.getItem();
		ItemStack stackCopy = stackInSlot.copy();

		int machineSlotCount = this.slots.size() - 36;
		int playerInventoryStart = machineSlotCount;
		int playerInventoryEnd = this.slots.size();
		if (index < machineSlotCount) {
			if (!this.moveItemStackTo(stackInSlot, playerInventoryStart, playerInventoryEnd, true)) {
				return ItemStack.EMPTY;
			}
		} else {
			boolean moved = false;

			for (int i = 0; i < machineSlotCount; i++) {
				Slot machineSlot = this.slots.get(i);

				if (machineSlot.mayPlace(stackInSlot)) {
					if (this.moveItemStackTo(stackInSlot, i, i + 1, false)) {
						moved = true;
						break;
					}
				}
			}

			if (!moved) {
				return ItemStack.EMPTY;
			}
		}

		if (stackInSlot.isEmpty()) {
			slot.set(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}

		slot.onTake(player, stackInSlot);

		return stackCopy;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getBlockEntity() {
		return this.access.evaluate((level, pos) -> level.getBlockEntity(pos)).map(e -> {
			if (e instanceof ContainerBlockEntity cbe) {
				return (T) cbe;
			}
			return null;
		}).orElse(null);
	}

	public BlockPos getBlockPos() {
		return this.access.evaluate((level, pos) -> pos).get();
	}
}
