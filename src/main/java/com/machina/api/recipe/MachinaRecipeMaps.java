package com.machina.api.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.machina.api.block.entity.RecipeBlockEntity;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public abstract class MachinaRecipeMaps<C extends Container> {

	protected final Map<ResourceLocation, MachinaRecipe<C>> recipes = new HashMap<>();

	public boolean isValid(C entity, MachinaRecipe<C> recipe) {
		if (entity instanceof RecipeBlockEntity rbe) {
			if (isExact()) {
				return rbe.hasExactItemInputs(recipe.getInputItems())
						&& rbe.hasExactFluidInputs(recipe.getInputFluids());
			}
			for (ItemStack i : recipe.getInputItems()) {
				if (!rbe.hasItemInput(i))
					return false;
			}
			for (FluidStack f : recipe.getInputFluids()) {
				if (!rbe.hasFluidInput(f))
					return false;
			}

			return true;
		}

		return false;
	}

	public boolean isExact() {
		return true;
	}

	protected abstract RecipeRegistryObject<C> getRegistryObject();

	protected abstract void addExtraRecipes(RecipeManager man);

	public abstract int getFlags();

	@SuppressWarnings("unchecked")
	public <T extends MachinaRecipe<C>> T getRecipe(ResourceLocation id) {
		return (T) recipes.get(id);
	}

	public void refresh(RecipeManager man) {
		recipes.clear();
		for (MachinaRecipe<C> r : man.getAllRecipesFor(getRegistryObject().type().get())) {
			recipes.put(r.getId(), r);
		}
		addExtraRecipes(man);
	}

	public void add(ResourceLocation id, MachinaRecipe<C> recipe) {
		recipes.put(id, recipe);
	}

	@SuppressWarnings("unchecked")
	public <T extends MachinaRecipe<C>> Optional<T> findRecipe(C entity) {
		return (Optional<T>) recipes.values().stream().filter(r -> isValid(entity, r)).findFirst();
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

	public boolean hasEnergy() {
		return (getFlags() & MachinaRecipe.HAS_ENERGY) != 0;
	}

	public boolean hasTime() {
		return (getFlags() & MachinaRecipe.HAS_TIME) != 0;
	}

	public boolean hasPressure() {
		return (getFlags() & MachinaRecipe.HAS_PRESSURE) != 0;
	}

	public boolean hasTemperature() {
		return (getFlags() & MachinaRecipe.HAS_TEMPERATURE) != 0;
	}

	public boolean hasPeriodicConsumption() {
		return (getFlags() & MachinaRecipe.HAS_PERIODIC_CONSUMPTION) != 0;
	}

	protected MachinaRecipeBuilder<C> builder() {
		return new MachinaRecipeBuilder<>(getRegistryObject());
	}

	protected static TagKey<Item> ci(String name) {
		return TagKey.create(Registries.ITEM, new ResourceLocation("c", name));
	}

	protected static TagKey<Fluid> cf(String name) {
		return TagKey.create(Registries.FLUID, new ResourceLocation("c", name));
	}
}
