package com.machina.recipe;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.RocketPartBenchBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeManager;

public class RocketPartBenchRecipeMaps extends MachinaRecipeMaps<RocketPartBenchBlockEntity> {

	public static final RocketPartBenchRecipeMaps INSTANCE = new RocketPartBenchRecipeMaps();

	@Override
	protected RecipeRegistryObject<RocketPartBenchBlockEntity> getRegistryObject() {
		return RecipeInit.ROCKET_PART_BENCH;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man, RegistryAccess access) {
	}
}