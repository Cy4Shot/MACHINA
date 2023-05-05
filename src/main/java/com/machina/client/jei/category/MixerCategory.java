package com.machina.client.jei.category;

import com.machina.recipe.impl.MixerRecipe;
import com.machina.registration.init.BlockInit;
import com.machina.util.MachinaRL;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.util.ResourceLocation;

public class MixerCategory extends BaseCategory<MixerRecipe> {

	public static final ResourceLocation UID = new MachinaRL("mixer");

	public MixerCategory(IGuiHelper helper) {
		super(helper, MixerRecipe.class, BlockInit.MIXER.get(), 144, 90);
	}

	@Override
	public void setIngredients(MixerRecipe recipe, IIngredients ingredients) {
		ingredients.setInputs(VanillaTypes.FLUID, recipe.fluidsIn);
		if (!recipe.itemIn.isEmpty())
			ingredients.setInput(VanillaTypes.ITEM, recipe.itemIn);
		if (!recipe.catalyst.isEmpty())
			ingredients.setInput(VanillaTypes.ITEM, recipe.catalyst);
		if (!recipe.fluidsOut.isEmpty())
			ingredients.setOutputs(VanillaTypes.FLUID, recipe.fluidsOut);
		if (!recipe.itemOut.isEmpty())
			ingredients.setOutput(VanillaTypes.ITEM, recipe.itemOut);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, MixerRecipe recipe, IIngredients ingredients) {
		IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();
		IGuiItemStackGroup items = recipeLayout.getItemStacks();

		int i = 0;
		for (i = 0; i < recipe.fluidsIn.size(); i++) {
			fluids.init(i, true, 1 + 25 * i, 1, 21, 58, 1, false, null);
		}

		for (int j = 0; j < recipe.fluidsOut.size(); j++) {
			fluids.init(i, false, 97 + 25 * j, 1, 21, 58, 1, false, null);
			i++;
		}

		int k = 0;
		if (!recipe.itemIn.isEmpty()) {
			items.init(k, true, 15, 68);
			k++;
		}
		if (!recipe.catalyst.isEmpty()) {
			items.init(k, true, 63, 35);
			k++;
		}
		if (!recipe.itemOut.isEmpty()) {
			items.init(k, false, 111, 66);
		}

		fluids.set(ingredients);
		items.set(ingredients);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}
}