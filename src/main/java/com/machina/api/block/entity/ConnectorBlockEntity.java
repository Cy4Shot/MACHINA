package com.machina.api.block.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.machina.api.block.ConnectorBlock;
import com.machina.api.cap.IConnectorStorage;
import com.machina.api.cap.sided.Side;
import com.machina.api.cap.sided.SidedLazyOptionalCache;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public abstract class ConnectorBlockEntity<T extends IConnectorStorage> extends BaseBlockEntity {

	protected final int[] roundrobin;
	private int recursionDepth;
	private boolean search = false;

	private final SidedLazyOptionalCache<T> cap;
	public List<Connection> connectors = new ArrayList<>();
	public final List<BlockPos> cache = new ArrayList<>();
	public List<Direction> dirs = new ArrayList<>();

	public ConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.cap = new SidedLazyOptionalCache<>();
		this.roundrobin = new int[Direction.values().length];
		this.revalidate();
	}

	public abstract T createStorage(Direction side);

	protected void revalidate() {
		for (Direction dir : Direction.values()) {
			cap.revalidate(dir, s -> true, this::createStorage);
		}
	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
		if (level.isClientSide())
			return;

		ConnectorBlockEntity<?> cbe = (ConnectorBlockEntity<?>) be;

		for (Direction dir : Direction.values()) {
			cbe.cap.get(dir).ifPresent(IConnectorStorage::tick);
		}

		if (cbe.search) {
			cbe.search();
			cbe.search = false;
			cbe.sync();
		}
	}

	public abstract Capability<?> getCapability();

	@Override
	public <C> @NotNull LazyOptional<C> getCapability(@NotNull Capability<C> cap, Direction d) {
		if (cap == getCapability())
			return this.cap.get(d).cast();

		return super.getCapability(cap, d);
	}

	@Override
	public void invalidateCaps() {
		this.cap.invalidate();
		super.invalidateCaps();
	}

	@Override
	public void setRemoved() {
		this.cap.invalidate();
		super.setRemoved();
	}

	public int getRoundRobinIndex(Direction direction) {
		return roundrobin[direction.get3DDataValue()];
	}

	public void setRoundRobinIndex(Direction direction, int value) {
		roundrobin[direction.get3DDataValue()] = value;
	}

	public List<Connection> getSortedConnections(Direction side) {
		return connectors.stream().sorted(Comparator.comparingInt(Connection::getDistance))
				.collect(Collectors.toList());
	}

	public abstract int getRate();

	@Override
	protected void saveAdditional(CompoundTag tag) {
		ListTag cons = new ListTag();
		this.connectors.forEach(pos -> {
			CompoundTag postag = new CompoundTag();
			postag.put("connector", pos.save());
			cons.add(postag);
		});
		tag.put("connectors", cons);

		ListTag sides = new ListTag();
		this.dirs.forEach(dir -> {
			CompoundTag postag = new CompoundTag();
			postag.putInt("dir", dir.get3DDataValue());
			sides.add(postag);
		});
		tag.put("dirs", sides);
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		connectors = new ArrayList<>();
		dirs = new ArrayList<>();

		ListTag cons = tag.getList("connectors", Tag.TAG_COMPOUND);
		for (int j = 0; j < cons.size(); j++) {
			connectors.add(Connection.load(cons.getCompound(j).getCompound("connector")));
		}

		ListTag sides = tag.getList("dirs", Tag.TAG_COMPOUND);
		for (int j = 0; j < sides.size(); j++) {
			dirs.add(Direction.from3DDataValue(sides.getCompound(j).getInt("dir")));
		}
		this.revalidate();
		super.load(tag);
	}

	public void enqueueSearch() {
		this.connectors.clear();
		this.search = true;
		this.sync();
	}

	@SuppressWarnings("unchecked")
	public void search() {
		if (this.level != null) {
			addToCache(this.worldPosition);

			dirs.forEach(dir -> connectors.add(new Connection(this.worldPosition, dir, 0, Side.INPUT)));

			Block b = this.getBlockState().getBlock();
			if (b instanceof ConnectorBlock)
				((ConnectorBlock<T>) b).searchConnectors(this.level, this.worldPosition, this, 0);
		}
		this.cache.clear();
	}

	public void addToCache(BlockPos pos) {
		this.cache.add(pos);
	}

	public boolean isInCache(BlockPos pos) {
		return this.cache.contains(pos);
	}

	public boolean pushRecursion() {
		if (recursionDepth >= 1) {
			return true;
		}
		recursionDepth++;
		return false;
	}

	public void popRecursion() {
		recursionDepth--;
	}

	public static class Connection {
		private final BlockPos pos;
		private final Direction direction;
		private final int distance;
		private final Side side;

		public Connection(BlockPos pos, Direction direction, int distance, Side side) {
			this.pos = pos;
			this.direction = direction;
			this.distance = distance;
			this.side = side;
		}

		public int getDistance() {
			return distance;
		}
		
		public boolean isInput() {
			return side.isInput();
		}
		
		public boolean isOutput() {
			return side.isOutput();
		}

		public BlockPos getPos() {
			return pos;
		}

		public BlockPos getDest() {
			return pos.relative(direction.getOpposite());
		}

		public Direction getDirection() {
			return direction;
		}

		@Override
		public String toString() {
			return "Connection{" + "pos=" + pos + ", direction=" + direction + ", distance=" + distance + '}';
		}

		public CompoundTag save() {
			CompoundTag tag = new CompoundTag();
			tag.put("CPos", NbtUtils.writeBlockPos(pos));
			tag.putInt("CDir", direction.get3DDataValue());
			tag.putInt("CDis", distance);
			side.save(tag, "CSide");
			return tag;
		}

		public static Connection load(CompoundTag nbt) {
			BlockPos p = NbtUtils.readBlockPos(nbt.getCompound("CPos"));
			Direction d = Direction.from3DDataValue(nbt.getInt("CDir"));
			int dist = nbt.getInt("CDis");
			Side side = Side.load(nbt, "CSide");
			return new Connection(p, d, dist, side);
		}
	}

}
