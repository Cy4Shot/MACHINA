package com.machina.datagen.common.recipe;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
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
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class MachinaShapelessRecipeBuilder {
	private final Item result;
	private final int count;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final Advancement.Builder advancement = Advancement.Builder.advancement();
	private String group;

	public MachinaShapelessRecipeBuilder(IItemProvider pResult, int pCount) {
		this.result = pResult.asItem();
		this.count = pCount;
	}

	public static MachinaShapelessRecipeBuilder shapeless(IItemProvider pResult) {
		return new MachinaShapelessRecipeBuilder(pResult, 1);
	}

	public static MachinaShapelessRecipeBuilder shapeless(IItemProvider pResult, int pCount) {
		return new MachinaShapelessRecipeBuilder(pResult, pCount);
	}

	public MachinaShapelessRecipeBuilder requires(ITag<Item> pTag) {
		return this.requires(Ingredient.of(pTag));
	}

	public MachinaShapelessRecipeBuilder requires(IItemProvider... items) {
		for (int i = 0; i < items.length; ++i) {
			this.requires(Ingredient.of(items[i]));
		}

		return this;
	}

	public MachinaShapelessRecipeBuilder requires(IItemProvider pItem) {
		return this.requires(pItem, 1);
	}

	public MachinaShapelessRecipeBuilder requires(IItemProvider pItem, int pQuantity) {
		for (int i = 0; i < pQuantity; ++i) {
			this.requires(Ingredient.of(pItem));
		}

		return this;
	}

	public MachinaShapelessRecipeBuilder requires(Ingredient pIngredient) {
		return this.requires(pIngredient, 1);
	}

	public MachinaShapelessRecipeBuilder requires(Ingredient pIngredient, int pQuantity) {
		for (int i = 0; i < pQuantity; ++i) {
			this.ingredients.add(pIngredient);
		}

		return this;
	}

	public MachinaShapelessRecipeBuilder unlockedBy(String p_200483_1_, ICriterionInstance p_200483_2_) {
		this.advancement.addCriterion(p_200483_1_, p_200483_2_);
		return this;
	}

	public MachinaShapelessRecipeBuilder group(String p_200490_1_) {
		this.group = p_200490_1_;
		return this;
	}

	public void save(Consumer<IFinishedRecipe> p_200482_1_) {
		this.save(p_200482_1_, Registry.ITEM.getKey(this.result));
	}

	public void save(Consumer<IFinishedRecipe> p_200484_1_, String p_200484_2_) {
		ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
		if ((new ResourceLocation(p_200484_2_)).equals(resourcelocation)) {
			throw new IllegalStateException("Shapeless Recipe " + p_200484_2_ + " should remove its 'save' argument");
		} else {
			this.save(p_200484_1_, new MachinaRL(p_200484_2_));
		}
	}

	public void save(Consumer<IFinishedRecipe> p_200485_1_, ResourceLocation p_200485_2_) {
		this.ensureValid(p_200485_2_);
		this.advancement.parent(new ResourceLocation("recipes/root"))
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_200485_2_))
				.rewards(AdvancementRewards.Builder.recipe(p_200485_2_)).requirements(IRequirementsStrategy.OR);
		p_200485_1_.accept(new MachinaShapelessRecipeBuilder.Result(p_200485_2_, this.result, this.count,
				this.group == null ? "" : this.group, this.ingredients, this.advancement,
				new ResourceLocation(p_200485_2_.getNamespace(), "recipes/" + p_200485_2_.getPath())));
	}

	private void ensureValid(ResourceLocation pId) {
		if (this.advancement.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + pId);
		}
	}

	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final Item result;
		private final int count;
		private final String group;
		private final List<Ingredient> ingredients;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;

		public Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, List<Ingredient> pIngredients,
				Advancement.Builder pAdvancement, ResourceLocation pAdvancementId) {
			this.id = pId;
			this.result = pResult;
			this.count = pCount;
			this.group = pGroup;
			this.ingredients = pIngredients;
			this.advancement = pAdvancement;
			this.advancementId = pAdvancementId;
		}

		public void serializeRecipeData(JsonObject pJson) {
			if (!this.group.isEmpty()) {
				pJson.addProperty("group", this.group);
			}

			JsonArray jsonarray = new JsonArray();

			for (Ingredient ingredient : this.ingredients) {
				jsonarray.add(ingredient.toJson());
			}

			pJson.add("ingredients", jsonarray);
			JsonObject jsonobject = new JsonObject();
			jsonobject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
			if (this.count > 1) {
				jsonobject.addProperty("count", this.count);
			}

			pJson.add("result", jsonobject);
		}

		public IRecipeSerializer<?> getType() {
			return IRecipeSerializer.SHAPELESS_RECIPE;
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
