package com.machina.recipe.maps;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.MixerBlockEntity;
import com.machina.recipe.MixerRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;

public class MixerRecipeMaps extends MachinaRecipeMaps<MixerBlockEntity> {

	public static final MixerRecipeMaps INSTANCE = new MixerRecipeMaps();

	@Override
	public boolean isValid(MixerBlockEntity entity, MachinaRecipe<MixerBlockEntity> recipe) {
		for (Ingredient i : recipe.getInputItems()) {
			if (!entity.hasAnyOf(Arrays.stream(i.getItems()).map(ItemStack::getItem).collect(Collectors.toSet())))
				return false;
		}

		return true;
	}

	@Override
	protected RecipeRegistryObject<MixerBlockEntity> getRegistryObject() {
		return RecipeInit.MIXER;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man) {

	}

	@Override
	public Class<? extends MachinaRecipe<MixerBlockEntity>> getRecipeClass() {
		return MixerRecipe.class;
	}
}