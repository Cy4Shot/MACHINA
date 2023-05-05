package com.machina.registration.init;

import java.util.Map;

import com.machina.recipe.impl.FabricatorRecipe;
import com.machina.recipe.impl.FabricatorRecipe.FabricatorRecipeType;
import com.machina.recipe.impl.HaberRecipe;
import com.machina.recipe.impl.HaberRecipe.HaberRecipeType;
import com.machina.recipe.impl.MelterRecipe;
import com.machina.recipe.impl.MelterRecipe.MelterRecipeType;
import com.machina.recipe.impl.MixerRecipe;
import com.machina.recipe.impl.MixerRecipe.MixerRecipeType;
import com.machina.recipe.impl.ShipConsoleRecipe;
import com.machina.recipe.impl.ShipConsoleRecipe.ShipConsoleRecipeType;
import com.machina.recipe.impl.StateConverterRecipe;
import com.machina.recipe.impl.StateConverterRecipe.StateConverterRecipeType;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent.Register;

public class RecipeInit {
	public static final IRecipeType<ShipConsoleRecipe> SHIP_CONSOLE_RECIPE = new ShipConsoleRecipeType();
	public static final IRecipeType<StateConverterRecipe> STATE_CONVERTER_RECIPE = new StateConverterRecipeType();
	public static final IRecipeType<FabricatorRecipe> FABRICATOR_RECIPE = new FabricatorRecipeType();
	public static final IRecipeType<HaberRecipe> HABER_RECIPE = new HaberRecipeType();
	public static final IRecipeType<MixerRecipe> MIXER_RECIPE = new MixerRecipeType();
	public static final IRecipeType<MelterRecipe> MELTER_RECIPE = new MelterRecipeType();

	public static void registerRecipes(Register<IRecipeSerializer<?>> event) {
		registerRecipe(event, SHIP_CONSOLE_RECIPE, ShipConsoleRecipe.SERIALIZER);
		registerRecipe(event, STATE_CONVERTER_RECIPE, StateConverterRecipe.SERIALIZER);
		registerRecipe(event, FABRICATOR_RECIPE, FabricatorRecipe.SERIALIZER);
		registerRecipe(event, HABER_RECIPE, HaberRecipe.SERIALIZER);
		registerRecipe(event, MIXER_RECIPE, MixerRecipe.SERIALIZER);
		registerRecipe(event, MELTER_RECIPE, MelterRecipe.SERIALIZER);
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
