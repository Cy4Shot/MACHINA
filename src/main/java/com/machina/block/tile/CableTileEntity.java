package com.machina.block.tile;

import java.util.ArrayList;
import java.util.List;

import com.machina.block.CableBlock;
import com.machina.block.tile.base.BaseEnergyTileEntity;
import com.machina.energy.MachinaEnergyStorage;
import com.machina.registration.init.TileEntityTypesInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class CableTileEntity extends BaseEnergyTileEntity {

	public List<BlockPos> connectors = new ArrayList<>();
	public List<BlockPos> cache = new ArrayList<>();
	public List<Direction> dirs = new ArrayList<>();

	public CableTileEntity(TileEntityType<?> type) {
		super(type);

		sides = new int[] { 3, 3, 3, 3, 3, 3 };
	}

	public CableTileEntity() {
		this(TileEntityTypesInit.CABLE.get());
	}

	@Override
	public MachinaEnergyStorage createStorage() {
		return new MachinaEnergyStorage(this, 1000, 1000, 1000);
	}

	@Override
	public void tick() {
		super.tick();
	}

	public List<LazyOptional<IEnergyStorage>> storages() {
		List<LazyOptional<IEnergyStorage>> tes = new ArrayList<>();
		for (Direction direction : dirs) {
			if (!canTransfer(direction))
				continue;
			final TileEntity te = this.level.getBlockEntity(this.worldPosition.relative(direction));
			if (te == null || !(te instanceof BaseEnergyTileEntity))
				continue;

			final BaseEnergyTileEntity ete = (BaseEnergyTileEntity) te;
			if (!ete.canRecieve(direction.getOpposite()))
				continue;

			tes.add(ete.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()));
		}
		return tes;
	}

	@Override
	public void outputEnergy() {
		if (this.energyDef.getEnergyStored() >= this.energyDef.getMaxExtract() && this.energyDef.canExtract()) {
			
			List<LazyOptional<IEnergyStorage>> tes = storages();
			this.connectors.forEach(connector -> {
				final TileEntity te = this.level.getBlockEntity(connector);
				if (te != null && te instanceof CableTileEntity)
					tes.addAll(((CableTileEntity) te).storages());
			});

			if (tes.size() == 0)
				return;

			int extract = this.energyDef.getMaxExtract() / tes.size();

			for (LazyOptional<IEnergyStorage> s : tes) {
				s.ifPresent(storage -> {
					if (storage.getEnergyStored() < storage.getMaxEnergyStored()) {
						final int toSend = CableTileEntity.this.energyDef.extractEnergy(extract, false);
						final int received = storage.receiveEnergy(toSend, false);

						CableTileEntity.this.energyDef
								.setEnergy(CableTileEntity.this.energyDef.getEnergyStored() + toSend - received);
					}
				});
			}
		}
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
						connectors.add(blockPos);
					}
					CableBlock cableBlock = (CableBlock) state.getBlock();
					cableBlock.searchCables(this.level, blockPos, this);
				}
			}
		}
		this.cache.clear();
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		CompoundNBT compound = super.save(nbt);

		ListNBT cons = new ListNBT();
		this.connectors.forEach(pos -> {
			CompoundNBT posnbt = new CompoundNBT();
			posnbt.put("connector", NBTUtil.writeBlockPos(pos));
			cons.add(posnbt);
		});
		compound.put("connectors", cons);

		ListNBT sides = new ListNBT();
		this.dirs.forEach(dir -> {
			CompoundNBT posnbt = new CompoundNBT();
			posnbt.putInt("dir", dir.get3DDataValue());
			sides.add(posnbt);
		});
		compound.put("dirs", sides);

		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		connectors = new ArrayList<>();
		dirs = new ArrayList<>();

		super.load(state, nbt);

		ListNBT cons = nbt.getList("connectors", Constants.NBT.TAG_COMPOUND);
		for (int j = 0; j < cons.size(); j++) {
			connectors.add(NBTUtil.readBlockPos(cons.getCompound(j).getCompound("connector")));
		}

		ListNBT sides = nbt.getList("dirs", Constants.NBT.TAG_COMPOUND);
		for (int j = 0; j < sides.size(); j++) {
			dirs.add(Direction.from3DDataValue(sides.getCompound(j).getInt("dir")));
		}
	}
}
