package com.machina.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class TERUtil {

	private static Minecraft mc = Minecraft.getInstance();

	public static void renderItem(ItemStack stack, double[] translation, Quaternion rotation, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, float partialTicks, int combinedOverlay, int lightLevel, float scale) {
		matrixStack.pushPose();
		matrixStack.translate(translation[0], translation[1], translation[2]);
		matrixStack.mulPose(rotation);
		matrixStack.scale(scale, scale, scale);

		IBakedModel model = mc.getItemRenderer().getModel(stack, null, null);
		mc.getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer,
				lightLevel, combinedOverlay, model);
		matrixStack.popPose();
	}

	public static void renderLabel(MatrixStack stack, IRenderTypeBuffer buffer, int lightLevel, double[] corner,
			ITextComponent text, int color) {

		FontRenderer font = mc.font;

		stack.pushPose();
		float scale = 0.01f;
		int opacity = (int) (.4f * 255.0f) << 24;
		float offset = (float) (-font.width(text) / 2);
		Matrix4f matrix = stack.last().pose();

		stack.translate(corner[0], corner[1] + .4f, corner[2]);
		stack.scale(scale, scale, scale);
		stack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
		stack.mulPose(Vector3f.ZP.rotationDegrees(180f));

		font.drawInBatch(text, offset, 0, color, false, matrix, buffer, false, opacity, lightLevel);
		stack.popPose();
	}

	public static void renderLabel(MatrixStack stack, IRenderTypeBuffer buffer, int lightLevel, double[] corner,
			float[] rot, ITextComponent text, int color, float s) {

		FontRenderer font = mc.font;

		stack.pushPose();
		float scale = 0.01f * s;
		int opacity = (int) (.4f * 255.0f) << 24;
		float offset = (float) (-font.width(text) / 2);
		Matrix4f matrix = stack.last().pose();

		stack.translate(corner[0], corner[1] + .4f, corner[2]);
		stack.scale(scale, scale, scale);
		stack.mulPose(Vector3f.XP.rotationDegrees(rot[0]));
		stack.mulPose(Vector3f.YP.rotationDegrees(rot[1]));
		stack.mulPose(Vector3f.ZP.rotationDegrees(rot[2]));

		font.drawInBatch(text, offset, 0, color, false, matrix, buffer, false, opacity, lightLevel);
		stack.popPose();
	}

	public static void preview(MatrixStack ms, World world, BlockPos pos) {
		IRenderTypeBuffer.Impl buffers = mc.renderBuffers().bufferSource();
		IVertexBuilder buffer = buffers.getBuffer(MachinaRenderTypes.PREVIEW_TYPE);
		renderBlockAt(ms, buffer, Blocks.WHITE_CONCRETE.defaultBlockState(), pos, 1.01f);
		buffers.endBatch(MachinaRenderTypes.PREVIEW_TYPE);
	}

	private static void renderBlockAt(MatrixStack ms, IVertexBuilder buffer, BlockState state, BlockPos pos,
			float scale) {
		float offset = -(scale - 1) / 2;
		ms.pushPose();
		ms.translate(pos.getX(), pos.getY(), pos.getZ());
		ms.scale(scale, scale, scale);
		ms.translate(offset, offset, offset);
		BlockRendererDispatcher brd = mc.getBlockRenderer();
		brd.getModelRenderer().renderModel(ms.last(), buffer, state, brd.getBlockModel(state), 1, 1, 1, 0xFFFFFF,
				OverlayTexture.NO_OVERLAY);
		ms.popPose();
	}

	public static int getLightLevel(World world, BlockPos pos) {
		int bLight = world.getBrightness(LightType.BLOCK, pos);
		int sLight = world.getBrightness(LightType.SKY, pos);
		return LightTexture.pack(bLight, sLight);
	}
}
