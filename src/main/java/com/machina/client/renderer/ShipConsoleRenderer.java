package com.machina.client.renderer;

import java.util.List;

import com.machina.block.ShipConsoleBlock;
import com.machina.block.tile.ShipConsoleTileEntity;
import com.machina.client.model.RocketModel;
import com.machina.client.util.TERUtil;
import com.machina.item.ShipComponentItem;
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
import net.minecraft.util.math.vector.Vector3f;

public class ShipConsoleRenderer extends TileEntityRenderer<ShipConsoleTileEntity> {
	private static final int overlay = OverlayTexture.pack(OverlayTexture.u(0), OverlayTexture.v(false));

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

		if (!te.completed) {

			String stage = String.format(StringUtils.translateScreen("ship_console.stage") + " %d / 5 - ", te.stage)
					+ ShipComponentItem.getNameForStage(te.stage);
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
					if (te.erroredPos.size() > 0) {
						TERUtil.renderLabel(stack, buffer, lightLevel,
								new double[] { .5d + .05d * x, .65d, .5d + .05d * z }, rot,
								StringUtils.translateCompScreen("ship_console.obstructed"), 0xff0000, 1f);
					} else {
						TERUtil.renderLabel(stack, buffer, lightLevel,
								new double[] { .5d + .05d * x, .65d, .5d + .05d * z }, rot,
								StringUtils.translateCompScreen("ship_console.craft_ready"), 0x00ff00, 1f);
					}
				} else {
					TERUtil.renderLabel(stack, buffer, lightLevel,
							new double[] { .5d - .05d * x, .69d, .5d - .05d * z }, rot,
							StringUtils.translateCompScreen("ship_console.missing"), 0xff0000, 1f);
				}
			} else {
				TERUtil.renderLabel(stack, buffer, lightLevel, new double[] { .5d + .05d * x, .65d, .5d + .05d * z },
						rot, StringUtils.translateCompScreen("ship_console.crafting"), 0x00ff00, 1f);
			}
		} else {
			TERUtil.renderLabel(stack, buffer, lightLevel, new double[] { .5d + .05d * x, .65d, .5d + .05d * z }, rot,
					StringUtils.translateCompScreen("ship_console.await"), 0x00ff00, 1f);
		}

		double off = Math.pow(Math.E, (double) te.animTick / 9D) - 1;
		rocket(stack, buffer, lightLevel, d, te.stage, !te.completed, te.progress > 0, off);

		BlockPos o = te.getBlockPos();
		for (BlockPos p : te.erroredPos) {
			TERUtil.preview(stack, te.getLevel(), p.immutable().offset(-o.getX(), -o.getY(), -o.getZ()));
		}
	}

	public void rocket(MatrixStack stack, IRenderTypeBuffer buff, int light, Direction d, int stage,
			boolean construction, boolean progress, double vOff) {
		stack.pushPose();
		stack.scale(-1.0F, -1.0F, 1.0F);
		stack.translate(d.getNormal().getX() * 2 - 0.5D, -1.501D - vOff, d.getNormal().getZ() * -2 + 0.5D);
		stack.mulPose(Vector3f.YP.rotationDegrees(180 + d.get2DDataValue() * 90));
		if (construction) {
			this.model.partRender(stack, light, overlay, 1.0F, 1.0F, 1.0F, 0.15F, stage - 1, progress);
		} else {
			IVertexBuilder builder = buff.getBuffer(this.model.rocket());
			this.model.renderToBuffer(stack, builder, light, overlay, 1.0F, 1.0F, 1.0F, 0.15F);
		}
		stack.popPose();
	}

	@Override
	public boolean shouldRenderOffScreen(ShipConsoleTileEntity pTe) {
		return true;
	}
}
