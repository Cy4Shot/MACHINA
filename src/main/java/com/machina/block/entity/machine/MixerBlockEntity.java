package com.machina.block.entity.machine;

import com.machina.api.block.entity.RecipeBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.MixerMenu;
import com.machina.recipe.MixerRecipe;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MixerBlockEntity extends RecipeBlockEntity<MixerRecipe> {

	public MixerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public MixerBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.MIXER.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
		fluidSlot(10_000, s -> true, SlotType.INPUT);
		fluidSlot(10_000, s -> true, SlotType.INPUT);
		fluidSlot(10_000, s -> true, SlotType.OUTPUT);
	}

	@Override
	public int getMaxEnergy() {
		// TODO: Config
		return 1_000_000;
	}

	@Override
	public RecipeRegistryObject<? extends RecipeBlockEntity<MixerRecipe>> getRecipe() {
		return RecipeInit.MIXER;
	}

	@Override
	protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
		return MixerMenu::new;
	}

	@Override
	public boolean activeModel() {
		return false;
	}
}
