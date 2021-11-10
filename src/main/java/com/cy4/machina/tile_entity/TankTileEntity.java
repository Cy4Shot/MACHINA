package com.cy4.machina.tile_entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cy4.machina.api.tile_entity.BaseTileEntity;
import com.cy4.machina.init.TileEntityTypesInit;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TankTileEntity extends BaseTileEntity {

	protected FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 5);
	private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

	public TankTileEntity() {
		super(TileEntityTypesInit.TANK_TILE_ENTITY_TYPE);
	}

	@Override
	public void load(BlockState state, CompoundNBT tag) {
		super.load(state, tag);
		tank.readFromNBT(tag);
	}

	@Override
	public CompoundNBT save(CompoundNBT tag) {
		tag = super.save(tag);
		tank.writeToNBT(tag);
		return tag;
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return holder.cast();
		return super.getCapability(capability, facing);
	}

}
