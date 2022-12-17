package com.machina.block.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.machina.block.AtmosphericSeparatorBlock;
import com.machina.block.container.AtmosphericSeparatorContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.BaseTileEntity;
import com.machina.block.tile.base.IFluidTileEntity;
import com.machina.capability.fluid.MachinaTank;
import com.machina.planet.PlanetData;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.TileEntityInit;
import com.machina.world.data.StarchartData;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class AtmosphericSeparatorTileEntity extends BaseTileEntity
		implements IMachinaContainerProvider, IFluidTileEntity, ITickableTileEntity {

	public int selected = -1;
	public float rate = 0;

	private MachinaTank tank = new MachinaTank(this, 0, p -> true, true, 0);
	private final LazyOptional<IFluidHandler> cap = LazyOptional.of(() -> tank);

	protected final IIntArray data = new IIntArray() {
		public int get(int index) {
			switch (index) {
			case 0:
				return selected;
			default:
				return 0;
			}
		}

		public void set(int index, int value) {
			switch (index) {
			case 0:
				selected = value;
				break;
			}
		}

		@Override
		public int getCount() {
			return 1;
		}
	};

	public AtmosphericSeparatorTileEntity() {
		super(TileEntityInit.ATMOSPHERIC_SEPARATOR.get());
	}

	public void setId(int id) {
		this.selected = id;
		RegistryKey<World> dim = this.level.dimension();
		PlanetData data = StarchartData.getDataOrNone(dim);
		Double atm = data.getAtmosphere(dim)[id];
		this.rate = 0.02f * (float) atm.doubleValue();

		tank.setCapacity((int) (rate * BUCKET));
		if (stored() > capacity()) {
			getFluid().setAmount(capacity());
		}
		this.sync();
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new AtmosphericSeparatorContainer(id, this);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound.putInt("selected", this.selected);
		compound.putFloat("rate", this.rate);
		tank.writeToNBT(compound);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		this.selected = compound.getInt("selected");
		this.rate = compound.getFloat("rate");
		tank.readFromNBT(compound);
		super.load(state, compound);
	}

	public IIntArray getData() {
		return this.data;
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
	public void tick() {
		if (level.isClientSide() || selected == -1)
			return;

		Fluid fluid = FluidInit.ATMOSPHERE.get(selected).fluid();

		tank.setFluid(new FluidStack(fluid, capacity() - stored()));

		Direction facing = getBlockState().getValue(AtmosphericSeparatorBlock.FACING);
		TileEntity te = getLevel().getBlockEntity(getBlockPos().relative(facing));
		if (te == null)
			return;

		IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite())
				.orElse(null);

		if (handler != null && handler.getFluidInTank(0) != null) {
			FluidStack toFill = new FluidStack(fluid, (int) (rate * BUCKET));

			tank.drain(handler.fill(toFill, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
		}
	}
}