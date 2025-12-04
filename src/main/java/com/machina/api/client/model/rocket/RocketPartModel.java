package com.machina.api.client.model.rocket;

import com.machina.api.rocket.RocketEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class RocketPartModel extends EntityModel<RocketEntity> {

    public RocketPartModel() {
    }

    @Override
    public void setupAnim(@NotNull RocketEntity p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_,
                          float p_102623_) {
    }

    protected abstract LayerDefinition createBodyLayer();

    public abstract ModelPart main();

    public void render(PoseStack poseStack, MultiBufferSource source, int packedLight, int packedOverlay, float red,
                       float green, float blue, float alpha) {
        this.renderToBuffer(poseStack, source.getBuffer(renderType(getTextureLocation())), packedLight, packedOverlay,
                red, green, blue, alpha);
    }

    protected abstract ResourceLocation getTextureLocation();

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        main().render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

////		Debug Cube
//		poseStack.pushPose();
//		poseStack.translate(0, 0, 0);
//		poseStack.scale(0.5F, 0.5F, 0.5F);
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, -1.0F, -1.0F).color(255, 0, 0, 255).uv(0, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), 1.0F, -1.0F, -1.0F).color(255, 0, 0, 255).uv(1, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), 1.0F, 1.0F, -1.0F).color(255, 0, 0, 255).uv(1, 1)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, 1.0F, -1.0F).color(255, 0, 0, 255).uv(0, 1)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, -1.0F, 1.0F).color(255, 0, 0, 255).uv(0, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), 1.0F, -1.0F, 1.0F).color(255, 0, 0, 255).uv(1, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), 1.0F, 1.0F, 1.0F).color(255, 0, 0, 255).uv(1, 1).overlayCoords(0)
//				.uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, 1.0F, 1.0F).color(255, 0, 0, 255).uv(0, 1)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, -1.0F, -1.0F).color(255, 0, 0, 255).uv(0, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, -1.0F, 1.0F).color(255, 0, 0, 255).uv(1, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), 1.0F, -1.0F, 1.0F).color(255, 0, 0, 255).uv(1, 1)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), 1.0F, -1.0F, -1.0F).color(255, 0, 0, 255).uv(0, 1)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, 1.0F, -1.0F).color(255, 0, 0, 255).uv(0, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, 1.0F, 1.0F).color(255, 0, 0, 255).uv(1, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), 1.0F, 1.0F, 1.0F).color(255, 0, 0, 255).uv(1, 1).overlayCoords(0)
//				.uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), 1.0F, 1.0F, -1.0F).color(255, 0, 0, 255).uv(0, 1)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, -1.0F, -1.0F).color(255, 0, 0, 255).uv(0, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, 1.0F, -1.0F).color(255, 0, 0, 255).uv(1, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, 1.0F, 1.0F).color(255, 0, 0, 255).uv(1, 1)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, -1.0F, 1.0F).color(255, 0, 0, 255).uv(0, 1)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), 1.0F, -1.0F, 1.0F).color(255, 0, 0, 255).uv(0, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), 1.0F, 1.0F, 1.0F).color(255, 0, 0, 255).uv(1, 0).overlayCoords(0)
//				.uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, -1.0F, -1.0F).color(255, 0, 0, 255).uv(0, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), -1.0F, 1.0F, -1.0F).color(255, 0, 0, 255).uv(1, 0)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), 1.0F, 1.0F, -1.0F).color(255, 0, 0, 255).uv(1, 1)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		vertexConsumer.vertex(poseStack.last().pose(), 1.0F, -1.0F, -1.0F).color(255, 0, 0, 255).uv(0, 1)
//				.overlayCoords(0).uv2(packedOverlay).normal(1.0F, 1.0F, 1.0F).endVertex();
//		poseStack.popPose();

    }

}
