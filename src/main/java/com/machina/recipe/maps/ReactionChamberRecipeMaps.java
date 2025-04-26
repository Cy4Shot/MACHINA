package com.machina.recipe.maps;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.ReactionChamberBlockEntity;
import com.machina.recipe.ReactionChamberRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.world.item.crafting.RecipeManager;

public class ReactionChamberRecipeMaps extends MachinaRecipeMaps<ReactionChamberBlockEntity> {

	public static final ReactionChamberRecipeMaps INSTANCE = new ReactionChamberRecipeMaps();

	@Override
	protected RecipeRegistryObject<ReactionChamberBlockEntity> getRegistryObject() {
		return RecipeInit.REACTION_CHAMBER;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man) {

	}

	@Override
	public Class<? extends MachinaRecipe<ReactionChamberBlockEntity>> getRecipeClass() {
		return ReactionChamberRecipe.class;
	}
}