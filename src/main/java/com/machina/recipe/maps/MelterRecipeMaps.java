package com.machina.recipe.maps;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.MelterBlockEntity;
import com.machina.recipe.MelterRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;

public class MelterRecipeMaps extends MachinaRecipeMaps<MelterBlockEntity> {

	public static final MelterRecipeMaps INSTANCE = new MelterRecipeMaps();

	@Override
	public boolean isValid(MelterBlockEntity entity, MachinaRecipe<MelterBlockEntity> recipe) {
		for (Ingredient i : recipe.getInputItems()) {
			if (!entity.hasAnyOf(Arrays.stream(i.getItems()).map(ItemStack::getItem).collect(Collectors.toSet())))
				return false;
		}

		return true;
	}

	@Override
	protected RecipeRegistryObject<MelterBlockEntity> getRegistryObject() {
		return RecipeInit.MELTER;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_TIME;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man) {

	}

	@Override
	public Class<? extends MachinaRecipe<MelterBlockEntity>> getRecipeClass() {
		return MelterRecipe.class;
	}
}