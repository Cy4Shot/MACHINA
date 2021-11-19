package com.cy4.machina.recipe.advanced_crafting.function;

import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.annotation.registries.recipe.RegisterAdvancedCraftingFunctionSerializer;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunction;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingRecipe;
import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;

@RegistryHolder
public class NoMirrorFunction extends AdvancedCraftingFunction {

	@RegisterAdvancedCraftingFunctionSerializer("no_mirror")
	public static final Serializer SERIALIZER = new Serializer();
	
	@Override
	public boolean matches(CraftingInventory inventory, AdvancedCraftingRecipe recipe, World level) {
		for (int i = 0; i <= inventory.getWidth() - recipe.getRecipeWidth(); i++) {
            for (int j = 0; j <= inventory.getHeight() - recipe.getRecipeHeight(); j++) {
                if (this.checkMatch(inventory, i, j, recipe)) {
                    return true;
                }
            }
        }
        
        return false;
	}
	
    private boolean checkMatch(CraftingInventory inventory, int x, int y, AdvancedCraftingRecipe recipe) {
        for (int i = 0; i < inventory.getWidth(); ++i) {
            for (int j = 0; j < inventory.getHeight(); ++j) {
                int k = i - x;
                int l = j - y;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < recipe.getRecipeWidth() && l < recipe.getRecipeHeight()) {
                    ingredient = recipe.getIngredients().get(k + l * recipe.getRecipeWidth());
                }

                if (!ingredient.test(inventory.getItem(i + j * inventory.getWidth()))) {
                    return false;
                }
            }
        }

        return true;
    }
    
    public static class Serializer extends AdvancedCraftingFunctionSerializer {

		@Override
		public AdvancedCraftingFunction deserialize(JsonObject obj) {
			return new NoMirrorFunction();
		}
    	
    }
	
}
