package com.machina.block.tile.base;

import com.machina.capability.energy.IEnergyContainer;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.util.math.DirectionUtil;
import com.machina.util.server.EnergyHelper;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class BaseEnergyTileEntity extends BaseTileEntity implements ITickableTileEntity, IEnergyContainer {

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
		if (cap == CapabilityEnergy.ENERGY) {
			if (d == null)
				return energyCap.cast();
			return energyDef.getCapability(cap, d);
		}

		return super.getCapability(cap, d);
	}

	@Override
	public MachinaEnergyStorage getStorage() {
		return this.energyDef;
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

	public Direction getDir() {
		return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
	}

	public int getSide(Direction side) {
		return sides[DirectionUtil.rotate(side, getDir()).get3DDataValue()];
	}

	public boolean canRecieve(Direction dir) {
		return (getSide(dir) & 1) != 0;
	}

	public boolean canTransfer(Direction dir) {
		return getSide(dir) > 1;
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;
		sendOutPower();
	}

	protected void sendOutPower() {
		DirectionUtil.DIRECTIONS.forEach(dir -> {
			if (canTransfer(dir))
				EnergyHelper.trySendTo(level, worldPosition, this, energyDef.getMaxExtract(), dir);
		});
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
