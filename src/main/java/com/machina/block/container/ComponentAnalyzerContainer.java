package com.machina.block.container;

import com.machina.block.container.slot.AcceptSlot;
import com.machina.block.container.slot.ResultSlot;
import com.machina.block.tile.ComponentAnalyzerTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerTypesInit;
import com.machina.registration.init.ItemInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ComponentAnalyzerContainer extends BaseContainer<ComponentAnalyzerTileEntity> {

	public ComponentAnalyzerTileEntity te;

	public ComponentAnalyzerContainer(final int windowId, final PlayerInventory playerInv,
			final ComponentAnalyzerTileEntity te) {
		super(ContainerTypesInit.COMPONENT_ANALYZER_CONTAINER_TYPE.get(), windowId, te);
		this.te = te;

		recreateSlots(playerInv);

		createData(() -> te.getData());
	}

	public void recreateSlots(final PlayerInventory playerInv) {
		this.slots.clear();
		this.addSlot(new AcceptSlot((IInventory) te, 0, -2, 74, ItemInit.SHIP_COMPONENT.get().getDefaultInstance()));
		this.addSlot(new ResultSlot((IInventory) te, 1, 159, 74));
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
		}
	}

	public ComponentAnalyzerContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack stack1 = slot.getItem();
			stack = stack1.copy();
			if (index < this.te.getContainerSize()
					&& !this.moveItemStackTo(stack1, this.te.getContainerSize(), this.slots.size(), true)) {
				return ItemStack.EMPTY;
			}
			if (!this.moveItemStackTo(stack1, 0, this.te.getContainerSize(), false)) {
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
	protected Block getBlock() {
		return BlockInit.COMPONENT_ANALYZER.get();
	}
}
