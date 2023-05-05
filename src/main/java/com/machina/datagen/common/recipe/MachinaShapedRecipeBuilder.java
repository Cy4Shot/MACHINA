package com.machina.datagen.common.recipe;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

public class MachinaShapedRecipeBuilder {
	private final Item result;
	private final int count;
	private final List<String> rows = Lists.newArrayList();
	public final Map<Character, JsonElement> key = Maps.newLinkedHashMap();
	private final Advancement.Builder advancement = Advancement.Builder.advancement();
	private String group;
	public Ingredient has;

	public MachinaShapedRecipeBuilder(IItemProvider pResult, int pCount) {
		this.result = pResult.asItem();
		this.count = pCount;
	}

	public static MachinaShapedRecipeBuilder shaped(IItemProvider pResult) {
		return shaped(pResult, 1);
	}

	public static MachinaShapedRecipeBuilder shaped(IItemProvider pResult, int pCount) {
		return new MachinaShapedRecipeBuilder(pResult, pCount);
	}
	
	public MachinaShapedRecipeBuilder define(Character pSymbol, String tag) {
		if (this.key.containsKey(pSymbol)) {
			throw new IllegalArgumentException("Symbol '" + pSymbol + "' is already defined!");
		} else if (pSymbol == ' ') {
			throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
		} else {
			JsonObject obj = new JsonObject();
			obj.addProperty("tag", tag);
			this.key.put(pSymbol, obj);
			return this;
		}
	}


	public MachinaShapedRecipeBuilder define(Character pSymbol, ITag<Item> pTag) {
		return this.define(pSymbol, Ingredient.of(pTag));
	}

	public MachinaShapedRecipeBuilder define(Character pSymbol, IItemProvider pItem) {
		return this.define(pSymbol, Ingredient.of(pItem));
	}

	public MachinaShapedRecipeBuilder define(Character pSymbol, Ingredient pIngredient) {
		if (this.has == null) {
			this.has = pIngredient;
		}
		if (this.key.containsKey(pSymbol)) {
			throw new IllegalArgumentException("Symbol '" + pSymbol + "' is already defined!");
		} else if (pSymbol == ' ') {
			throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
		} else {
			this.key.put(pSymbol, pIngredient.toJson());
			return this;
		}
	}

	public MachinaShapedRecipeBuilder pattern(String pPattern) {
		if (!this.rows.isEmpty() && pPattern.length() != this.rows.get(0).length()) {
			throw new IllegalArgumentException("Pattern must be the same width on every line!");
		} else {
			this.rows.add(pPattern);
			return this;
		}
	}

	public MachinaShapedRecipeBuilder unlockedBy(String p_200465_1_, ICriterionInstance p_200465_2_) {
		this.advancement.addCriterion(p_200465_1_, p_200465_2_);
		return this;
	}

	public MachinaShapedRecipeBuilder group(String p_200473_1_) {
		this.group = p_200473_1_;
		return this;
	}

	public void save(Consumer<IFinishedRecipe> p_200464_1_) {
		this.save(p_200464_1_, Registry.ITEM.getKey(this.result));
	}

	public void save(Consumer<IFinishedRecipe> p_200466_1_, String p_200466_2_) {
		ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
		if ((new ResourceLocation(p_200466_2_)).equals(resourcelocation)) {
			throw new IllegalStateException("Shaped Recipe " + p_200466_2_ + " should remove its 'save' argument");
		} else {
			this.save(p_200466_1_, new MachinaRL(p_200466_2_));
		}
	}

	public void save(Consumer<IFinishedRecipe> p_200467_1_, ResourceLocation p_200467_2_) {
		this.ensureValid(p_200467_2_);
		this.advancement.parent(new ResourceLocation("recipes/root"))
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_200467_2_))
				.rewards(AdvancementRewards.Builder.recipe(p_200467_2_)).requirements(IRequirementsStrategy.OR);
		p_200467_1_.accept(new MachinaShapedRecipeBuilder.Result(p_200467_2_, this.result, this.count,
				this.group == null ? "" : this.group, this.rows, this.key, this.advancement,
				new ResourceLocation(p_200467_2_.getNamespace(), "recipes/" + p_200467_2_.getPath())));
	}

	/**
	 * Makes sure that this recipe is valid and obtainable.
	 */
	private void ensureValid(ResourceLocation pId) {
		if (this.rows.isEmpty()) {
			throw new IllegalStateException("No pattern is defined for shaped recipe " + pId + "!");
		} else {
			Set<Character> set = Sets.newHashSet(this.key.keySet());
			set.remove(' ');

			for (String s : this.rows) {
				for (int i = 0; i < s.length(); ++i) {
					char c0 = s.charAt(i);
					if (!this.key.containsKey(c0) && c0 != ' ') {
						throw new IllegalStateException(
								"Pattern in recipe " + pId + " uses undefined symbol '" + c0 + "'");
					}

					set.remove(c0);
				}
			}

			if (!set.isEmpty()) {
				throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + pId);
			} else if (this.rows.size() == 1 && this.rows.get(0).length() == 1) {
				throw new IllegalStateException("Shaped recipe " + pId
						+ " only takes in a single item - should it be a shapeless recipe instead?");
			} else if (this.advancement.getCriteria().isEmpty()) {
				throw new IllegalStateException("No way of obtaining recipe " + pId);
			}
		}
	}

	public class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final Item result;
		private final int count;
		private final String group;
		private final List<String> pattern;
		private final Map<Character, JsonElement> key;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;

		public Result(ResourceLocation p_i48271_2_, Item p_i48271_3_, int p_i48271_4_, String p_i48271_5_,
				List<String> p_i48271_6_, Map<Character, JsonElement> p_i48271_7_, Advancement.Builder p_i48271_8_,
				ResourceLocation p_i48271_9_) {
			this.id = p_i48271_2_;
			this.result = p_i48271_3_;
			this.count = p_i48271_4_;
			this.group = p_i48271_5_;
			this.pattern = p_i48271_6_;
			this.key = p_i48271_7_;
			this.advancement = p_i48271_8_;
			this.advancementId = p_i48271_9_;
		}

		public void serializeRecipeData(JsonObject pJson) {
			if (!this.group.isEmpty()) {
				pJson.addProperty("group", this.group);
			}

			JsonArray jsonarray = new JsonArray();

			for (String s : this.pattern) {
				jsonarray.add(s);
			}

			pJson.add("pattern", jsonarray);
			JsonObject jsonobject = new JsonObject();

			for (Entry<Character, JsonElement> entry : this.key.entrySet()) {
				jsonobject.add(String.valueOf(entry.getKey()), entry.getValue());
			}

			pJson.add("key", jsonobject);
			JsonObject jsonobject1 = new JsonObject();
			jsonobject1.addProperty("item", Registry.ITEM.getKey(this.result).toString());
			if (this.count > 1) {
				jsonobject1.addProperty("count", this.count);
			}

			pJson.add("result", jsonobject1);
		}

		public IRecipeSerializer<?> getType() {
			return IRecipeSerializer.SHAPED_RECIPE;
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
