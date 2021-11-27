package com.machina.api.container.tracker;

import java.util.LinkedList;
import java.util.List;

import com.machina.api.tile_entity.MachinaEnergyStorage;
import com.machina.api.util.FunctionalIntReferenceHolder;

import net.minecraft.util.IntReferenceHolder;

public class EnergyTracker implements IDataTracker {

	private final MachinaEnergyStorage energyStorage;
	
	public EnergyTracker(MachinaEnergyStorage energyStorage) {
		this.energyStorage = energyStorage;
	}

	@Override
	public List<IntReferenceHolder> getDataSlots() {
		List<IntReferenceHolder> slots = new LinkedList<>();
		slots.add(new FunctionalIntReferenceHolder(energyStorage::getEnergyStored, val -> energyStorage.receiveEnergy(val, false)));
		return slots;
	}
	
	public int getEnergyStored() { return energyStorage.getEnergyStored(); }
	public int getMaxReceive() { return energyStorage.getEnergyStored(); }
	public int getMaxExtract() { return energyStorage.getEnergyStored(); }
	
}
