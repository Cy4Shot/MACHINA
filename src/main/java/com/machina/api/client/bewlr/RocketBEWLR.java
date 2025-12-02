package com.machina.api.client.bewlr;

import org.jetbrains.annotations.NotNull;

import com.machina.api.item.RocketItem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RocketBEWLR extends BlockEntityWithoutLevelRenderer {

    public static final RocketBEWLR INSTANCE = new RocketBEWLR();

    public RocketBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, @NotNull ItemDisplayContext ctx, @NotNull PoseStack pose, @NotNull MultiBufferSource buffer,
                             int light, int overlay) {
        if (stack.getItem() instanceof RocketItem part) {
//            ItemTransform transforms = ItemTransformUtil.BLOCK.getTransform(ctx);
//            float scale = rocket.getGUIScale();
//
//            pose.pushPose();
//
//            pose.translate(0.5F, 0.75F, 0.5F);
//            transforms.apply(false, pose);
//            pose.scale(1, -1, 1);
//
//            if (ctx.equals(ItemDisplayContext.GUI)) {
//                pose.scale(scale, scale, scale);
//            }
//
//            rocket.bake().render(pose, buffer, light, overlay, 1f, 1f, 1f, 1f);
//
//            pose.popPose();
        }
    }

}
