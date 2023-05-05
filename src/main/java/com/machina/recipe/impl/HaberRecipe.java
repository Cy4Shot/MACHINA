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

public class HaberRecipe implements IMachinaRecipe {

	public static final Serializer SERIALIZER = new Serializer();

	public final NonNullList<FluidStack> fluids;
	public final FluidStack fOut;
	public final ItemStack catalyst;
	public final int power;
	public final ResourceLocation id;

	public HaberRecipe(ResourceLocation id, NonNullList<FluidStack> f, FluidStack fO, ItemStack c, int p) {
		this.id = id;
		this.fluids = f;
		this.fOut = fO;
		this.catalyst = c;
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
		return RecipeInit.HABER_RECIPE;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<HaberRecipe> {

		Serializer() {
			this.setRegistryName(Machina.MOD_ID, "haber");
		}

		@Override
		public HaberRecipe fromJson(ResourceLocation id, JsonObject json) {
			NonNullList<FluidStack> f = NonNullList.create();
			JSONUtils.getAsJsonArray(json, "input").forEach(el -> {
				f.add(JsonUtils.fluidFromJson(el.getAsJsonObject()));
			});
			ItemStack cat = JsonUtils.itemFromJson(JSONUtils.getAsJsonObject(json, "catalyst"));
			final int p = JSONUtils.getAsInt(json, "power", 0);
			FluidStack fO = JsonUtils.fluidFromJson(JSONUtils.getAsJsonObject(json, "output").getAsJsonObject("fluid"));
			return new HaberRecipe(id, f, fO, cat, p);
		}

		@Override
		public HaberRecipe fromNetwork(ResourceLocation id, PacketBuffer buff) {

			int fluidSize = buff.readInt();

			NonNullList<FluidStack> f = NonNullList.create();
			for (int i = 0; i < fluidSize; i++) {
				f.add(buff.readFluidStack());
			}
			ItemStack cat = buff.readItem();
			int p = buff.readInt();
			FluidStack fO = buff.readFluidStack();
			return new HaberRecipe(id, f, fO, cat, p);
		}

		@Override
		public void toNetwork(PacketBuffer buff, HaberRecipe recipe) {

			buff.writeInt(recipe.fluids.size());

			for (int i = 0; i < recipe.fluids.size(); i++) {
				buff.writeFluidStack(recipe.fluids.get(i));
			}
			buff.writeItem(recipe.catalyst);
			buff.writeInt(recipe.power);
			buff.writeFluidStack(recipe.fOut);
		}
	}

	public static class HaberRecipeType implements IRecipeType<HaberRecipe> {

		@Override
		public String toString() {
			return Machina.MOD_ID + ":haber";
		}
	}
}