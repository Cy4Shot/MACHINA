package com.machina.network.s2c;

import com.machina.network.INetworkMessage;
import com.machina.weather.event.WeatherClientEvents;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2CSyncWeather implements INetworkMessage {

	private final CompoundNBT weather;

	public S2CSyncWeather(final CompoundNBT weather) {
		this.weather = weather;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			try {
				WeatherClientEvents.checkClientWeather();
				WeatherClientEvents.weatherManager.nbtSyncFromServer(weather);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		context.setPacketHandled(true);
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeNbt(weather);
	}

	public static S2CSyncWeather decode(PacketBuffer buffer) {
		return new S2CSyncWeather(buffer.readNbt());
	}
}