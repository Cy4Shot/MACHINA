package com.machina.client.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.machina.client.jei.category.BaseCategory;
import com.machina.client.jei.category.StateConverterCategory;
import com.machina.recipe.IMachinaRecipe;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.RecipeInit;
import com.machina.util.text.MachinaRL;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class MachinaJEI implements IModPlugin {

	private static List<Entry<? extends IMachinaRecipe>> ENTRIES = new ArrayList<>();

	static {
		register(RecipeInit.STATE_CONVERTER_RECIPE, StateConverterCategory.UID, BlockInit.STATE_CONVERTER.get(),
				StateConverterCategory::new);
	}

	@Override
	public ResourceLocation getPluginUid() {
		return new MachinaRL("jei_plugin");
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		ENTRIES.forEach(entry -> {
			registration.addRecipeCatalyst(entry.stack, entry.id);
		});
	}

	@SuppressWarnings("resource")
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager man = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

		ENTRIES.forEach(entry -> {
			registration.addRecipes(RecipeInit.getRecipes(entry.recipe, man).values(), entry.id);
		});
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();

		ENTRIES.forEach(entry -> {
			registration.addRecipeCategories(entry.category.apply(helper));
		});
	}

	private static <T extends IMachinaRecipe> void register(IRecipeType<T> recipe, ResourceLocation id,
			IItemProvider block, Function<IGuiHelper, ? extends BaseCategory<T>> category) {
		ENTRIES.add(new Entry<T>(recipe, id, block, category));
	}

	public static class Entry<T extends IMachinaRecipe> {
		ItemStack stack;
		ResourceLocation id;
		IRecipeType<T> recipe;
		Function<IGuiHelper, ? extends BaseCategory<T>> category;

		public Entry(IRecipeType<T> recipe, ResourceLocation id, IItemProvider block,
				Function<IGuiHelper, ? extends BaseCategory<T>> category) {
			this.recipe = recipe;
			this.stack = new ItemStack(block);
			this.id = id;
			this.category = category;
		}
	}
}