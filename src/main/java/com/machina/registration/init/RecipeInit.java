package com.machina.registration.init;

import java.util.Map;

import com.machina.recipe.PressurizedChamberRecipe;
import com.machina.recipe.PressurizedChamberRecipe.PressurizedChamberRecipeType;
import com.machina.recipe.ShipConsoleRecipe;
import com.machina.recipe.ShipConsoleRecipe.ShipConsoleRecipeType;
import com.machina.recipe.StateConverterRecipe;
import com.machina.recipe.StateConverterRecipe.StateConverterRecipeType;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent.Register;

public class RecipeInit {
	public static final IRecipeType<ShipConsoleRecipe> SHIP_CONSOLE_RECIPE = new ShipConsoleRecipeType();
	public static final IRecipeType<PressurizedChamberRecipe> PRESSURIZED_CHAMBER_RECIPE = new PressurizedChamberRecipeType();
	public static final IRecipeType<StateConverterRecipe> STATE_CONVERTER_RECIPE = new StateConverterRecipeType();

	public static void registerRecipes(Register<IRecipeSerializer<?>> event) {
		registerRecipe(event, SHIP_CONSOLE_RECIPE, ShipConsoleRecipe.SERIALIZER);
		registerRecipe(event, PRESSURIZED_CHAMBER_RECIPE, PressurizedChamberRecipe.SERIALIZER);
		registerRecipe(event, STATE_CONVERTER_RECIPE, StateConverterRecipe.SERIALIZER);
	}

	private static void registerRecipe(Register<IRecipeSerializer<?>> event, IRecipeType<?> type,
			IRecipeSerializer<?> serializer) {
		Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(type.toString()), type);
		event.getRegistry().register(serializer);
	}

	public static Map<ResourceLocation, IRecipe<?>> getRecipes(IRecipeType<?> type, RecipeManager manager) {
		return manager.recipes.get(type);
	}
}
