package com.machina.block.tile.machine;

import com.machina.block.BatteryBlock;
import com.machina.block.container.BatteryContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.MachinaTileEntity;
import com.machina.capability.energy.IEnergyTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.helper.EnergyHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.ITickableTileEntity;

public class BatteryTileEntity extends MachinaTileEntity
		implements IMachinaContainerProvider, ITickableTileEntity, IEnergyTileEntity {

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
		this.energy = add(new MachinaEnergyStorage(this, 10000, 1000));
	}

	@Override
	public void tick() {
		if (level.isClientSide())
			return;

		if (isGeneratorMode()) {
			EnergyHelper.sendOutPower(energy, level, worldPosition, () -> setChanged());
		}
	}

	@Override
	public boolean isGeneratorMode() {
		return level.getBlockState(worldPosition).getValue(BatteryBlock.LIT);
	}
}
