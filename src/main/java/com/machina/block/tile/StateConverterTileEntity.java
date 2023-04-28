package com.machina.block.tile;

import com.machina.block.container.StateConverterContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.CustomTE;
import com.machina.block.tile.base.IHeatTileEntity;
import com.machina.capability.CustomFluidStorage;
import com.machina.capability.MachinaTank;
import com.machina.recipe.StateConverterRecipe;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class StateConverterTileEntity extends CustomTE
		implements IHeatTileEntity, ITickableTileEntity, IMachinaContainerProvider {

	public float heat = 0;
	public float reqHeat = 0;
	public boolean above = true;
	public boolean recipe = false;

	public StateConverterTileEntity() {
		super(TileEntityInit.STATE_CONVERTER.get());
	}

	CustomFluidStorage fluid;

	@Override
	public void createStorages() {
		this.fluid = add(new MachinaTank(this, 10000, p -> true, false, 0),
				new MachinaTank(this, 10000, p -> false, true, 1));
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		float target = HeatHelper.calculateTemperatureRegulators(worldPosition, level);
		heat = HeatHelper.limitHeat(heat + (target - heat) * 0.05f, level.dimension());
		sync();

		for (IRecipe<?> r : RecipeInit.getRecipes(RecipeInit.STATE_CONVERTER_RECIPE, level.getRecipeManager())
				.values()) {
			StateConverterRecipe recipe = (StateConverterRecipe) r;

			if (!tankContains(fluid.tank(0), recipe.input, false))
				continue;
			if (!fluid.tank(1).isEmpty() && !fluid.getFluidInTank(1).isFluidEqual(recipe.output))
				continue;
			if (fluid.getFluidInTank(1).getAmount() + recipe.output.getAmount() > fluid.getTankCapacity(1))
				continue;

			above = recipe.above;
			reqHeat = recipe.heat;
			if ((above ? (normalized() > reqHeat) : (reqHeat < normalized()))
					|| !tankContains(fluid.tank(0), recipe.input, true)) {
				sync();
				return;
			}

			// Consume
			fluid.tank(0).drain(recipe.input, FluidAction.EXECUTE);

			// Output
			if (!recipe.output.isEmpty()) {
				fluid.tank(1).fill(recipe.output.copy(), FluidAction.EXECUTE);
			}
			sync();
			return;
		}

		sync();
	}

	public float heatFull() {
		return HeatHelper.propFull(normalized(), this.level.dimension());
	}

	public float normalized() {
		return HeatHelper.normalizeHeat(heat, this.level.dimension());
	}

	public boolean tankContains(MachinaTank tank, FluidStack stack, boolean amount) {
		if (!tank.getFluid().isFluidEqual(stack))
			return false;
		if (tank.getFluid().getAmount() < stack.getAmount() && amount)
			return false;

		return true;
	}

	public void clear() {
		fluid.tank(0).setFluid(FluidStack.EMPTY);
		sync();
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new StateConverterContainer(windowId, this);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound.putFloat("Heat", heat);
		compound.putFloat("ReqHeat", reqHeat);
		compound.putBoolean("Above", above);
		compound.putBoolean("Recipe", recipe);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		heat = compound.getFloat("Heat");
		reqHeat = compound.getFloat("ReqHeat");
		above = compound.getBoolean("Above");
		recipe = compound.getBoolean("Recipe");
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
