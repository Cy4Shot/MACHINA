package com.machina.api.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.machina.registration.init.RecipeInit.RecipeRegistryObject;
import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeManager;

public abstract class MachinaRecipeMaps<C extends Container> {

	private final Map<ResourceLocation, MachinaRecipe<C>> recipes = new HashMap<>();

	public abstract boolean isValid(C entity, MachinaRecipe<C> recipe);

	protected abstract RecipeRegistryObject<C> getRegistryObject();

	protected abstract void addExtraRecipes(RecipeManager man);

	public abstract Class<? extends MachinaRecipe<C>> getRecipeClass();
	
	public abstract int getFlags();

	@SuppressWarnings("unchecked")
	public <T extends MachinaRecipe<C>> T getRecipe(ResourceLocation id) {
		Class<T> clazz = (Class<T>) getRecipeClass();
		return clazz.cast(recipes.get(id));
	}

	public void refresh(RecipeManager man) {
		recipes.clear();
		for (MachinaRecipe<C> r : man.getAllRecipesFor(getRegistryObject().type().get())) {
			recipes.put(r.getId(), r);
		}
		addExtraRecipes(man);
	}

	public void add(Pair<ResourceLocation, MachinaRecipe<C>> pair) {
		add(pair.getFirst(), pair.getSecond());
	}

	public void add(ResourceLocation id, MachinaRecipe<C> recipe) {
		recipes.put(id, recipe);
	}

	@SuppressWarnings("unchecked")
	public <T extends MachinaRecipe<C>> Optional<T> findRecipe(C entity) {
		Class<T> clazz = (Class<T>) getRecipeClass();
		return recipes.values().stream().filter(r -> isValid(entity, r)).findFirst().map(clazz::cast);
	}

	public List<MachinaRecipe<C>> all() {
		return new ArrayList<>(recipes.values());
	}
}
