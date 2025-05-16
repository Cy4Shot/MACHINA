package com.machina.api.client.bewlr;

import com.machina.api.item.RocketPartItem;
import com.machina.client.rocket.model.RocketPartModel;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RocketPartBEWLR extends BlockEntityWithoutLevelRenderer {

	public static final RocketPartBEWLR INSTANCE = new RocketPartBEWLR();

	public RocketPartBEWLR() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack pose, MultiBufferSource buffer,
			int light, int overlay) {
		if (stack.getItem() instanceof RocketPartItem part) {
			RocketPartModel model = part.getRocketPart().bake();
			model.render(pose, buffer, light, overlay, 1f, 1f, 1f, 1f);
		}
	}

}
