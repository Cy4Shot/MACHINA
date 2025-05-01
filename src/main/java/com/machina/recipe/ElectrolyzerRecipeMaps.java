package com.machina.recipe;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.ElectrolyzerBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.world.item.crafting.RecipeManager;

public class ElectrolyzerRecipeMaps extends MachinaRecipeMaps<ElectrolyzerBlockEntity> {

	public static final ElectrolyzerRecipeMaps INSTANCE = new ElectrolyzerRecipeMaps();

	@Override
	protected RecipeRegistryObject<ElectrolyzerBlockEntity> getRegistryObject() {
		return RecipeInit.ELECTROLYZER;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_PERIODIC_CONSUMPTION;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man) {

	}
}