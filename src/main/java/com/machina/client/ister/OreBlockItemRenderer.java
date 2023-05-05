package com.machina.client.ister;

import com.machina.block.OreBlock;
import com.machina.client.model.OreModel;
import com.machina.item.OreBlockItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class OreBlockItemRenderer extends ItemStackTileEntityRenderer {

	@Override
	public void renderByItem(ItemStack itemStack, ItemCameraTransforms.TransformType pTransformType,
			MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int overlay) {
		if (pTransformType == ItemCameraTransforms.TransformType.GUI) {
			matrixStack.pushPose();
			IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
			RenderHelper.setupFor3DItems();
			this.render(matrixStack, bufferIn, combinedLightIn, itemStack, overlay);
			irendertypebuffer$impl.endBatch();
			RenderSystem.enableDepthTest();
			RenderHelper.setupFor3DItems();
			matrixStack.popPose();
		} else {
			this.render(matrixStack, bufferIn, combinedLightIn, itemStack, overlay);
		}
	}

	private void render(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, ItemStack itemStack,
			int overlay) {
		matrixStack.pushPose();
		matrixStack.translate(0.5f, 0.5f, 0.5f);
		OreBlock ore = (OreBlock) ((OreBlockItem) itemStack.getItem()).getBlock();
		Minecraft.getInstance().getItemRenderer().render(itemStack, ItemCameraTransforms.TransformType.GUI, false,
				matrixStack, bufferIn, combinedLightIn, overlay,
				new OreModel(ore.getBgTexturePath(), ore.getBgTexturePath(), ore.getFgTexturePath()));
		matrixStack.popPose();
	}
}
