package com.machina.client.entity;

import com.machina.api.util.math.VecUtil;
import com.machina.client.model.rocket.RocketModel;
import com.machina.rocket.RocketEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RocketRenderer extends EntityRenderer<RocketEntity> {

    public RocketRenderer(Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(RocketEntity entity) {
        return null;
    }

    @Override
    public void render(RocketEntity e, float yaw, float part, PoseStack pose, MultiBufferSource buff, int light) {
        RocketModel model = e.getModel();
        if (model != null) {
            pose.pushPose();
            pose.scale(1, -1, -1);
            pose.mulPose(VecUtil.rotationDegrees(VecUtil.YP, e.getYRot()));
            model.render(pose, buff, light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
            pose.popPose();
        }
    }
}
