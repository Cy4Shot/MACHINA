package com.machina.api.cap.fluid;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.machina.api.cap.IConnectorStorage;
import com.machina.block.entity.connector.FluidPipeBlockEntity;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class PipeFluidStorage implements IFluidHandler, IConnectorStorage {

	protected FluidPipeBlockEntity pipe;
	protected Direction side;
	protected long lastReceived;

	public PipeFluidStorage(FluidPipeBlockEntity p, Direction side) {
		this.pipe = p;
		this.side = side;
	}

	@Override
	public void tick() {
		if (pipe.getLevel().getGameTime() - lastReceived > 1) {
			pullFluid(pipe, side);
		}
	}

	@Override
	public int getTanks() {
		return 1;
	}

	private FluidStack getFluid() {
		// TODO: Better

		return FluidStack.EMPTY;
	}

	@Override
	public @NotNull FluidStack getFluidInTank(int tank) {
		return getFluid();
	}

	@Override
	public int getTankCapacity(int tank) {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return true;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return receive(pipe, side, resource.getAmount(), action == FluidAction.SIMULATE);
	}

	@Override
	public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
		return FluidStack.EMPTY;
	}

	@Override
	public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
		return FluidStack.EMPTY;
	}

	public void pullFluid(FluidPipeBlockEntity be, Direction side) {
		IFluidHandler handler = getFluidHandler(be, be.getBlockPos().relative(side), side.getOpposite());
		if (handler == null)
			return;

		insertEqually(be, side, be.getSortedConnections(side), handler);
	}

	public int receive(FluidPipeBlockEntity be, Direction side, int amount, boolean simulate) {
		return receiveEqually(be, side, be.getSortedConnections(side), Math.min(be.getRate(), amount), simulate);
	}

	protected void insertEqually(FluidPipeBlockEntity be, Direction side,
			List<FluidPipeBlockEntity.Connection> connections, IFluidHandler handler) {
		if (connections.isEmpty())
			return;

		int completeAmount = be.getRate();
		int fluidToTransfer = completeAmount;
		int p = be.getRoundRobinIndex(side) % connections.size();

		List<IFluidHandler> destinations = new ArrayList<>(connections.size());
		for (int i = 0; i < connections.size(); i++) {
			int index = (i + p) % connections.size();

			FluidPipeBlockEntity.Connection connection = connections.get(index);
			IFluidHandler destination = getFluidHandler(be, connection.getPos().relative(connection.getDirection()),
					connection.getDirection().getOpposite());

			if (destination != null) {
				if (destination.fill(new FluidStack(getFluid(), 1), FluidAction.SIMULATE) >= 1)
					destinations.add(destination);
			}
		}

		for (IFluidHandler destination : destinations) {
			int simulatedExtract = handler
					.drain(Math.min(Math.max(completeAmount / destinations.size(), 1), fluidToTransfer),
							FluidAction.SIMULATE)
					.getAmount();
			if (simulatedExtract > 0) {
				int transferred = pushFluid(handler, destination, simulatedExtract);
				if (transferred > 0)
					fluidToTransfer -= transferred;
			}

			p = (p + 1) % connections.size();

			if (fluidToTransfer <= 0)
				break;
		}

		be.setRoundRobinIndex(side, p);
	}

	protected int receiveEqually(FluidPipeBlockEntity be, Direction side,
			List<FluidPipeBlockEntity.Connection> connections, int maxReceive, boolean simulate) {
		if (connections.isEmpty() || maxReceive <= 0)
			return 0;
		if (be.pushRecursion())
			return 0;
		int actuallyTransferred = 0;
		int fluidToTransfer = maxReceive;
		int p = be.getRoundRobinIndex(side) % connections.size();
		List<Pair<IFluidHandler, Integer>> destinations = new ArrayList<>(connections.size());
		for (int i = 0; i < connections.size(); i++) {
			int index = (i + p) % connections.size();

			FluidPipeBlockEntity.Connection connection = connections.get(index);
			IFluidHandler destination = getFluidHandler(be, connection.getPos().relative(connection.getDirection()),
					connection.getDirection().getOpposite());

			if (destination != null) {
				if (destination.fill(new FluidStack(getFluid(), 1), FluidAction.SIMULATE) >= 1)
					destinations.add(new Pair<>(destination, index));
			}
		}

		for (Pair<IFluidHandler, Integer> destination : destinations) {
			int maxTransfer = Math.min(Math.max(maxReceive / destinations.size(), 1), fluidToTransfer);
			int extracted = destination.getFirst().fill(new FluidStack(getFluid(), Math.min(maxTransfer, maxReceive)),
					simulate ? FluidAction.SIMULATE : FluidAction.EXECUTE);
			if (extracted > 0) {
				fluidToTransfer -= extracted;
				actuallyTransferred += extracted;
			}

			p = destination.getSecond() + 1;

			if (fluidToTransfer <= 0)
				break;
		}

		if (!simulate)
			be.setRoundRobinIndex(side, p);
		be.popRecursion();
		return actuallyTransferred;
	}

	@Nullable
	private IFluidHandler getFluidHandler(FluidPipeBlockEntity be, BlockPos pos, Direction direction) {
		BlockEntity te = be.getLevel().getBlockEntity(pos);
		if (te == null)
			return null;
		return te.getCapability(ForgeCapabilities.FLUID_HANDLER, direction).orElse(null);
	}

	public int pushFluid(IFluidHandler provider, IFluidHandler receiver, int maxAmount) {
		int fluidSim = provider.drain(maxAmount, FluidAction.SIMULATE).getAmount();
		int receivedSim = receiver.fill(new FluidStack(getFluid(), fluidSim), FluidAction.SIMULATE);
		int fluid = provider.drain(receivedSim, FluidAction.EXECUTE).getAmount();
		receiver.fill(new FluidStack(getFluid(), fluid), FluidAction.EXECUTE);
		return fluid;
	}
}