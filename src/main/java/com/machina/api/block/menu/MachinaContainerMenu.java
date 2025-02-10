package com.machina.api.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.block.menu.slot.InvSlot;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public abstract class MachinaContainerMenu<T extends MachinaBlockEntity> extends AbstractContainerMenu {

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

	protected abstract MachineBlock getBlock();

	@Override
	public boolean stillValid(@NotNull Player player) {
		return this.be.stillValid(player);
	}

	public Component getName() {
		return this.getBlock().getName();
	}

	public BlockState getDefaultState() {
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

	public void invSlots(Inventory inv, int offset) {
		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new InvSlot(inv, j1 + l * 9 + 9, 28 + j1 * 20, 83 + l * 20 + offset));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new InvSlot(inv, i1, 28 + i1 * 20, 148 + offset));
		}
	}
	
	public Container getContainer() {
		return this.be;
	}

}
