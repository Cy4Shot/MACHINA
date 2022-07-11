package com.machina.client.renderer;

import com.machina.block.tile.CargoCrateTileEntity;
import com.machina.client.model.CargoCrateModel;
import com.machina.client.util.TERUtil;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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

		CargoCrateTileEntity ccte = (CargoCrateTileEntity) te;
		int lightLevel = TERUtil.getLightLevel(te.getLevel(), te.getBlockPos());

		TERUtil.renderItem(ccte.getItem(0), new double[] { .5d, .4d, .5d },
				Vector3f.YP.rotationDegrees(180f - mc.player.yRot), stack, bufferIn, partialTicks, combinedOverlayIn,
				lightLevel, 0.8f);

		if (ccte.open && !ccte.getItem(0).isEmpty()) {
			ITextComponent label = new TranslationTextComponent("machina.screen.cargo_crate.open");
			TERUtil.renderLabel(stack, bufferIn, lightLevel, new double[] { .5d, .9d, .5d }, label, 0xffffff);
		}
	}
}