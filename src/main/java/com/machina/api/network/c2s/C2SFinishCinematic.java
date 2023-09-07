package com.machina.api.network.c2s;

import com.machina.api.event.CinematicCompleteEvent;
import com.machina.api.network.C2SMessage;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;

public record C2SFinishCinematic(String id) implements C2SMessage {
	public static C2SFinishCinematic decode(FriendlyByteBuf buf) {
		return new C2SFinishCinematic(buf.readUtf());
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(id);
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> MinecraftForge.EVENT_BUS.post(new CinematicCompleteEvent(player, id)));
	}
}