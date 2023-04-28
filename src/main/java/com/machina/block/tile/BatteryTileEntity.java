package com.machina.block.tile;

import com.machina.block.container.BatteryContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.MachinaTileEntity;
import com.machina.capability.energy.IEnergyTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.ITickableTileEntity;

public class BatteryTileEntity extends MachinaTileEntity implements IMachinaContainerProvider, ITickableTileEntity, IEnergyTileEntity {

	public BatteryTileEntity() {
		super(TileEntityInit.BATTERY.get());
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new BatteryContainer(windowId, this);
	}

	MachinaEnergyStorage energy;

	@Override
	public void createStorages() {
		energy = add(new MachinaEnergyStorage(this, 10000, 1000));
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

	@Override
	public void tick() {
		if (level.isClientSide())
			return;
	}

	@Override
	public boolean isGeneratorMode() {
		return false;
	}
}
