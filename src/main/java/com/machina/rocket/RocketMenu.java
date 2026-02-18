package com.machina.rocket;

import org.jetbrains.annotations.Nullable;

import com.machina.api.block.entity.ContainerBlockEntity;
import com.machina.api.block.menu.MachinaAnyMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.api.rocket.RocketProps;
import com.machina.api.util.ItemStackUtil;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class RocketMenu extends MachinaAnyMenu {

	public RocketEntity entity;

	public RocketMenu(int id, Inventory inv) {
		this(id, inv, new ItemStackHandler(2), null);
	}

	public RocketMenu(int w, Inventory playerInv, IItemHandler container, RocketEntity entity) {
		super(MenuTypeInit.ROCKET.get(), w);

		this.entity = entity;

		if (entity != null) {
			RocketProps props = entity.getProps();

			this.addSlot(
					new AcceptSlot(container, 0, 0, 0, s -> ItemStackUtil.hasFluid(s, props.fuelStack().getFluid())));
			this.addSlot(new AcceptSlot(container, 1, 0, 0,
					s -> ItemStackUtil.hasFluid(s, props.coolantStack().getFluid())));
		}

		this.invSlots(playerInv, 0);
		rebuildSlotPositions(0);
	}

	public void rebuildSlotPositions(int tab) {
		if (entity == null)
			return;

		Slot fuelSlot = this.getSlot(0);
		Slot clntSlot = this.getSlot(1);

		if (tab == 1) {
			fuelSlot.x = 177;
			fuelSlot.y = 36;
			clntSlot.x = 199;
			clntSlot.y = 36;
		} else {
			fuelSlot.x = -1000;
			fuelSlot.y = -1000;
			clntSlot.x = -1000;
			clntSlot.y = -1000;
		}
	}

	@Override
	public boolean stillValid(Player p) {
		return p.distanceTo(this.entity) < 10f;
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

	@Override
	public @Nullable BlockState getDefaultState() {
		return null;
	}

	@Override
	public ContainerBlockEntity getBlockEntity() {
		return null;
	}

	@Override
	public Component getName() {
		return this.entity.getDisplayName();
	}
}