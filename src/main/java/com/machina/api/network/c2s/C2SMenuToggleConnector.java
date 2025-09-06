package com.machina.api.network.c2s;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.network.C2SMessage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public record C2SMenuToggleConnector(Direction dir, BlockPos pos) implements C2SMessage {
    public static C2SMenuToggleConnector decode(FriendlyByteBuf buf) {
        return new C2SMenuToggleConnector(buf.readEnum(Direction.class), buf.readBlockPos());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(dir);
        buf.writeBlockPos(pos);
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            BlockEntity be = player.level().getBlockEntity(pos);
            if (be instanceof ConnectorBlockEntity<?, ?> c) {
                c.setConnection(dir, c.getConnection(dir).toggleIO());
            }
        });
    }
}