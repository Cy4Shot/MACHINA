package com.machina.client.bewlr;

import org.jetbrains.annotations.NotNull;

import com.machina.api.item.RocketItem;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.util.math.ItemTransformUtil;
import com.machina.client.model.rocket.RocketModel;
import com.machina.client.model.rocket.RocketModel.RocketModelBuilder;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RocketBEWLR extends BlockEntityWithoutLevelRenderer {

    public static final RocketBEWLR INSTANCE = new RocketBEWLR();

    public RocketBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, @NotNull ItemDisplayContext ctx, @NotNull PoseStack pose,
            @NotNull MultiBufferSource buffer, int light, int overlay) {
        if (stack.getItem() instanceof RocketItem) {
            RocketModelBuilder builder = new RocketModelBuilder();
            for (RocketPartType type : RocketPartType.values()) {
                builder.add(RocketItem.getPart(stack, type));
            }
            RocketModel model = builder.build();
            float scale = model.getGUIScale();
            ItemTransform transforms = ItemTransformUtil.BLOCK.getTransform(ctx);

            pose.pushPose();
            pose.translate(0.5F, 0.75F, 0.5F);
            transforms.apply(false, pose);
            pose.scale(1, -1, 1);
            if (ctx.equals(ItemDisplayContext.GUI)) {

                // Align to item slot
                pose.translate(0, 1.25f, 0);
                pose.scale(scale * 1.5f, scale * 1.5f, scale * 1.5f);
            } else if (ctx.equals(ItemDisplayContext.GROUND)) {

                // Restore IRL scaling
                float irlScale = 0.5f / scale;
                pose.scale(irlScale, irlScale, irlScale);
            }

            model.render(pose, buffer, light, overlay, 0xFFFFFFFF);
            pose.popPose();
        }
    }

}
