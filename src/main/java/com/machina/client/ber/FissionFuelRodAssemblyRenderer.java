package com.machina.client.ber;

import org.joml.Matrix4f;

import com.machina.block.entity.machine.fission_reactor.FissionFuelRodAssemblyBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;

public class FissionFuelRodAssemblyRenderer implements BlockEntityRenderer<FissionFuelRodAssemblyBlockEntity> {

	public FissionFuelRodAssemblyRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public void render(FissionFuelRodAssemblyBlockEntity be, float partialTick, PoseStack pose,
			MultiBufferSource buffer, int packedLight, int packedOverlay) {

		Minecraft mc = Minecraft.getInstance();
		Font font = mc.font;

		Component text = Component.literal(String.valueOf(be.getInsertStage()));

		pose.pushPose();
		pose.translate(0.5D, 1.5D, 0.5D);
		pose.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
		pose.scale(0.025F, -0.025F, 0.025F);
		Matrix4f matrix = pose.last().pose();

		float x = -font.width(text) / 2f;
		font.drawInBatch(text, x, 0, 0xFFFFFFFF, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);

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
