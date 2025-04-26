package com.machina.api.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.machina.api.block.entity.RecipeBlockEntity;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public abstract class MachinaRecipeMaps<C extends Container> {

	private final Map<ResourceLocation, MachinaRecipe<C>> recipes = new HashMap<>();

	public boolean isValid(C entity, MachinaRecipe<C> recipe) {
		if (entity instanceof RecipeBlockEntity<?> rbe) {
			for (Ingredient i : recipe.getInputItems()) {
				if (!rbe.hasAnyOf(Arrays.stream(i.getItems()).map(ItemStack::getItem).collect(Collectors.toSet())))
					return false;
			}
			for (FluidStack f : recipe.getInputFluids()) {
				if (!rbe.hasFluid(f))
					return false;
			}

			return true;
		}

		return false;
	}

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

	public void add(MachinaRecipe<C> recipe) {
		add(recipe.getId(), recipe);
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

	public boolean hasInputs() {
		return true;
	}

	public boolean hasOutputs() {
		return true;
	}

	protected static TagKey<Item> ci(String name) {
		return TagKey.create(Registries.ITEM, new ResourceLocation("c", name));
	}

	protected static TagKey<Fluid> cf(String name) {
		return TagKey.create(Registries.FLUID, new ResourceLocation("c", name));
	}
}
