/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.recipe.ac.function;

import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunction;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;
import com.cy4.machina.api.registry.annotation.RegistryHolder;
import com.cy4.machina.api.registry.annotation.recipe.RegisterACFunctionSerializer;
import com.google.gson.JsonObject;

@RegistryHolder
public class EmptyFunction extends AdvancedCraftingFunction {

	@RegisterACFunctionSerializer("no_function")
	public static final Serializer SERIALIZER = new Serializer();

	public static final JsonObject EMTPY_OBJECT = createJsonObj();

	private static JsonObject createJsonObj() {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", "machina:no_function");
		return obj;
	}

	public static final class Serializer extends AdvancedCraftingFunctionSerializer<EmptyFunction> {

		@Override
		public EmptyFunction deserialize(JsonObject obj) {
			return new EmptyFunction();
		}
	}

}
