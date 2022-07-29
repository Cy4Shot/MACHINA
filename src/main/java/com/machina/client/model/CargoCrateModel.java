package com.machina.client.model;

import com.machina.block.tile.CargoCrateTileEntity;
import com.machina.util.text.MachinaRL;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CargoCrateModel extends AnimatedGeoModel<CargoCrateTileEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(CargoCrateTileEntity animatable) {
		return new MachinaRL("animations/cargo_crate.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(CargoCrateTileEntity object) {
		return new MachinaRL("geo/cargo_crate.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(CargoCrateTileEntity object) {
		return new MachinaRL("textures/geo/cargo_crate.png");
	}
}
