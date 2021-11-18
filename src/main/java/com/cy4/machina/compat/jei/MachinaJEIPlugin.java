package com.cy4.machina.compat.jei;

import java.util.Collection;
import java.util.stream.Collectors;

import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingRecipe;
import com.cy4.machina.util.MachinaRL;

import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;

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
		
		registration.addRecipes(getRecipes(manager, AdvancedCraftingRecipe.TYPE), VANILLA_CRAFTING);
	}
	
	private static Collection<?> getRecipes(RecipeManager manager, IRecipeType<?> type) {
		return manager.getRecipes().parallelStream().filter(recipe -> recipe.getType() == type)
				.collect(Collectors.toList());
	}

}
