package com.cy4.machina.api.recipe.advanced_crafting;

import java.util.Map;

import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.annotation.registries.recipe.RegisterRecipeSerializer;
import com.cy4.machina.recipe.ac.function.EmptyFunction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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
