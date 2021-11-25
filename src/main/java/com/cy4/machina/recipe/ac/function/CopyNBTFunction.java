/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.recipe.ac.function;

import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunction;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingRecipe;
import com.cy4.machina.api.registry.annotation.RegistryHolder;
import com.cy4.machina.api.registry.annotation.recipe.RegisterACFunctionSerializer;
import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;

@RegistryHolder
public class CopyNBTFunction extends AdvancedCraftingFunction {

	public final int copyNBTSlot;

	public CopyNBTFunction(int copyNBTSlot) {
		this.copyNBTSlot = copyNBTSlot;
	}

	@Override
	public ItemStack assemble(ItemStack original, CraftingInventory inv, AdvancedCraftingRecipe recipe) {
		original.setTag(inv.getItem(copyNBTSlot).getOrCreateTag());
		return original;
	}

	@RegisterACFunctionSerializer("copy_nbt")
	public static final Serializer SERIALIZER = new Serializer();

	public static final class Serializer extends AdvancedCraftingFunctionSerializer<CopyNBTFunction> {

		@Override
		public CopyNBTFunction deserialize(JsonObject obj) {
			return new CopyNBTFunction(JSONUtils.getAsInt(obj, "copy_slot", 4));
		}

	}

}
