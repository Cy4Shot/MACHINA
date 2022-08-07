package com.machina.client.renderer;

import com.machina.block.tile.TankTileEntity;
import com.machina.client.util.TERUtil;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class TankRenderer extends TileEntityRenderer<TankTileEntity> {

	public TankRenderer(TileEntityRendererDispatcher disp) {
		super(disp);
	}

	@Override
	public void render(TankTileEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer buff,
			int packedLightIn, int pCombinedOverlay) {
		float height = 0.05f + 0.9f * te.propFull();
		TERUtil.renderFluid(stack, te.getFluid(), buff, 0.05f, 0.95f, 0.05f, height, 0.05f, 0.95f, 1f);
	}
}