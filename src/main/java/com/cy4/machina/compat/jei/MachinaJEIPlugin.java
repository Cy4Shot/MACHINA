package com.cy4.machina.compat.jei;

import static com.cy4.machina.compat.jei.MachinaJEITypes.PLANET_TRAIT;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingRecipe;
import com.cy4.machina.compat.jei.category.AdvancedCraftingRecipeExtension;
import com.cy4.machina.compat.jei.trait.PlanetTraitJEIHelper;
import com.cy4.machina.compat.jei.trait.PlanetTraitJEIRenderer;
import com.cy4.machina.init.RecipeInit;
import com.cy4.machina.util.MachinaRL;

import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;

@JeiPlugin
public class MachinaJEIPlugin implements IModPlugin {

	public static final ResourceLocation PLUGIN_ID = new MachinaRL("jei_plugin");

	public static final ResourceLocation VANILLA_CRAFTING = new ResourceLocation("minecraft", "crafting");

	@Override
	public ResourceLocation getPluginUid() { return PLUGIN_ID; }

	@SuppressWarnings("resource")
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

		registration.addRecipes(getRecipes(manager, RecipeInit.ADVANCED_CRAFTING_RECIPE_TYPE), VANILLA_CRAFTING);

		addIngredientInfo(registration);
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
