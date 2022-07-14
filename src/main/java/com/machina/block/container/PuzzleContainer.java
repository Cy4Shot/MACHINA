package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.PuzzleTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class PuzzleContainer extends BaseContainer<PuzzleTileEntity> {

	public final PuzzleTileEntity te;

	public PuzzleContainer(final int windowId, final PuzzleTileEntity te) {
		super(ContainerInit.PUZZLE.get(), windowId, te);
		this.te = te;
	}

	public PuzzleContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, getTileEntity(playerInv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.PUZZLE_BLOCK.get();
	}
}