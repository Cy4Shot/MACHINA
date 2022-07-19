package com.machina.block.tile;

import com.machina.block.AtmosphericSeperatorBlock;
import com.machina.block.container.AtmosphericSeperatorContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.BaseTileEntity;
import com.machina.block.tile.base.IFluidTileEntity;
import com.machina.capability.fluid.MachinaTank;
import com.machina.registration.init.AttributeInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.server.FluidUtils;
import com.machina.util.server.ServerHelper;
import com.machina.world.data.PlanetData;
import com.machina.world.data.StarchartData;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class AtmosphericSeperatorTileEntity extends BaseTileEntity
		implements IMachinaContainerProvider, IFluidTileEntity, ITickableTileEntity {

	public int selected = -1;
	public float rate = 0;

//	public final MachinaTank tank = new MachinaTank(this, getCapacity(), p -> true);
//	private final LazyOptional<IFluidHandler> fluidCap = LazyOptional.of(() -> tank);

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

	public AtmosphericSeperatorTileEntity() {
		super(TileEntityInit.ATMOSPHERIC_SEPERATOR.get());
	}

	public void setId(int id) {
		this.selected = id;
		PlanetData data = StarchartData.getDataOrNone(ServerHelper.server(), this.level.dimension());
		Double atm = data.getAttribute(AttributeInit.ATMOSPHERE)[id];
		this.rate = 0.02f * (float) atm.doubleValue();
		this.sync();
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new AtmosphericSeperatorContainer(id, this);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound.putInt("selected", this.selected);
		compound.putFloat("rate", this.rate);
//		write(compound);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		this.selected = compound.getInt("selected");
		this.rate = compound.getFloat("rate");
//		read(compound);
		super.load(state, compound);
	}

	public IIntArray getData() {
		return this.data;
	}

//	@Override
//	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
//		return cast(cap, side, (c, s) -> super.getCapability(c, s));
//	}
//
//	@Override
//	protected void invalidateCaps() {
//		invalidate();
//		super.invalidateCaps();
//	}
//
//	@Override
//	public int getCapacity() {
//		return getTransfer();
//	}
//
//	@Override
//	public int getTransfer() {
//		return (int) (rate * BUCKET);
//	}
//
//	@Override
//	public MachinaTank getTank() {
//		return tank;
//	}
//
//	@Override
//	public LazyOptional<IFluidHandler> getCap() {
//		return fluidCap;
//	}
//
//	@Override
//	public BaseTileEntity getTe() {
//		return this;
//	}

	@Override
	public void tick() {
		if (level.isClientSide() || selected == -1)
			return;

//		tank.setFluid(new FluidStack(FluidInit.ATMOSPHERE.get(selected).fluid(), getCapacity() - getFluidAmount()));

		Direction facing = getBlockState().getValue(AtmosphericSeperatorBlock.FACING);
		TileEntity te = getLevel().getBlockEntity(getBlockPos().relative(facing));
		if (te != null) {
			if (FluidUtils.hasFluid(te, facing)) {
//				FluidUtils.tryFill(level, worldPosition, facing, tank, getTransfer());
			}
		}
	}

	@Override
	public void setFluid(FluidStack fluid) {
		// TODO Auto-generated method stub
		
	}
}