/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.recipe.ac.function;

import com.google.gson.JsonObject;
import com.machina.api.recipe.advanced_crafting.AdvancedCraftingFunction;
import com.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;
import com.machina.api.recipe.advanced_crafting.AdvancedCraftingRecipe;
import com.machina.api.registry.annotation.RegistryHolder;
import com.machina.api.registry.annotation.recipe.RegisterACFunctionSerializer;

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
