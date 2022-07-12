package com.machina.recipe;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.machina.Machina;
import com.machina.item.ShipComponentItem;
import com.machina.item.ShipComponentItem.ShipComponentType;
import com.machina.registration.init.RecipeInit;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
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

public class ShipConsoleRecipe implements IRecipe<IInventory> {

	public static final Serializer SERIALIZER = new Serializer();

	private final NonNullList<ItemStack> items;
	private final int componentId;
	private final ResourceLocation id;

	public ShipConsoleRecipe(ResourceLocation id, NonNullList<ItemStack> i, int cId) {
		this.items = i;
		this.id = id;
		this.componentId = cId;
	}

	@Override
	public boolean matches(IInventory pInv, World pLevel) {
		for (int i = 0; i < 4; i++) {
			if (!this.items.get(i).equals(pInv.getItem(i)))
				return false;
			ItemStack stack = this.items.get(i);
			if (stack.getItem() instanceof ShipComponentItem) {
				if (!(ShipComponentItem.getType(stack).nbtID == (byte) this.componentId)) {
					return false;
				}
			}
		}
		return true;
	}

	public List<ItemStack> createRequirements() {
		List<ItemStack> req = new ArrayList<>();
		this.items.forEach(stack -> {
			if (stack.getItem() instanceof ShipComponentItem) {
				ShipComponentItem.setType(stack, ShipComponentType.fromId((byte) this.componentId).get());
			}
			req.add(stack);
		});
		return req;
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
		return RecipeInit.SHIP_CONSOLE_RECIPE;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<ShipConsoleRecipe> {

		Serializer() {
			this.setRegistryName(Machina.MOD_ID, "ship_console");
		}

		@Override
		public ShipConsoleRecipe fromJson(ResourceLocation id, JsonObject json) {
			final JsonArray ing = JSONUtils.getAsJsonArray(json, "input");
			NonNullList<ItemStack> items = NonNullList.create();
			ing.forEach(el -> {
				items.add(ShapedRecipe.itemFromJson(el.getAsJsonObject()));
			});
			final int cId = JSONUtils.getAsInt(json, "component", 0);
			return new ShipConsoleRecipe(id, items, cId);
		}

		@Override
		public ShipConsoleRecipe fromNetwork(ResourceLocation id, PacketBuffer buff) {
			NonNullList<ItemStack> items = NonNullList.create();
			for (int i = 0; i < 4; i++) {
				items.add(buff.readItem());
			}
			int cId = buff.readInt();
			return new ShipConsoleRecipe(id, items, cId);
		}

		@Override
		public void toNetwork(PacketBuffer buff, ShipConsoleRecipe recipe) {
			for (int i = 0; i < 4; i++) {
				buff.writeItem(recipe.items.get(i));
			}
			buff.writeInt(recipe.componentId);
		}
	}
	
	public static class ShipConsoleRecipeType implements IRecipeType<ShipConsoleRecipe> {

		@Override
		public String toString() {
			return Machina.MOD_ID + ":ship_console";
		}
	}
}