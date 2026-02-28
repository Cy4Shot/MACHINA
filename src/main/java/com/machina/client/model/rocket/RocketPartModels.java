package com.machina.client.model.rocket;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.machina.api.rocket.part.RocketPart;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RocketPartModels {

    private static final Map<RocketPart, Supplier<? extends RocketPartModel>> MODELS = new HashMap<>();

    public static <T extends RocketPartModel> void register(RocketPart part, Supplier<T> model) {
        MODELS.put(part, model);
    }

    public static RocketPartModel bake(RocketPart part) {
        return MODELS.get(part).get();
    }
}