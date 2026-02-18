package com.machina.recipe;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.ReactionChamberBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeManager;

public class ReactionChamberRecipeMaps extends MachinaRecipeMaps<ReactionChamberBlockEntity> {

	public static final ReactionChamberRecipeMaps INSTANCE = new ReactionChamberRecipeMaps();

	@Override
	protected RecipeRegistryObject<ReactionChamberBlockEntity> getRegistryObject() {
		return RecipeInit.REACTION_CHAMBER;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_PERIODIC_CONSUMPTION;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man, RegistryAccess access) {

	}
}