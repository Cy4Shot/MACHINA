package com.machina.block.tile;

import com.machina.energy.EnergyDefinition;
import com.machina.util.server.BlockUtils;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class EnergyTileEntity extends TileEntity implements ITickableTileEntity {

	protected final EnergyDefinition energyDef;
	private final LazyOptional<IEnergyStorage> energyCap;

	protected final IIntArray data = new IIntArray() {
		public int get(int index) {
			return EnergyTileEntity.this.sides[index];
		}

		public void set(int index, int value) {
			EnergyTileEntity.this.sides[index] = value;
		}

		@Override
		public int getCount() {
			return EnergyTileEntity.this.sides.length;
		}
	};

	// 1 or 3 is in. 2 or 3 is out. D-U-N-S-W-E
	public int[] sides = new int[] { 3, 3, 3, 3, 3, 3 };

	public EnergyTileEntity(TileEntityType<?> type, EnergyDefinition storage) {
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
		for (Direction dir : BlockUtils.DIRECTIONS)
			if (sides[dir.get3DDataValue()] >= 2)
				transfer(dir, dir.getOpposite());
	}

	protected void recieveAll() {
		for (Direction dir : BlockUtils.DIRECTIONS)
			if ((sides[dir.get3DDataValue()] + 1) % 2 == 0)
				transfer(dir, dir.getOpposite());
	}

	private void transfer(Direction d1, Direction d2) {
		TileEntity te = level.getBlockEntity(worldPosition.relative(d1));
		if (te != null) {
			te.getCapability(CapabilityEnergy.ENERGY, d2).ifPresent(e -> {
				if (e.canReceive() && e.getEnergyStored() < e.getMaxEnergyStored()) {
					energyDef.extractEnergy(e.receiveEnergy(energyDef.getOutput(), false), false);
				}
			});
		}
	}

	protected void recieve(Direction d1, Direction d2) {
		TileEntity te = level.getBlockEntity(worldPosition.relative(d1));
		if (te != null) {
			te.getCapability(CapabilityEnergy.ENERGY, d2).ifPresent(e -> {
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

	@Override
	public CompoundNBT getUpdateTag() {
		return this.save(new CompoundNBT());
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.save(nbt);
		return new SUpdateTileEntityPacket(this.getBlockPos(), 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.load(this.getBlockState(), pkt.getTag());
	}

	public IIntArray getData() {
		return this.data;
	}

}
