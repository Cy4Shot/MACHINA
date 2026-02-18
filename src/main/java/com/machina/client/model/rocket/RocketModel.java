package com.machina.client.model.rocket;

import java.util.ArrayList;
import java.util.List;

import com.machina.api.rocket.part.RocketPart;
import com.machina.rocket.RocketEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;

public class RocketModel extends EntityModel<RocketEntity> {
	private final List<RocketPart<?>> parts;

	public RocketModel(List<RocketPart<?>> parts) {
		this.parts = parts;
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer buff, int light, int overlay, int color) {
	}

	public void render(PoseStack stack, MultiBufferSource buff, int light, int overlay, int color) {
		stack.pushPose();
		for (RocketPart<?> part : this.parts) {
			stack.translate(0, -part.getModelHeight(), 0);
			part.bake().render(stack, buff, light, overlay, color);
			stack.translate(0, -part.getModelOffset(), 0);
		}
		stack.popPose();
	}

	public float getGUIScale() {
		float tot = 0;
		for (RocketPart<?> part : this.parts) {
			tot += part.getModelHeight();
			tot += part.getModelOffset();
		}
		return 1f / tot;
	}

	@Override
	public void setupAnim(RocketEntity p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_,
			float p_102623_) {
	}

	public static class RocketModelBuilder {
		private final List<RocketPart<?>> parts;

		public RocketModelBuilder() {
			this.parts = new ArrayList<>();
		}

		public RocketModelBuilder add(RocketPart<?> part) {
			this.parts.add(part);
			return this;
		}

		public RocketModel build() {
			return new RocketModel(this.parts);
		}
	}
}
