package com.cy4.machina.client.models.tile;

import com.cy4.machina.Machina;
import com.cy4.machina.tile_entities.RocketTile;

import net.minecraft.util.ResourceLocation;

import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RocketTileModel extends AnimatedGeoModel<RocketTile> {
    @Override
    public ResourceLocation getModelLocation(RocketTile object) {
        return new ResourceLocation(Machina.MOD_ID, "geo/machina_rocket.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RocketTile object) {
        return new ResourceLocation(Machina.MOD_ID, "textures/gecko/test_texture.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(RocketTile animatable) {
        return new ResourceLocation(Machina.MOD_ID, "animations/rocket.animation.json");
    }
}
