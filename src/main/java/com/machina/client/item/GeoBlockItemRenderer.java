package com.machina.client.item;

import com.machina.client.model.CustomBlockModel;
import com.machina.item.AnimatableBlockItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class GeoBlockItemRenderer extends GeoItemRenderer<AnimatableBlockItem> {

	public GeoBlockItemRenderer(final CustomBlockModel<?> blockModel) {
		super(new AnimatedGeoModel<AnimatableBlockItem>() {
			@Override
			public ResourceLocation getAnimationFileLocation(AnimatableBlockItem animatable) {
				return blockModel.getAnimation(animatable);
			}

			@Override
			public ResourceLocation getModelLocation(AnimatableBlockItem animatable) {
				return blockModel.getModel(animatable);
			}

			@Override
			public ResourceLocation getTextureLocation(AnimatableBlockItem animatable) {
				return blockModel.getTexture(animatable);
			}
		});
	}

	@Override
	public void renderByItem(ItemStack itemStack, ItemCameraTransforms.TransformType pTransformType,
			MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int p_239207_6_) {
		if (pTransformType == ItemCameraTransforms.TransformType.GUI) {
			matrixStack.pushPose();
			IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
			RenderHelper.setupFor3DItems();
			this.render((AnimatableBlockItem) itemStack.getItem(), matrixStack, bufferIn, combinedLightIn, itemStack);
			irendertypebuffer$impl.endBatch();
			RenderSystem.enableDepthTest();
			RenderHelper.setupFor3DItems();
			matrixStack.popPose();
		} else {
			this.render((AnimatableBlockItem) itemStack.getItem(), matrixStack, bufferIn, combinedLightIn, itemStack);
		}
	}
}