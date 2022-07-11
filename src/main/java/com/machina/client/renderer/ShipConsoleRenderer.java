package com.machina.client.renderer;

import java.util.List;

import com.machina.block.ShipConsoleBlock;
import com.machina.block.tile.ShipConsoleTileEntity;
import com.machina.client.model.RocketModel;
import com.machina.client.util.TERUtil;
import com.machina.item.ShipComponentItem;
import com.machina.util.MachinaRL;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class ShipConsoleRenderer extends TileEntityRenderer<ShipConsoleTileEntity> {

	protected RocketModel model;

	public ShipConsoleRenderer(TileEntityRendererDispatcher disp) {
		super(disp);
		this.model = new RocketModel();
	}

	@Override
	public void render(ShipConsoleTileEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer,
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

		String stage = String.format(StringUtils.translate("machina.screen.ship_console.stage") + " %d / 5 - ",
				te.stage) + ShipComponentItem.getNameForStage(te.stage);
		TERUtil.renderLabel(stack, buffer, lightLevel, new double[] { .5d - .15d * x, .72d, .5d - .15d * z }, rot,
				StringUtils.toComp(stage), 0xffffff, .5f);

		if (!te.isInProgress) {

			List<ItemStack> missing = te.getMissingItems();

			int i = 0;
			for (ItemStack item : missing) {
				i++;
				String text = StringUtils.prettyItemStack(item);
				TERUtil.renderLabel(stack, buffer, lightLevel,
						new double[] { .5d + .07d * i * x, .66d - .03d * i, .5d + .07d * i * z }, rot,
						StringUtils.toComp(text), 0xff0000, 0.5f);
			}

			if (missing.isEmpty()) {
				TERUtil.renderLabel(stack, buffer, lightLevel, new double[] { .5d + .05d * x, .65d, .5d + .05d * z },
						rot, StringUtils.translateComp("machina.screen.ship_console.craft_ready"), 0x00ff00, 1f);
			} else {
				TERUtil.renderLabel(stack, buffer, lightLevel, new double[] { .5d - .05d * x, .69d, .5d - .05d * z },
						rot, StringUtils.translateComp("machina.screen.ship_console.missing"), 0xff0000, 1f);
			}
		} else {
			TERUtil.renderLabel(stack, buffer, lightLevel, new double[] { .5d + .05d * x, .65d, .5d + .05d * z }, rot,
					StringUtils.translateComp("machina.screen.ship_console.crafting"), 0x00ff00, 1f);
		}

		BlockPos o = te.getBlockPos();
		for (BlockPos p : te.erroredPos) {
			TERUtil.preview(stack, te.getLevel(), p.immutable().offset(-o.getX(), -o.getY(), -o.getZ()));
		}

		rocket(partialTicks, stack, buffer, packedLightIn);

	}

	public void rocket(float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
		pMatrixStack.pushPose();
		pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
		pMatrixStack.translate(0.0D, (double) -3.501F, 0.0D);
		IVertexBuilder ivertexbuilder = pBuffer
				.getBuffer(this.model.renderType(new MachinaRL("textures/entity/rocket.png")));
		this.model.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight,
				OverlayTexture.pack(OverlayTexture.u(0), OverlayTexture.v(false)), 1.0F, 1.0F, 1.0F, 0.15F);
		pMatrixStack.popPose();
	}

	@Override
	public boolean shouldRenderOffScreen(ShipConsoleTileEntity pTe) {
		return true;
	}
}
