package com.machina.api.block.tile;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import org.jetbrains.annotations.NotNull;

public interface IMachinaMenuProvider extends MenuProvider {

	@Override
	default @NotNull Component getDisplayName() {
		return Component.empty();
	}
}