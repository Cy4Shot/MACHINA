package com.machina.datagen.server;

import java.util.function.Consumer;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

public class DatagenRecipes extends RecipeProvider implements IConditionBuilder {

	public DatagenRecipes(PackOutput po) {
		super(po);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> gen) {
		
	}

}
