package com.machina.client.renderer;

import com.machina.block.tile.CargoCrateTileEntity;
import com.machina.client.model.CargoCrateModel;
import com.machina.registration.init.ItemInit;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class CargoCrateRenderer extends GeoBlockRenderer<CargoCrateTileEntity> {

	private Minecraft mc = Minecraft.getInstance();

	public CargoCrateRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn, new CargoCrateModel());
	}

	@Override
	public void render(TileEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		super.render(te, partialTicks, stack, bufferIn, combinedLightIn, combinedOverlayIn);

		ClientPlayerEntity player = mc.player;
		int lightLevel = getLightLevel(te.getLevel(), te.getBlockPos());

		renderItem(new ItemStack(ItemInit.SHIP_COMPONENT.get()), new double[] { .5d, .4d, .5d },
				Vector3f.YP.rotationDegrees(180f - player.yRot), stack, bufferIn, partialTicks, combinedOverlayIn,
				lightLevel, 0.8f);

		ITextComponent label = new TranslationTextComponent("machina.cargo_crate.open");
		renderLabel(stack, bufferIn, lightLevel, new double[] { .5d, .9d, .5d }, label, 0xffffff);
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

	private void renderLabel(MatrixStack stack, IRenderTypeBuffer buffer, int lightLevel, double[] corner,
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

	private int getLightLevel(World world, BlockPos pos) {
		int bLight = world.getBrightness(LightType.BLOCK, pos);
		int sLight = world.getBrightness(LightType.SKY, pos);
		return LightTexture.pack(bLight, sLight);
	}
}