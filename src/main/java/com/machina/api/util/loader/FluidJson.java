package com.machina.api.util.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class FluidJson {
    public static FluidStack load(JsonElement json) {
        if (json == null || !json.isJsonObject()) {
            return FluidStack.EMPTY;
        }
        JsonObject obj = json.getAsJsonObject();
        if (obj.has("fluid") && obj.has("amount")) {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(obj.get("fluid").getAsString()));
            if (fluid == null) {
                return FluidStack.EMPTY;
            }
            return new FluidStack(fluid, obj.get("amount").getAsInt());
        }

        return FluidStack.EMPTY;
    }

    public static JsonObject save(FluidStack stack) {
        JsonObject obj = new JsonObject();
        obj.addProperty("fluid", Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(stack.getFluid())).toString());
        obj.addProperty("amount", stack.getAmount());
        return obj;
    }
}
