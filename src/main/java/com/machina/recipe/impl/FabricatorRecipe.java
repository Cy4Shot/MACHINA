package com.machina.recipe.impl;

import java.util.Collections;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.machina.Machina;
import com.machina.recipe.IMachinaRecipe;
import com.machina.registration.init.RecipeInit;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class FabricatorRecipe implements IMachinaRecipe {

	public static final Serializer SERIALIZER = new Serializer();

	private final NonNullList<ItemStack> items;
	private final String bpId;
	private final ResourceLocation id;

	public FabricatorRecipe(ResourceLocation id, NonNullList<ItemStack> i, String bpId) {
		this.items = i;
		this.id = id;
		this.bpId = bpId;
	}

	@Override
	public boolean matches(IInventory pInv, World pLevel) {
		for (int i = 0; i < this.items.size(); i++) {
			if (!pInv.hasAnyOf(Collections.singleton(this.get(i).getItem())))
				return false;
		}
		return true;
	}

	public List<ItemStack> createRequirements() {
		return this.items;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> i = NonNullList.create();
		this.items.forEach(item -> {
			i.add(Ingredient.of(item));
		});
		return i;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return RecipeInit.FABRICATOR_RECIPE;
	}

	public ItemStack get(int id) {
		if (id >= this.items.size()) {
			return ItemStack.EMPTY;
		}
		return this.items.get(id);
	}

	public String blueprintId() {
		return this.bpId;
	}

	public NonNullList<ItemStack> items() {
		return this.items;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<FabricatorRecipe> {

		Serializer() {
			this.setRegistryName(Machina.MOD_ID, "fabricator");
		}

		@Override
		public FabricatorRecipe fromJson(ResourceLocation id, JsonObject json) {
			final JsonArray ing = JSONUtils.getAsJsonArray(json, "input");
			NonNullList<ItemStack> items = NonNullList.create();
			ing.forEach(el -> {
				items.add(ShapedRecipe.itemFromJson(el.getAsJsonObject()));
			});
			final String bpId = JSONUtils.getAsString(json, "blueprint");
			return new FabricatorRecipe(id, items, bpId);
		}

		@Override
		public FabricatorRecipe fromNetwork(ResourceLocation id, PacketBuffer buff) {
			NonNullList<ItemStack> items = NonNullList.create();
			for (int i = 0; i < 16; i++) {
				items.add(buff.readItem());
			}
			String bpId = buff.readUtf();
			return new FabricatorRecipe(id, items, bpId);
		}

		@Override
		public void toNetwork(PacketBuffer buff, FabricatorRecipe recipe) {
			for (int i = 0; i < 16; i++) {
				buff.writeItem(recipe.get(i));
			}
			buff.writeUtf(recipe.bpId);
		}
	}

	public static class FabricatorRecipeType implements IRecipeType<FabricatorRecipe> {

		@Override
		public String toString() {
			return Machina.MOD_ID + ":fabricator";
		}
	}
}