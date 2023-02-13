package com.machina.block.tile;

import com.machina.block.container.FabricatorContainer;
import com.machina.block.tile.base.BaseLockableTileEntity;
import com.machina.blueprint.Blueprint;
import com.machina.item.BlueprintItem;
import com.machina.registration.init.BlueprintInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;

public class FabricatorTileEntity extends BaseLockableTileEntity {

	public FabricatorTileEntity(TileEntityType<?> type) {
		super(type, 17);
	}

	public FabricatorTileEntity() {
		this(TileEntityInit.FABRICATOR.get());
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new FabricatorContainer(id, player, this);
	}

	public void etch(String id) {
		getItem(0).shrink(1);
		ItemStack stack = ItemInit.BLUEPRINT.get().getDefaultInstance();
		BlueprintItem.set(stack, BlueprintInit.BLUEPRINTS.getOrDefault(id, Blueprint.EMPTY));
		setItem(1, stack);
	}
}