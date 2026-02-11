package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.rocket.RocketEntity;
import com.machina.rocket.RocketMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public record C2SRocketSetTab(int entity, byte tab) implements C2SMessage {
    public static C2SRocketSetTab decode(FriendlyByteBuf buf) {
        return new C2SRocketSetTab(buf.readInt(), buf.readByte());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entity);
        buf.writeByte(tab);
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            if (player.containerMenu instanceof RocketMenu menu) {
                if (menu.entity.getId() != entity) return;
                menu.rebuildSlots(tab, player.getInventory());
                menu.broadcastChanges();
            }
        });
    }
}