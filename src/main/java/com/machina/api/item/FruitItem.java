package com.machina.api.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class FruitItem extends BlockItem {

    public FruitItem(Block b, Properties p) {
        super(b, p);
    }

    @Override
    public @NotNull InteractionResult place(BlockPlaceContext ctx) {
        if (ctx.getPlayer() != null) {
            return InteractionResult.FAIL;
        }
        return super.place(ctx);
    }
}
