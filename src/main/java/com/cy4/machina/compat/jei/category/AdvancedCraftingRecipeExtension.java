/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
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

package com.cy4.machina.compat.jei.category;

import java.util.LinkedList;
import java.util.List;

import com.cy4.machina.util.MachinaRL;
import com.machina.api.recipe.advanced_crafting.AdvancedCraftingRecipe;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraftforge.common.util.Size2i;

public class AdvancedCraftingRecipeExtension implements ICraftingCategoryExtension {

	private final AdvancedCraftingRecipe recipe;
	private final Minecraft minecraft = Minecraft.getInstance();

	public AdvancedCraftingRecipeExtension(AdvancedCraftingRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public void setIngredients(IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void drawInfo(int recipeWidth, int recipeHeight, MatrixStack matrixStack, double mouseX, double mouseY) {
		if (!recipe.getFunction().isEmtpy() && !getTooltipStrings(100, 10).isEmpty()) {
			minecraft.textureManager.bind(new MachinaRL("textures/gui/jei/jei_components.png"));
			minecraft.screen.blit(matrixStack, recipeWidth - 16, recipeHeight - 51, 2, 2, 7, 7);
		}
	}

	@Override
	public List<ITextComponent> getTooltipStrings(double mouseX, double mouseY) {
		List<ITextComponent> tooltips = new LinkedList<>();
		if (mouseX >= 100 && mouseX <= 107 && mouseY >= 3 && mouseY <= 10) {
			recipe.getFunction().addJeiInfo(tooltips);
		}
		return tooltips;
	}

	@Override
	public Size2i getSize() { return new Size2i(recipe.getWidth(), recipe.getHeight()); }

	@Override
	public ResourceLocation getRegistryName() { return recipe.getId(); }

}
