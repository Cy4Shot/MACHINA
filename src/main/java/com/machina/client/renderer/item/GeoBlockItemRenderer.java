package com.machina.client.renderer.item;

import com.machina.client.model.CustomBlockModel;
import com.machina.item.AnimatableBlockItem;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class GeoBlockItemRenderer extends GeoItemRenderer<AnimatableBlockItem> {

	public GeoBlockItemRenderer(final CustomBlockModel<?> blockModel) {
		super(new AnimatedGeoModel<AnimatableBlockItem>() {
			@Override
			public ResourceLocation getAnimationFileLocation(AnimatableBlockItem animatable) {
				return blockModel.getAnimation(animatable);
			}

			@Override
			public ResourceLocation getModelLocation(AnimatableBlockItem animatable) {
				return blockModel.getModel(animatable);
			}

			@Override
			public ResourceLocation getTextureLocation(AnimatableBlockItem animatable) {
				return blockModel.getTexture(animatable);
			}
		});
	}
}