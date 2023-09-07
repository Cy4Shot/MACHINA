package com.machina.api.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

public class CinematicCompleteEvent extends Event {
	public final ServerPlayer player;
	public final String id;

	public CinematicCompleteEvent(ServerPlayer player, String id) {
		this.player = player;
		this.id = id;
	}
}