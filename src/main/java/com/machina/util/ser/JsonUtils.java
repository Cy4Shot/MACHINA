package com.machina.util.ser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class JsonUtils {
	public static FluidStack fluidFromJson(JsonObject obj) {
		String s = JSONUtils.getAsString(obj, "fluid");

		if (s.equals("EMPTY"))
			return FluidStack.EMPTY;

		Registry.FLUID.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
			return new JsonSyntaxException("Unknown fluid '" + s + "'");
		});
		if (obj.has("data")) {
			throw new JsonParseException("Disallowed data tag found");
		} else {
			return getFluidStack(obj);
		}
	}

	public static FluidStack getFluidStack(JsonObject json) {
		String fluidName = JSONUtils.getAsString(json, "fluid");

		Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName));

		if (fluid == null)
			throw new JsonSyntaxException("Unknown fluid '" + fluidName + "'");

		return new FluidStack(fluid, JSONUtils.getAsInt(json, "amount", 1));
	}

	public static ItemStack itemFromJson(JsonObject p_199798_0_) {
		String s = JSONUtils.getAsString(p_199798_0_, "item");

		if (s.equals("EMPTY"))
			return ItemStack.EMPTY;

		Registry.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
			return new JsonSyntaxException("Unknown item '" + s + "'");
		});
		if (p_199798_0_.has("data")) {
			throw new JsonParseException("Disallowed data tag found");
		} else {
			return CraftingHelper.getItemStack(p_199798_0_, true);
		}
	}
}
