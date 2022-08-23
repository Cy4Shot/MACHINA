package com.machina.client.model;

import com.machina.util.text.MachinaRL;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public abstract class CustomBlockModel<T extends IAnimatable> extends AnimatedGeoModel<T> {

	public static <T extends IAnimatable> CustomBlockModel<T> create(final String name) {
		return new CustomBlockModel<T>() {
			@Override
			public <M extends IAnimatable> ResourceLocation getAnimation(M animatable) {
				return new MachinaRL("animations/" + name + ".animation.json");
			}

			@Override
			public <M extends IAnimatable> ResourceLocation getTexture(M animatable) {
				return new MachinaRL("textures/geo/" + name + ".png");
			}

			@Override
			public <M extends IAnimatable> ResourceLocation getModel(M animatable) {
				return new MachinaRL("geo/" + name + ".geo.json");
			}

		};
	}

	@Override
	public ResourceLocation getAnimationFileLocation(T animatable) {
		return getAnimation(animatable);
	}

	@Override
	public ResourceLocation getModelLocation(T animatable) {
		return getModel(animatable);
	}

	@Override
	public ResourceLocation getTextureLocation(T animatable) {
		return getTexture(animatable);
	}

	public abstract <M extends IAnimatable> ResourceLocation getAnimation(M animatable);

	public abstract <M extends IAnimatable> ResourceLocation getTexture(M animatable);

	public abstract <M extends IAnimatable> ResourceLocation getModel(M animatable);
}