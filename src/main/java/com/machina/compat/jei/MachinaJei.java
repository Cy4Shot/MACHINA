package com.machina.compat.jei;

import org.jetbrains.annotations.NotNull;

import com.machina.api.util.MachinaRL;
import com.machina.client.screen.menu.connector.FluidPipeScreen;
import com.machina.client.screen.menu.item.AdvancedItemFilterScreen;
import com.machina.client.screen.menu.item.FluidFilterScreen;
import com.machina.client.screen.menu.item.ItemFilterScreen;
import com.machina.compat.jei.base.MachinaGhostHandler;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;

@JeiPlugin
public class MachinaJei implements IModPlugin {

	@Override
	public @NotNull ResourceLocation getPluginUid() {
		return new MachinaRL("jei_plugin");
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration reg) {
		//@formatter:off
		reg.addGhostIngredientHandler(ItemFilterScreen.class, new MachinaGhostHandler<ItemFilterScreen>());
		reg.addGhostIngredientHandler(AdvancedItemFilterScreen.class, new MachinaGhostHandler<AdvancedItemFilterScreen>());
		reg.addGhostIngredientHandler(FluidFilterScreen.class, new MachinaGhostHandler<FluidFilterScreen>());
		reg.addGhostIngredientHandler(FluidPipeScreen.class, new MachinaGhostHandler<FluidPipeScreen>());
		//@formatter:on
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration reg) {
		IGuiHelper gui = reg.getJeiHelpers().getGuiHelper();

		RecipeInit.RECIPES.forEach(recipe -> {
			reg.addRecipeCategories(recipe.jei().category(gui));
		});
	}

	private <T extends Container> void registerRecipe(IRecipeRegistration reg, RecipeRegistryObject<T> obj) {
		reg.addRecipes(obj.jei().type(), obj.maps().all());
	}

	@Override
	public void registerRecipes(IRecipeRegistration reg) {
		RecipeInit.RECIPES.forEach(recipe -> {
			registerRecipe(reg, recipe);
		});
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
		RecipeInit.RECIPES.forEach(recipe -> {
			reg.addRecipeCatalyst(recipe.block().get().asItem().getDefaultInstance(), recipe.jei().type());
		});

		reg.addRecipeCatalyst(BlockInit.ELECTRIC_SMELTER.get().asItem().getDefaultInstance(), RecipeTypes.SMELTING);
	}
}
