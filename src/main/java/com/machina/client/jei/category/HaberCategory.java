package com.machina.client.jei.category;

import com.machina.recipe.impl.HaberRecipe;
import com.machina.registration.init.BlockInit;
import com.machina.util.text.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.util.ResourceLocation;

public class HaberCategory extends BaseCategory<HaberRecipe> {

	public static final ResourceLocation UID = new MachinaRL("haber");

	public HaberCategory(IGuiHelper helper) {
		super(helper, HaberRecipe.class, BlockInit.HABER_CONTROLLER.get(), 133, 86);
	}

	@Override
	public void setIngredients(HaberRecipe recipe, IIngredients ingredients) {
		ingredients.setInputs(VanillaTypes.FLUID, recipe.fluids);
		ingredients.setInput(VanillaTypes.ITEM, recipe.catalyst);
		ingredients.setOutput(VanillaTypes.FLUID, recipe.fOut);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, HaberRecipe recipe, IIngredients ingredients) {
		IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();
		IGuiItemStackGroup items = recipeLayout.getItemStacks();

		fluids.init(0, true, 1, 1, 21, 58, 1, false, null);
		fluids.init(1, true, 26, 1, 21, 58, 1, false, null);
		fluids.init(2, true, 51, 1, 21, 58, 1, false, null);
		fluids.init(3, false, 110, 1, 22, 58, 1, false, null);
		items.init(0, true, 28, 68);

		fluids.set(ingredients);
		items.set(ingredients);
	}

	@Override
	public void draw(HaberRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}
}