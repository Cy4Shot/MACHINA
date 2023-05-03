package com.machina.block.tile.multiblock.haber;

import java.util.stream.Collectors;

import com.machina.block.container.HaberContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.multiblock.MultiblockMasterTileEntity;
import com.machina.capability.energy.IEnergyTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.capability.fluid.MachinaFluidStorage;
import com.machina.capability.fluid.MachinaTank;
import com.machina.capability.inventory.MachinaItemStorage;
import com.machina.recipe.impl.HaberRecipe;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.text.MachinaRL;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
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
	HaberRecipe rec;

	@Override
	public void createStorages() {
		this.items = add(new MachinaItemStorage(1, (i, s) -> s.getItem().equals(ItemInit.IRON_CATALYST.get())));
		this.energy = add(new MachinaEnergyStorage(this, 1000000, 1000));
		this.fluid = add(new MachinaTank(this, 10000, s -> s.getFluid().isSame(FluidInit.METHANE.fluid()), false, 0),
				new MachinaTank(this, 10000, s -> s.getFluid().isSame(FluidInit.NITROGEN.fluid()), false, 1),
				new MachinaTank(this, 10000, s -> s.getFluid().isSame(Fluids.WATER.getFluid()), false, 2),
				new MachinaTank(this, 10000, s -> s.getFluid().isSame(FluidInit.LIQUID_AMMONIA.fluid()), true, 3));
	}

	public void validateRecipe() {
		if (rec == null) {
			this.rec = (HaberRecipe) RecipeInit.getRecipes(RecipeInit.HABER_RECIPE, level.getRecipeManager()).values()
					.stream().collect(Collectors.toList()).get(0);
		}
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		validateRecipe();

		if (!hasCatalyst() || !hasMethane() || !hasNitrogen() || !hasWater() || !hasOutput() || !hasPower()) {
			return;
		}

		// Consume
		fluid.drainRaw(rec.fluids.get(0), FluidAction.EXECUTE);
		fluid.drainRaw(rec.fluids.get(1), FluidAction.EXECUTE);
		fluid.drainRaw(rec.fluids.get(2), FluidAction.EXECUTE);
		this.energy.consumeEnergy(rec.power);
		items.getStackInSlot(0).setDamageValue(items.getStackInSlot(0).getDamageValue() + 1);
		if (items.getStackInSlot(0).getDamageValue() >= items.getStackInSlot(0).getMaxDamage()) {
			items.getStackInSlot(0).shrink(1);
		}

		// Output
		fluid.tank(3).rawFill(rec.fOut, FluidAction.EXECUTE);
		setChanged();
	}

	public boolean hasCatalyst() {
		validateRecipe();
		return !items.getStackInSlot(0).isEmpty();
	}

	public boolean hasMethane() {
		validateRecipe();
		return fluid.getFluidInTank(0).getAmount() >= rec.fluids.get(0).getAmount();
	}

	public boolean hasNitrogen() {
		validateRecipe();
		return fluid.getFluidInTank(1).getAmount() >= rec.fluids.get(2).getAmount();
	}

	public boolean hasWater() {
		validateRecipe();
		return fluid.getFluidInTank(2).getAmount() >= rec.fluids.get(1).getAmount();
	}

	public boolean hasOutput() {
		validateRecipe();
		return fluid.getFluidInTank(3).getAmount() + rec.fOut.getAmount() <= fluid.getTankCapacity(3);
	}

	public boolean hasPower() {
		validateRecipe();
		return energy.getEnergyStored() >= rec.power;
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
