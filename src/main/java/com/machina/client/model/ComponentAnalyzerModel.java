package com.machina.client.model;

import com.machina.block.tile.ComponentAnalyzerTileEntity;
import com.machina.util.text.MachinaRL;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;

public class ComponentAnalyzerModel extends CustomBlockModel<ComponentAnalyzerTileEntity> {

	@Override
	public <M extends IAnimatable> ResourceLocation getModel(M animatable) {
		return new MachinaRL("geo/component_analyzer.geo.json");
	}

	@Override
	public <M extends IAnimatable> ResourceLocation getTexture(M animatable) {
		return new MachinaRL("textures/geo/component_analyzer.png");
	}

	@Override
	public <M extends IAnimatable> ResourceLocation getAnimation(M animatable) {
		return new MachinaRL("animations/component_analyzer.animation.json");
	}
}
