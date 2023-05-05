package com.machina.block.container.base;

import com.machina.util.StringUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;

public interface IMachinaContainerProvider extends INamedContainerProvider {
	
	@Override
	Container createMenu(int id, PlayerInventory inventory, PlayerEntity player);
	
	@Override
	default ITextComponent getDisplayName() {
		return StringUtils.EMPTY;
	}
}
