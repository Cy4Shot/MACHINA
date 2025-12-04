package com.machina.api.rocket.part.impl;

import java.util.function.Supplier;

import com.machina.api.client.model.rocket.RocketPartModel;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.registration.init.FluidInit.FluidObject;

import net.minecraft.resources.ResourceLocation;

public class ChassisPart<T extends RocketPartModel> extends RocketPart<T> {

    private final FluidObject coolant;
    private final float coolantEfficiency;

    public ChassisPart(ResourceLocation loc, float height, Supplier<T> model, float weight, float off, float guiScale,
                       FluidObject coolant, float coolantEfficiency) {
        super(loc, RocketPartType.CHASSIS, height, off, guiScale, model, weight);
        this.coolant = coolant;
        this.coolantEfficiency = coolantEfficiency;
    }

    public FluidObject getCoolant() {
        return coolant;
    }

    public float getCoolantEfficiency() {
        return coolantEfficiency;
    }

}
