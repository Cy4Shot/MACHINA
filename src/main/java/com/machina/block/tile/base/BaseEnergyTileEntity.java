package com.machina.block.tile.base;

import com.machina.energy.EnergyDefinition;
import com.machina.util.server.BlockUtils;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class BaseEnergyTileEntity extends BaseTileEntity implements ITickableTileEntity {

	protected final EnergyDefinition energyDef;
	private final LazyOptional<IEnergyStorage> energyCap;

	protected final IIntArray data = new IIntArray() {
		public int get(int index) {
			return BaseEnergyTileEntity.this.sides[index];
		}

		public void set(int index, int value) {
			BaseEnergyTileEntity.this.sides[index] = value;
		}

		@Override
		public int getCount() {
			return BaseEnergyTileEntity.this.sides.length;
		}
	};

	// 1 or 3 is in. 2 or 3 is out. D-U-N-S-W-E
	public int[] sides = new int[] { 3, 3, 3, 3, 3, 3 };

	@Override
	public void tick() {
		recieveAll();
		transferAll();
	}

	public BaseEnergyTileEntity(TileEntityType<?> type, EnergyDefinition storage) {
		super(type);
		this.energyDef = storage;
		this.energyCap = LazyOptional.of(() -> storage);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction d) {
		if (cap == CapabilityEnergy.ENERGY)
			return energyCap.cast();

		return super.getCapability(cap, d);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		energyDef.save(nbt);
		nbt.putIntArray("Sides", sides);
		return super.save(nbt);
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		energyDef.load(nbt);
		sides = nbt.getIntArray("Sides");
		super.load(state, nbt);
	}

	protected void transferAll() {
		BlockUtils.DIRECTIONS.forEach(dir -> {
			if (canTransfer(dir)) {
				transfer(dir, dir.getOpposite());
			}
		});
	}

	protected void recieveAll() {
		BlockUtils.DIRECTIONS.forEach(dir -> {
			if (canRecieve(dir)) {
				recieve(dir, dir.getOpposite());
			}
		});
	}

	public boolean canRecieve(Direction dir) {
		return (sides[dir.get3DDataValue()] + 1) % 2 == 0;
	}

	public boolean canTransfer(Direction dir) {
		return sides[dir.get3DDataValue()] >= 2;
	}

	protected void transfer(Direction to, Direction from) {
		TileEntity te = level.getBlockEntity(worldPosition.relative(to));
		if (te != null) {
			te.getCapability(CapabilityEnergy.ENERGY, from).ifPresent(e -> {
				if (e.canReceive() && e.getEnergyStored() < e.getMaxEnergyStored()) {
					energyDef.extractEnergy(e.receiveEnergy(energyDef.getOutput(), false), false);
				}
			});
		}
	}

	protected void recieve(Direction from, Direction to) {
		TileEntity te = level.getBlockEntity(worldPosition.relative(from));
		if (te != null) {
			te.getCapability(CapabilityEnergy.ENERGY, to).ifPresent(e -> {
				if (e.canExtract() && e.getEnergyStored() > 0) {
					energyDef.receiveEnergy(e.extractEnergy(energyDef.getInput(), false), false);
				}
			});
		}
	}

	public int getEnergy() {
		return this.energyDef.getEnergyStored();
	}

	public void fillEnergy() {
		this.energyDef.receiveEnergy(this.energyDef.getMaxEnergyStored() - getEnergy(), false);
	}

	public int getMaxEnergy() {
		return this.energyDef.getMaxEnergyStored();
	}

	public float propFull() {
		return (float) this.getEnergy() / (float) this.getMaxEnergy();
	}

	public IIntArray getData() {
		return this.data;
	}

}
