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

	private float rx = 0;
	private float ry = 0;

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

			float newRx = rx + (targetRx - rx) * 0.001f;
			float newRy = ry + (targetRy - ry) * 0.001f;

			bone.setRotX(newRx);
			bone.setRotY(newRy);

			this.rx = newRx;
			this.ry = newRy;
		});
		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight,
				packedOverlay, colour);
	}

	@Override
	public ResourceLocation getTextureLocation(HightechTurretBlockEntity animatable) {
		return MachinaRL
				.create("textures/block/hightech_turret_" + String.valueOf(animatable.getChargeState()) + ".png");
	}

}
