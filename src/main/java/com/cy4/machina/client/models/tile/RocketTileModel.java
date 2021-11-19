package com.cy4.machina.client.models.tile;

import com.cy4.machina.Machina;
import com.cy4.machina.tile_entity.RocketTile;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
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
