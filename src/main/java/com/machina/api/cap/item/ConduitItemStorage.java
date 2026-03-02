package com.machina.api.cap.item;

import com.machina.api.cap.IConnectorStorage;
import com.machina.block.entity.connector.ItemConduitBlockEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConduitItemStorage implements IItemHandler, IConnectorStorage<ItemStack> {

	protected final ItemConduitBlockEntity conduit;
	protected final Direction side;
	protected long lastReceived;

	public ConduitItemStorage(ItemConduitBlockEntity c, Direction side) {
		this.conduit = c;
		this.side = side;
	}

	@Override
	public void tick() {
		if (Objects.requireNonNull(conduit.getLevel()).getGameTime() - lastReceived > 1) {
			pullItem(conduit, side);
		}
	}

	@Override
	public int getSlots() {
		// Some mods will validate slot count before pushing.
		// Return 9 to ensure that the item conduit is not skipped
		return 9;
	}

	@Override
	public @NotNull ItemStack getStackInSlot(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return conduit.getRate();
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		return true;
	}

	@Override
	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
		return receive(conduit, side, stack, simulate);
	}

    @Override
	public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}

	public void pullItem(ItemConduitBlockEntity be, Direction side) {
		if (!be.getConnection(side).isOutput()) {
			return;
		}

		IItemHandler handler = getItemHandler(be, be.getBlockPos().relative(side), side.getOpposite());
		if (handler == null)
			return;

		insertEqually(be, side, be.getSortedConnections(), handler);
	}

	public @NotNull ItemStack receive(ItemConduitBlockEntity be, Direction side, ItemStack stack, boolean simulate) {
		if (!be.getConnection(side).isOutput()) {
			return ItemStack.EMPTY;
		}
		return receiveEqually(be, side, be.getSortedConnections(),
				new ItemStack(stack.getItem(), Math.min(be.getRate(), stack.getCount())), simulate);
	}

	protected void insertEqually(ItemConduitBlockEntity be, Direction side,
			List<ItemConduitBlockEntity.Connection> connections, IItemHandler handler) {
		if (connections.isEmpty())
			return;

		int completeAmount = be.getRate();
		int itemsToTransfer = completeAmount;
		int p = be.getRoundRobinIndex(side) % connections.size();

		ItemStack testExtract = drainItem(handler, 1, true);
		if (testExtract.isEmpty() || !be.filter(side, testExtract))
			return;

		List<IItemHandler> destinations = new ArrayList<>(connections.size());
		for (int i = 0; i < connections.size(); i++) {
			int index = (i + p) % connections.size();

			ItemConduitBlockEntity.Connection connection = connections.get(index);
			if (connection.getSide(be.getLevel()).isInput()) {
				IItemHandler destination = getItemHandler(be, connection.pos().relative(connection.direction()),
						connection.direction().getOpposite());

				if (destination != null) {
					ItemStack tester = new ItemStack(testExtract.getItem(), 1);
					if (fillItem(destination, tester, true) >= 1 && connection.filter(be.getLevel(), tester))
						destinations.add(destination);
				}
			}
		}

		for (IItemHandler destination : destinations) {
			ItemStack simulatedExtract = drainItem(handler,
					Math.min(Math.max(completeAmount / destinations.size(), 1), itemsToTransfer), true);
			if (simulatedExtract.getCount() > 0) {
				ItemStack transferred = pushItem(handler, destination, simulatedExtract);
				if (transferred.getCount() > 0)
					itemsToTransfer -= transferred.getCount();
			}

			p = (p + 1) % connections.size();

			if (itemsToTransfer <= 0)
				break;
		}

		be.setRoundRobinIndex(side, p);
	}

	protected @NotNull ItemStack receiveEqually(ItemConduitBlockEntity be, Direction side,
			List<ItemConduitBlockEntity.Connection> connections, ItemStack maxReceive, boolean simulate) {
		if (connections.isEmpty() || maxReceive.getCount() <= 0 || !be.filter(side, maxReceive))
			return ItemStack.EMPTY;
		if (be.pushRecursion())
			return ItemStack.EMPTY;
		int actuallyTransferred = 0;
		int itemsToTransfer = maxReceive.getCount();
		int p = be.getRoundRobinIndex(side) % connections.size();
		List<Pair<IItemHandler, Integer>> destinations = new ArrayList<>(connections.size());
		for (int i = 0; i < connections.size(); i++) {
			int index = (i + p) % connections.size();

			ItemConduitBlockEntity.Connection connection = connections.get(index);
			if (connection.getSide(be.getLevel()).isInput()) {
				IItemHandler destination = getItemHandler(be, connection.pos().relative(connection.direction()),
						connection.direction().getOpposite());

				if (destination != null) {
					ItemStack tester = new ItemStack(maxReceive.getItem(), 1);
					if (fillItem(destination, tester, true) >= 1 && connection.filter(be.getLevel(), tester))
						destinations.add(new Pair<>(destination, index));
				}
			}
		}

		for (Pair<IItemHandler, Integer> destination : destinations) {
			int maxTransfer = Math.min(Math.max(maxReceive.getCount() / destinations.size(), 1), itemsToTransfer);
			int extracted = fillItem(destination.getFirst(),
					new ItemStack(maxReceive.getItem(), Math.min(maxTransfer, maxReceive.getCount())), simulate);
			if (extracted > 0) {
				itemsToTransfer -= extracted;
				actuallyTransferred += extracted;
			}

			p = destination.getSecond() + 1;

			if (itemsToTransfer <= 0)
				break;
		}

		if (!simulate)
			be.setRoundRobinIndex(side, p);
		be.popRecursion();
		return new ItemStack(maxReceive.getItem(), actuallyTransferred);
	}

	@Nullable
	private IItemHandler getItemHandler(ItemConduitBlockEntity be, BlockPos pos, Direction direction) {
		return be.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, pos, direction);
	}

	private static ItemStack drainItem(IItemHandler handler, ItemStack test, boolean simulate) {
		for (int i = 0; i < handler.getSlots(); i++) {
			ItemStack stack = handler.extractItem(i, test.getCount(), simulate);
			if (test.getItem() == stack.getItem() && !stack.isEmpty())
				return stack;
		}
		return ItemStack.EMPTY;
	}

	private static ItemStack drainItem(IItemHandler handler, int amount, boolean simulate) {
		for (int i = 0; i < handler.getSlots(); i++) {
			ItemStack stack = handler.extractItem(i, amount, simulate);
			if (!stack.isEmpty())
				return stack;
		}
		return ItemStack.EMPTY;
	}

	private static int fillItem(IItemHandler handler, ItemStack stack, boolean simulate) {
		int count = stack.getCount();
		for (int i = 0; i < handler.getSlots(); i++) {
			stack = handler.insertItem(i, stack, simulate);
			if (stack.isEmpty()) {
				return count;
			}
		}
		return count - stack.getCount();
	}

	private ItemStack pushItem(IItemHandler provider, IItemHandler receiver, ItemStack stack) {
		ItemStack itemSim = drainItem(provider, stack, true);
		int receivedSim = fillItem(receiver, itemSim, true);
		ItemStack items = drainItem(provider, receivedSim, false);
		fillItem(receiver, items, false);
		return items;
	}
}