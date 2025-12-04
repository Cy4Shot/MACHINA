package com.machina.api.rocket.part.impl;

import java.util.function.Supplier;

import com.machina.api.client.model.rocket.RocketPartModel;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;

import net.minecraft.resources.ResourceLocation;

public class LifeSupportPart<T extends RocketPartModel> extends RocketPart<T> {

    private final int slots;

    public LifeSupportPart(ResourceLocation loc, float height, Supplier<T> model, float weight, float off,
                           float guiScale, int slots) {
        super(loc, RocketPartType.LIFE_SUPPORT, height, off, guiScale, model, weight);
        this.slots = slots;
    }

    public int getSlots() {
        return slots;
    }

}
