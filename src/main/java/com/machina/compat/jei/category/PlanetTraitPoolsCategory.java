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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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

package com.machina.compat.jei.category;

import static com.machina.api.ModIDs.MACHINA;

import java.util.LinkedList;
import java.util.List;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.planet.trait.pool.PlanetTraitPool;
import com.machina.api.planet.trait.pool.PlanetTraitPool.PlanetTraitPoolEntry;
import com.machina.compat.jei.MachinaJEITypes;
import com.machina.init.ItemInit;
import com.matyrobbrt.lib.util.ColourCodes;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

public class PlanetTraitPoolsCategory implements IRecipeCategory<PlanetTraitPoolRecipe> {

	public static final ResourceLocation ID = new ResourceLocation(MACHINA, "planet_trait_pools_category");

	private final IDrawable back;
	private final IDrawable icon;

	public PlanetTraitPoolsCategory(final IGuiHelper helper) {
		this.back = helper.createBlankDrawable(180, 140);
		this.icon = helper.createDrawableIngredient(new ItemStack(ItemInit.ITEM_GROUP_ICON));
	}

	@Override
	public ResourceLocation getUid() { return ID; }

	@Override
	public Class<? extends PlanetTraitPoolRecipe> getRecipeClass() { return PlanetTraitPoolRecipe.class; }

	@Override
	public String getTitle() { return "Planet Trait Pools"; }

	@Override
	public IDrawable getBackground() { return back; }

	@Override
	public IDrawable getIcon() { return icon; }

	@Override
	public void setIngredients(PlanetTraitPoolRecipe recipe, IIngredients ingredients) {
		recipe.setIngredients(ingredients);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, PlanetTraitPoolRecipe recipe, IIngredients ingredients) {
		IGuiIngredientGroup<PlanetTrait> traits = recipeLayout.getIngredientsGroup(MachinaJEITypes.PLANET_TRAIT);
		PlanetTraitPool pool = recipe.getPool();
		final List<Integer> entrySize = initEntriesSize(pool);
		for (int coloumn = 0; coloumn < entrySize.size(); coloumn++) {
			for (int row = 0; row < entrySize.get(coloumn); row++) {
				traits.init(getIndexForColoumnRow(entrySize, coloumn, row), false, 10 + coloumn * 18, 33 + row * 18);
			}
		}
		traits.set(ingredients);
	}

	public static List<Integer> initEntriesSize(PlanetTraitPool pool) {
		final List<Integer> list = new LinkedList<>();
		for (int i = 0; i < pool.entries.size(); i++) {
			list.add(pool.entries.get(i).values.size());
		}
		return list;
	}

	public static int getIndexForColoumnRow(List<Integer> list, int coloumn, int row) {
		if (coloumn > list.size()) { return 0; }
		int count = 0;
		for (int i = 0; i < coloumn; i++) {
			count += list.get(i);
		}
		count += row;
		return count;
	}

	@SuppressWarnings("resource")
	@Override
	public void draw(PlanetTraitPoolRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		FontRenderer font = Minecraft.getInstance().font;
		Screen.drawCenteredString(matrixStack, font, ColourCodes.YELLOW + recipe.name, 90, 0, 0xffffff);
		Screen.drawString(matrixStack, font, "Minimum rolls: " + ColourCodes.GOLD + recipe.getPool().minRolls
				+ ColourCodes.WHITE + "; Maximium rolls: " + ColourCodes.RED + recipe.getPool().maxRolls, 10, 11,
				0xffffff);
		for (int i = 0; i < recipe.getPool().entries.size(); i++) {
			PlanetTraitPoolEntry entry = recipe.getPool().entries.get(i);
			Screen.drawCenteredString(matrixStack, font, "" + entry.weight, 18 + i * 18, 23, 0xffffff);
		}
	}

}
