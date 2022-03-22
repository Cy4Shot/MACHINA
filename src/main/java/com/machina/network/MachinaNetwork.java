package com.machina.network;

import java.util.function.Function;

import com.machina.network.message.C2SDevPlanetCreationGUI;
import com.machina.network.message.INetworkMessage;
import com.machina.network.message.S2CStarchartSyncMessage;
import com.machina.network.message.S2CSyncGuiValues;
import com.machina.util.MachinaRL;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MachinaNetwork extends BaseNetwork {

	public static final String NETWORK_VERSION = "0.1.0";

	public static final SimpleChannel CHANNEL = newSimpleChannel("channel");

	public static void init() {
		registerServerToClient(S2CSyncGuiValues.class, S2CSyncGuiValues::decode);
		registerServerToClient(S2CStarchartSyncMessage.class, S2CStarchartSyncMessage::decode);

		registerClientToServer(C2SDevPlanetCreationGUI.class, C2SDevPlanetCreationGUI::decode);

		BaseNetwork.MACHINA_CHANNEL = CHANNEL;
	}

	private static SimpleChannel newSimpleChannel(String name) {
		return NetworkRegistry.newSimpleChannel(new MachinaRL(name), () -> NETWORK_VERSION,
				version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));
	}

	private static <M extends INetworkMessage> void registerServerToClient(Class<M> type,
			Function<PacketBuffer, M> decoder) {
		registerServerToClient(CHANNEL, type, decoder);
	}

	private static <M extends INetworkMessage> void registerClientToServer(Class<M> type,
			Function<PacketBuffer, M> decoder) {
		registerClientToServer(CHANNEL, type, decoder);
	}

}
