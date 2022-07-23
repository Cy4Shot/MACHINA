package com.machina.block.tile;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.machina.block.container.FuelStorageUnitContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.BaseLockableTileEntity;
import com.machina.block.tile.base.IFluidTileEntity;
import com.machina.block.tile.base.IHeatTileEntity;
import com.machina.capability.fluid.MachinaTank;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.server.HeatUtils;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FuelStorageUnitTileEntity extends BaseLockableTileEntity
		implements IHeatTileEntity, IMachinaContainerProvider, IFluidTileEntity, ITickableTileEntity {

	private MachinaTank tank = new MachinaTank(this, 100000, p -> p.getFluid().isSame(Fluids.WATER), false, 0);
	private final LazyOptional<IFluidHandler> cap = LazyOptional.of(() -> tank);
	public float heat = 0;
	public static float maxTemp = -80f;

	public FuelStorageUnitTileEntity() {
		super(TileEntityInit.FUEL_STORAGE_UNIT.get(), 2);
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		float target = HeatUtils.calculateTemperatureRegulators(worldPosition, level, false);
		heat = HeatUtils.limitHeat(heat + (target - heat) * 0.05f, level.dimension());

		// Deplete
		if (heat > maxTemp) {
			if (!tank.isEmpty())
				tank.getFluid().setAmount(tank.getFluidAmount() - 30);
			Random r = new Random();
			if (r.nextInt(50) == 0) {
				this.getItem(0).shrink(1);
			}
			if (r.nextInt(50) == 0) {
				this.getItem(1).shrink(1);
			}
		}

		sync();
	}

	@Override
	public void setFluid(FluidStack fluid) {
		this.tank.setFluid(fluid);
	}

	@Override
	public FluidStack getFluid() {
		return this.tank.getFluid();
	}

	@Override
	public int stored() {
		return this.tank.getFluidAmount();
	}

	@Override
	public int capacity() {
		return this.tank.getCapacity();
	}

	public float propFull() {
		return (float) stored() / (float) capacity();
	}

	@Override
	public float getHeat() {
		return heat;
	}

	public float heatFull() {
		return HeatUtils.propFull(heat, this.level.dimension());
	}

	public float normalizedHeat() {
		return HeatUtils.normalizeHeat(heat, this.level.dimension());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> c, @Nullable Direction direction) {
		if (c == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return cap.cast();
		}

		return super.getCapability(c, direction);
	}

	@Override
	protected void invalidateCaps() {
		cap.invalidate();
		super.invalidateCaps();
	}

	@Override
	public boolean isGenerator() {
		return false;
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		tank.writeToNBT(compound);
		compound.putFloat("Heat", this.heat);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		tank.readFromNBT(compound);
		this.heat = compound.getFloat("Heat");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory pPlayer) {
		return new FuelStorageUnitContainer(id, pPlayer, this);
	}
}