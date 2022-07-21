package com.machina.block.tile;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.machina.block.container.PressurizedChamberContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.BaseEnergyLootTileEntity;
import com.machina.block.tile.base.IHeatTileEntity;
import com.machina.block.tile.base.IMultiFluidTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.capability.fluid.MachinaTank;
import com.machina.capability.fluid.MultiTankCapability;
import com.machina.recipe.PressurizedChamberRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.server.HeatUtils;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class PressurizedChamberTileEntity extends BaseEnergyLootTileEntity
		implements IMachinaContainerProvider, IMultiFluidTileEntity, IHeatTileEntity {

	public LinkedList<MachinaTank> tanks = new LinkedList<>(Arrays.asList(
			new MachinaTank(this, 10000, exclusiveTank(0), 0), new MachinaTank(this, 10000, exclusiveTank(1), 1),
			new MachinaTank(this, 10000, exclusiveTank(2), 2), new MachinaTank(this, 10000, p -> false, 3)));
	private final LazyOptional<MultiTankCapability> cap = LazyOptional.of(() -> new MultiTankCapability(tanks));

	public boolean isRunning = false;
	public float heat = 0;
	public float reqHeat = 0;
	public String result = "";

	public Predicate<FluidStack> exclusiveTank(int id) {
		return fluid -> {
			for (MachinaTank tank : tanks) {
				if (tank.id != id && tank.getFluid().isFluidEqual(fluid))
					return false;
			}
			return true;
		};
	}

	public PressurizedChamberTileEntity() {
		super(TileEntityInit.PRESSURIZED_CHAMBER.get(), 2);

		this.sides = new int[] { 1, 1, 1, 1, 1, 1 };
	}

	@Override
	public void tick() {
		super.tick();

		if (this.level.isClientSide())
			return;

		float target = HeatUtils.calculateTemperatureRegulators(worldPosition, level);
		heat = HeatUtils.limitHeat(heat + (target - heat) * 0.05f, level.dimension());
		sync();

		for (IRecipe<?> r : RecipeInit.getRecipes(RecipeInit.PRESSURIZED_CHAMBER_RECIPE, level.getRecipeManager())
				.values()) {
			PressurizedChamberRecipe recipe = (PressurizedChamberRecipe) r;

			// Tests
			if (!contains(recipe.fluids))
				continue;
			if (!tanks.get(3).isEmpty() && !tanks.get(3).getFluid().isFluidEqual(recipe.fOut))
				continue;
			if (!getItem(1).isEmpty() && !getItem(1).getItem().equals(recipe.iOut.getItem()))
				continue;
			if (tanks.get(3).getFluidAmount() + recipe.fOut.getAmount() > tanks.get(3).getCapacity())
				continue;
			if (!getItem(0).getItem().equals(recipe.catalyst.getItem()))
				continue;
			if (getEnergyStored() < recipe.power)
				continue;

			// Set result and return if not running
			reqHeat = recipe.heat;
			if (!recipe.fOut.isEmpty())
				result = recipe.fOut.getTranslationKey();
			if (!recipe.iOut.isEmpty())
				result = recipe.iOut.getItem().getDescriptionId();
			if (!isRunning || heat < reqHeat) {
				sync();
				return;
			}

			// Consume
			for (FluidStack stack : recipe.fluids) {
				cap.orElseGet(() -> new MultiTankCapability(new LinkedList<>())).drain(stack, FluidAction.EXECUTE);
			}
			this.energyDef.consumeEnergy(recipe.power);
			getItem(0).setDamageValue(getItem(0).getDamageValue() + 1);
			if (getItem(0).getDamageValue() >= getItem(0).getMaxDamage()) {
				getItem(0).shrink(1);
			}

			// Output
			if (!recipe.fOut.isEmpty()) {
				tanks.get(3).rawFill(recipe.fOut, FluidAction.EXECUTE);
			}
			if (!recipe.iOut.isEmpty()) {
				if (getItem(1).isEmpty()) {
					setItem(1, recipe.iOut);
				} else {
					getItem(1).grow(recipe.iOut.getCount());
				}
			}
			sync();
			return;
		}

		result = "";
		sync();
	}
	
	public float heatFull() {
		return HeatUtils.propFull(heat, this.level.dimension());
	}
	
	public float normalized() {
		return HeatUtils.normalizeHeat(heat, this.level.dimension());
	}

	public boolean contains(NonNullList<FluidStack> fluids) {
		for (FluidStack stack : fluids) {
			if (!anyTankContains(stack))
				return false;
		}
		return true;
	}

	public boolean anyTankContains(FluidStack stack) {
		for (MachinaTank tank : tanks) {
			if (tankContains(tank, stack))
				return true;
		}

		return false;
	}

	public boolean tankContains(MachinaTank tank, FluidStack stack) {
		if (!tank.getFluid().isFluidEqual(stack))
			return false;
		if (tank.getFluid().getAmount() < stack.getAmount())
			return false;

		return true;
	}

	public void runToggle() {
		this.isRunning = !this.isRunning;
		sync();
	}

	public void clear(int id) {
		this.tanks.get(id).setFluid(FluidStack.EMPTY);
		sync();
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new PressurizedChamberContainer(windowId, inv, this);
	}

	@Override
	public MachinaEnergyStorage createStorage() {
		return new MachinaEnergyStorage(this, 1000000, 1000, 0);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		tanks.forEach(tank -> tank.writeToNBT(compound));
		compound.putBoolean("Running", isRunning);
		compound.putString("Result", result);
		compound.putFloat("Heat", heat);
		compound.putFloat("ReqHeat", reqHeat);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		tanks.forEach(tank -> tank.readFromNBT(compound));
		isRunning = compound.getBoolean("Running");
		result = compound.getString("Result");
		heat = compound.getFloat("Heat");
		reqHeat = compound.getFloat("ReqHeat");
		super.load(state, compound);
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
	public void setFluid(FluidStack fluid, int index) {
		tanks.get(index).setFluid(fluid);
	}

	@Override
	public FluidStack getFluid(int index) {
		return tanks.get(index).getFluid();
	}

	@Override
	public int stored(int index) {
		return tanks.get(index).getFluidAmount();
	}

	@Override
	public int capacity(int index) {
		return tanks.get(index).getCapacity();
	}

	@Override
	public float getHeat() {
		return heat;
	}

	@Override
	public boolean isGenerator() {
		return false;
	}
}
