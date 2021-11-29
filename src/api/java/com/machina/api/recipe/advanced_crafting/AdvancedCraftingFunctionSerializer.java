/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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

package com.machina.api.recipe.advanced_crafting;

import java.util.Optional;

import com.google.gson.JsonObject;
import com.machina.api.annotation.ChangedByReflection;
import com.machina.api.registry.annotation.RegisterCustomRegistry;
import com.machina.api.registry.annotation.RegistryHolder;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.helper.ClassHelper;
import com.machina.api.util.helper.CustomRegistryHelper;
import com.machina.api.util.objects.TargetField;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

@RegistryHolder
public abstract class AdvancedCraftingFunctionSerializer<F extends AdvancedCraftingFunction>
		extends ForgeRegistryEntry<AdvancedCraftingFunctionSerializer<?>> {

	@ChangedByReflection(when = "commonSetup (when the registry is built)")
	public static final IForgeRegistry<AdvancedCraftingFunctionSerializer<?>> REGISTRY = null;

	@RegisterCustomRegistry
	public static void createRegistry(final RegistryEvent.NewRegistry event) {
		CustomRegistryHelper.<AdvancedCraftingFunctionSerializer<?>>registerRegistry(new TargetField(AdvancedCraftingFunctionSerializer.class, "REGISTRY"),
				ClassHelper.withWildcard(AdvancedCraftingFunctionSerializer.class),
				new MachinaRL("advanced_crafting_function_serializer"), Optional.of(new MachinaRL("no_function")));
	}

	/**
	 * Create a new function based on the given json object
	 *
	 * @param nbt
	 * @return
	 */
	public abstract F deserialize(JsonObject obj);

}
