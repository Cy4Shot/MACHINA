package com.machina.block.tile.multiblock.haber;

import com.machina.block.container.HaberContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.multiblock.MultiblockMasterTileEntity;
import com.machina.capability.energy.IEnergyTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.capability.fluid.MachinaFluidStorage;
import com.machina.capability.fluid.MachinaTank;
import com.machina.capability.inventory.MachinaItemStorage;
import com.machina.config.CommonConfig;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.text.MachinaRL;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class HaberControllerTileEntity extends MultiblockMasterTileEntity
		implements ITickableTileEntity, IEnergyTileEntity, IMachinaContainerProvider {

	public HaberControllerTileEntity() {
		this(TileEntityInit.HABER_CONTROLLER.get());
	}

	public HaberControllerTileEntity(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return new MachinaRL("haber");
	}

	MachinaItemStorage items;
	MachinaEnergyStorage energy;
	MachinaFluidStorage fluid;

	@Override
	public void createStorages() {
		this.items = add(new MachinaItemStorage(1, (i, s) -> s.getItem().equals(ItemInit.IRON_CATALYST.get())));
		this.energy = add(new MachinaEnergyStorage(this, 1000000, 1000));
		this.fluid = add(new MachinaTank(this, 10000, s -> s.getFluid().isSame(FluidInit.METHANE.fluid()), false, 0),
				new MachinaTank(this, 10000, s -> s.getFluid().isSame(FluidInit.NITROGEN.fluid()), false, 1),
				new MachinaTank(this, 10000, s -> s.getFluid().isSame(FluidInit.LIQUID_AMMONIA.fluid()), true, 2));
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		if (fluid.tank(2).isFull())
			return;
		if (items.getStackInSlot(0).isEmpty())
			return;
		if (fluid.getFluidInTank(0).getAmount() < CommonConfig.haberMethaneConsumeRate.get())
			return;
		if (fluid.getFluidInTank(1).getAmount() < CommonConfig.haberNitrogenConsumeRate.get())
			return;
		if (fluid.getFluidInTank(2).getAmount() + CommonConfig.haberAmmoniaOutputRate.get() > fluid.getTankCapacity(2))
			return;
		if (energy.getEnergyStored() < CommonConfig.haberPowerRate.get())
			return;

		// Consume
		fluid.drainRaw(new FluidStack(FluidInit.METHANE.fluid(), CommonConfig.haberMethaneConsumeRate.get()),
				FluidAction.EXECUTE);
		fluid.drainRaw(new FluidStack(FluidInit.NITROGEN.fluid(), CommonConfig.haberNitrogenConsumeRate.get()),
				FluidAction.EXECUTE);

		this.energy.consumeEnergy(CommonConfig.haberPowerRate.get());
		items.getStackInSlot(0).setDamageValue(items.getStackInSlot(0).getDamageValue() + 1);
		if (items.getStackInSlot(0).getDamageValue() >= items.getStackInSlot(0).getMaxDamage()) {
			items.getStackInSlot(0).shrink(1);
		}

		// Output
		fluid.fillRaw(new FluidStack(FluidInit.LIQUID_AMMONIA.flowing(), CommonConfig.haberAmmoniaOutputRate.get()),
				FluidAction.EXECUTE);
		setChanged();
	}

	@Override
	public boolean isGeneratorMode() {
		return false;
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new HaberContainer(id, inventory, this);
	}
}
