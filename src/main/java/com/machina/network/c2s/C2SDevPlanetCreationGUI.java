package com.machina.network.c2s;

import com.machina.network.INetworkMessage;
import com.machina.world.PlanetRegistrationHandler;

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
		ServerWorld world = PlanetRegistrationHandler.createPlanet(context.getSender().getServer(),
				String.valueOf(dimensionID));
		if (action == ActionType.TELEPORT) {
			PlanetRegistrationHandler.sendPlayerToDimension(context.getSender(), world, context.getSender().position());
		}
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeEnum(action);
		buffer.writeInt(dimensionID);
	}

	public static C2SDevPlanetCreationGUI decode(PacketBuffer buffer) {
		return new C2SDevPlanetCreationGUI(buffer.readEnum(ActionType.class), buffer.readInt());
	}

	public enum ActionType {
		CREATE,
		TELEPORT;
	}
}