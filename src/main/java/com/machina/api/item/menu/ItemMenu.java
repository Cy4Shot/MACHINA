package com.machina.api.item.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.block.menu.MachinaAnyMenu;
import com.machina.api.network.c2s.C2SItemMenuSync;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class ItemMenu extends MachinaAnyMenu {

	public ItemStack stack;
	private final Inventory inv;
	private final InteractionHand hand;

	protected ItemMenu(MenuType<?> t, int w, Inventory inv, InteractionHand hand) {
		super(t, w);

		this.stack = inv.player.getItemInHand(hand);
		this.inv = inv;
		this.hand = hand;
	}

	@Override
	public boolean stillValid(Player p) {
		ItemStack main = p.getMainHandItem();
		ItemStack off = p.getOffhandItem();
		return !main.isEmpty() && main.getItem() == stack.getItem()
				|| !off.isEmpty() && off.getItem() == stack.getItem();
	}

	protected static InteractionHand hand(FriendlyByteBuf buf) {
		return buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}

	public abstract ItemStack getItem();

	public void containerChanged() {
		int slot = hand == InteractionHand.MAIN_HAND ? inv.selected : Inventory.SLOT_OFFHAND;
		PacketDistributor.sendToServer(new C2SItemMenuSync(slot, stack));
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

	protected abstract int getContainerSize();

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
		return getItem().getHoverName();
	}
}
