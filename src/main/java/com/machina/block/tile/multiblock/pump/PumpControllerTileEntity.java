package com.machina.block.tile.multiblock.pump;

import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.machina.block.container.PumpContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.multiblock.MultiblockMasterTileEntity;
import com.machina.capability.energy.IEnergyTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.capability.fluid.MachinaFluidStorage;
import com.machina.capability.fluid.MachinaTank;
import com.machina.recipe.impl.PumpRecipe;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.MachinaRL;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class PumpControllerTileEntity extends MultiblockMasterTileEntity
		implements ITickableTileEntity, IMachinaContainerProvider, IEnergyTileEntity {

	public PumpControllerTileEntity() {
		this(TileEntityInit.PUMP_CONTROLLER.get());
	}

	public PumpControllerTileEntity(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return new MachinaRL("pump");
	}

	MachinaEnergyStorage energy;
	MachinaFluidStorage fluid;
	PumpRecipe rec;
	BlockPos head;

	@Override
	public void createStorages() {
		this.energy = add(new MachinaEnergyStorage(this, 100000, 1000));
		this.fluid = add(new MachinaTank(this, 1000000, stack -> stack.getFluid().equals(Fluids.WATER), true, 0),
				new MachinaTank(this, 2000, stack -> stack.getFluid().equals(FluidInit.BRINE.fluid()), true, 1));
	}

	@Override
	public Consumer<BlockPos> postValidate() {
		return pos -> {
			if (level.getBlockState(pos).getBlock().equals(BlockInit.PUMP_HEAD.get())) {
				this.head = pos;
			}
		};
	}

	public void validateRecipe() {
		if (rec == null) {
			this.rec = (PumpRecipe) RecipeInit.getRecipes(RecipeInit.PUMP_RECIPE, level.getRecipeManager()).values()
					.stream().collect(Collectors.toList()).get(0);
		}
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		validateRecipe();

		if (!hasInput() || !hasWaterSpace() || !hasBrineSpace() || !hasPower()) {
			return;
		}

		// Consume
		this.energy.consumeEnergy(rec.power);
		level.setBlock(head.below(), Blocks.AIR.defaultBlockState(), 3);

		// Output
		fluid.tank(0).fill(rec.water, FluidAction.EXECUTE);
		fluid.tank(1).fill(rec.brine, FluidAction.EXECUTE);
		setChanged();
	}

	public boolean hasWaterSpace() {
		validateRecipe();
		return fluid.getFluidInTank(0).getAmount() + rec.water.getAmount() <= fluid.getTankCapacity(0);
	}

	public boolean hasBrineSpace() {
		validateRecipe();
		return fluid.getFluidInTank(1).getAmount() + rec.brine.getAmount() <= fluid.getTankCapacity(1);
	}

	public boolean hasPower() {
		validateRecipe();
		return energy.getEnergyStored() >= rec.power;
	}
	
	public boolean hasInput() {
		return head != null && level.getFluidState(head.below()).getType().equals(Fluids.WATER);
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

	@Override
	public boolean isGeneratorMode() {
		return false;
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new PumpContainer(id, inventory, this);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT tag) {
		if (head != null) {
			tag.put("head", NBTUtil.writeBlockPos(head));
		}
		return super.save(tag);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT tag) {
		if (tag.contains("head")) {
			head = NBTUtil.readBlockPos(tag.getCompound("head"));
		}
		super.load(state, tag);
	}
}
