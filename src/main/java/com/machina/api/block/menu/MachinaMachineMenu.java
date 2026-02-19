package com.machina.api.block.menu;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.block.menu.data.EnergyPropData;

import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;

public abstract class MachinaMachineMenu<T extends MachinaBlockEntity> extends MachinaContainerMenu<T> {

	private int energy;
	private int maxEnergy;

	public MachinaMachineMenu(MenuType<?> type, int id, ContainerLevelAccess access) {
		super(type, id, access);

		this.addDataSlots(EnergyPropData.of(this::getBlockEntity, x -> this.maxEnergy = x, x -> this.energy = x));
	}

	public int getEnergy() {
		return this.energy;
	}

	public int getMaxEnergy() {
		return this.maxEnergy;
	}

	public float getEnergyF() {
		if (this.getMaxEnergy() == 0) {
			return 0;
		}
		return (float) this.getEnergy() / (float) this.getMaxEnergy();
	}
}
