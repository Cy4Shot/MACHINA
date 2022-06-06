package com.machina.energy;

import javax.annotation.Nullable;

import com.machina.block.tile.base.BaseEnergyTileEntity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

public class MachinaEnergyStorage extends EnergyStorage {

	private final BaseEnergyTileEntity te;

	public MachinaEnergyStorage(BaseEnergyTileEntity te, int capacity) {
		super(capacity);
		this.te = te;
	}

	public MachinaEnergyStorage(BaseEnergyTileEntity te, int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
		this.te = te;
	}

	public MachinaEnergyStorage(BaseEnergyTileEntity te, int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
		this.te = te;
	}

	public MachinaEnergyStorage(BaseEnergyTileEntity te, int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
		this.te = te;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		this.te.sync();
		return super.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		this.te.sync();
		return super.receiveEnergy(maxReceive, simulate);
	}

	public void save(CompoundNBT nbt) {
		nbt.putInt("Energy", energy);
	}

	public void load(CompoundNBT nbt) {
		this.energy = nbt.getInt("Energy");
	}

	public int getMaxExtract() {
		return this.maxExtract;
	}

	public void setEnergy(int energy) {
		this.energy = Math.max(0, Math.min(energy, this.capacity));
	}

	public static boolean hasEnergy(@Nullable TileEntity te, @Nullable Direction dir) {
		return (te == null ? LazyOptional.empty()
				: te.getCapability(CapabilityEnergy.ENERGY, dir != null ? dir.getOpposite() : null)).isPresent();
	}
}
