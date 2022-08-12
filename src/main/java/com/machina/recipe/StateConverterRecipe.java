package com.machina.recipe;

import com.google.gson.JsonObject;
import com.machina.Machina;
import com.machina.registration.init.RecipeInit;
import com.machina.util.serial.JsonUtils;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
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

public class StateConverterRecipe implements IRecipe<IInventory> {

	public static final Serializer SERIALIZER = new Serializer();

	public final ResourceLocation id;
	public final FluidStack input;
	public final FluidStack output;
	public final int heat;
	public final boolean above;

	public StateConverterRecipe(ResourceLocation id, FluidStack in, FluidStack out, int heat, boolean above) {
		this.id = id;
		this.input = in;
		this.output = out;
		this.heat = heat;
		this.above = above;
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean matches(IInventory pInv, World pLevel) {
		return true;
	}

	@Override
	public ItemStack assemble(IInventory pInv) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
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
		return RecipeInit.STATE_CONVERTER_RECIPE;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<StateConverterRecipe> {

		Serializer() {
			this.setRegistryName(Machina.MOD_ID, "state_converter");
		}

		@Override
		public StateConverterRecipe fromJson(ResourceLocation id, JsonObject json) {
			FluidStack i = JsonUtils.fluidFromJson(JSONUtils.getAsJsonObject(json, "input"));
			FluidStack o = JsonUtils.fluidFromJson(JSONUtils.getAsJsonObject(json, "output"));
			final int h = JSONUtils.getAsInt(json, "heat", 0);
			final boolean a = JSONUtils.getAsBoolean(json, "above", true);
			return new StateConverterRecipe(id, i, o, h, a);
		}

		@Override
		public StateConverterRecipe fromNetwork(ResourceLocation id, PacketBuffer buff) {
			FluidStack i = buff.readFluidStack();
			FluidStack o = buff.readFluidStack();
			int h = buff.readInt();
			boolean a = buff.readBoolean();
			return new StateConverterRecipe(id, i, o, h, a);
		}

		@Override
		public void toNetwork(PacketBuffer buff, StateConverterRecipe recipe) {
			buff.writeFluidStack(recipe.input);
			buff.writeFluidStack(recipe.output);
			buff.writeInt(recipe.heat);
			buff.writeBoolean(recipe.above);
		}
	}

	public static class StateConverterRecipeType implements IRecipeType<StateConverterRecipe> {

		@Override
		public String toString() {
			return Machina.MOD_ID + ":state_converter";
		}
	}
}