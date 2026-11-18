package com.machina.client.ber;

import org.jetbrains.annotations.Nullable;

import com.machina.api.util.MachinaRL;
import com.machina.block.entity.HightechTurretBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class HightechTurretRenderer extends GeoBlockRenderer<HightechTurretBlockEntity> {

	public HightechTurretRenderer(BlockEntityRendererProvider.Context ctx) {
		super(BlockEntityInit.HIGHTECH_TURRET.get());
	}

	@Override
	public void preRender(PoseStack poseStack, HightechTurretBlockEntity animatable, BakedGeoModel model,
			@Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender,
			float partialTick, int packedLight, int packedOverlay, int colour) {
		model.getBone("Gun Pos").ifPresent(bone -> {
			float targetRx = animatable.getTargetRotX();
			float targetRy = animatable.getTargetRotY();

			float lerpFactor = 0.05f;
			animatable.clientRotX += (targetRx - animatable.clientRotX) * lerpFactor;

			float dy = (float) Math.IEEEremainder(targetRy - animatable.clientRotY, 2 * Math.PI);
			animatable.clientRotY += dy * lerpFactor;

			bone.setRotX(animatable.clientRotX);
			bone.setRotY(animatable.clientRotY);
		});
		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight,
				packedOverlay, colour);
	}

	@Override
	public ResourceLocation getTextureLocation(HightechTurretBlockEntity animatable) {
		return MachinaRL
				.create("textures/block/hightech_turret_" + String.valueOf(animatable.getAlertLevel()) + ".png");
	}

}
