package com.machina.api.rocket.part.impl;

import java.util.function.Supplier;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.client.rocket.model.RocketPartModel;

import net.minecraft.resources.ResourceLocation;

public class ShieldPart<T extends RocketPartModel> extends RocketPart<T> {

    private final float maxAtmPressure;

    public ShieldPart(ResourceLocation loc, float height, Supplier<T> model, float weight, float off, float guiScale,
                      float maxAtmPressure) {
        super(loc, RocketPartType.SHIELD, height, off, guiScale, model, weight);
        this.maxAtmPressure = maxAtmPressure;
    }

    public float getMaxAtmPressure() {
        return maxAtmPressure;
    }

}
