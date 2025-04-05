package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.block.entity.machine.MixerBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class MixerMenu extends MachinaContainerMenu<MixerBlockEntity> {

	public MixerMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, clientLevel(), buf.readBlockPos(), inv);
	}

	public MixerMenu(int id, Level level, BlockPos pos, Inventory inv) {
		super(MenuTypeInit.MIXER.get(), level, pos, id);

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.MIXER.get();
	}
}
