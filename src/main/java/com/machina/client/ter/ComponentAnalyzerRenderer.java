package com.machina.client.ter;

import com.machina.block.ComponentAnalyzerBlock;
import com.machina.block.tile.machine.ComponentAnalyzerTileEntity;
import com.machina.client.model.CustomBlockModel;
import com.machina.client.util.TERUtil;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class ComponentAnalyzerRenderer extends GeoBlockRenderer<ComponentAnalyzerTileEntity> {

	public ComponentAnalyzerRenderer(TileEntityRendererDispatcher disp) {
		super(disp, CustomBlockModel.create("component_analyzer"));
	}

	@Override
	public void render(TileEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		super.render(te, partialTicks, stack, bufferIn, combinedLightIn, combinedOverlayIn);

		ComponentAnalyzerTileEntity cate = (ComponentAnalyzerTileEntity) te;
		int lightLevel = TERUtil.getLightLevel(cate.getLevel(), cate.getBlockPos());

		Direction d = te.getBlockState().getValue(ComponentAnalyzerBlock.FACING);

		ItemStack item = cate.getItem(cate.getItem(0).isEmpty() ? 1 : 0);
		Vector3f rot =  new Vector3f(0, d.toYRot(), 0);
		switch (d) {
		case WEST:
			rot.add(90, 90, 90);
			break;
		case EAST:
			rot.add(-90, 90, 90);
			break;
		case SOUTH:
			rot.add(-90, 0, 0);
			break;
		default:
			rot.add(90, 0, 0);
			break;
		}
		TERUtil.renderItem(item, new double[] { .5d + .15d * d.getStepX(), .3d, .5d + .15d * d.getStepZ()}, rot, stack, bufferIn,
				partialTicks, combinedOverlayIn, lightLevel, 0.8f);
	}
	
	@Override
	public boolean shouldRenderOffScreen(TileEntity pTe) {
		return true;
	}
}
