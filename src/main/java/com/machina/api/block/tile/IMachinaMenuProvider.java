package com.machina.api.block.tile;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to allow Machina BlockEntities to have menus, and hence GUIs.
 * @author Cy4Shot
 * @since Machina v0.1.0
 */
public interface IMachinaMenuProvider extends MenuProvider {

	@Override
	default @NotNull Component getDisplayName() {
		return Component.empty();
	}
}