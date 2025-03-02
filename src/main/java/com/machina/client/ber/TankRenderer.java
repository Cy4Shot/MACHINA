package com.machina.client.ber;

import com.machina.api.client.BERUtil;
import com.machina.block.entity.machine.TankBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class TankRenderer implements BlockEntityRenderer<TankBlockEntity> {

	public TankRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public void render(TankBlockEntity be, float partial, PoseStack pose, MultiBufferSource buff, int packedLight,
			int combinedOverlay) {
		float height = 0.1f + 0.8f * be.getFluidF(0);
		if (height > 0) {
			BERUtil.renderFluid(pose, be.getFluid(0), buff, 0.1f, 0.9f, 0.1f, height, 0.1f, 0.9f, 1f);
		}
	}
}
