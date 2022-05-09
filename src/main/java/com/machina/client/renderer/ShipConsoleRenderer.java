package com.machina.client.renderer;

import java.util.List;

import com.machina.block.ShipConsoleBlock;
import com.machina.block.tile.ShipConsoleTileEntity;
import com.machina.client.util.TERUtil;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;

public class ShipConsoleRenderer extends TileEntityRenderer<ShipConsoleTileEntity> {

	public ShipConsoleRenderer(TileEntityRendererDispatcher disp) {
		super(disp);
	}

	@Override
	public void render(ShipConsoleTileEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn,
			int packedLightIn, int pCombinedOverlay) {
		int lightLevel = TERUtil.getLightLevel(te.getLevel(), te.getBlockPos());
		Direction d = te.getBlockState().getValue(ShipConsoleBlock.FACING);

		float[] rot;
		switch (d) {
		case EAST:
			rot = new float[] { 90f, 330f, 270f };
			break;
		case SOUTH:
			rot = new float[] { 120f, 0f, 0f };
			break;
		case WEST:
			rot = new float[] { 90f, 30f, 90f };
			break;
		default:
			rot = new float[] { 60f, 0f, 180f };
			break;

		}

		int x = d.getNormal().getX();
		int z = d.getNormal().getZ();

		TERUtil.renderLabel(stack, bufferIn, lightLevel, new double[] { .5d - .15d * x, .72d, .5d - .15d * z }, rot,
				new StringTextComponent("Stage " + String.valueOf(te.stage) + " / 5"), 0xffffff, .5f);

		if (!te.isInProgress) {

			List<ItemStack> missing = te.getMissingItems();

			int i = 0;
			for (ItemStack item : missing) {
				i++;
				String text = String.valueOf(item.getCount()) + "x " + item.getDisplayName().getString();
				TERUtil.renderLabel(stack, bufferIn, lightLevel,
						new double[] { .5d + .07d * i * x, .66d - .03d * i, .5d + .07d * i * z }, rot,
						new StringTextComponent(text), 0xff0000, 0.5f);
			}

			if (missing.isEmpty()) {
				TERUtil.renderLabel(stack, bufferIn, lightLevel, new double[] { .5d + .05d * x, .65d, .5d + .05d * z },
						rot, new StringTextComponent("Craft Ready"), 0x00ff00, 1f);
			} else {
				TERUtil.renderLabel(stack, bufferIn, lightLevel, new double[] { .5d - .05d * x, .69d, .5d - .05d * z },
						rot, new StringTextComponent("Missing Items"), 0xff0000, 1f);
			}
		} else {
			TERUtil.renderLabel(stack, bufferIn, lightLevel, new double[] { .5d + .05d * x, .65d, .5d + .05d * z }, rot,
					new StringTextComponent("Crafting..."), 0x00ff00, 1f);
		}
	}

}
