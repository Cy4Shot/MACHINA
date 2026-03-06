package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;
import com.machina.weather.WeatherEvent;
import com.machina.weather.manager.ClientWeatherManager;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record S2CWeatherEventChange(WeatherEvent event) implements S2CMessage<S2CWeatherEventChange> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CWeatherEventChange> streamCodec() {
		return WeatherEvent.STREAM_CODEC.map(S2CWeatherEventChange::new, S2CWeatherEventChange::event);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		ClientWeatherManager.getSystem((ClientLevel) player.level()).setCurrentEvent(event);
	}
}