package com.machina.api.block.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.machina.api.block.entity.ContainerBlockEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class MachinaContainerMenu<T extends WorldlyContainer> extends MachinaAnyMenu {

	public final T be;

	@SuppressWarnings("unchecked")
	public MachinaContainerMenu(MenuType<?> type, Level level, BlockPos pos, int id) {
		super(type, id);
		this.be = (T) level.getBlockEntity(pos);
	}

	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	protected static Level clientLevel() {
		return Minecraft.getInstance().level;
	}

	protected abstract Block getBlock();

	@Override
	public boolean stillValid(@NotNull Player player) {
		return this.be.stillValid(player);
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
	public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack stack1 = slot.getItem();
			stack = stack1.copy();
			if (index < be.getContainerSize()
					&& !this.moveItemStackTo(stack1, be.getContainerSize(), this.slots.size(), true)) {
				return ItemStack.EMPTY;
			}
			if (!this.moveItemStackTo(stack1, 0, be.getContainerSize(), false)) {
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

	public Container getContainer() {
		return this.be;
	}

	@Override
	public @Nullable ContainerBlockEntity getBlockEntity() {
		if (this.be instanceof ContainerBlockEntity containerBlockEntity) {
			return containerBlockEntity;
		}
		return null;
	}

}
