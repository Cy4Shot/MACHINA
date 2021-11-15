package com.cy4.machina.network.message.to_server;

import com.cy4.machina.api.network.message.IMachinaMessage;
import com.cy4.machina.world.DynamicDimensionHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;

import net.minecraftforge.fml.network.NetworkEvent.Context;

public class DevPlanetCreationGUIMessage implements IMachinaMessage {

	public final ActionType action;
	public final int dimensionID;

	public DevPlanetCreationGUIMessage(ActionType action, int dimensionID) {
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

	public static DevPlanetCreationGUIMessage decode(PacketBuffer buffer) {
		ActionType action = buffer.readEnum(ActionType.class);
		int dimensionID = buffer.readInt();
		return new DevPlanetCreationGUIMessage(action, dimensionID);
	}

	public enum ActionType {
		CREATE, TELEPORT;
	}

}
