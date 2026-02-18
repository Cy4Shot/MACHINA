package com.machina.api.client;

import com.machina.api.util.math.VecUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public class BERUtil {

	private static final Minecraft MC = Minecraft.getInstance();

	public static void renderFluid(PoseStack stack, FluidStack fluid, MultiBufferSource buff, float minX, float maxX,
			float minY, float maxY, float minZ, float maxZ, float a) {
		if (fluid == null || fluid.isEmpty())
			return;

		IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(fluid.getFluid());
		TextureAtlasSprite sprite = MC.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ext.getStillTexture(fluid));
		VertexConsumer builder = buff.getBuffer(RenderType.translucent());

		int color = ext.getTintColor(fluid);
		float r = (color >> 16 & 0xFF) / 255.0F;
		float g = (color >> 8 & 0xFF) / 255.0F;
		float b = (color & 0xFF) / 255.0F;

		stack.pushPose();
		add(builder, stack, minX, maxY, maxZ, sprite.getU0(), sprite.getV1(), r, g, b, a);
		add(builder, stack, maxX, maxY, maxZ, sprite.getU1(), sprite.getV1(), r, g, b, a);
		add(builder, stack, maxX, maxY, minZ, sprite.getU1(), sprite.getV0(), r, g, b, a);
		add(builder, stack, minX, maxY, minZ, sprite.getU0(), sprite.getV0(), r, g, b, a);
		add(builder, stack, maxX, maxY, maxZ, sprite.getU0(), sprite.getV1(), r, g, b, a);
		add(builder, stack, minX, maxY, maxZ, sprite.getU1(), sprite.getV1(), r, g, b, a);
		add(builder, stack, minX, maxY, minZ, sprite.getU1(), sprite.getV0(), r, g, b, a);
		add(builder, stack, maxX, maxY, minZ, sprite.getU0(), sprite.getV0(), r, g, b, a);
		add(builder, stack, maxX, maxY, maxZ, sprite.getU0(), sprite.getV0(), r, g, b, a);
		add(builder, stack, minX, maxY, maxZ, sprite.getU1(), sprite.getV0(), r, g, b, a);
		add(builder, stack, minX, minY, maxZ, sprite.getU1(), sprite.getV1(), r, g, b, a);
		add(builder, stack, maxX, minY, maxZ, sprite.getU0(), sprite.getV1(), r, g, b, a);
		add(builder, stack, maxX, minY, minZ, sprite.getU0(), sprite.getV1(), r, g, b, a);
		add(builder, stack, minX, minY, minZ, sprite.getU1(), sprite.getV1(), r, g, b, a);
		add(builder, stack, minX, maxY, minZ, sprite.getU1(), sprite.getV0(), r, g, b, a);
		add(builder, stack, maxX, maxY, minZ, sprite.getU0(), sprite.getV0(), r, g, b, a);
		add(builder, stack, maxX, maxY, minZ, sprite.getU0(), sprite.getV0(), r, g, b, a);
		add(builder, stack, minX, maxY, minZ, sprite.getU1(), sprite.getV0(), r, g, b, a);
		add(builder, stack, minX, minY, minZ, sprite.getU1(), sprite.getV1(), r, g, b, a);
		add(builder, stack, maxX, minY, minZ, sprite.getU0(), sprite.getV1(), r, g, b, a);
		add(builder, stack, maxX, minY, maxZ, sprite.getU0(), sprite.getV1(), r, g, b, a);
		add(builder, stack, minX, minY, maxZ, sprite.getU1(), sprite.getV1(), r, g, b, a);
		add(builder, stack, minX, maxY, maxZ, sprite.getU1(), sprite.getV0(), r, g, b, a);
		add(builder, stack, maxX, maxY, maxZ, sprite.getU0(), sprite.getV0(), r, g, b, a);
		stack.mulPose(VecUtil.rotationDegrees(VecUtil.YP, 90f));
		stack.translate(-1f, 0, 0);
		add(builder, stack, maxX, maxY, maxZ, sprite.getU0(), sprite.getV0(), r, g, b, a);
		add(builder, stack, minX, maxY, maxZ, sprite.getU1(), sprite.getV0(), r, g, b, a);
		add(builder, stack, minX, minY, maxZ, sprite.getU1(), sprite.getV1(), r, g, b, a);
		add(builder, stack, maxX, minY, maxZ, sprite.getU0(), sprite.getV1(), r, g, b, a);
		add(builder, stack, maxX, minY, minZ, sprite.getU0(), sprite.getV1(), r, g, b, a);
		add(builder, stack, minX, minY, minZ, sprite.getU1(), sprite.getV1(), r, g, b, a);
		add(builder, stack, minX, maxY, minZ, sprite.getU1(), sprite.getV0(), r, g, b, a);
		add(builder, stack, maxX, maxY, minZ, sprite.getU0(), sprite.getV0(), r, g, b, a);
		add(builder, stack, maxX, maxY, minZ, sprite.getU0(), sprite.getV0(), r, g, b, a);
		add(builder, stack, minX, maxY, minZ, sprite.getU1(), sprite.getV0(), r, g, b, a);
		add(builder, stack, minX, minY, minZ, sprite.getU1(), sprite.getV1(), r, g, b, a);
		add(builder, stack, maxX, minY, minZ, sprite.getU0(), sprite.getV1(), r, g, b, a);
		add(builder, stack, maxX, minY, maxZ, sprite.getU0(), sprite.getV1(), r, g, b, a);
		add(builder, stack, minX, minY, maxZ, sprite.getU1(), sprite.getV1(), r, g, b, a);
		add(builder, stack, minX, maxY, maxZ, sprite.getU1(), sprite.getV0(), r, g, b, a);
		add(builder, stack, maxX, maxY, maxZ, sprite.getU0(), sprite.getV0(), r, g, b, a);
		stack.popPose();
	}

	private static void add(VertexConsumer c, PoseStack s, float x, float y, float z, float u, float v, float r,
			float g, float b, float a) {
		c.addVertex(s.last().pose(), x, y, z).setColor(r, g, b, a).setUv(u, v).setUv2(0, 240).setNormal(1, 0, 0);
	}
}
