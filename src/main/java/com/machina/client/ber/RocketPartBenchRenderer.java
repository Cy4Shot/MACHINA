package com.machina.client.ber;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.RenderTypes;
import com.machina.api.client.shader.ShaderHandler;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.util.math.VecUtil;
import com.machina.block.entity.machine.RocketPartBenchBlockEntity;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;

public class RocketPartBenchRenderer implements BlockEntityRenderer<RocketPartBenchBlockEntity> {

	public RocketPartBenchRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public void render(RocketPartBenchBlockEntity be, float partial, @NotNull PoseStack pose,
			@NotNull MultiBufferSource buff, int light, int overlay) {
		if (be.isCrafting()) {
			RocketPart part = be.output();
			Level level = be.getLevel();

			if (part != null && level != null) {

				float scale = part.getGUIScale();
				float rot = level.getGameTime() % 360;
				VertexConsumer vc = buff.getBuffer(RenderTypes.CONSTRUCT);

				// Ease on a scaled arcsin
				float eased = (float) Math.pow(2 * Math.asin(be.getProgressPercent()) / Math.PI, 0.5);
				Uniform revealAmount = ShaderHandler.ROCKET_PART_BENCH.instance().getUniform("RevealAmount");
				if (revealAmount != null) {
					revealAmount.set(eased);
				}

				pose.pushPose();
				pose.translate(0.5f, 1.75f, 0.5f);
				pose.mulPose(VecUtil.rotationDegrees(VecUtil.YP, rot));
				pose.mulPose(VecUtil.rotationDegrees(VecUtil.XP, 180));
				pose.scale(scale, scale, scale);
				part.bake().renderToBuffer(pose, vc, 0xF000F0, overlay, 0xFFFFFFFF);
				pose.popPose();
			}
		}
	}
}
