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
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class CustomSmithingRecipeBuilder {
	private final Ingredient base;
	private final Ingredient addition;
	private final Item result;
	private final Advancement.Builder advancement = Advancement.Builder.advancement();
	private final IRecipeSerializer<?> type;

	public CustomSmithingRecipeBuilder(IRecipeSerializer<?> pType, Ingredient pBase, Ingredient pAddition,
			Item pResult) {
		this.type = pType;
		this.base = pBase;
		this.addition = pAddition;
		this.result = pResult;
	}

	public static CustomSmithingRecipeBuilder smithing(Ingredient pBase, Ingredient pAddition, Item pResult) {
		return new CustomSmithingRecipeBuilder(IRecipeSerializer.SMITHING, pBase, pAddition, pResult);
	}

	public CustomSmithingRecipeBuilder unlocks(String pName, ICriterionInstance pCriterion) {
		this.advancement.addCriterion(pName, pCriterion);
		return this;
	}

	public void save(Consumer<IFinishedRecipe> pFinishedRecipeConsumer, String pId) {
		this.save(pFinishedRecipeConsumer, new MachinaRL(pId));
	}

	public void save(Consumer<IFinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pId) {
		this.ensureValid(pId);
		this.advancement.parent(new ResourceLocation("recipes/root"))
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
				.rewards(AdvancementRewards.Builder.recipe(pId)).requirements(IRequirementsStrategy.OR);
		pFinishedRecipeConsumer.accept(new CustomSmithingRecipeBuilder.Result(pId, this.type, this.base, this.addition,
				this.result, this.advancement, new ResourceLocation(pId.getNamespace(), "recipes/" + pId.getPath())));
	}

	private void ensureValid(ResourceLocation pId) {
		if (this.advancement.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + pId);
		}
	}

	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final Ingredient base;
		private final Ingredient addition;
		private final Item result;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;
		private final IRecipeSerializer<?> type;

		public Result(ResourceLocation pId, IRecipeSerializer<?> pType, Ingredient pBase, Ingredient pAddition,
				Item pResult, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId) {
			this.id = pId;
			this.type = pType;
			this.base = pBase;
			this.addition = pAddition;
			this.result = pResult;
			this.advancement = pAdvancement;
			this.advancementId = pAdvancementId;
		}

		@SuppressWarnings("deprecation")
		public void serializeRecipeData(JsonObject pJson) {
			pJson.add("base", this.base.toJson());
			pJson.add("addition", this.addition.toJson());
			JsonObject jsonobject = new JsonObject();
			jsonobject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
			pJson.add("result", jsonobject);
		}

		public ResourceLocation getId() {
			return this.id;
		}

		public IRecipeSerializer<?> getType() {
			return this.type;
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
