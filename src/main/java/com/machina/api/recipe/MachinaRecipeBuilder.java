package com.machina.api.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;
import com.machina.api.util.MachinaRL;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class MachinaRecipeBuilder<T extends RecipeInput> implements RecipeBuilder {

    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    private final RecipeRegistryObject<T> reg;

    protected final List<ItemStack> inputItems = new ArrayList<>();
    protected final List<FluidStack> inputFluids = new ArrayList<>();
    protected final List<ItemStack> outputItems = new ArrayList<>();
    protected final List<FluidStack> outputFluids = new ArrayList<>();
    private int energy;
    private int time;
    private float pressure;
    private float temperature;
    private int periodicConsumption = 1;

    public MachinaRecipeBuilder(RecipeRegistryObject<T> reg) {
        this.reg = reg;
    }

    public MachinaRecipeBuilder<T> energy(int energy) {
        this.energy = energy;
        return this;
    }

    public MachinaRecipeBuilder<T> time(int time) {
        this.time = time;
        return this;
    }

    public MachinaRecipeBuilder<T> pressure(float pressure) {
        this.pressure = pressure;
        return this;
    }

    public MachinaRecipeBuilder<T> temp(float temperature) {
        this.temperature = temperature;
        return this;
    }

    public MachinaRecipeBuilder<T> period(int periodicConsumption) {
        this.periodicConsumption = periodicConsumption;
        return this;
    }

    public MachinaRecipeBuilder<T> in(Item item) {
        this.inputItems.add(new ItemStack(item, 1));
        return this;
    }

    public MachinaRecipeBuilder<T> in(ItemStack item) {
        this.inputItems.add(item);
        return this;
    }

    public MachinaRecipeBuilder<T> in(Item item, int count) {
        this.inputItems.add(new ItemStack(item, count));
        return this;
    }

    public MachinaRecipeBuilder<T> in(FluidStack input) {
        this.inputFluids.add(input);
        return this;
    }

    public MachinaRecipeBuilder<T> in(Fluid input, int count) {
        this.inputFluids.add(new FluidStack(input, count));
        return this;
    }

    public MachinaRecipeBuilder<T> out(Item item) {
        this.outputItems.add(new ItemStack(item, 1));
        return this;
    }

    public MachinaRecipeBuilder<T> out(Item item, int count) {
        this.outputItems.add(new ItemStack(item, count));
        return this;
    }

    public MachinaRecipeBuilder<T> out(FluidStack output) {
        this.outputFluids.add(output);
        return this;
    }

    public MachinaRecipeBuilder<T> out(Fluid output, int count) {
        this.outputFluids.add(new FluidStack(output, count));
        return this;
    }

    public static MachinaRecipeBuilder<?> create(RecipeRegistryObject<?> reg) {
        return new MachinaRecipeBuilder<>(reg);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.advancement.addCriterion(name, criterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(String group) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return Items.AIR;
    }

    @Override
    public void save(RecipeOutput p_176499_) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(RecipeOutput recipe, @NotNull String string) {
        this.save(recipe, MachinaRL.create(string));
    }

    @SuppressWarnings("removal")
    @Override
    public void save(RecipeOutput save, @NotNull ResourceLocation loc) {
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(loc))
                .rewards(AdvancementRewards.Builder.recipe(loc)).requirements(RequirementsStrategy.OR);
        save.accept(new Result<>(loc, this.advancement, this.reg, () -> this.reg.factory().apply(loc, energy, time,
                pressure, temperature, periodicConsumption, inputItems, inputFluids, outputItems, outputFluids)));
    }

    public void save(ResourceLocation loc, BiConsumer<ResourceLocation, MachinaRecipe<T>> saver) {
        saver.accept(loc, this.reg.factory().apply(loc, energy, time, pressure, temperature, periodicConsumption,
                inputItems, inputFluids, outputItems, outputFluids));
    }

    public static class Result<T extends RecipeInput> implements FinishedRecipe {

        private final ResourceLocation id;
        private final Advancement.Builder advancement;
        private final RecipeRegistryObject<T> reg;
        private final Supplier<MachinaRecipe<T>> recipe;

        protected Result(ResourceLocation id, Advancement.Builder advancement, RecipeRegistryObject<T> reg,
                         Supplier<MachinaRecipe<T>> recipe) {
            this.id = id;
            this.advancement = advancement;
            this.reg = reg;
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject obj) {
            this.reg.serializer().get().toJson(obj, this.recipe.get());
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return this.id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return this.reg.serializer().get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return this.id.withPrefix("recipes/misc/");
        }
    }

}
