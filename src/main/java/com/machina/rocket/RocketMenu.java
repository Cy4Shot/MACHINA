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

	private static final int FUEL_SLOT = 0;
	private static final int COOL_SLOT = 1;
	private static final int STORAGE_START = 2;
	private static final int STORAGE_COLUMNS = 9;
	private static final int STORAGE_SLOT_SPACING = 20;
	private static final int STORAGE_SLOT_X = 31;
	private static final int STORAGE_SLOT_Y = -18;

	public RocketEntity entity;

	public RocketMenu(int id, Inventory inv) {
		this(id, inv, new ItemStackHandler(2), null);
	}

	public RocketMenu(int w, Inventory playerInv, IItemHandler container, RocketEntity entity) {
		super(MenuTypeInit.ROCKET.get(), w);

		this.entity = entity;

		if (entity != null) {
			RocketProps props = entity.getProps();

			this.addSlot(new AcceptSlot(container, FUEL_SLOT, 0, 0,
					s -> ItemStackUtil.hasFluid(s, props.fuelStack().getFluid())));
			this.addSlot(new AcceptSlot(container, COOL_SLOT, 0, 0,
					s -> ItemStackUtil.hasFluid(s, props.coolantStack().getFluid())));

			for (int i = 0; i < props.slots(); i++) {
				this.addSlot(new AcceptSlot(container, STORAGE_START + i, 0, 0, s -> true));
			}
		}

		this.invSlots(playerInv, 0);
		rebuildSlotPositions(0);
	}

	public void rebuildSlotPositions(int tab) {
		if (entity == null)
			return;

		RocketProps props = entity.getProps();
		Slot fuelSlot = this.getSlot(FUEL_SLOT);
		Slot clntSlot = this.getSlot(COOL_SLOT);

		if (tab == 1) {
			fuelSlot.x = 177;
			fuelSlot.y = 36;
			clntSlot.x = 199;
			clntSlot.y = 36;
			for (int i = 0; i < props.slots(); i++) {
				Slot storageSlot = this.getSlot(STORAGE_START + i);
				storageSlot.x = -1000;
				storageSlot.y = -1000;
			}
		} else if (tab == 2) {
			fuelSlot.x = -1000;
			fuelSlot.y = -1000;
			clntSlot.x = -1000;
			clntSlot.y = -1000;

			for (int i = 0; i < props.slots(); i++) {
				Slot storageSlot = this.getSlot(STORAGE_START + i);
				storageSlot.x = STORAGE_SLOT_X + (i % STORAGE_COLUMNS) * STORAGE_SLOT_SPACING;
				storageSlot.y = STORAGE_SLOT_Y + (i / STORAGE_COLUMNS) * STORAGE_SLOT_SPACING;
			}
		} else {
			fuelSlot.x = -1000;
			fuelSlot.y = -1000;
			clntSlot.x = -1000;
			clntSlot.y = -1000;

			for (int i = 0; i < props.slots(); i++) {
				Slot storageSlot = this.getSlot(STORAGE_START + i);
				storageSlot.x = -1000;
				storageSlot.y = -1000;
			}
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
