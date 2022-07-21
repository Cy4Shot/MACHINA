package com.machina.block.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.machina.block.container.TankContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.BaseTileEntity;
import com.machina.block.tile.base.IFluidTileEntity;
import com.machina.capability.fluid.MachinaTank;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TankTileEntity extends BaseTileEntity
		implements IMachinaContainerProvider, ITickableTileEntity, IFluidTileEntity {

	private MachinaTank tank = new MachinaTank(this, 10000, p -> true, 0);
	private final LazyOptional<IFluidHandler> cap = LazyOptional.of(() -> tank);

	public TankTileEntity() {
		super(TileEntityInit.TANK.get());
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		tank.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		tank.readFromNBT(nbt);
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
	public void setFluid(FluidStack fluid) {
		tank.setFluid(fluid);
	}

	@Override
	public FluidStack getFluid() {
		return tank.getFluid();
	}

	@Override
	public int stored() {
		return tank.getFluidAmount();
	}

	@Override
	public int capacity() {
		return tank.getCapacity();
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new TankContainer(id, this);
	}
}