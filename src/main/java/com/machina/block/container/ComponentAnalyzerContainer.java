package com.machina.block.container;

import java.util.Objects;

import com.machina.block.container.slot.AcceptSlot;
import com.machina.block.container.slot.ResultSlot;
import com.machina.block.tile.ComponentAnalyzerTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerTypesInit;
import com.machina.registration.init.ItemInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;

public class ComponentAnalyzerContainer extends Container {

	public final ComponentAnalyzerTileEntity te;
	private final IWorldPosCallable canInteractWithCallable;
	public final IIntArray data;

	public ComponentAnalyzerContainer(final int windowId, final PlayerInventory playerInv,
			final ComponentAnalyzerTileEntity te) {
		super(ContainerTypesInit.COMPONENT_ANALYZER_CONTAINER_TYPE.get(), windowId);
		this.te = te;
		this.canInteractWithCallable = IWorldPosCallable.create(te.getLevel(), te.getBlockPos());

		recreateSlots(playerInv);

		this.data = te.getData();
		checkContainerDataCount(data, 1);
		this.addDataSlots(data);
	}

	public void recreateSlots(final PlayerInventory playerInv) {
		this.slots.clear();
		this.addSlot(new AcceptSlot((IInventory) te, 0, -2, 74, ItemInit.SHIP_COMPONENT.get()));
		this.addSlot(new ResultSlot((IInventory) te, 1, 159, 74));
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
		}
	}

	public ComponentAnalyzerContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	private static ComponentAnalyzerTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "Player Inventory cannot be null.");
		Objects.requireNonNull(data, "Packet Buffer cannot be null.");
		final TileEntity te = playerInv.player.level.getBlockEntity(data.readBlockPos());
		if (te instanceof ComponentAnalyzerTileEntity) {
			return (ComponentAnalyzerTileEntity) te;
		}
		throw new IllegalStateException("Tile Entity Is Not Correct");
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		return stillValid(canInteractWithCallable, player, BlockInit.COMPONENT_ANALYZER.get());
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack stack1 = slot.getItem();
			stack = stack1.copy();
			if (index < ComponentAnalyzerTileEntity.slots
					&& !this.moveItemStackTo(stack1, ComponentAnalyzerTileEntity.slots, this.slots.size(), true)) {
				return ItemStack.EMPTY;
			}
			if (!this.moveItemStackTo(stack1, 0, ComponentAnalyzerTileEntity.slots, false)) {
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
