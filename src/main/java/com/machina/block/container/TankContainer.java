package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.container.slot.AcceptSlot;
import com.machina.block.tile.machine.TankTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;
import com.machina.util.helper.ItemStackHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TankContainer extends BaseContainer<TankTileEntity> {

	public final TankTileEntity te;

	public TankContainer(final int id, final PlayerInventory inv, final TankTileEntity te) {
		super(ContainerInit.TANK.get(), id, te);
		this.te = te;

		recreateSlots(inv);
	}

	public TankContainer(final int id, final PlayerInventory inv, final PacketBuffer data) {
		this(id, inv, getTileEntity(inv, data));
	}

	public void recreateSlots(final PlayerInventory inv) {
		this.slots.clear();
		this.addSlot(new AcceptSlot((IInventory) te, 0, -4, 72,
				stack -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()));
		this.addSlot(new AcceptSlot((IInventory) te, 1, 161, 72, stack -> ItemStackHelper.isBucket(stack)));
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(inv, col, 8 + col * 18, 142));
		}
	}

	@Override
	protected int getContainerSize() {
		return 2;
	}

	@Override
	protected Block getBlock() {
		return BlockInit.TANK.get();
	}
}
