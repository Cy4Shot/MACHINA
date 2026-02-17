package com.machina.api.cap.energy;

import com.machina.api.cap.IConnectorStorage;
import com.machina.block.entity.connector.EnergyCableBlockEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CableEnergyStorage implements IEnergyStorage, IConnectorStorage<Integer> {

    protected final EnergyCableBlockEntity cable;
    protected final Direction side;
    protected long lastReceived;

    public CableEnergyStorage(EnergyCableBlockEntity c, Direction side) {
        this.cable = c;
        this.side = side;
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

    public int receive(EnergyCableBlockEntity be, Direction side, int amount, boolean simulate) {
        if (!cable.getConnection(side).isOutput()) {
            return 0;
        }
        return receiveEqually(be, side, be.getSortedConnections(), Math.min(be.getRate(), amount), simulate);
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
                        connection.pos().relative(connection.direction()),
                        connection.direction().getOpposite());

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
        return be.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
    }

    @Override
    public void tick() {
    }
}