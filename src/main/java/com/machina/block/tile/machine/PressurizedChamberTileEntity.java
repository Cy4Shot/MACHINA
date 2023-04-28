package com.machina.block.tile.machine;

import java.util.function.Predicate;

import com.machina.block.container.PressurizedChamberContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.MachinaTileEntity;
import com.machina.capability.energy.IEnergyTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.capability.fluid.MachinaFluidStorage;
import com.machina.capability.fluid.MachinaTank;
import com.machina.capability.heat.IHeatTileEntity;
import com.machina.capability.inventory.MachinaItemStorage;
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

public class PressurizedChamberTileEntity extends MachinaTileEntity
		implements IMachinaContainerProvider, IHeatTileEntity, ITickableTileEntity, IEnergyTileEntity {

	public boolean isRunning = false;
	public float heat = 0;
	public float reqHeat = 0;
	public String result = "";
	public int color = 0xFF_ff0000;

	public PressurizedChamberTileEntity() {
		super(TileEntityInit.PRESSURIZED_CHAMBER.get());
	}

	MachinaItemStorage items;
	MachinaFluidStorage fluid;
	MachinaEnergyStorage energy;
	
	public MachinaTank getById(int id) {
		return this.fluid.tank(id);
	}

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
		this.items = add(new MachinaItemStorage(2));
		this.fluid = add(new MachinaTank(this, 10000, exclusiveTank(0), false, 0),
				new MachinaTank(this, 10000, exclusiveTank(1), false, 1),
				new MachinaTank(this, 10000, exclusiveTank(2), false, 2),
				new MachinaTank(this, 10000, p -> false, true, 3));
		this.energy = add(new MachinaEnergyStorage(this, 1000000, 1000));
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		float target = HeatHelper.calculateTemperatureRegulators(worldPosition, level);
		heat = HeatHelper.limitHeat(heat + (target - heat) * 0.05f, level.dimension());
		setChanged();

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
				setChanged();
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
			setChanged();
			return;
		}

		result = "";
		color = 0xFF_ff0000;
		setChanged();
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
		setChanged();
	}

	public void clear(int id) {
		fluid.setFluidInTank(id, FluidStack.EMPTY);
		setChanged();
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
