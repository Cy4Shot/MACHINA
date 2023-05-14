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

public class PumpRecipe implements IMachinaRecipe {

	public static final Serializer SERIALIZER = new Serializer();

	public final FluidStack water;
	public final FluidStack brine;
	public final int power;
	public final ResourceLocation id;

	public PumpRecipe(ResourceLocation id, FluidStack water, FluidStack brine, int p) {
		this.id = id;
		this.water = water;
		this.brine = brine;
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
		return RecipeInit.PUMP_RECIPE;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<PumpRecipe> {

		Serializer() {
			this.setRegistryName(Machina.MOD_ID, "pump");
		}

		@Override
		public PumpRecipe fromJson(ResourceLocation id, JsonObject json) {
			final FluidStack w = JsonUtils.fluidFromJson(JSONUtils.getAsJsonObject(json, "water"));
			final FluidStack b = JsonUtils.fluidFromJson(JSONUtils.getAsJsonObject(json, "brine"));
			final int p = JSONUtils.getAsInt(json, "power", 0);
			return new PumpRecipe(id, w, b, p);
		}

		@Override
		public PumpRecipe fromNetwork(ResourceLocation id, PacketBuffer buff) {
			final FluidStack w = buff.readFluidStack();
			final FluidStack b = buff.readFluidStack();
			final int p = buff.readInt();
			return new PumpRecipe(id, w, b, p);
		}

		@Override
		public void toNetwork(PacketBuffer buff, PumpRecipe recipe) {
			buff.writeFluidStack(recipe.water);
			buff.writeFluidStack(recipe.brine);
			buff.writeInt(recipe.power);
		}
	}

	public static class PumpRecipeType implements IRecipeType<PumpRecipe> {

		@Override
		public String toString() {
			return Machina.MOD_ID + ":pump";
		}
	}
}