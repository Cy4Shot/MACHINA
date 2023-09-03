package com.machina.network;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.logging.log4j.util.TriConsumer;

import com.machina.Machina;
import com.machina.network.s2c.S2CFluidSync;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class MachinaNetwork {
	public static final SimpleChannel CHANNEL = NetworkRegistry
			.newSimpleChannel(new ResourceLocation(Machina.MOD_ID, "main"), () -> "0", "0"::equals, "0"::equals);

	public static void init() {

		int i = 0;

		// C2S

		// S2C

		CHANNEL.registerMessage(i++, S2CFluidSync.class, S2CFluidSync::encode, S2CFluidSync::decode,
				makeClientBoundHandler(S2CFluidSync::handle));
	}

	@SuppressWarnings("unused")
	private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeServerBoundHandler(
			TriConsumer<T, MinecraftServer, ServerPlayer> handler) {
		return (m, ctx) -> {
			handler.accept(m, ctx.get().getSender().getServer(), ctx.get().getSender());
			ctx.get().setPacketHandled(true);
		};
	}

	private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeClientBoundHandler(Consumer<T> consumer) {
		return (m, ctx) -> {
			consumer.accept(m);
			ctx.get().setPacketHandled(true);
		};
	}

	public static void sendToClients(Object packet) {
		for (ServerPlayer player : Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers()) {
			sendToClient(player, packet);
		}
	}

	public static void sendToClient(ServerPlayer player, Object packet) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
	}
}
