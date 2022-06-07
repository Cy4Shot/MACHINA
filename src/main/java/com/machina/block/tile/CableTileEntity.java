package com.machina.block.tile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.machina.block.CableBlock;
import com.machina.block.tile.base.BaseTileEntity;
import com.machina.energy.CableEnergyStorage;
import com.machina.registration.init.TileEntityTypesInit;
import com.machina.util.DirectionalLazyOptionalCache;
import com.machina.util.server.BlockUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

//https://github.com/henkelmax/pipez/blob/e84a48ef95f44b13a37e9c2b06a41e24d706345c/src/main/java/de/maxhenkel/pipez/blocks/tileentity/PipeLogicTileEntity.java
public class CableTileEntity extends BaseTileEntity implements ITickableTileEntity {

	private final DirectionalLazyOptionalCache<CableEnergyStorage> energyCap;

	// 1 or 3 is in. 2 or 3 is out. D-U-N-S-W-E
	protected final int[] roundrobin;
	private int recursionDepth;

	public List<Connection> connectors = new ArrayList<>();
	public List<BlockPos> cache = new ArrayList<>();
	public List<Direction> dirs = new ArrayList<>();

	public CableTileEntity(TileEntityType<?> type) {
		super(type);
		this.energyCap = new DirectionalLazyOptionalCache<>();
		this.roundrobin = new int[Direction.values().length];
		revalidate();
	}
	
	public void revalidate() {
		BlockUtils.DIRECTIONS.forEach(dir -> {
			energyCap.revalidate(dir, s -> true, (s) -> new CableEnergyStorage(this, s));
		});
	}

	public CableTileEntity() {
		this(TileEntityTypesInit.CABLE.get());
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;
		BlockUtils.DIRECTIONS.forEach(dir -> {
			energyCap.get(dir).ifPresent(CableEnergyStorage::tick);
		});
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

	public int getRate() {
		return 2000;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction d) {
		if (cap == CapabilityEnergy.ENERGY)
			return energyCap.get(d).cast();

		return super.getCapability(cap, d);
	}

	@Override
	protected void invalidateCaps() {
		super.invalidateCaps();
		this.energyCap.invalidate();
	}

	@Override
	public void setRemoved() {
		this.energyCap.invalidate();
		super.setRemoved();
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		ListNBT cons = new ListNBT();
		this.connectors.forEach(pos -> {
			CompoundNBT posnbt = new CompoundNBT();
			posnbt.put("connector", pos.save());
			cons.add(posnbt);
		});
		nbt.put("connectors", cons);

		ListNBT sides = new ListNBT();
		this.dirs.forEach(dir -> {
			CompoundNBT posnbt = new CompoundNBT();
			posnbt.putInt("dir", dir.get3DDataValue());
			sides.add(posnbt);
		});
		nbt.put("dirs", sides);
		return super.save(nbt);
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		connectors = new ArrayList<>();
		dirs = new ArrayList<>();

		ListNBT cons = nbt.getList("connectors", Constants.NBT.TAG_COMPOUND);
		for (int j = 0; j < cons.size(); j++) {
			connectors.add(Connection.load(cons.getCompound(j).getCompound("connector")));
		}

		ListNBT sides = nbt.getList("dirs", Constants.NBT.TAG_COMPOUND);
		for (int j = 0; j < sides.size(); j++) {
			dirs.add(Direction.from3DDataValue(sides.getCompound(j).getInt("dir")));
		}
		energyCap.revalidate(a -> true, s -> new CableEnergyStorage(this, s));
		super.load(state, nbt);
	}

	public void search(Block block) {
		if (this.level != null) {
			this.cache.add(this.getBlockPos());
			for (Direction direction : Direction.values()) {
				BlockPos blockPos = this.getBlockPos().relative(direction);
				BlockState state = this.level.getBlockState(blockPos);
				if (state.getBlock() == block) {
					TileEntity tile1 = this.level.getBlockEntity(blockPos);
					if (tile1 instanceof CableTileEntity) {
						connectors.add(new Connection(this.worldPosition, direction, 0));
					}
					CableBlock cableBlock = (CableBlock) state.getBlock();
					cableBlock.searchCables(this.level, blockPos, this, 0);
				}
			}
		}
		this.cache.clear();
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

		public Connection(BlockPos pos, Direction direction, int distance) {
			this.pos = pos;
			this.direction = direction;
			this.distance = distance;
		}

		public int getDistance() {
			return distance;
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

		public CompoundNBT save() {
			CompoundNBT nbt = new CompoundNBT();
			nbt.put("CPos", NBTUtil.writeBlockPos(pos));
			nbt.putInt("CDir", direction.get3DDataValue());
			nbt.putInt("CDis", distance);
			return nbt;
		}

		public static Connection load(CompoundNBT nbt) {
			BlockPos p = NBTUtil.readBlockPos(nbt.getCompound("CPos"));
			Direction d = Direction.from3DDataValue(nbt.getInt("CDir"));
			int dist = nbt.getInt("CDis");
			return new Connection(p, d, dist);
		}
	}
}
