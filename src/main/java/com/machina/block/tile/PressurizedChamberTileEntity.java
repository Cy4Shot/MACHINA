package com.machina.block.tile;

import com.machina.block.container.PressurizedChamberContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.BaseEnergyTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class PressurizedChamberTileEntity extends BaseEnergyTileEntity implements IMachinaContainerProvider {

	public PressurizedChamberTileEntity() {
		super(TileEntityInit.PRESSURIZED_CHAMBER.get());
		
		this.sides = new int[] {1, 1, 1, 1, 1, 1};
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new PressurizedChamberContainer(windowId, this);
	}

	@Override
	public MachinaEnergyStorage createStorage() {
		return new MachinaEnergyStorage(this, 10000, 1000, 0);
	}
}
