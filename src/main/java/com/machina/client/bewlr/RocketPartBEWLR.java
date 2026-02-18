package com.machina.client.bewlr;

import org.jetbrains.annotations.NotNull;

import com.machina.api.item.RocketPartItem;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.util.math.ItemTransformUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RocketPartBEWLR extends BlockEntityWithoutLevelRenderer {

	public static final RocketPartBEWLR INSTANCE = new RocketPartBEWLR();

	public RocketPartBEWLR() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack stack, @NotNull ItemDisplayContext ctx, @NotNull PoseStack pose,
			@NotNull MultiBufferSource buffer, int light, int overlay) {
		if (stack.getItem() instanceof RocketPartItem part) {

			RocketPart<?> rocket = part.getRocketPart();
			ItemTransform transforms = ItemTransformUtil.BLOCK.getTransform(ctx);
			float scale = rocket.getGUIScale();

			pose.pushPose();

			pose.translate(0.5F, 0.75F, 0.5F);
			transforms.apply(false, pose);
			pose.scale(1, -1, 1);

			if (ctx.equals(ItemDisplayContext.GUI)) {
				pose.scale(scale, scale, scale);
			}

			rocket.bake().render(pose, buffer, light, overlay, 0xFFFFFFFF);

			pose.popPose();
		}
	}

}
