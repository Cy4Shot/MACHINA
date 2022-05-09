package com.machina.client.renderer;

import com.machina.block.tile.ComponentAnalyzerTileEntity;
import com.machina.client.util.TERUtil;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class ComponentAnalyzerRenderer extends TileEntityRenderer<ComponentAnalyzerTileEntity> {

	private Minecraft mc = Minecraft.getInstance();

	public ComponentAnalyzerRenderer(TileEntityRendererDispatcher disp) {
		super(disp);
	}

	@Override
	public void render(ComponentAnalyzerTileEntity te, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer bufferIn, int packedLightIn, int pCombinedOverlay) {
		ItemStack item = te.getItem(te.getItem(0).isEmpty() ? 1 : 0);
		TERUtil.renderItem(item, new double[] { .5d, .4d, .5d }, Vector3f.YP.rotationDegrees(180f - mc.player.yRot),
				stack, bufferIn, partialTicks, pCombinedOverlay, TERUtil.getLightLevel(te.getLevel(), te.getBlockPos()),
				0.8f);
	}

}
