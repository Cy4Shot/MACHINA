package com.cy4.machina.client.models.tile;

import com.cy4.machina.tile_entity.RocketTile;
import com.cy4.machina.util.MachinaRL;

import net.minecraft.util.ResourceLocation;

import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RocketTileModel extends AnimatedGeoModel<RocketTile> {
	@Override
	public ResourceLocation getModelLocation(RocketTile object) {
		return new MachinaRL("geo/rocket.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(RocketTile object) {
		return new MachinaRL("textures/gecko/template.png");
	}

	@Override
	public ResourceLocation getAnimationFileLocation(RocketTile animatable) {
		return new MachinaRL("animations/rocket.animation.json");
	}
}
