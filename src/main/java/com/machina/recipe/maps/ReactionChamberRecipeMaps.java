package com.machina.recipe.maps;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.ReactionChamberBlockEntity;
import com.machina.recipe.ReactionChamberRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;

public class ReactionChamberRecipeMaps extends MachinaRecipeMaps<ReactionChamberBlockEntity> {

	public static final ReactionChamberRecipeMaps INSTANCE = new ReactionChamberRecipeMaps();

	@Override
	public boolean isValid(ReactionChamberBlockEntity entity, MachinaRecipe<ReactionChamberBlockEntity> recipe) {
		for (Ingredient i : recipe.getInputItems()) {
			if (!entity.hasAnyOf(Arrays.stream(i.getItems()).map(ItemStack::getItem).collect(Collectors.toSet())))
				return false;
		}

		return true;
	}

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