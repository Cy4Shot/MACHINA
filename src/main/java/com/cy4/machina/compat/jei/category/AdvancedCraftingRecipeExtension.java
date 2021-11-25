/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.compat.jei.category;

import java.util.LinkedList;
import java.util.List;

import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingRecipe;
import com.cy4.machina.util.MachinaRL;
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
