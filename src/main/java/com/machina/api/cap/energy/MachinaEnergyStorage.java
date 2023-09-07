package com.machina.api.cap.energy;

import javax.annotation.Nullable;

import com.machina.api.cap.ICustomStorage;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;

/**
 * Block entity storage for energy.
 * 
 * @author Cy4
 * @since Machina v0.1.0
 */
public class MachinaEnergyStorage extends EnergyStorage implements ICustomStorage {

	private Runnable onChanged;
	private final IEnergyBlockEntity be;

	public MachinaEnergyStorage(IEnergyBlockEntity be, int capacity, int maxTransfer) {
		super(capacity, maxTransfer, maxTransfer);
		this.be = be;
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
	public void setChanged(Runnable runnable) {
		this.onChanged = runnable;
	}

	@Override
	public String getTag() {
		return "energy";
	}

	@Override
	public boolean canExtract() {
		return super.canExtract() && be.isGenerator();
	}

	@Override
	public boolean canReceive() {
		return super.canReceive() && !be.isGenerator();
	}

	public boolean isFull() {
		return this.capacity == this.energy;
	}

	public static boolean hasEnergy(@Nullable BlockEntity te, @Nullable Direction dir) {
		return (te == null ? LazyOptional.empty()
				: te.getCapability(ForgeCapabilities.ENERGY, dir != null ? dir.getOpposite() : null)).isPresent();
	}

	@Override
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.put("energy", this.serializeNBT());
		return tag;
	}

	@Override
	public void deserialize(CompoundTag nbt) {
		this.deserializeNBT(nbt.get("energy"));
	}
}