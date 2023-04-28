package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.container.slot.CompletableSlot;
import com.machina.block.tile.machine.FuelStorageUnitTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;
import com.machina.registration.init.ItemInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class FuelStorageUnitContainer extends BaseContainer<FuelStorageUnitTileEntity> {

	public final FuelStorageUnitTileEntity te;

	public FuelStorageUnitContainer(int id, final PlayerInventory playerInv, FuelStorageUnitTileEntity te) {
		super(ContainerInit.FUEL_STORAGE_UNIT.get(), id, te);
		this.te = te;

		recreateSlots(playerInv);
	}

	public void recreateSlots(final PlayerInventory playerInv) {
		this.slots.clear();
		this.addSlot(new CompletableSlot((IInventory) te, 0, -5, 92,
				() -> new ItemStack(ItemInit.ALUMINUM_INGOT.get(), 64), false));
		this.addSlot(new CompletableSlot((IInventory) te, 1, 162, 92,
				() -> new ItemStack(ItemInit.AMMONIUM_NITRATE.get(), 64), false));
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
		}
	}

	public FuelStorageUnitContainer(final int id, final PlayerInventory inv, final PacketBuffer data) {
		this(id, inv, getTileEntity(inv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.FUEL_STORAGE_UNIT.get();
	}

	@Override
	protected int getContainerSize() {
		return 2;
	}

	public CompletableSlot getCompletableSlot(int pSlotId) {
		return (CompletableSlot) super.getSlot(pSlotId);
	}
}