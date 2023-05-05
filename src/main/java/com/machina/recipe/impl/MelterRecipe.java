package com.machina.recipe.impl;

import com.google.gson.JsonObject;
import com.machina.Machina;
import com.machina.recipe.IMachinaRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.util.nbt.JsonUtils;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class MelterRecipe implements IMachinaRecipe {

	public static final Serializer SERIALIZER = new Serializer();

	public final ItemStack input;
	public final FluidStack output;
	public final int power;
	public final ResourceLocation id;

	public MelterRecipe(ResourceLocation id, ItemStack in, FluidStack out, int p) {
		this.id = id;
		this.input = in;
		this.output = out;
		this.power = p;
	}

	@Override
	public boolean matches(IInventory pInv, World pLevel) {
		return true;
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
		return NonNullList.create();
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return RecipeInit.MELTER_RECIPE;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<MelterRecipe> {

		Serializer() {
			this.setRegistryName(Machina.MOD_ID, "melter");
		}

		@Override
		public MelterRecipe fromJson(ResourceLocation id, JsonObject json) {
			ItemStack in = JsonUtils.itemFromJson(JSONUtils.getAsJsonObject(json, "input"));
			FluidStack out = JsonUtils.fluidFromJson(JSONUtils.getAsJsonObject(json, "output"));
			final int p = JSONUtils.getAsInt(json, "power", 0);
			return new MelterRecipe(id, in, out, p);
		}

		@Override
		public MelterRecipe fromNetwork(ResourceLocation id, PacketBuffer buff) {
			ItemStack in = buff.readItem();
			FluidStack out = buff.readFluidStack();
			int p = buff.readInt();
			return new MelterRecipe(id, in, out, p);
		}

		@Override
		public void toNetwork(PacketBuffer buff, MelterRecipe recipe) {
			buff.writeItem(recipe.input);
			buff.writeFluidStack(recipe.output);
			buff.writeInt(recipe.power);
		}
	}

	public static class MelterRecipeType implements IRecipeType<MelterRecipe> {

		@Override
		public String toString() {
			return Machina.MOD_ID + ":melter";
		}
	}
}