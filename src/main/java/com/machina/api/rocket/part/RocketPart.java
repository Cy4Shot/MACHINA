package com.machina.api.rocket.part;

import java.util.function.Supplier;

import com.machina.client.rocket.model.RocketPartModel;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RocketPart<T extends RocketPartModel> {
	private final ResourceLocation loc;
	private final RocketPartType type;
	private final float height, offset;
	private final Supplier<T> model;

	private final float weight;

	public RocketPart(ResourceLocation loc, RocketPartType type, float height, float offset, Supplier<T> model,
			float weight) {
		this.loc = loc;
		this.type = type;
		this.height = height;
		this.offset = offset;
		this.model = model;
		this.weight = weight;
	}

	public Component getName() {
		return Component.translatable("rocket_part." + loc.getNamespace() + "." + loc.getPath());
	}

	@OnlyIn(Dist.CLIENT)
	public T bake() {
		return model.get();
	}

	public float getWeight() {
		return weight;
	}

	public RocketPartType getType() {
		return type;
	}

	public float getHeight() {
		return height;
	}

	public float getModelOffset() {
		return height + offset;
	}
}