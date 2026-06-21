package com.machina.client.ber;

import com.machina.block.entity.machine.fission_reactor.FissionFuelRodAssemblyBlockEntity;
import com.machina.client.model.ModeratorRodModel;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class FissionFuelRodAssemblyRenderer implements BlockEntityRenderer<FissionFuelRodAssemblyBlockEntity> {

	private static final ModeratorRodModel MODEL = new ModeratorRodModel();

	public FissionFuelRodAssemblyRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public void render(FissionFuelRodAssemblyBlockEntity be, float partialTick, PoseStack pose,
			MultiBufferSource buffer, int packedLight, int packedOverlay) {

		pose.pushPose();
		pose.translate(0.5D, 0.125D - 0.125 * be.getInsertStage(), 0.5D);
		MODEL.render(pose, buffer, packedLight, packedOverlay, 0xFFFFFFFF);
		pose.popPose();
	}

	@Override
	public boolean shouldRenderOffScreen(FissionFuelRodAssemblyBlockEntity be) {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 256;
	}
}
