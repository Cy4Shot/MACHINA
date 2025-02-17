package com.machina.api.cap.energy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.machina.api.cap.IConnectorStorage;
import com.machina.block.entity.connector.EnergyCableBlockEntity;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class CableEnergyStorage implements IEnergyStorage, IConnectorStorage {

	protected final EnergyCableBlockEntity cable;
	protected final Direction side;
	protected long lastReceived;

	public CableEnergyStorage(EnergyCableBlockEntity c, Direction side) {
		this.cable = c;
		this.side = side;
	}

	public void tick() {
		if (Objects.requireNonNull(cable.getLevel()).getGameTime() - lastReceived > 1) {
			pullEnergy(cable, side);
		}
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		lastReceived = Objects.requireNonNull(cable.getLevel()).getGameTime();
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

	public void pullEnergy(EnergyCableBlockEntity be, Direction side) {
		IEnergyStorage energyStorage = getEnergyStorage(be, be.getBlockPos().relative(side), side.getOpposite());
		if (energyStorage == null || !energyStorage.canExtract())
			return;

		insertEqually(be, side, be.getSortedConnections(side), energyStorage);
	}

	public int receive(EnergyCableBlockEntity be, Direction side, int amount, boolean simulate) {
		if (!cable.getConnection(side).isOutput()) {
			return 0;
		}
		return receiveEqually(be, side, be.getSortedConnections(side), Math.min(be.getRate(), amount), simulate);
	}

	protected void insertEqually(EnergyCableBlockEntity be, Direction side,
			List<EnergyCableBlockEntity.Connection> connections, IEnergyStorage energyStorage) {
		if (connections.isEmpty())
			return;

		int completeAmount = be.getRate();
		int energyToTransfer = completeAmount;
		int p = be.getRoundRobinIndex(side) % connections.size();

		List<IEnergyStorage> destinations = new ArrayList<>(connections.size());
		for (int i = 0; i < connections.size(); i++) {
			int index = (i + p) % connections.size();

			EnergyCableBlockEntity.Connection connection = connections.get(index);
			if (connection.getSide(be.getLevel()).isInput()) {
				IEnergyStorage destination = getEnergyStorage(be,
						connection.getPos().relative(connection.getDirection()),
						connection.getDirection().getOpposite());

				if (destination != null) {
					boolean canRecieve = destination.canReceive();
					if (canRecieve && destination.receiveEnergy(1, true) >= 1)
						destinations.add(destination);
				}
			}
		}

		for (IEnergyStorage destination : destinations) {
			int simulatedExtract = energyStorage
					.extractEnergy(Math.min(Math.max(completeAmount / destinations.size(), 1), energyToTransfer), true);
			if (simulatedExtract > 0) {
				int transferred = pushEnergy(energyStorage, destination, simulatedExtract);
				if (transferred > 0)
					energyToTransfer -= transferred;
			}

			p = (p + 1) % connections.size();

			if (energyToTransfer <= 0)
				break;
		}

		be.setRoundRobinIndex(side, p);
	}

	protected int receiveEqually(EnergyCableBlockEntity be, Direction side,
			List<EnergyCableBlockEntity.Connection> connections, int maxReceive, boolean simulate) {
		if (connections.isEmpty() || maxReceive <= 0)
			return 0;
		if (be.pushRecursion())
			return 0;
		int actuallyTransferred = 0;
		int energyToTransfer = maxReceive;
		int p = be.getRoundRobinIndex(side) % connections.size();
		List<Pair<IEnergyStorage, Integer>> destinations = new ArrayList<>(connections.size());
		for (int i = 0; i < connections.size(); i++) {
			int index = (i + p) % connections.size();

			EnergyCableBlockEntity.Connection connection = connections.get(index);
			if (connection.getSide(be.getLevel()).isInput()) {
				IEnergyStorage destination = getEnergyStorage(be,
						connection.getPos().relative(connection.getDirection()),
						connection.getDirection().getOpposite());

				if (destination != null) {
					boolean canRecieve = destination.canReceive();
					if (canRecieve && destination.receiveEnergy(1, true) >= 1)
						destinations.add(new Pair<>(destination, index));
				}
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

			if (energyToTransfer <= 0)
				break;
		}

		if (!simulate)
			be.setRoundRobinIndex(side, p);
		be.popRecursion();
		return actuallyTransferred;
	}

	@Nullable
	private IEnergyStorage getEnergyStorage(EnergyCableBlockEntity be, BlockPos pos, Direction direction) {
		BlockEntity te = Objects.requireNonNull(be.getLevel()).getBlockEntity(pos);
		if (te == null)
			return null;
		return te.getCapability(ForgeCapabilities.ENERGY, direction).orElse(null);
	}

	public static int pushEnergy(IEnergyStorage provider, IEnergyStorage receiver, int maxAmount) {
		int energySim = provider.extractEnergy(maxAmount, true);
		int receivedSim = receiver.receiveEnergy(energySim, true);
		int energy = provider.extractEnergy(receivedSim, false);
		receiver.receiveEnergy(energy, false);
		return energy;
	}
}