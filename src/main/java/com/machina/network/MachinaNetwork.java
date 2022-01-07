/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2021 Machina Team
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p>
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.network;

import java.util.function.Function;

import com.machina.api.util.MachinaRL;
import com.machina.network.message.C2SDevPlanetCreationGUI;
import com.machina.network.message.S2CStarchartSyncMessage;
import com.matyrobbrt.lib.network.BaseNetwork;
import com.matyrobbrt.lib.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;

import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MachinaNetwork extends BaseNetwork {

    public static final String NETWORK_VERSION = "0.1.0";

    public static final SimpleChannel CHANNEL = newSimpleChannel("channel");

    public static void init() {
        registerClientToServer(C2SDevPlanetCreationGUI.class, C2SDevPlanetCreationGUI::decode);

        registerServerToClient(S2CStarchartSyncMessage.class, S2CStarchartSyncMessage::decode);
    }

    private static SimpleChannel newSimpleChannel(String name) {
        return NetworkRegistry.newSimpleChannel(new MachinaRL(name), () -> NETWORK_VERSION,
                version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));
    }

	private static <M extends INetworkMessage> void registerServerToClient(Class<M> type,
			Function<PacketBuffer, M> decoder) {
        registerServerToClient(CHANNEL, type, decoder);
    }

    private static <M extends INetworkMessage> void registerClientToServer(Class<M> type, Function<PacketBuffer, M> decoder) {
        registerClientToServer(CHANNEL, type, decoder);
    }

}
