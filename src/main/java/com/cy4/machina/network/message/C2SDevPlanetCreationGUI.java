/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.network.message;

import com.cy4.machina.world.DynamicDimensionHelper;
import com.machina.api.network.message.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;

import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SDevPlanetCreationGUI implements INetworkMessage {

	public final ActionType action;
	public final int dimensionID;

	public C2SDevPlanetCreationGUI(ActionType action, int dimensionID) {
		this.action = action;
		this.dimensionID = dimensionID;
	}

	@Override
	public void handle(Context context) {
		ServerWorld world = DynamicDimensionHelper.createPlanet(context.getSender().getServer(),
				String.valueOf(dimensionID));
		if (action == ActionType.TELEPORT) {
			DynamicDimensionHelper.sendPlayerToDimension(context.getSender(), world, context.getSender().position());
		}
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeEnum(action);
		buffer.writeInt(dimensionID);
	}

	public static C2SDevPlanetCreationGUI decode(PacketBuffer buffer) {
		ActionType action = buffer.readEnum(ActionType.class);
		int dimensionID = buffer.readInt();
		return new C2SDevPlanetCreationGUI(action, dimensionID);
	}

	public enum ActionType {
		CREATE, TELEPORT;
	}

}
