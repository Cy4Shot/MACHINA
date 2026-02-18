package com.machina.api.recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.machina.api.block.entity.RecipeBlockEntity;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public abstract class MachinaRecipeMaps<C extends RecipeInput> {

    protected final Map<ResourceLocation, RecipeHolder<? extends MachinaRecipe<C>>> recipes = new HashMap<>();

    public boolean isValid(C entity, RecipeHolder<? extends MachinaRecipe<C>> holder) {
        if (entity instanceof RecipeBlockEntity rbe) {
            MachinaRecipe<C> recipe = holder.value();
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

    public RecipeHolder<? extends MachinaRecipe<C>> getRecipe(ResourceLocation id) {
        return recipes.get(id);
    }

    public void refresh(RecipeManager man) {
        recipes.clear();
        for (RecipeHolder<? extends MachinaRecipe<C>> r : man.getAllRecipesFor(getRegistryObject().type().get())) {
            recipes.put(r.id(), r);
        }
        addExtraRecipes(man);
    }

    public void add(ResourceLocation id, MachinaRecipe<C> recipe) {
        recipes.put(id, new RecipeHolder<>(id, recipe));
    }

    public Optional<RecipeHolder<? extends MachinaRecipe<C>>> findRecipe(C entity) {
        return recipes.values().stream().filter(r -> isValid(entity, r)).findFirst();
    }

    public Optional<RecipeHolder<? extends MachinaRecipe<C>>> findRecipe(
            Predicate<? super RecipeHolder<? extends MachinaRecipe<C>>> output) {
        return recipes.values().stream().filter(output).findFirst();
    }

    public List<MachinaRecipe<C>> all() {
        return recipes.values().stream().map(RecipeHolder::value).collect(Collectors.toList());
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
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
    }

    protected static TagKey<Fluid> cf(String name) {
        return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath("c", name));
    }
}
