package com.machina.block.tile.machine;

import com.machina.block.container.MelterContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.MachinaTileEntity;
import com.machina.capability.energy.IEnergyTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.capability.fluid.MachinaFluidStorage;
import com.machina.capability.fluid.MachinaTank;
import com.machina.capability.inventory.MachinaItemStorage;
import com.machina.recipe.impl.MelterRecipe;
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
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class MelterTileEntity extends MachinaTileEntity
		implements ITickableTileEntity, IMachinaContainerProvider, IEnergyTileEntity {

	public MelterTileEntity() {
		super(TileEntityInit.MELTER.get());
	}

	MachinaEnergyStorage energy;
	MachinaFluidStorage fluid;
	MachinaItemStorage items;

	public ResourceLocation rec = new MachinaRL("empty");
	public int progress;

	protected final IIntArray data = new IIntArray() {
		public int get(int index) {
			switch (index) {
			case 0:
				return MelterTileEntity.this.progress;
			default:
				return 0;
			}
		}

		public void set(int index, int value) {
			switch (index) {
			case 0:
				MelterTileEntity.this.progress = value;
				break;
			}
		}

		@Override
		public int getCount() {
			return 1;
		}
	};

	public IIntArray getData() {
		return data;
	}

	@Override
	public void createStorages() {
		this.energy = add(new MachinaEnergyStorage(this, 100000, 1000));
		this.fluid = add(new MachinaTank(this, 10000, f -> false, true, 0));
		this.items = add(new MachinaItemStorage(1));
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		boolean found = false;
		for (IRecipe<?> r : RecipeInit.getRecipes(RecipeInit.MELTER_RECIPE, level.getRecipeManager()).values()) {
			MelterRecipe recipe = (MelterRecipe) r;

			if (!items.getStackInSlot(0).getItem().equals(recipe.input.getItem())
					|| items.getStackInSlot(0).getCount() < recipe.input.getCount())
				continue;

			found = true;
			this.rec = recipe.getId();
			break;

		}
		if (!found) {
			this.rec = new MachinaRL("empty");
		} else if (hasPower() && hasSpace()) {
			MelterRecipe recipe = (MelterRecipe) RecipeInit
					.getRecipes(RecipeInit.MELTER_RECIPE, level.getRecipeManager()).get(this.rec);
			this.progress++;
			energy.consumeEnergy(recipe.power / getCookTime());
			if (this.progress == getCookTime()) {
				items.getStackInSlot(0).shrink(1);
				fluid.fillRaw(recipe.output, FluidAction.EXECUTE);
				this.progress = 0;
				setChanged();

			}
		}
		setChanged();
	}
	
	public int getCookTime() {
		return 100;
	}

	public boolean hasPower() {
		MelterRecipe recipe = (MelterRecipe) RecipeInit.getRecipes(RecipeInit.MELTER_RECIPE, level.getRecipeManager())
				.get(this.rec);
		return energy.getEnergyStored() >= recipe.power;
	}

	public boolean hasSpace() {
		if (this.rec.equals(new MachinaRL("empty"))) {
			return true;
		}
		MelterRecipe recipe = (MelterRecipe) RecipeInit.getRecipes(RecipeInit.MELTER_RECIPE, level.getRecipeManager())
				.get(this.rec);
		return fluid.fillRaw(recipe.output, FluidAction.SIMULATE) > 0;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new MelterContainer(windowId, inv, this);
	}

	@Override
	public boolean isGeneratorMode() {
		return false;
	}

	@Override
	public CompoundNBT save(CompoundNBT tag) {
		tag.putString("rec", this.rec.toString());
		tag.putInt("progress", this.progress);
		return super.save(tag);
	}

	@Override
	public void load(BlockState state, CompoundNBT tag) {
		this.rec = new ResourceLocation(tag.getString("rec"));
		this.progress = tag.getInt("progress");
		super.load(state, tag);
	}

	public FluidStack getFluid(int id) {
		return (id == 0 ? fluid.tank(0) : fluid.tank(1)).getFluid();
	}

	public int stored(int id) {
		return (id == 0 ? fluid.tank(0) : fluid.tank(1)).getFluidAmount();
	}

	public int capacity(int id) {
		return (id == 0 ? fluid.tank(0) : fluid.tank(1)).getCapacity();
	}

	public float propFull(int id) {
		return (float) stored(id) / (float) capacity(id);
	}
}
