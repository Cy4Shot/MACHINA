package com.machina.block.tile;

import com.machina.block.container.BatteryContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.CustomTE;
import com.machina.capability.CustomEnergyStorage;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class BatteryTileEntity extends CustomTE implements IMachinaContainerProvider {

	public BatteryTileEntity() {
		super(TileEntityInit.BATTERY.get());
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new BatteryContainer(windowId, this);
	}
	
	CustomEnergyStorage energy;

	@Override
	public void createStorages() {
		energy = add(new CustomEnergyStorage(10000, 100));
	}
	
	public int getEnergy() {
		return this.energy.getEnergyStored();
	}

	public int getMaxEnergy() {
		return this.energy.getMaxEnergyStored();
	}

	public float propFull() {
		return (float) this.getEnergy() / (float) this.getMaxEnergy();
	}
}
