package com.machina.api.rocket.part;

import java.util.function.Supplier;

import org.joml.Vector3d;

import com.machina.client.rocket.model.RocketPartModel;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RocketPart<T extends RocketPartModel> {
	private final ResourceLocation loc;
	private final RocketPartType type;
	private final Vector3d upAnchor;
	private final Vector3d downAnchor;
	private final Supplier<T> model;

	private final float weight;

	public RocketPart(ResourceLocation loc, RocketPartType type, Vector3d upAnchor, Vector3d downAnchor,
			Supplier<T> model, float weight) {
		this.loc = loc;
		this.type = type;
		this.upAnchor = upAnchor;
		this.downAnchor = downAnchor;
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

	public Vector3d getDownAnchor() {
		return downAnchor;
	}

	public Vector3d getUpAnchor() {
		return upAnchor;
	}

	public RocketPartType getType() {
		return type;
	}
}