package com.machina.block.container.base;

import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;

public abstract class BaseContainer<T extends TileEntity> extends Container {
	
	protected final IWorldPosCallable canInteractWithCallable;
	public IIntArray data;

	protected BaseContainer(ContainerType<?> pMenuType, int pContainerId, T te) {
		super(pMenuType, pContainerId);
		this.canInteractWithCallable = IWorldPosCallable.create(te.getLevel(), te.getBlockPos());
	}

	@SuppressWarnings("unchecked")
	protected static <T extends TileEntity> T getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "Player Inventory cannot be null.");
		Objects.requireNonNull(data, "Packet Buffer cannot be null.");
		final TileEntity te = playerInv.player.level.getBlockEntity(data.readBlockPos());
		return (T) te;
	}
	
	protected void createData(Supplier<IIntArray> s) {
		this.data = s.get();
		checkContainerDataCount(data, data.getCount());
		this.addDataSlots(data);
	}
	
	@Override
	public boolean stillValid(PlayerEntity player) {
		return stillValid(canInteractWithCallable, player, getBlock());
	}
	
	protected abstract Block getBlock();

}
