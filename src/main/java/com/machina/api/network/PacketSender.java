package com.machina.api.network;

import com.machina.Machina;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketSender {

	public static final SimpleChannel CHANNEL = NetworkRegistry
			.newSimpleChannel(new ResourceLocation(Machina.MOD_ID, "main"), () -> "0", "0"::equals, "0"::equals);

	public static void sendToClients(Object packet) {
		for (ServerPlayer player : Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers()) {
			sendToClient(player, packet);
		}
	}

	public static void sendToClient(ServerPlayer player, Object packet) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
	}
}
