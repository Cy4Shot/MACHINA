package com.cy4.machina.recipe.advanced_crafting.function;

import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.annotation.registries.recipe.RegisterACFunctionSerializer;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunction;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;
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
