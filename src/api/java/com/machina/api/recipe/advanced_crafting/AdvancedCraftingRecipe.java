/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.api.recipe.advanced_crafting;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.machina.api.registry.annotation.RegistryHolder;
import com.machina.api.registry.annotation.recipe.RegisterRecipeSerializer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.registries.ForgeRegistryEntry;

@RegistryHolder
public class AdvancedCraftingRecipe extends ShapedRecipe {

	private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

	@RegisterRecipeSerializer("advanced_crafting")
	public static final Serializer SERIALIZER = new Serializer();

	private final JsonObject functionData;

	public AdvancedCraftingRecipe(ResourceLocation id, String group, int width, int height,
			NonNullList<Ingredient> inputs, ItemStack output, JsonObject functionData) {
		super(id, group, width, height, inputs, output);
		this.functionData = functionData;
	}

	@Override
	public boolean matches(CraftingInventory pInv, World pLevel) {
		return getFunction().matches(pInv, this, pLevel);
	}

	public boolean actualMatches(CraftingInventory inventory, World world) {
		return super.matches(inventory, world);
	}

	public AdvancedCraftingFunction getFunction() {
		AdvancedCraftingFunctionSerializer<?> serializer = AdvancedCraftingFunctionSerializer.REGISTRY
				.getValue(new ResourceLocation(JSONUtils.getAsString(functionData, "type")));
		return serializer != null ? serializer.deserialize(functionData)
				: EmptyFunction.SERIALIZER.deserialize(functionData);
	}

	@Override
	public IRecipeSerializer<?> getSerializer() { return SERIALIZER; }

	@Override
	public ItemStack assemble(CraftingInventory pInv) {
		return getFunction().assemble(super.assemble(pInv), pInv, this);
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<AdvancedCraftingRecipe> {

		@Override
		public AdvancedCraftingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			String s = JSONUtils.getAsString(json, "group", "");
			Map<String, Ingredient> map = ShapedRecipe.keyFromJson(JSONUtils.getAsJsonObject(json, "key"));
			String[] astring = ShapedRecipe.patternFromJson(JSONUtils.getAsJsonArray(json, "pattern"));
			int i = astring[0].length();
			int j = astring.length;
			NonNullList<Ingredient> nonnulllist = ShapedRecipe.dissolvePattern(astring, map, i, j);
			ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
			JsonObject serializer = json.has("function") ? JSONUtils.getAsJsonObject(json, "function")
					: EmptyFunction.EMTPY_OBJECT;
			return new AdvancedCraftingRecipe(recipeId, s, i, j, nonnulllist, itemstack, serializer);
		}

		@Override
		public AdvancedCraftingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			int i = buffer.readVarInt();
			int j = buffer.readVarInt();
			String s = buffer.readUtf(32767);
			NonNullList<Ingredient> inputs = NonNullList.withSize(i * j, Ingredient.EMPTY);

			for (int k = 0; k < inputs.size(); ++k) {
				inputs.set(k, Ingredient.fromNetwork(buffer));
			}

			ItemStack output = buffer.readItem();

			JsonObject functionData = GSON.fromJson(buffer.readNbt().toString(), JsonObject.class);

			return new AdvancedCraftingRecipe(recipeId, s, i, j, inputs, output, functionData);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, AdvancedCraftingRecipe recipe) {
			buffer.writeVarInt(recipe.getRecipeWidth());
			buffer.writeVarInt(recipe.getRecipeHeight());
			buffer.writeUtf(recipe.getGroup());

			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.getResultItem());
			try {
				buffer.writeNbt(JsonToNBT.parseTag(recipe.functionData.toString()));
			} catch (CommandSyntaxException e) {
				e.printStackTrace();
			}
		}
	}

}
