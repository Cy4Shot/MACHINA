package com.machina.client.jei.category;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.machina.recipe.impl.MelterRecipe;
import com.machina.registration.init.BlockInit;
import com.machina.util.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.util.ResourceLocation;

public class MelterCategory extends BaseCategory<MelterRecipe> {

	public static final ResourceLocation UID = new MachinaRL("melter");

	private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

	public MelterCategory(IGuiHelper helper) {
		super(helper, MelterRecipe.class, BlockInit.MELTER.get(), 128, 60);

		this.cachedArrows = CacheBuilder.newBuilder().maximumSize(62)
				.build(new CacheLoader<Integer, IDrawableAnimated>() {
					@Override
					public IDrawableAnimated load(Integer cookTime) {
						return helper.drawableBuilder(MelterCategory.this.bg(), 128, 0, 61, 16).buildAnimated(cookTime,
								IDrawableAnimated.StartDirection.LEFT, false);
					}
				});
	}

	@Override
	public void setIngredients(MelterRecipe recipe, IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.ITEM, recipe.input);
		ingredients.setOutput(VanillaTypes.FLUID, recipe.output);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, MelterRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup items = recipeLayout.getItemStacks();
		IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();

		items.init(0, true, 0, 23);
		fluids.init(1, false, 106, 1, 21, 58, 1, false, null);

		items.set(ingredients);
		fluids.set(ingredients);
	}

	@Override
	public void draw(MelterRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		cachedArrows.getUnchecked(100).draw(matrixStack, 31, 23);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}
}