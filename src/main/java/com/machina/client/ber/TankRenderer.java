package com.machina.client.ber;

import org.jetbrains.annotations.NotNull;

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
	public void render(TankBlockEntity be, float partial, @NotNull PoseStack pose, @NotNull MultiBufferSource buff,
			int packedLight, int combinedOverlay) {
		float prop = be.getFluidF(0);
		if (prop > 0) {
			float dist = 0.01f;
			float ndist = 1f - dist;
			BERUtil.renderFluid(pose, be.getFluid(0), buff, dist, ndist, dist, dist + (ndist - dist) * prop, dist,
					ndist, 1f);
		}
	}
}
