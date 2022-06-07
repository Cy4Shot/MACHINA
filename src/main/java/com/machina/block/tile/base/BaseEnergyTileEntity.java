package com.machina.block.tile.base;

import java.util.concurrent.atomic.AtomicInteger;

import com.machina.energy.MachinaEnergyStorage;

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

	protected final MachinaEnergyStorage energyDef;
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

	public BaseEnergyTileEntity(TileEntityType<?> type) {
		super(type);
		this.energyDef = createStorage();
		this.energyCap = LazyOptional.of(() -> this.energyDef);
	}

	public abstract MachinaEnergyStorage createStorage();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction d) {
		if (cap == CapabilityEnergy.ENERGY)
			return energyCap.cast();

		return super.getCapability(cap, d);
	}

	@Override
	protected void invalidateCaps() {
		super.invalidateCaps();
		this.energyCap.invalidate();
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

	public boolean canRecieve(Direction dir) {
		return (sides[dir.get3DDataValue()] + 1) % 2 == 0;
	}

	public boolean canTransfer(Direction dir) {
		return sides[dir.get3DDataValue()] > 1;
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;
		sendOutPower();
	}

	protected void sendOutPower() {
		AtomicInteger capacity = new AtomicInteger(energyDef.getEnergyStored());
		if (capacity.get() > 0) {
			for (Direction direction : Direction.values()) {
				if (!canTransfer(direction))
					continue;
				TileEntity te = level.getBlockEntity(worldPosition.relative(direction));
				if (te != null) {
					boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
						if (handler.canReceive()) {
							int received = handler.receiveEnergy(Math.min(capacity.get(), energyDef.getMaxExtract()),
									false);
							capacity.addAndGet(-received);
							energyDef.consumeEnergy(received);
							setChanged();
							return capacity.get() > 0;
						} else {
							return true;
						}
					}).orElse(true);
					if (!doContinue) {
						return;
					}
				}
			}
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
