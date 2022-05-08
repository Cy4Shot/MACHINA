package com.machina.client.renderer;

import com.machina.block.tile.ComponentAnalyzerTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class ComponentAnalyzerRenderer extends TileEntityRenderer<ComponentAnalyzerTileEntity> {

	private Minecraft mc = Minecraft.getInstance();

	public ComponentAnalyzerRenderer(TileEntityRendererDispatcher disp) {
		super(disp);
	}

	@Override
	public void render(ComponentAnalyzerTileEntity te, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer bufferIn, int packedLightIn, int pCombinedOverlay) {
		renderItem(te.getItem(0), new double[] { .5d, .4d, .5d }, Vector3f.YP.rotationDegrees(180f - mc.player.yRot),
				stack, bufferIn, partialTicks, pCombinedOverlay, getLightLevel(te.getLevel(), te.getBlockPos()), 0.8f);
	}

	private void renderItem(ItemStack stack, double[] translation, Quaternion rotation, MatrixStack matrixStack,
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

	private int getLightLevel(World world, BlockPos pos) {
		int bLight = world.getBrightness(LightType.BLOCK, pos);
		int sLight = world.getBrightness(LightType.SKY, pos);
		return LightTexture.pack(bLight, sLight);
	}

}
