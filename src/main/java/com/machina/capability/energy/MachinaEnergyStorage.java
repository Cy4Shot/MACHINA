package com.machina.capability.energy;

import java.util.Arrays;
import java.util.EnumMap;

import javax.annotation.Nullable;

import com.machina.block.tile.base.BaseEnergyTileEntity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class MachinaEnergyStorage extends EnergyStorage implements ICapabilityProvider {

	private final BaseEnergyTileEntity te;
	private final LazyOptional<MachinaEnergyStorage> lazy;
	private final EnumMap<Direction, LazyOptional<Connection>> connections = new EnumMap<>(Direction.class);

	public MachinaEnergyStorage(BaseEnergyTileEntity te, int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
		this.te = te;
		this.lazy = LazyOptional.of(() -> this);
		Arrays.stream(Direction.values()).forEach(d -> connections.put(d, LazyOptional.of(() -> new Connection(d))));
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		this.te.sync();
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		this.te.sync();
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
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
	
	public void setEnergyDirectly(int amount) {
        this.energy = amount;
    }
	
	public LazyOptional<IEnergyStorage> getEnergy(Direction side) {
		return this.connections.get(side).cast();
	}

	public void consumeEnergy(int energy) {
		this.energy -= energy;
		if (this.energy < 0) {
			this.energy = 0;
		}
		this.te.sync();
	}

	public static boolean hasEnergy(@Nullable TileEntity te, @Nullable Direction dir) {
		return (te == null ? LazyOptional.empty()
				: te.getCapability(CapabilityEnergy.ENERGY, dir != null ? dir.getOpposite() : null)).isPresent();
	}

	public class Connection implements IEnergyStorage {
		private Direction dir;
		private long lastReceiveTick;

		public Connection(Direction dir) {
			this.dir = dir;
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			World world = MachinaEnergyStorage.this.te.getLevel();
			if (world == null)
				return 0;

			if (!canReceive())
				return 0;

			int received = MachinaEnergyStorage.this.receiveEnergy(maxReceive, simulate);
			if (received > 0 && !simulate)
				this.lastReceiveTick = world.getGameTime();
			return received;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			World world = MachinaEnergyStorage.this.te.getLevel();
			if (world == null)
				return 0;

			if (!canExtract())
				return 0;

			long time = world.getGameTime();
			if (time != this.lastReceiveTick) {
				return MachinaEnergyStorage.this.extractEnergy(maxExtract, simulate);
			}
			return 0;
		}

		@Override
		public int getEnergyStored() {
			return MachinaEnergyStorage.this.getEnergyStored();
		}

		@Override
		public int getMaxEnergyStored() {
			return MachinaEnergyStorage.this.getMaxEnergyStored();
		}

		@Override
		public boolean canExtract() {
			System.out.println("Extract check : " + dir);
			return MachinaEnergyStorage.this.te.canTransfer(dir) && MachinaEnergyStorage.this.maxExtract > 0;
		}

		@Override
		public boolean canReceive() {
			System.out.println("Recieve check : " + dir);
			return MachinaEnergyStorage.this.te.canRecieve(dir) && MachinaEnergyStorage.this.maxReceive > 0;
		}
	}
	
	@Override
	public boolean canExtract() {
		System.out.println("EXTRACT CHECK WITHOUT DIR :(((");
		return super.canExtract();
	}
	
	@Override
	public boolean canReceive() {
		System.out.println("RECEIVE CHECK WITHOUT DIR :(((");
		return super.canReceive();
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (side == null)
			return CapabilityEnergy.ENERGY.orEmpty(cap, lazy.cast());
		return CapabilityEnergy.ENERGY.orEmpty(cap, connections.get(side).cast());
	}

	public void invalidate() {
		this.lazy.invalidate();
		connections.values().forEach(LazyOptional::invalidate);
	}
}
