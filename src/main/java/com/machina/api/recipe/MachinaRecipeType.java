package com.machina.api.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

public class MachinaRecipeType<R extends RecipeInput> implements RecipeType<MachinaRecipe<R>> {
    private final ResourceLocation name;
    private final int flags;

    public MachinaRecipeType(ResourceLocation name, int flags) {
        this.name = name;
        this.flags = flags;
    }

    public int getFlags() {
        return flags;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    public RecipeType<MachinaRecipe<R>> get() {
        return this;
    }
}
