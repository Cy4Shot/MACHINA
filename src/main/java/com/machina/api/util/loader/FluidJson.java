package com.machina.api.util.loader;

import java.util.Objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidJson {
	public static FluidStack load(JsonElement json) {
		if (json == null || !json.isJsonObject()) {
			return FluidStack.EMPTY;
		}
		JsonObject obj = json.getAsJsonObject();
		if (obj.has("fluid") && obj.has("amount")) {
			Fluid fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(obj.get("fluid").getAsString()));
			if (fluid == null) {
				return FluidStack.EMPTY;
			}
			return new FluidStack(fluid, obj.get("amount").getAsInt());
		}

		return FluidStack.EMPTY;
	}

	public static JsonObject save(FluidStack stack) {
		JsonObject obj = new JsonObject();
		obj.addProperty("fluid", Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(stack.getFluid())).toString());
		obj.addProperty("amount", stack.getAmount());
		return obj;
	}
}
