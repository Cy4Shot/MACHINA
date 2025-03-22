package com.machina.compat.jei;

import org.jetbrains.annotations.NotNull;

import com.machina.api.util.MachinaRL;
import com.machina.client.screen.menu.connector.FluidPipeScreen;
import com.machina.client.screen.menu.item.AdvancedItemFilterScreen;
import com.machina.client.screen.menu.item.FluidFilterScreen;
import com.machina.client.screen.menu.item.ItemFilterScreen;
import com.machina.compat.jei.base.MachinaGhostHandler;
import com.machina.compat.jei.category.GrinderRecipeCategory;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
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
	public void registerCategories(IRecipeCategoryRegistration reg) {
		IGuiHelper gui = reg.getJeiHelpers().getGuiHelper();

		reg.addRecipeCategories(new GrinderRecipeCategory(gui));
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

	private <T extends Container> void registerRecipe(IRecipeRegistration reg, RecipeRegistryObject<T> obj) {
		reg.addRecipes(obj.jeiType(), obj.maps().all());
	}

	@Override
	public void registerRecipes(IRecipeRegistration reg) {
		registerRecipe(reg, RecipeInit.GRINDER);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
		reg.addRecipeCatalyst(BlockInit.GRINDER.get().asItem().getDefaultInstance(), RecipeInit.GRINDER.jeiType());
	}
}
