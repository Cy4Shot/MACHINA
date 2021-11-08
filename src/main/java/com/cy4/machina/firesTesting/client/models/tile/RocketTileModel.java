package com.cy4.machina.firesTesting.client.models.tile;

import com.cy4.machina.Machina;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RocketTileModel extends AnimatedGeoModel
{
    @Override
    public ResourceLocation getModelLocation(Object object) {
        return new ResourceLocation(Machina.MOD_ID, "geo/rocket.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object object) {
        return new ResourceLocation(Machina.MOD_ID, "textures/gecko/template.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object animatable) {
        return new ResourceLocation(Machina.MOD_ID, "animations/rocket.animation.json");
    }
}
