package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.machine.MixerTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class MixerContainer extends BaseContainer<MixerTileEntity> {

	public MixerContainer(int id, MixerTileEntity te) {
		super(ContainerInit.MIXER.get(), id, te);
	}

	public MixerContainer(final int id, final PlayerInventory inv, final PacketBuffer data) {
		this(id, getTileEntity(inv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.MIXER.get();
	}
}