package com.machina.block.entity.machine;

import com.machina.api.block.entity.RecipeBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MelterBlockEntity extends RecipeBlockEntity {

	public MelterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public MelterBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.MELTER.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
		itemSlot(SlotType.INPUT);
		fluidSlot(16_000, s -> true, SlotType.OUTPUT);
	}

	@Override
	public int getMaxEnergy() {
		// TODO: Config
		return 1_000_000;
	}

	@Override
	public RecipeRegistryObject<? extends RecipeBlockEntity> getRecipe() {
		return RecipeInit.MELTER;
	}

	@Override
	public boolean activeModel() {
		return false;
	}
}
