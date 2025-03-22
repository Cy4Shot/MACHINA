package com.machina.recipe.maps;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.CompressorBlockEntity;
import com.machina.recipe.CompressorRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;

public class CompressorRecipeMaps extends MachinaRecipeMaps<CompressorBlockEntity> {

	public static final CompressorRecipeMaps INSTANCE = new CompressorRecipeMaps();

	@Override
	public boolean isValid(CompressorBlockEntity entity, MachinaRecipe<CompressorBlockEntity> recipe) {
		for (Ingredient i : recipe.getInputItems()) {
			if (!entity.hasAnyOf(Arrays.stream(i.getItems()).map(ItemStack::getItem).collect(Collectors.toSet())))
				return false;
		}

		return true;
	}

	@Override
	protected RecipeRegistryObject<CompressorBlockEntity> getRegistryObject() {
		return RecipeInit.COMPRESSOR;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_TIME;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man) {

	}

	@Override
	public Class<? extends MachinaRecipe<CompressorBlockEntity>> getRecipeClass() {
		return CompressorRecipe.class;
	}
}