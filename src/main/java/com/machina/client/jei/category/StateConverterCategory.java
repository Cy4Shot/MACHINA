package com.machina.client.jei.category;

import java.util.function.Function;

import com.machina.client.util.UIHelper;
import com.machina.config.CommonConfig;
import com.machina.recipe.impl.StateConverterRecipe;
import com.machina.registration.init.BlockInit;
import com.machina.util.Color;
import com.machina.util.text.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.util.ResourceLocation;

public class StateConverterCategory extends BaseCategory<StateConverterRecipe> {

	public static final ResourceLocation UID = new MachinaRL("state_converter");

	final Function<Float, IDrawable> heat;

	public StateConverterCategory(IGuiHelper helper) {
		super(helper, StateConverterRecipe.class, BlockInit.STATE_CONVERTER.get(), 133, 78);

		this.heat = (i) -> helper.createDrawable(bg(), 0, 78, (int) (131f * i), 14);
	}

	@Override
	public void setIngredients(StateConverterRecipe recipe, IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.FLUID, recipe.input);
		ingredients.setOutput(VanillaTypes.FLUID, recipe.output);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, StateConverterRecipe recipe, IIngredients ingredients) {
		IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();

		fluids.init(0, true, 1, 1, 21, 58, 1, false, null);
		fluids.init(1, false, 111, 1, 21, 58, 1, false, null);

		fluids.set(ingredients);
	}

	@Override
	public void draw(StateConverterRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {

		// Heat
		float t = (float) recipe.heat / (float) CommonConfig.maxHeat.get();
		heat.apply(t).draw(matrixStack, 1, 63);
		UIHelper.drawCenteredStringWithBorder(matrixStack, String.valueOf(recipe.heat) + "K", back.getWidth() / 2, 66,
				Color.interpolate(Color.GREEN, Color.RED, t).maxBrightness().getRGB(), 0xFF_0e0e0e);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}
}