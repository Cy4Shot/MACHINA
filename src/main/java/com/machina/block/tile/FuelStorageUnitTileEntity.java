package com.machina.block.tile;

import java.util.Random;

import com.machina.block.container.FuelStorageUnitContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.CustomTE;
import com.machina.block.tile.base.IHeatTileEntity;
import com.machina.capability.MachinaTank;
import com.machina.capability.CustomFluidStorage;
import com.machina.capability.CustomItemStorage;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.server.HeatHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.fluids.FluidStack;

public class FuelStorageUnitTileEntity extends CustomTE
		implements IHeatTileEntity, IMachinaContainerProvider, ITickableTileEntity {

	public float heat = 0;
	public static float maxTemp = 180f;

	public FuelStorageUnitTileEntity() {
		super(TileEntityInit.FUEL_STORAGE_UNIT.get());
	}

	CustomItemStorage items;
	CustomFluidStorage fluids;

	@Override
	public void createStorages() {
		this.items = add(new CustomItemStorage(2));
		this.fluids = add(new MachinaTank(this, 100000, p -> p.getFluid().isSame(Fluids.WATER), false, 0));
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		float target = HeatHelper.calculateTemperatureRegulators(worldPosition, level);
		heat = HeatHelper.limitHeat(heat + (target - heat) * 0.05f, level.dimension());

		// Deplete
		if (normalizedHeat() > maxTemp) {
			if (!fluids.isEmpty())
				fluids.getFluidInTank(0).setAmount(stored() - 30);
			Random r = new Random();
			if (r.nextInt(50) == 0) {
				items.getStackInSlot(0).shrink(1);
			}
			if (r.nextInt(50) == 0) {
				items.getStackInSlot(1).shrink(1);
			}
		}

		sync();
	}

	public int stored() {
		return fluids.getFluidInTank(0).getAmount();
	}

	public int capacity() {
		return fluids.getTankCapacity(0);
	}

	public float propFull() {
		return (float) stored() / (float) capacity();
	}

	public FluidStack getFluid() {
		return fluids.getFluidInTank(0);
	}

	@Override
	public float getHeat() {
		return heat;
	}

	public float heatFull() {
		return HeatHelper.propFull(normalizedHeat(), this.level.dimension());
	}

	public float normalizedHeat() {
		return HeatHelper.normalizeHeat(heat, this.level.dimension());
	}

	@Override
	public boolean isGenerator() {
		return false;
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putFloat("Heat", this.heat);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.heat = compound.getFloat("Heat");
	}

	@Override
	public Container createMenu(int id, PlayerInventory pPlayer, PlayerEntity e) {
		return new FuelStorageUnitContainer(id, pPlayer, this);
	}
}