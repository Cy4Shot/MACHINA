/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.network;

import java.util.function.Function;

import com.cy4.machina.api.network.BaseNetwork;
import com.cy4.machina.api.network.message.IMachinaMessage;
import com.cy4.machina.network.message.C2SDevPlanetCreationGUI;
import com.cy4.machina.network.message.S2CSyncStarchart;
import com.cy4.machina.util.MachinaRL;

import net.minecraft.network.PacketBuffer;

import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MachinaNetwork extends BaseNetwork {

	public static final String NETWORK_VERSION = "0.1.0";

	public static final SimpleChannel CHANNEL = newSimpleChannel("channel");

	public static void init() {
		registerServerToClient(S2CSyncStarchart.class, S2CSyncStarchart::decode);
		registerClientToServer(C2SDevPlanetCreationGUI.class, C2SDevPlanetCreationGUI::decode);
	}

	private static SimpleChannel newSimpleChannel(String name) {
		return NetworkRegistry.newSimpleChannel(new MachinaRL(name), () -> NETWORK_VERSION,
				version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));
	}

	private static <M extends IMachinaMessage> void registerServerToClient(Class<M> type,
			Function<PacketBuffer, M> decoder) {
		registerServerToClient(CHANNEL, type, decoder);
	}

	private static <M extends IMachinaMessage> void registerClientToServer(Class<M> type,
			Function<PacketBuffer, M> decoder) {
		registerClientToServer(CHANNEL, type, decoder);
	}

}
