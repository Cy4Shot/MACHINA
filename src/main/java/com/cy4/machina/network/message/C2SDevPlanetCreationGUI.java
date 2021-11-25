/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.network.message;

import com.cy4.machina.api.network.message.IMachinaMessage;
import com.cy4.machina.world.DynamicDimensionHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;

import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SDevPlanetCreationGUI implements IMachinaMessage {

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
