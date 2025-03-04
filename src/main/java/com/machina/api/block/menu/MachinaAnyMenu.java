package com.machina.api.block.menu;

import org.jetbrains.annotations.Nullable;

import com.machina.api.block.entity.ContainerBlockEntity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachinaAnyMenu extends AbstractContainerMenu {

	protected MachinaAnyMenu(MenuType<?> menu, int id) {
		super(menu, id);
	}
	
	public abstract Component getName();

	@Nullable
	public abstract ContainerBlockEntity getBlockEntity();

	@Nullable
	public abstract BlockState getDefaultState();
}
