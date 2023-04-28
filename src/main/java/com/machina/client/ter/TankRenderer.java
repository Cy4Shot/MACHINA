package com.machina.client.ter;

import com.machina.block.tile.machine.TankTileEntity;
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
		float height = 0.1f + 0.8f * te.propFull();
		TERUtil.renderFluid(stack, te.getFluid(), buff, 0.1f, 0.9f, 0.1f, height, 0.1f, 0.9f, 1f);
	}
}