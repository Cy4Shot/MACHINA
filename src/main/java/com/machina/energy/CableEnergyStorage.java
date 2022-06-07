package com.machina.energy;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.machina.block.tile.CableTileEntity;
import com.mojang.datafixers.util.Pair;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class CableEnergyStorage implements IEnergyStorage {

	protected CableTileEntity cable;
	protected Direction side;
	protected long lastReceived;

	public CableEnergyStorage(CableTileEntity c, Direction side) {
		this.cable = c;
		this.side = side;
	}

	public void tick() {
		if (cable.getLevel().getGameTime() - lastReceived > 1) {
			pullEnergy(cable, side);
		}
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		lastReceived = cable.getLevel().getGameTime();
		return receive(cable, side, maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored() {
		return 0;
	}

	@Override
	public int getMaxEnergyStored() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canReceive() {
		return true;
	}

	public void pullEnergy(CableTileEntity tileEntity, Direction side) {
		if (!tileEntity.canTransfer(side)) {
			return;
		}
		IEnergyStorage energyStorage = getEnergyStorage(tileEntity, tileEntity.getBlockPos().relative(side),
				side.getOpposite());
		if (energyStorage == null || !energyStorage.canExtract()) {
			return;
		}

		List<CableTileEntity.Connection> connections = tileEntity.getSortedConnections(side);

		insertEqually(tileEntity, side, connections, energyStorage);
	}

	public int receive(CableTileEntity tileEntity, Direction side, int amount, boolean simulate) {
		if (!tileEntity.canTransfer(side)) {
			return 0;
		}

		List<CableTileEntity.Connection> connections = tileEntity.getSortedConnections(side);

		int maxTransfer = Math.min(tileEntity.getRate(), amount);

		return receiveEqually(tileEntity, side, connections, maxTransfer, simulate);
	}

	protected void insertEqually(CableTileEntity tileEntity, Direction side,
			List<CableTileEntity.Connection> connections, IEnergyStorage energyStorage) {
		if (connections.isEmpty()) {
			return;
		}

		int completeAmount = tileEntity.getRate();
		int energyToTransfer = completeAmount;

		int p = tileEntity.getRoundRobinIndex(side) % connections.size();

		List<IEnergyStorage> destinations = new ArrayList<>(connections.size());
		for (int i = 0; i < connections.size(); i++) {
			int index = (i + p) % connections.size();

			CableTileEntity.Connection connection = connections.get(index);
			IEnergyStorage destination = getEnergyStorage(tileEntity,
					connection.getPos().relative(connection.getDirection()), connection.getDirection().getOpposite());
			if (destination != null && destination.canReceive() && destination.receiveEnergy(1, true) >= 1) {
				destinations.add(destination);
			}
		}

		for (IEnergyStorage destination : destinations) {
			int simulatedExtract = energyStorage
					.extractEnergy(Math.min(Math.max(completeAmount / destinations.size(), 1), energyToTransfer), true);
			if (simulatedExtract > 0) {
				int transferred = pushEnergy(energyStorage, destination, simulatedExtract);
				if (transferred > 0) {
					energyToTransfer -= transferred;
				}
			}

			p = (p + 1) % connections.size();

			if (energyToTransfer <= 0) {
				break;
			}
		}

		tileEntity.setRoundRobinIndex(side, p);
	}

	protected int receiveEqually(CableTileEntity tileEntity, Direction side,
			List<CableTileEntity.Connection> connections, int maxReceive, boolean simulate) {
		if (connections.isEmpty() || maxReceive <= 0) {
			return 0;
		}
		if (tileEntity.pushRecursion()) {
			return 0;
		}
		int actuallyTransferred = 0;
		int energyToTransfer = maxReceive;
		int p = tileEntity.getRoundRobinIndex(side) % connections.size();
		List<Pair<IEnergyStorage, Integer>> destinations = new ArrayList<>(connections.size());
		for (int i = 0; i < connections.size(); i++) {
			int index = (i + p) % connections.size();

			CableTileEntity.Connection connection = connections.get(index);
			IEnergyStorage destination = getEnergyStorage(tileEntity,
					connection.getPos().relative(connection.getDirection()), connection.getDirection().getOpposite());
			if (destination != null && destination.canReceive() && destination.receiveEnergy(1, true) >= 1) {
				destinations.add(new Pair<>(destination, index));
			}
		}

		for (Pair<IEnergyStorage, Integer> destination : destinations) {
			int maxTransfer = Math.min(Math.max(maxReceive / destinations.size(), 1), energyToTransfer);
			int extracted = destination.getFirst().receiveEnergy(Math.min(maxTransfer, maxReceive), simulate);
			if (extracted > 0) {
				energyToTransfer -= extracted;
				actuallyTransferred += extracted;
			}

			p = destination.getSecond() + 1;

			if (energyToTransfer <= 0) {
				break;
			}
		}

		if (!simulate) {
			tileEntity.setRoundRobinIndex(side, p);
		}
		tileEntity.popRecursion();
		return actuallyTransferred;
	}

	@Nullable
	private IEnergyStorage getEnergyStorage(CableTileEntity tileEntity, BlockPos pos, Direction direction) {
		TileEntity te = tileEntity.getLevel().getBlockEntity(pos);
		if (te == null) {
			return null;
		}
		return te.getCapability(CapabilityEnergy.ENERGY, direction).orElse(null);
	}

	public static int pushEnergy(IEnergyStorage provider, IEnergyStorage receiver, int maxAmount) {
		int energySim = provider.extractEnergy(maxAmount, true);
		int receivedSim = receiver.receiveEnergy(energySim, true);
		int energy = provider.extractEnergy(receivedSim, false);
		receiver.receiveEnergy(energy, false);
		return energy;
	}

}