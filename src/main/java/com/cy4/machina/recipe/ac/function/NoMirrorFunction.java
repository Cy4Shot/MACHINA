/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.recipe.ac.function;

import java.util.List;

import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.annotation.registries.recipe.RegisterACFunctionSerializer;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunction;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingRecipe;
import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

@RegistryHolder
public class NoMirrorFunction extends AdvancedCraftingFunction {

	@RegisterACFunctionSerializer("no_mirror")
	public static final Serializer SERIALIZER = new Serializer();

	@Override
	public boolean matches(CraftingInventory inventory, AdvancedCraftingRecipe recipe, World level) {
		for (int i = 0; i <= inventory.getWidth() - recipe.getRecipeWidth(); i++) {
			for (int j = 0; j <= inventory.getHeight() - recipe.getRecipeHeight(); j++) {
				if (this.checkMatch(inventory, i, j, recipe)) { return true; }
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

				if (!ingredient.test(inventory.getItem(i + j * inventory.getWidth()))) { return false; }
			}
		}

		return true;
	}

	@Override
	public void addJeiInfo(List<ITextComponent> tooltipList) {
		tooltipList.add(new StringTextComponent("This recipe cannot be mirrored!"));
	}

	public static class Serializer extends AdvancedCraftingFunctionSerializer<NoMirrorFunction> {

		@Override
		public NoMirrorFunction deserialize(JsonObject obj) {
			return new NoMirrorFunction();
		}

	}

}
