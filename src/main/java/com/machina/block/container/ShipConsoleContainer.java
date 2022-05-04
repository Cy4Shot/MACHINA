package com.machina.block.container;

import java.util.Objects;

import com.machina.block.container.slot.HintedSlot;
import com.machina.block.tile.ShipConsoleTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerTypesInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class ShipConsoleContainer extends Container {

	public final ShipConsoleTileEntity te;
	private final IWorldPosCallable canInteractWithCallable;

	public ShipConsoleContainer(final int windowId, final PlayerInventory playerInv, final ShipConsoleTileEntity te) {
		super(ContainerTypesInit.SHIP_CONSOLE_CONTAINER_TYPE.get(), windowId);
		this.te = te;
		this.canInteractWithCallable = IWorldPosCallable.create(te.getLevel(), te.getBlockPos());
		
		this.addSlot(new HintedSlot((IInventory) te, 0, -6, 60, te.requried.get(0)));
		this.addSlot(new HintedSlot((IInventory) te, 1, 19, 60, te.requried.get(1)));
		this.addSlot(new HintedSlot((IInventory) te, 2, -6, 84, te.requried.get(2)));
		this.addSlot(new HintedSlot((IInventory) te, 3, 19, 84, te.requried.get(3)));

		// Main Player Inventory
//		for (int row = 0; row < 3; row++) {
//			for (int col = 0; col < 9; col++) {
//				this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 166 - (4 - row) * 18 - 10));
//			}
//		}
//
//		// Player Hotbar
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
		}
	}

	public ShipConsoleContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	private static ShipConsoleTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "Player Inventory cannot be null.");
		Objects.requireNonNull(data, "Packet Buffer cannot be null.");
		final TileEntity te = playerInv.player.level.getBlockEntity(data.readBlockPos());
		if (te instanceof ShipConsoleTileEntity) {
			return (ShipConsoleTileEntity) te;
		}
		throw new IllegalStateException("Tile Entity Is Not Correct");
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		return stillValid(canInteractWithCallable, player, BlockInit.SHIP_CONSOLE.get());

	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack stack1 = slot.getItem();
			stack = stack1.copy();
			if (index < ShipConsoleTileEntity.slots
					&& !this.moveItemStackTo(stack1, ShipConsoleTileEntity.slots, this.slots.size(), true)) {
				return ItemStack.EMPTY;
			}
			if (!this.moveItemStackTo(stack1, 0, ShipConsoleTileEntity.slots, false)) {
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
}
