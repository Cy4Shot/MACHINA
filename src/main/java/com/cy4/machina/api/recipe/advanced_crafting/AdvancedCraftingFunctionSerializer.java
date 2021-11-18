package com.cy4.machina.api.recipe.advanced_crafting;

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.cy4.machina.api.annotation.ChangedByReflection;
import com.cy4.machina.util.MachinaRL;
import com.google.gson.JsonObject;

import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public abstract class AdvancedCraftingFunctionSerializer extends ForgeRegistryEntry<AdvancedCraftingFunctionSerializer> {
	
	@ChangedByReflection(when = "commonSetup (when the registry is built)")
	public static final IForgeRegistry<AdvancedCraftingFunctionSerializer> REGISTRY = null;

	public static void createRegistry() {
		RegistryBuilder<AdvancedCraftingFunctionSerializer> registryBuilder = new RegistryBuilder<>();
		registryBuilder.setName(new MachinaRL("advanced_crafting_function_serializer"));
		registryBuilder.setType(AdvancedCraftingFunctionSerializer.class);

		try {
			Field registryField = AdvancedCraftingFunctionSerializer.class.getDeclaredField("REGISTRY");
			registryField.setAccessible(true);
			FieldUtils.removeFinalModifier(registryField);
			IForgeRegistry<AdvancedCraftingFunctionSerializer> registry = registryBuilder.create();
			registryField.set(AdvancedCraftingFunction.class, registry);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
		}
	}
	
	/**
	 * Create a new function based on the given json object
	 * @param nbt
	 * @return
	 */
	public abstract AdvancedCraftingFunction deserialize(JsonObject obj);

}
