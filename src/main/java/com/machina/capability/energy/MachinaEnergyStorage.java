package com.machina.capability.energy;

import javax.annotation.Nullable;

import com.machina.capability.ICustomStorage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

public class MachinaEnergyStorage extends EnergyStorage implements ICustomStorage {

	private Runnable onChanged;
	private IEnergyTileEntity te;

	public MachinaEnergyStorage(IEnergyTileEntity te, int capacity, int maxTransfer) {
		super(capacity, maxTransfer, maxTransfer);
		this.te = te;
	}

	protected void onEnergyChanged() {
		onChanged.run();
	}

	public void setEnergy(int energy) {
		this.energy = energy;
		onEnergyChanged();
	}

	public void addEnergy(int energy) {
		this.energy += energy;
		if (this.energy > getMaxEnergyStored()) {
			this.energy = getEnergyStored();
		}
		onEnergyChanged();
	}

	public void consumeEnergy(int energy) {
		this.energy -= energy;
		if (this.energy < 0) {
			this.energy = 0;
		}
		onEnergyChanged();
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int r = super.receiveEnergy(maxReceive, simulate);
		if (r > 0) {
			onEnergyChanged();
		}
		return r;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int r = super.extractEnergy(maxExtract, simulate);
		if (r > 0) {
			onEnergyChanged();
		}
		return r;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("energy", getEnergyStored());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		setEnergy(nbt.getInt("energy"));
	}

	@Override
	public void setChanged(Runnable runnable) {
		this.onChanged = runnable;
	}

	@Override
	public String getTag() {
		return "energy";
	}

	@Override
	public boolean canExtract() {
		return super.canExtract() && te.isGeneratorMode();
	}

	@Override
	public boolean canReceive() {
		return super.canReceive() && !te.isGeneratorMode();
	}

	public boolean isFull() {
		return this.capacity == this.energy;
	}

	public static boolean hasEnergy(@Nullable TileEntity te, @Nullable Direction dir) {
		return (te == null ? LazyOptional.empty()
				: te.getCapability(CapabilityEnergy.ENERGY, dir != null ? dir.getOpposite() : null)).isPresent();
	}
}