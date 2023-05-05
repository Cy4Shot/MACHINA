package com.machina.block.tile.machine;

import java.util.function.Predicate;

import com.machina.block.container.MixerContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.MachinaTileEntity;
import com.machina.capability.energy.IEnergyTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.capability.fluid.MachinaFluidStorage;
import com.machina.capability.fluid.MachinaTank;
import com.machina.capability.inventory.MachinaItemStorage;
import com.machina.recipe.impl.MixerRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.MachinaRL;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class MixerTileEntity extends MachinaTileEntity
		implements ITickableTileEntity, IMachinaContainerProvider, IEnergyTileEntity {

	public MixerTileEntity() {
		super(TileEntityInit.MIXER.get());
	}

	MachinaEnergyStorage energy;
	MachinaFluidStorage fluid;
	MachinaItemStorage items;

	public ResourceLocation rec = new MachinaRL("empty");

	public Predicate<FluidStack> exclusiveTank(int id) {
		return f -> {
			for (MachinaTank tank : fluid.tanks()) {
				if (tank.id != id && tank.getFluid().isFluidEqual(f))
					return false;
			}
			return true;
		};
	}

	@Override
	public void createStorages() {
		this.energy = add(new MachinaEnergyStorage(this, 100000, 100));
		this.fluid = add(new MachinaTank(this, 10000, exclusiveTank(0), false, 0),
				new MachinaTank(this, 10000, exclusiveTank(1), false, 1),
				new MachinaTank(this, 10000, f -> false, true, 2), new MachinaTank(this, 10000, f -> false, true, 3));
		this.items = add(new MachinaItemStorage(3));
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		for (IRecipe<?> r : RecipeInit.getRecipes(RecipeInit.MIXER_RECIPE, level.getRecipeManager()).values()) {
			MixerRecipe recipe = (MixerRecipe) r;

			// Check Recipe
			if (!contains(recipe.fluidsIn, true))
				continue;
			if (!recipe.itemIn.isEmpty() && !items.getStackInSlot(0).getItem().equals(recipe.itemIn.getItem()))
				continue;
			if (!recipe.catalyst.isEmpty() && !items.getStackInSlot(1).getItem().equals(recipe.catalyst.getItem()))
				continue;

			// Recipe Found
			this.rec = recipe.getId();

			// Check space and power
			if (outputsFull())
				return;
			if (!hasPower())
				return;

			// Consume
			for (FluidStack stack : recipe.fluidsIn) {
				fluid.drainRaw(stack, FluidAction.EXECUTE);
			}
			this.energy.consumeEnergy(recipe.power);
			if (!items.getStackInSlot(1).isEmpty()) {
				items.getStackInSlot(1).setDamageValue(items.getStackInSlot(1).getDamageValue() + 1);
				if (items.getStackInSlot(1).getDamageValue() >= items.getStackInSlot(1).getMaxDamage()) {
					items.getStackInSlot(1).shrink(1);
				}
			}
			if (!items.getStackInSlot(0).isEmpty()) {
				items.getStackInSlot(0).shrink(1);
			}

			// Output
			fillOutputs(recipe.fluidsOut, FluidAction.EXECUTE);
			if (!recipe.itemOut.isEmpty()) {
				if (items.getStackInSlot(2).isEmpty()) {
					items.setStackInSlot(2, recipe.itemOut.copy());
				} else {
					items.getStackInSlot(2).grow(recipe.itemOut.copy().getCount());
				}
			}
			setChanged();
			return;
		}

		this.rec = new MachinaRL("empty");
		setChanged();
	}

	public boolean outputsFull() {
		MixerRecipe recipe = (MixerRecipe) RecipeInit.getRecipes(RecipeInit.MIXER_RECIPE, level.getRecipeManager())
				.get(this.rec);
		if (!fillOutputs(recipe.fluidsOut, FluidAction.SIMULATE))
			return true;
		if (!recipe.itemOut.isEmpty() && !items.getStackInSlot(2).isEmpty()) {
			if (items.getStackInSlot(2).getItem().equals(recipe.itemOut.getItem())
					&& items.getStackInSlot(2).getMaxStackSize() - items.getStackInSlot(2).getCount() > recipe.itemOut
							.getCount()) {
				return true;
			} else if (!items.getStackInSlot(2).getItem().equals(recipe.itemOut.getItem())) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPower() {
		MixerRecipe recipe = (MixerRecipe) RecipeInit.getRecipes(RecipeInit.MIXER_RECIPE, level.getRecipeManager())
				.get(this.rec);
		return energy.getEnergyStored() >= recipe.power;
	}

	public boolean fillOutputs(NonNullList<FluidStack> fluids, FluidAction action) {
		for (FluidStack stack : fluids) {
			if (!fillOutput(stack, action))
				return false;
		}
		return true;
	}

	public boolean fillOutput(FluidStack f, FluidAction action) {
		for (int i = 0; i < fluid.getTanks(); i++) {
			MachinaTank t = fluid.tank(i);
			if (t.output && t.rawFill(f, action) == f.getAmount()) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(NonNullList<FluidStack> fluids, boolean amount) {
		for (FluidStack stack : fluids) {
			if (!anyTankContains(stack, amount))
				return false;
		}
		return true;
	}

	public boolean anyTankContains(FluidStack stack, boolean amount) {
		for (int i = 0; i < fluid.getTanks(); i++) {
			if (tankContains(fluid.tank(i), stack, amount))
				return true;
		}
		return false;
	}

	public boolean tankContains(MachinaTank tank, FluidStack stack, boolean amount) {
		if (!tank.getFluid().isFluidEqual(stack))
			return false;
		if (tank.getFluid().getAmount() < stack.getAmount() && amount)
			return false;
		return true;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new MixerContainer(windowId, inv, this);
	}

	@Override
	public boolean isGeneratorMode() {
		return false;
	}

	@Override
	public CompoundNBT save(CompoundNBT tag) {
		tag.putString("rec", this.rec.toString());
		return super.save(tag);
	}

	@Override
	public void load(BlockState state, CompoundNBT tag) {
		this.rec = new ResourceLocation(tag.getString("rec"));
		super.load(state, tag);
	}
}
