package com.machina.client.rocket.model;

import com.machina.api.rocket.RocketEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public abstract class RocketPartModel extends EntityModel<RocketEntity> {

	public RocketPartModel() {
	}

	@Override
	public void setupAnim(RocketEntity p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_,
			float p_102623_) {
	}

	protected abstract LayerDefinition createBodyLayer();

	protected abstract ModelPart main();

	public void render(PoseStack poseStack, MultiBufferSource source, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		this.renderToBuffer(poseStack, source.getBuffer(renderType(getTextureLocation())), packedLight, packedOverlay,
				red, green, blue, alpha);
	}

	protected abstract ResourceLocation getTextureLocation();

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		main().render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}
