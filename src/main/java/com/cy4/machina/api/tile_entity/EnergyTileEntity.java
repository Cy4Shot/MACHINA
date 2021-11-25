package com.cy4.machina.api.tile_entity;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cy4.machina.api.inventory.IEnergyHolderTile;
import com.cy4.machina.tile_entity.util.MachinaEnergyStorage;
import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class EnergyTileEntity extends BaseTileEntity implements IEnergyHolderTile, ITickableTileEntity {

	protected final MachinaEnergyStorage energyStorage = createEnergyStorage();
	protected final LazyOptional<IEnergyStorage> energyOptional = LazyOptional.of(() -> energyStorage);

	protected EnergyTileEntity(TileEntityType<?> p_i48289_1_) {
		super(p_i48289_1_);
	}

	protected abstract MachinaEnergyStorage createEnergyStorage();

	@Override
	public MachinaEnergyStorage getStorage() { return energyStorage; }

	protected List<Direction> getSidesForEnergy() { return Lists.newArrayList(Direction.values()); }

	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityEnergy.ENERGY && (getSidesForEnergy().contains(side) || side == null)) {
			return energyOptional.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void tick() {
		if (level.isClientSide()) {
			clientTick();
		} else {
			serverTick();
		}
	}

	protected void serverTick() {

	}

	protected void clientTick() {

	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		nbt.put("energy", energyStorage.serialize());
		return nbt;
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		energyStorage.deserialize(nbt.getCompound("energy"));
	}
}
