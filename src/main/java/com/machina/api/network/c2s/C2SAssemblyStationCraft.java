package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.api.util.block.BlockHelper;
import com.machina.block.entity.machine.RocketAssemblyStationBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record C2SAssemblyStationCraft(BlockPos pos) implements C2SMessage {
    public static C2SAssemblyStationCraft decode(FriendlyByteBuf buf) {
        return new C2SAssemblyStationCraft(buf.readBlockPos());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> BlockHelper.doWithTe(player.level(), pos, RocketAssemblyStationBlockEntity.class,
                RocketAssemblyStationBlockEntity::startCrafting));
    }
}