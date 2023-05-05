package com.machina.recipe.impl;

import com.google.gson.JsonElement;
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

public class MixerRecipe implements IMachinaRecipe {

	public static final Serializer SERIALIZER = new Serializer();

	public final NonNullList<FluidStack> fluidsIn;
	public final ItemStack itemIn;
	public final ItemStack catalyst;
	public final NonNullList<FluidStack> fluidsOut;
	public final ItemStack itemOut;
	public final int power;
	public final ResourceLocation id;

	public MixerRecipe(ResourceLocation id, NonNullList<FluidStack> fIn, NonNullList<FluidStack> fOut, ItemStack c,
			ItemStack iIn, ItemStack iOut, int p) {
		this.id = id;
		this.fluidsIn = fIn;
		this.itemIn = iIn;
		this.catalyst = c;
		this.fluidsOut = fOut;
		this.itemOut = iOut;
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
		return RecipeInit.MIXER_RECIPE;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<MixerRecipe> {

		Serializer() {
			this.setRegistryName(Machina.MOD_ID, "mixer");
		}

		@Override
		public MixerRecipe fromJson(ResourceLocation id, JsonObject json) {

			// Fluids In
			NonNullList<FluidStack> fi = NonNullList.create();
			JSONUtils.getAsJsonArray(json, "fluids").forEach(el -> {
				fi.add(JsonUtils.fluidFromJson(el.getAsJsonObject()));
			});

			// Item In
			ItemStack ii = ItemStack.EMPTY;
			if (json.has("item")) {
				ii = JsonUtils.itemFromJson(json.get("item").getAsJsonObject());
			}

			// Catalyst
			ItemStack c = ItemStack.EMPTY;
			if (json.has("catalyst")) {
				c = JsonUtils.itemFromJson(json.get("catalyst").getAsJsonObject());
			}

			// Outputs
			NonNullList<FluidStack> fo = NonNullList.create();
			ItemStack io = ItemStack.EMPTY;
			for (JsonElement el : JSONUtils.getAsJsonArray(json, "outputs")) {
				JsonObject obj = el.getAsJsonObject();
				if (obj.has("fluid")) {
					fo.add(JsonUtils.fluidFromJson(obj));
				} else if (obj.has("item")) {
					io = JsonUtils.itemFromJson(obj);
				}
			}

			// Power
			int p = JSONUtils.getAsInt(json, "power", 0);

			return new MixerRecipe(id, fi, fo, c, ii, io, p);
		}

		@Override
		public MixerRecipe fromNetwork(ResourceLocation id, PacketBuffer buff) {

			// Fluids In
			int fis = buff.readInt();
			NonNullList<FluidStack> fi = NonNullList.create();
			for (int i = 0; i < fis; i++) {
				fi.add(buff.readFluidStack());
			}

			// Item In
			ItemStack ii = buff.readItem();

			// Catalyst
			ItemStack c = buff.readItem();

			// Fluids Out
			int fos = buff.readInt();
			NonNullList<FluidStack> fo = NonNullList.create();
			for (int i = 0; i < fos; i++) {
				fo.add(buff.readFluidStack());
			}

			// Item Out
			ItemStack io = buff.readItem();

			// Power
			int p = buff.readInt();

			return new MixerRecipe(id, fi, fo, c, ii, io, p);
		}

		@Override
		public void toNetwork(PacketBuffer buff, MixerRecipe recipe) {

			// Fluids In
			buff.writeInt(recipe.fluidsIn.size());
			for (int i = 0; i < recipe.fluidsIn.size(); i++) {
				buff.writeFluidStack(recipe.fluidsIn.get(i));
			}

			// Item In
			buff.writeItem(recipe.itemIn);

			// Catalyst
			buff.writeItem(recipe.catalyst);

			// Fluids Out
			buff.writeInt(recipe.fluidsOut.size());
			for (int i = 0; i < recipe.fluidsOut.size(); i++) {
				buff.writeFluidStack(recipe.fluidsOut.get(i));
			}

			// Item Out
			buff.writeItem(recipe.itemOut);

			// Power
			buff.writeInt(recipe.power);
		}
	}

	public static class MixerRecipeType implements IRecipeType<MixerRecipe> {

		@Override
		public String toString() {
			return Machina.MOD_ID + ":mixer";
		}
	}
}