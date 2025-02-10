package com.machina.api.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.gson.JsonObject;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class MachinaRecipeBuilder<T extends Container> implements RecipeBuilder {

	private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

	private final RecipeRegistryObject<T> reg;
	private final float xp;

    protected final List<Ingredient> inputItems = new ArrayList<>();
	protected final List<FluidStack> inputFluids = new ArrayList<>();
	protected final List<ItemStack> outputItems = new ArrayList<>();
	protected final List<FluidStack> outputFluids = new ArrayList<>();
	private int energy;
	private int time;
	private float pressure;
	private float temperature;

	public MachinaRecipeBuilder(RecipeRegistryObject<T> reg, float xp) {
		this.reg = reg;
		this.xp = xp;
	}

	public MachinaRecipeBuilder<T> withEnergy(int energy) {
		this.energy = energy;
		return this;
	}

	public MachinaRecipeBuilder<T> withTime(int time) {
		this.time = time;
		return this;
	}

	public MachinaRecipeBuilder<T> withPressure(float pressure) {
		this.pressure = pressure;
		return this;
	}

	public MachinaRecipeBuilder<T> withTemperature(float temperature) {
		this.temperature = temperature;
		return this;
	}

	public MachinaRecipeBuilder<T> withInputItem(Item item, int count) {
		this.inputItems.add(Ingredient.of(new ItemStack(item, count)));
		return this;
	}

	public MachinaRecipeBuilder<T> withInputFluid(FluidStack input) {
		this.inputFluids.add(input);
		return this;
	}

	public MachinaRecipeBuilder<T> withOutputItem(Item item, int count) {
		this.outputItems.add(new ItemStack(item, count));
		return this;
	}

	public MachinaRecipeBuilder<T> withOutputFluid(FluidStack output) {
		this.outputFluids.add(output);
		return this;
	}

	public static MachinaRecipeBuilder<?> create(RecipeRegistryObject<?> reg, float xp) {
		return new MachinaRecipeBuilder<>(reg, xp);
	}

	@Override
	public @NotNull RecipeBuilder unlockedBy(@NotNull String key, @NotNull CriterionTriggerInstance criterion) {
		this.advancement.addCriterion(key, criterion);
		return this;
	}

	// TODO: What?
	@SuppressWarnings("DataFlowIssue")
	@Override
	public @NotNull RecipeBuilder group(String group) {
        return null;
	}

	// TODO: What?
	@Override
	@SuppressWarnings("DataFlowIssue")
	public @NotNull Item getResult() {
		return null;
	}

	@Override
	public void save(@NotNull Consumer<FinishedRecipe> p_176499_) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void save(@NotNull Consumer<FinishedRecipe> recipe, @NotNull String string) {
		this.save(recipe, new ResourceLocation(string));
	}

	@Override
	public void save(Consumer<FinishedRecipe> save, @NotNull ResourceLocation loc) {
		this.advancement.parent(ROOT_RECIPE_ADVANCEMENT)
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(loc))
				.rewards(AdvancementRewards.Builder.recipe(loc)).requirements(RequirementsStrategy.OR);
		save.accept(new Result<>(loc, this.advancement, this.reg, () -> this.reg.factory().apply(loc, energy, time,
                pressure, temperature, xp, inputItems, inputFluids, outputItems, outputFluids)));
	}

	public static class Result<T extends Container> implements FinishedRecipe {

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
