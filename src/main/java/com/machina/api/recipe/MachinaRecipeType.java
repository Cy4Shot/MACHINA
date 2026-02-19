package com.machina.api.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

public record MachinaRecipeType<R extends RecipeInput>(ResourceLocation name,
                                                       int flags) implements RecipeType<MachinaRecipe<R>> {


    @Override
    public String toString() {
        return name.toString();
    }

    public RecipeType<MachinaRecipe<R>> get() {
        return this;
    }
}
