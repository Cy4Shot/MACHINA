package com.machina.block.tile;

import java.util.function.Predicate;

import com.machina.block.container.PressurizedChamberContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.CustomTE;
import com.machina.block.tile.base.IHeatTileEntity;
import com.machina.capability.CustomEnergyStorage;
import com.machina.capability.CustomFluidStorage;
import com.machina.capability.MachinaTank;
import com.machina.capability.CustomItemStorage;
import com.machina.capability.IEnergyTileEntity;
import com.machina.recipe.PressurizedChamberRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.server.HeatHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class PressurizedChamberTileEntity extends CustomTE
		implements IMachinaContainerProvider, IHeatTileEntity, ITickableTileEntity, IEnergyTileEntity {

	public boolean isRunning = false;
	public float heat = 0;
	public float reqHeat = 0;
	public String result = "";
	public int color = 0xFF_ff0000;

	public PressurizedChamberTileEntity() {
		super(TileEntityInit.PRESSURIZED_CHAMBER.get());
	}

	CustomItemStorage items;
	CustomFluidStorage fluid;
	CustomEnergyStorage energy;
	
	public MachinaTank getById(int id) {
		return this.fluid.tank(id);
	}

	public Predicate<FluidStack> exclusiveTank(int id) {
		return fluid -> {
			for (int i = 0; i < 4; i++) {
				if (i != id && this.fluid.getFluidInTank(id).isFluidEqual(fluid))
					return false;
			}
			return true;
		};
	}

	@Override
	public void createStorages() {
		this.items = add(new CustomItemStorage(2));
		this.fluid = add(new MachinaTank(this, 10000, exclusiveTank(0), false, 0),
				new MachinaTank(this, 10000, exclusiveTank(1), false, 1),
				new MachinaTank(this, 10000, exclusiveTank(2), false, 2),
				new MachinaTank(this, 10000, p -> false, true, 3));
		this.energy = add(new CustomEnergyStorage(this, 1000000, 1000));
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		float target = HeatHelper.calculateTemperatureRegulators(worldPosition, level);
		heat = HeatHelper.limitHeat(heat + (target - heat) * 0.05f, level.dimension());
		sync();

		for (IRecipe<?> r : RecipeInit.getRecipes(RecipeInit.PRESSURIZED_CHAMBER_RECIPE, level.getRecipeManager())
				.values()) {
			PressurizedChamberRecipe recipe = (PressurizedChamberRecipe) r;

			// Tests
			if (!contains(recipe.fluids, false))
				continue;
			if (!fluid.tank(3).isEmpty() && !fluid.getFluidInTank(3).isFluidEqual(recipe.fOut))
				continue;
			if (!items.getStackInSlot(1).isEmpty() && !items.getStackInSlot(1).getItem().equals(recipe.iOut.getItem()))
				continue;
			if (items.getStackInSlot(1).getCount() >= items.getStackInSlot(1).getMaxStackSize())
				return;
			if (fluid.getFluidInTank(3).getAmount() + recipe.fOut.getAmount() > fluid.getTankCapacity(3))
				continue;
			if (!items.getStackInSlot(0).getItem().equals(recipe.catalyst.getItem()))
				continue;
			if (energy.getEnergyStored() < recipe.power)
				continue;

			// Set result and return if not running
			reqHeat = recipe.heat;
			color = recipe.col;
			if (!recipe.fOut.isEmpty())
				result = recipe.fOut.getTranslationKey();
			if (!recipe.iOut.isEmpty())
				result = recipe.iOut.getItem().getDescriptionId();
			if (!isRunning || normalized() < reqHeat || !contains(recipe.fluids, true)) {
				sync();
				return;
			}

			// Consume
			for (FluidStack stack : recipe.fluids) {
				for (int i = 0; i < fluid.getTanks(); i++) {
					fluid.tank(i).drainRaw(stack, FluidAction.EXECUTE);
				}
			}
			this.energy.consumeEnergy(recipe.power);
			items.getStackInSlot(0).setDamageValue(items.getStackInSlot(0).getDamageValue() + 1);
			if (items.getStackInSlot(0).getDamageValue() >= items.getStackInSlot(0).getMaxDamage()) {
				items.getStackInSlot(0).shrink(1);
			}

			// Output
			if (!recipe.fOut.isEmpty()) {
				fluid.tank(3).rawFill(recipe.fOut.copy(), FluidAction.EXECUTE);
			}
			if (!recipe.iOut.isEmpty()) {
				if (items.getStackInSlot(1).isEmpty()) {
					items.setStackInSlot(1, recipe.iOut.copy());
				} else {
					items.getStackInSlot(1).grow(recipe.iOut.copy().getCount());
				}
			}
			sync();
			return;
		}

		result = "";
		color = 0xFF_ff0000;
		sync();
	}

	public float heatFull() {
		return HeatHelper.propFull(normalized(), this.level.dimension());
	}

	public float normalized() {
		return HeatHelper.normalizeHeat(heat, this.level.dimension());
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

	public void runToggle() {
		this.isRunning = !this.isRunning;
		sync();
	}

	public void clear(int id) {
		fluid.setFluidInTank(id, FluidStack.EMPTY);
		sync();
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new PressurizedChamberContainer(windowId, inv, this);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound.putBoolean("Running", isRunning);
		compound.putString("Result", result);
		compound.putFloat("Heat", heat);
		compound.putFloat("ReqHeat", reqHeat);
		compound.putInt("Color", color);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		isRunning = compound.getBoolean("Running");
		result = compound.getString("Result");
		heat = compound.getFloat("Heat");
		reqHeat = compound.getFloat("ReqHeat");
		color = compound.getInt("Color");
		super.load(state, compound);
	}

	@Override
	public float getHeat() {
		return heat;
	}

	@Override
	public boolean isGenerator() {
		return false;
	}

	public int getEnergy() {
		return this.energy.getEnergyStored();
	}

	public int getMaxEnergy() {
		return this.energy.getMaxEnergyStored();
	}

	public float propFull() {
		return (float) this.getEnergy() / (float) this.getMaxEnergy();
	}
	
	@Override
	public boolean isGeneratorMode() {
		return false;
	}
}
