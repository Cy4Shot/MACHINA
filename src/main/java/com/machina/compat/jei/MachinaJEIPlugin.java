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

package com.machina.compat.jei;

import static com.machina.compat.jei.MachinaJEITypes.PLANET_TRAIT;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.planet.trait.pool.PlanetTraitPoolManager;
import com.machina.api.recipe.advanced_crafting.AdvancedCraftingRecipe;
import com.machina.api.util.MachinaRL;
import com.machina.compat.jei.category.AdvancedCraftingRecipeExtension;
import com.machina.compat.jei.category.PlanetTraitPoolRecipe;
import com.machina.compat.jei.category.PlanetTraitPoolsCategory;
import com.machina.compat.jei.trait.PlanetTraitJEIHelper;
import com.machina.compat.jei.trait.PlanetTraitJEIRenderer;
import com.machina.init.RecipeInit;

import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;

@JeiPlugin
public class MachinaJEIPlugin implements IModPlugin {

	public static final ResourceLocation PLUGIN_ID = new MachinaRL("jei_plugin");

	public static final ResourceLocation VANILLA_CRAFTING = new ResourceLocation("minecraft", "crafting");

	@Override
	public ResourceLocation getPluginUid() { return PLUGIN_ID; }

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(new PlanetTraitPoolsCategory(helper));
	}

	@SuppressWarnings("resource")
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

		registration.addRecipes(getRecipes(manager, RecipeInit.ADVANCED_CRAFTING_RECIPE_TYPE), VANILLA_CRAFTING);
		registration.addRecipes(getPlanetTraitPools(), PlanetTraitPoolsCategory.ID);

		addIngredientInfo(registration);
	}

	private static Collection<PlanetTraitPoolRecipe> getPlanetTraitPools() {
		return PlanetTraitPoolManager.INSTANCE.getEntrySet().stream()
				.map(entry -> new PlanetTraitPoolRecipe(entry.getValue(), entry.getKey().toString()))
				.collect(Collectors.toList());
	}

	private static Collection<?> getRecipes(RecipeManager manager, IRecipeType<?> type) {
		return manager.getRecipes().parallelStream().filter(recipe -> recipe.getType() == type)
				.collect(Collectors.toList());
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
		registration.getCraftingCategory().addCategoryExtension(AdvancedCraftingRecipe.class,
				AdvancedCraftingRecipeExtension::new);
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		List<PlanetTrait> traits = PlanetTrait.REGISTRY.getValues().stream().filter(PlanetTrait::showsInJei)
				.collect(Collectors.toList());
		PlanetTraitJEIHelper traitHelper = new PlanetTraitJEIHelper();
		PlanetTraitJEIRenderer traitRenderer = new PlanetTraitJEIRenderer();
		registration.register(MachinaJEITypes.PLANET_TRAIT, traits, traitHelper, traitRenderer);
	}

	public void addIngredientInfo(IRecipeRegistration registration) {
		PlanetTrait.REGISTRY.getValues().forEach(trait -> {
			if (trait.hasDescription()) {
				registration.addIngredientInfo(trait, PLANET_TRAIT,
						trait.getDescription().toArray(new ITextComponent[] {}));
			}
		});
	}

}
