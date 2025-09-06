package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record C2SItemMenuSync(int slot, ItemStack stack) implements C2SMessage {
    public static C2SItemMenuSync decode(FriendlyByteBuf buf) {
        return new C2SItemMenuSync(buf.readInt(), buf.readItem());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(slot);
        buf.writeItem(stack);
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            player.getInventory().setItem(slot, stack);
            player.getInventory().setChanged();
        });
    }
}