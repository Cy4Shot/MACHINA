package com.machina.block.entity.machine;

import com.machina.api.block.entity.RecipeBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ComposterVatBlockEntity extends RecipeBlockEntity {

	public ComposterVatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public ComposterVatBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.COMPOSTER_VAT.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
		itemSlot(SlotType.INPUT);
		fluidSlot(16_000, s -> s.getFluid().equals(FluidInit.AMMONIA.fluid()), SlotType.OUTPUT);
	}

	@Override
	public int getMaxEnergy() {
		// TODO: Config
		return 1_000_000;
	}

	@Override
	public RecipeRegistryObject<? extends RecipeBlockEntity> getRecipe() {
		return RecipeInit.COMPOSTER_VAT;
	}

	@Override
	public boolean activeModel() {
		return false;
	}
}
