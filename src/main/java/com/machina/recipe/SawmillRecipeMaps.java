package com.machina.recipe;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.SawmillBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.world.item.crafting.RecipeManager;

public class SawmillRecipeMaps extends MachinaRecipeMaps<SawmillBlockEntity> {

	public static final SawmillRecipeMaps INSTANCE = new SawmillRecipeMaps();

	@Override
	protected RecipeRegistryObject<SawmillBlockEntity> getRegistryObject() {
		return RecipeInit.SAWMILL;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_TIME;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man) {

	}
}