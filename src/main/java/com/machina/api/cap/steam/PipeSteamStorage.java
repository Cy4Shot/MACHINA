package com.machina.api.cap.steam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.machina.api.cap.IConnectorStorage;
import com.machina.block.entity.connector.SteamPipeBlockEntity;
import com.machina.registration.CapabilityRegistrar;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class PipeSteamStorage implements ISteamHandler, IConnectorStorage<Integer> {

	protected final SteamPipeBlockEntity cable;
	protected final Direction side;
	protected long lastReceived;

	public PipeSteamStorage(SteamPipeBlockEntity c, Direction side) {
		this.cable = c;
		this.side = side;
	}

	@Override
	public int receiveSteam(int maxReceive, boolean simulate) {
		lastReceived = Objects.requireNonNull(cable.getLevel()).getGameTime();
		return receive(cable, side, maxReceive, simulate);
	}

	@Override
	public int extractSteam(int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canReceive() {
		return true;
	}

	public int receive(SteamPipeBlockEntity be, Direction side, int amount, boolean simulate) {
		if (!cable.getConnection(side).isOutput()) {
			return 0;
		}
		return receiveEqually(be, side, be.getSortedConnections(), Math.min(be.getRate(), amount), simulate);
	}

	protected int receiveEqually(SteamPipeBlockEntity be, Direction side,
			List<SteamPipeBlockEntity.Connection> connections, int maxReceive, boolean simulate) {
		if (connections.isEmpty() || maxReceive <= 0)
			return 0;
		if (be.pushRecursion())
			return 0;
		int actuallyTransferred = 0;
		int steamToTransfer = maxReceive;
		int p = be.getRoundRobinIndex(side) % connections.size();
		List<Pair<ISteamHandler, Integer>> destinations = new ArrayList<>(connections.size());
		for (int i = 0; i < connections.size(); i++) {
			int index = (i + p) % connections.size();

			SteamPipeBlockEntity.Connection connection = connections.get(index);
			if (connection.getSide(be.getLevel()).isInput()) {
				ISteamHandler destination = getSteamStorage(be, connection.pos().relative(connection.direction()));

				if (destination != null) {
					boolean canRecieve = destination.canReceive();
					if (canRecieve && destination.receiveSteam(1, true) >= 1)
						destinations.add(new Pair<>(destination, index));
				}
			}
		}

		for (Pair<ISteamHandler, Integer> destination : destinations) {
			int maxTransfer = Math.min(Math.max(maxReceive / destinations.size(), 1), steamToTransfer);
			int extracted = destination.getFirst().receiveSteam(Math.min(maxTransfer, maxReceive), simulate);
			if (extracted > 0) {
				steamToTransfer -= extracted;
				actuallyTransferred += extracted;
			}

			p = destination.getSecond() + 1;

			if (steamToTransfer <= 0)
				break;
		}

		if (!simulate)
			be.setRoundRobinIndex(side, p);
		be.popRecursion();
		return actuallyTransferred;
	}

	@Nullable
	private ISteamHandler getSteamStorage(SteamPipeBlockEntity be, BlockPos pos) {
		return be.getLevel().getCapability(CapabilityRegistrar.Block.STEAM, pos);
	}

	@Override
	public void tick() {
	}
}