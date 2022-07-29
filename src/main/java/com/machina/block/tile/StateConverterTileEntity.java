package com.machina.block.tile;

import java.util.Arrays;
import java.util.LinkedList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.machina.block.container.StateConverterContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.BaseTileEntity;
import com.machina.block.tile.base.IHeatTileEntity;
import com.machina.block.tile.base.IMultiFluidTileEntity;
import com.machina.capability.fluid.MachinaTank;
import com.machina.capability.fluid.MultiTankCapability;
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
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class StateConverterTileEntity extends BaseTileEntity
		implements IHeatTileEntity, ITickableTileEntity, IMachinaContainerProvider, IMultiFluidTileEntity {

	public LinkedList<MachinaTank> tanks = new LinkedList<>(Arrays.asList(
			new MachinaTank(this, 10000, p -> true, false, 0), new MachinaTank(this, 10000, p -> false, true, 1)));
	private final LazyOptional<MultiTankCapability> cap = LazyOptional.of(() -> new MultiTankCapability(tanks));

	public float heat = 0;
	public float reqHeat = 0;
	public boolean above = true;
	public boolean recipe = false;

	public StateConverterTileEntity() {
		super(TileEntityInit.STATE_CONVERTER.get());
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		float target = HeatHelper.calculateTemperatureRegulators(worldPosition, level, above);
		heat = HeatHelper.limitHeat(heat + (target - heat) * 0.05f, level.dimension());
		sync();

		for (IRecipe<?> r : RecipeInit.getRecipes(RecipeInit.STATE_CONVERTER_RECIPE, level.getRecipeManager())
				.values()) {
			StateConverterRecipe recipe = (StateConverterRecipe) r;

			if (!tankContains(tanks.get(0), recipe.input, false))
				continue;
			if (!tanks.get(1).isEmpty() && !tanks.get(1).getFluid().isFluidEqual(recipe.output))
				continue;
			if (tanks.get(1).getFluidAmount() + recipe.output.getAmount() > tanks.get(1).getCapacity())
				continue;

			above = recipe.above;
			reqHeat = recipe.heat;
			if ((above ? (normalized() > reqHeat) : (reqHeat < normalized())) || !tankContains(tanks.get(0), recipe.input, true)) {
				sync();
				return;
			}

			// Consume
			cap.orElseGet(() -> new MultiTankCapability(new LinkedList<>())).drainRaw(recipe.input,
					FluidAction.EXECUTE);

			// Output
			if (!recipe.output.isEmpty()) {
				tanks.get(1).rawFill(recipe.output.copy(), FluidAction.EXECUTE);
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
		this.tanks.get(0).setFluid(FluidStack.EMPTY);
		sync();
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new StateConverterContainer(windowId, this);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		tanks.forEach(tank -> tank.writeToNBT(compound));
		compound.putFloat("Heat", heat);
		compound.putFloat("ReqHeat", reqHeat);
		compound.putBoolean("Above", above);
		compound.putBoolean("Recipe", recipe);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		tanks.forEach(tank -> tank.readFromNBT(compound));
		heat = compound.getFloat("Heat");
		reqHeat = compound.getFloat("ReqHeat");
		above = compound.getBoolean("Above");
		recipe = compound.getBoolean("Recipe");
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
