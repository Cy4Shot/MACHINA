package com.machina.datagen.builder;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.machina.util.MachinaRL;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class CustomCookingRecipeBuilder {
	private final Item result;
	private final Ingredient ingredient;
	private final float experience;
	private final int cookingTime;
	private final Advancement.Builder advancement = Advancement.Builder.advancement();
	private String group;
	private final CookingRecipeSerializer<?> serializer;

	private CustomCookingRecipeBuilder(IItemProvider pResult, Ingredient pIngredient, float pExperience,
			int pCookingTime, CookingRecipeSerializer<?> pSerializer) {
		this.result = pResult.asItem();
		this.ingredient = pIngredient;
		this.experience = pExperience;
		this.cookingTime = pCookingTime;
		this.serializer = pSerializer;
	}

	public static CustomCookingRecipeBuilder cooking(Ingredient pIngredient, IItemProvider pResult, float pExperience,
			int pCookingTime, CookingRecipeSerializer<?> pSerializer) {
		return new CustomCookingRecipeBuilder(pResult, pIngredient, pExperience, pCookingTime, pSerializer);
	}

	public static CustomCookingRecipeBuilder blasting(Ingredient pIngredient, IItemProvider pResult, float pExperience,
			int pCookingTime) {
		return cooking(pIngredient, pResult, pExperience, pCookingTime, IRecipeSerializer.BLASTING_RECIPE);
	}

	public static CustomCookingRecipeBuilder smelting(Ingredient pIngredient, IItemProvider pResult, float pExperience,
			int pCookingTime) {
		return cooking(pIngredient, pResult, pExperience, pCookingTime, IRecipeSerializer.SMELTING_RECIPE);
	}

	public CustomCookingRecipeBuilder unlockedBy(String p_218628_1_, ICriterionInstance p_218628_2_) {
		this.advancement.addCriterion(p_218628_1_, p_218628_2_);
		return this;
	}

	@SuppressWarnings("deprecation")
	public void save(Consumer<IFinishedRecipe> p_218630_1_) {
		this.save(p_218630_1_, Registry.ITEM.getKey(this.result));
	}

	@SuppressWarnings("deprecation")
	public void save(Consumer<IFinishedRecipe> p_218632_1_, String p_218632_2_) {
		ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
		ResourceLocation resourcelocation1 = new MachinaRL(p_218632_2_);
		if (resourcelocation1.equals(resourcelocation)) {
			throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
		} else {
			this.save(p_218632_1_, resourcelocation1);
		}
	}

	public void save(Consumer<IFinishedRecipe> p_218635_1_, ResourceLocation p_218635_2_) {
		this.ensureValid(p_218635_2_);
		this.advancement.parent(new ResourceLocation("recipes/root"))
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_218635_2_))
				.rewards(AdvancementRewards.Builder.recipe(p_218635_2_)).requirements(IRequirementsStrategy.OR);
		p_218635_1_.accept(new CustomCookingRecipeBuilder.Result(p_218635_2_, this.group == null ? "" : this.group,
				this.ingredient, this.result, this.experience, this.cookingTime, this.advancement,
				new ResourceLocation(p_218635_2_.getNamespace(), "recipes/" + p_218635_2_.getPath()), this.serializer));
	}

	private void ensureValid(ResourceLocation pId) {
		if (this.advancement.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + pId);
		}
	}

	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final Ingredient ingredient;
		private final Item result;
		private final float experience;
		private final int cookingTime;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;
		private final IRecipeSerializer<? extends AbstractCookingRecipe> serializer;

		public Result(ResourceLocation pId, String pGroup, Ingredient pIngredient, Item pResult, float pExperience,
				int pCookingTime, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId,
				IRecipeSerializer<? extends AbstractCookingRecipe> pSerializer) {
			this.id = pId;
			this.group = pGroup;
			this.ingredient = pIngredient;
			this.result = pResult;
			this.experience = pExperience;
			this.cookingTime = pCookingTime;
			this.advancement = pAdvancement;
			this.advancementId = pAdvancementId;
			this.serializer = pSerializer;
		}

		@SuppressWarnings("deprecation")
		public void serializeRecipeData(JsonObject pJson) {
			if (!this.group.isEmpty()) {
				pJson.addProperty("group", this.group);
			}

			pJson.add("ingredient", this.ingredient.toJson());
			pJson.addProperty("result", Registry.ITEM.getKey(this.result).toString());
			pJson.addProperty("experience", this.experience);
			pJson.addProperty("cookingtime", this.cookingTime);
		}

		public IRecipeSerializer<?> getType() {
			return this.serializer;
		}

		public ResourceLocation getId() {
			return this.id;
		}

		@Nullable
		public JsonObject serializeAdvancement() {
			return this.advancement.serializeToJson();
		}

		@Nullable
		public ResourceLocation getAdvancementId() {
			return this.advancementId;
		}
	}
}
