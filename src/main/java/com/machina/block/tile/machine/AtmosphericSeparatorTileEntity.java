package com.machina.block.tile.machine;

import com.machina.block.AtmosphericSeparatorBlock;
import com.machina.block.container.AtmosphericSeparatorContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.MachinaTileEntity;
import com.machina.capability.fluid.MachinaFluidStorage;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class AtmosphericSeparatorTileEntity extends MachinaTileEntity
		implements IMachinaContainerProvider, ITickableTileEntity {

	public int selected = -1;
	public float rate = 0;

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

	MachinaFluidStorage fluid;

	@Override
	public void createStorages() {
		this.fluid = add(new MachinaTank(this, 0, p -> true, true, 0));
	}

	public void setId(int id) {
		this.selected = id;
		RegistryKey<World> dim = this.level.dimension();
		PlanetData data = StarchartData.getDataOrNone(dim);
		Double atm = data.getAtmosphere(dim)[id];
		this.rate = 0.02f * (float) atm.doubleValue();

		fluid.tank(0).setCapacity((int) (rate * 1000));
		if (fluid.getFluidInTank(0).getAmount() > fluid.getTankCapacity(0)) {
			fluid.getFluidInTank(0).setAmount(fluid.getTankCapacity(0));
		}
		this.setChanged();
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new AtmosphericSeparatorContainer(id, this);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound.putInt("selected", this.selected);
		compound.putFloat("rate", this.rate);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		this.selected = compound.getInt("selected");
		this.rate = compound.getFloat("rate");
		super.load(state, compound);
	}

	public IIntArray getData() {
		return this.data;
	}

	@Override
	public void tick() {
		if (level.isClientSide() || selected == -1)
			return;

		Fluid fl = FluidInit.ATMOSPHERE.get(selected).fluid();

		fluid.setFluidInTank(0, new FluidStack(fl, fluid.getTankCapacity(0) - fluid.getFluidInTank(0).getAmount()));

		Direction facing = getBlockState().getValue(AtmosphericSeparatorBlock.FACING);
		TileEntity te = getLevel().getBlockEntity(getBlockPos().relative(facing));
		if (te == null)
			return;

		IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite())
				.orElse(null);

		if (handler != null && handler.getFluidInTank(0) != null) {
			FluidStack toFill = new FluidStack(fl, (int) (rate * 1000));

			fluid.drain(handler.fill(toFill, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
		}
	}
}