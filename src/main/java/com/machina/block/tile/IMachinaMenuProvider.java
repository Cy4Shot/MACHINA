package com.machina.block.tile;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;

public interface IMachinaMenuProvider extends MenuProvider {

	@Override
	default Component getDisplayName() {
		return Component.empty();
	}
}