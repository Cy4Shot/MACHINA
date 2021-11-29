/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.recipe.ac.function;

import java.util.List;

import com.google.gson.JsonObject;
import com.machina.api.recipe.advanced_crafting.AdvancedCraftingFunction;
import com.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;
import com.machina.api.recipe.advanced_crafting.AdvancedCraftingRecipe;
import com.machina.api.registry.annotation.RegistryHolder;
import com.machina.api.registry.annotation.recipe.RegisterACFunctionSerializer;

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
