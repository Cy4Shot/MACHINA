package com.cy4.machina.api.recipe.advanced_crafting;

import java.util.Optional;

import com.cy4.machina.api.annotation.ChangedByReflection;
import com.cy4.machina.util.MachinaRL;
import com.cy4.machina.util.helper.ClassHelper;
import com.cy4.machina.util.helper.CustomRegistryHelper;
import com.cy4.machina.util.objects.TargetField;
import com.google.gson.JsonObject;

import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class AdvancedCraftingFunctionSerializer<F extends AdvancedCraftingFunction>
		extends ForgeRegistryEntry<AdvancedCraftingFunctionSerializer<F>> {

	@ChangedByReflection(when = "commonSetup (when the registry is built)")
	public static final IForgeRegistry<AdvancedCraftingFunctionSerializer<?>> REGISTRY = null;

	public static void createRegistry() {
		CustomRegistryHelper.registerRegistry(new TargetField(AdvancedCraftingFunctionSerializer.class, "REGISTRY"),
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
