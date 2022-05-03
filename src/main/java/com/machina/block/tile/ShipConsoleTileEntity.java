package com.machina.block.tile;

import com.machina.block.container.ShipConsoleContainer;
import com.machina.registration.init.TileEntityTypesInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ShipConsoleTileEntity extends TileEntity implements INamedContainerProvider {

	public static int slots = 1;

	public ShipConsoleTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public ShipConsoleTileEntity() {
		this(TileEntityTypesInit.SHIP_CONSOLE.get());
	}

	@Override
	public Container createMenu(int id, PlayerInventory player, PlayerEntity p) {
		return new ShipConsoleContainer(id, player, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("machina.container.ship_console");
	}

}
