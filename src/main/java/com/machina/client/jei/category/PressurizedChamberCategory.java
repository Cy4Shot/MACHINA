package com.machina.client.jei.category;

import java.util.function.Function;

import com.machina.client.util.UIHelper;
import com.machina.config.CommonConfig;
import com.machina.recipe.impl.PressurizedChamberRecipe;
import com.machina.registration.init.BlockInit;
import com.machina.util.Color;
import com.machina.util.text.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.util.ResourceLocation;

public class PressurizedChamberCategory extends BaseCategory<PressurizedChamberRecipe> {

	public static final ResourceLocation UID = new MachinaRL("pressurized_chamber");

	final Function<Float, IDrawable> heat;

	public PressurizedChamberCategory(IGuiHelper helper) {
		super(helper, PressurizedChamberRecipe.class, BlockInit.PRESSURIZED_CHAMBER.get(), 133, 107);

		this.heat = (i) -> helper.createDrawable(bg(), 0, 107, (int) (131f * i), 14);
	}

	@Override
	public void setIngredients(PressurizedChamberRecipe recipe, IIngredients ingredients) {
		ingredients.setInputs(VanillaTypes.FLUID, recipe.fluids);
		if (!recipe.catalyst.isEmpty())
			ingredients.setInput(VanillaTypes.ITEM, recipe.catalyst);
		if (!recipe.fOut.isEmpty())
			ingredients.setOutput(VanillaTypes.FLUID, recipe.fOut);
		if (!recipe.iOut.isEmpty())
			ingredients.setOutput(VanillaTypes.ITEM, recipe.iOut);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, PressurizedChamberRecipe recipe, IIngredients ingredients) {
		IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();
		IGuiItemStackGroup items = recipeLayout.getItemStacks();

		fluids.init(0, true, 1, 1, 21, 58, 1, false, null);
		fluids.init(1, true, 26, 1, 21, 58, 1, false, null);
		fluids.init(2, true, 51, 1, 21, 58, 1, false, null);
		if (!recipe.fOut.isEmpty())
			fluids.init(3, false, 110, 1, 22, 58, 1, false, null);

		if (!recipe.catalyst.isEmpty())
			items.init(0, true, 28, 68);
		if (!recipe.iOut.isEmpty())
			items.init(1, false, 112, 66);

		fluids.set(ingredients);
		items.set(ingredients);
	}

	@Override
	public void draw(PressurizedChamberRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {

		// Heat
		float t = (float) recipe.heat / (float) CommonConfig.maxHeat.get();
		heat.apply(t).draw(matrixStack, 1, 92);
		UIHelper.drawCenteredStringWithBorder(matrixStack, String.valueOf(recipe.heat) + "K", back.getWidth() / 2, 95,
				Color.interpolate(Color.GREEN, Color.RED, t).maxBrightness().getRGB(), 0xFF_0e0e0e);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}
}