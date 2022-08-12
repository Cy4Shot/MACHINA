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

public class PressurizedChamberRecipe implements IRecipe<IInventory> {

	public static final Serializer SERIALIZER = new Serializer();

	public final NonNullList<FluidStack> fluids;
	public final FluidStack fOut;
	public final ItemStack iOut;
	public final ItemStack catalyst;
	public final int heat;
	public final int power;
	public final int col;
	public final ResourceLocation id;

	public PressurizedChamberRecipe(ResourceLocation id, NonNullList<FluidStack> f, FluidStack fO, ItemStack iO,
			ItemStack c, int h, int p, int col) {
		this.id = id;
		this.fluids = f;
		this.fOut = fO;
		this.iOut = iO;
		this.catalyst = c;
		this.heat = h;
		this.power = p;
		this.col = col;
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
		return iOut;
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
		return RecipeInit.PRESSURIZED_CHAMBER_RECIPE;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<PressurizedChamberRecipe> {

		Serializer() {
			this.setRegistryName(Machina.MOD_ID, "pressurized_chamber");
		}

		@Override
		public PressurizedChamberRecipe fromJson(ResourceLocation id, JsonObject json) {
			NonNullList<FluidStack> f = NonNullList.create();
			JSONUtils.getAsJsonArray(json, "input").forEach(el -> {
				f.add(JsonUtils.fluidFromJson(el.getAsJsonObject()));
			});
			ItemStack cat = JsonUtils.itemFromJson(JSONUtils.getAsJsonObject(json, "catalyst"));
			final int h = JSONUtils.getAsInt(json, "heat", 0);
			final int p = JSONUtils.getAsInt(json, "power", 0);
			final int c = JSONUtils.getAsInt(json, "color", 0xFF_ff0000);
			FluidStack fO = JsonUtils.fluidFromJson(JSONUtils.getAsJsonObject(json, "output").getAsJsonObject("fluid"));
			ItemStack iO = JsonUtils.itemFromJson(JSONUtils.getAsJsonObject(json, "output").getAsJsonObject("item"));
			return new PressurizedChamberRecipe(id, f, fO, iO, cat, h, p, c);
		}

		@Override
		public PressurizedChamberRecipe fromNetwork(ResourceLocation id, PacketBuffer buff) {

			int fluidSize = buff.readInt();

			NonNullList<FluidStack> f = NonNullList.create();
			for (int i = 0; i < fluidSize; i++) {
				f.add(buff.readFluidStack());
			}
			ItemStack cat = buff.readItem();
			int h = buff.readInt();
			int p = buff.readInt();
			int c = buff.readInt();
			FluidStack fO = buff.readFluidStack();
			ItemStack iO = buff.readItem();
			return new PressurizedChamberRecipe(id, f, fO, iO, cat, h, p, c);
		}

		@Override
		public void toNetwork(PacketBuffer buff, PressurizedChamberRecipe recipe) {

			buff.writeInt(recipe.fluids.size());

			for (int i = 0; i < recipe.fluids.size(); i++) {
				buff.writeFluidStack(recipe.fluids.get(i));
			}
			buff.writeItem(recipe.catalyst);
			buff.writeInt(recipe.heat);
			buff.writeInt(recipe.power);
			buff.writeInt(recipe.col);
			buff.writeFluidStack(recipe.fOut);
			buff.writeItem(recipe.iOut);
		}
	}

	public static class PressurizedChamberRecipeType implements IRecipeType<PressurizedChamberRecipe> {

		@Override
		public String toString() {
			return Machina.MOD_ID + ":pressurized_chamber";
		}
	}
}