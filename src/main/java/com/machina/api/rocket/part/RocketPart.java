package com.machina.api.rocket.part;

import java.util.function.Supplier;

import com.machina.client.rocket.model.RocketPartModel;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RocketPart<T extends RocketPartModel> {
	private final ResourceLocation loc;
	private final RocketPartType type;
	private final float height, offset, guiScale;
	private final Supplier<T> model;

	private final float mass;

	public RocketPart(ResourceLocation loc, RocketPartType type, float height, float offset, float guiScale,
			Supplier<T> model, float mass) {
		this.loc = loc;
		this.type = type;
		this.height = height;
		this.offset = offset;
		this.guiScale = guiScale;
		this.model = model;
		this.mass = mass;
	}

	public MutableComponent getName() {
		return Component.translatable("rocket_part." + loc.getNamespace() + "." + loc.getPath());
	}

	@OnlyIn(Dist.CLIENT)
	public T bake() {
		return model.get();
	}

	public float getMass() {
		return mass;
	}

	public RocketPartType getType() {
		return type;
	}

	public float getGUIScale() {
		return (1f / height) * guiScale;
	}

	public float getModelOffset() {
		return height + offset;
	}
}