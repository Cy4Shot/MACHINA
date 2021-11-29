package com.machina.modules.test;

import com.machina.api.container.BaseContainer;
import com.machina.api.util.InventoryUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;

public class TestContainer extends BaseContainer {
	
	private final IWorldPosCallable canInteractWithCallable;

	public TestContainer(final int windowId, final PlayerInventory playerInv, final TestTileEntity te) {
		super(TestModule.TEST_CONTAINER_TYPE, windowId);
		this.canInteractWithCallable = IWorldPosCallable.create(te.getLevel(), te.getBlockPos());
		InventoryUtils.createPlayerSlots(playerInv, 8, 84).forEach(this::addSlot);
	}
	
	@Override
	public boolean stillValid(PlayerEntity p_75145_1_) {
		return stillValid(canInteractWithCallable, p_75145_1_, TestModule.TEST_BLOCK);
	}

	public TestContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data, TestTileEntity.class));
	}

}
