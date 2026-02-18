package com.machina.api.network.c2s;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.network.C2SMessage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public record C2SMenuToggleConnector(Direction dir, BlockPos pos) implements C2SMessage<C2SMenuToggleConnector> {
    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, C2SMenuToggleConnector> streamCodec() {
        return StreamCodec.composite(Direction.STREAM_CODEC, C2SMenuToggleConnector::dir, BlockPos.STREAM_CODEC,
                C2SMenuToggleConnector::pos, C2SMenuToggleConnector::new);
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            BlockEntity be = player.level().getBlockEntity(pos);
            if (be instanceof ConnectorBlockEntity<?, ?> c) {
                c.setConnection(dir, c.getConnection(dir).toggleIO());
            }
        });
    }
}