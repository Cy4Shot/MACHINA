package com.machina.client.ber;

import com.machina.api.client.RenderTypes;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.util.math.VecUtil;
import com.machina.block.entity.machine.RocketPartBenchBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RocketPartBenchRenderer implements BlockEntityRenderer<RocketPartBenchBlockEntity> {

	public RocketPartBenchRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public void render(RocketPartBenchBlockEntity be, float partial, PoseStack pose, MultiBufferSource buff, int light,
			int overlay) {
		if (be.isCrafting()) {
			RocketPart<?> part = be.output();

			if (part != null) {

				float scale = part.getGUIScale();
				float rot = be.getLevel().getGameTime() % 360;
				VertexConsumer vc = buff.getBuffer(RenderTypes.CONSTRUCT);

				pose.pushPose();
				pose.translate(0.5f, 1.75f, 0.5f);
				pose.mulPose(VecUtil.rotationDegrees(VecUtil.YP, rot));
				pose.mulPose(VecUtil.rotationDegrees(VecUtil.XP, 180));
				pose.scale(scale, scale, scale);
				part.bake().renderToBuffer(pose, vc, 0xF000F0, overlay, 1f, 1f, 1f, 1f);
				pose.popPose();
			}
		}
	}

}
