package com.machina.client.model;

import com.machina.block.tile.CargoCrateTileEntity;
import com.machina.util.text.MachinaRL;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;

public class CargoCrateModel extends CustomBlockModel<CargoCrateTileEntity> {

	@Override
	public <M extends IAnimatable> ResourceLocation getAnimation(M animatable) {
		return new MachinaRL("animations/cargo_crate.animation.json");
	}

	@Override
	public <M extends IAnimatable> ResourceLocation getModel(M animatable) {
		return new MachinaRL("geo/cargo_crate.geo.json");
	}

	@Override
	public <M extends IAnimatable> ResourceLocation getTexture(M animatable) {
		return new MachinaRL("textures/geo/cargo_crate.png");
	}
}
