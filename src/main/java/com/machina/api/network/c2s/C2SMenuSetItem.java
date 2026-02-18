package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public record C2SMenuSetItem(int slot, ItemStack stack, BlockPos pos) implements C2SMessage<C2SMenuSetItem> {

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, C2SMenuSetItem> streamCodec() {
        return StreamCodec.composite(ByteBufCodecs.INT, C2SMenuSetItem::slot, ItemStack.STREAM_CODEC,
                C2SMenuSetItem::stack, BlockPos.STREAM_CODEC, C2SMenuSetItem::pos, C2SMenuSetItem::new).cast();
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            BlockEntity be = player.level().getBlockEntity(pos);
            if (be instanceof Container c) {
                c.setItem(slot, stack);
                c.setChanged();
            }
        });
    }
}