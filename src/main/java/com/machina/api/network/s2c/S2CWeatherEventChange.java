package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;
import com.machina.weather.WeatherEvent;
import com.machina.weather.manager.ClientWeatherManager;
import com.machina.weather.system.ClientWeatherSystem;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record S2CWeatherEventChange(WeatherEvent event, int durationTicks, int remainingTicks, int ageTicks)
		implements S2CMessage<S2CWeatherEventChange> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CWeatherEventChange> streamCodec() {
		return StreamCodec.composite(WeatherEvent.STREAM_CODEC, S2CWeatherEventChange::event, ByteBufCodecs.INT,
				S2CWeatherEventChange::durationTicks, ByteBufCodecs.INT, S2CWeatherEventChange::remainingTicks,
				ByteBufCodecs.INT, S2CWeatherEventChange::ageTicks, S2CWeatherEventChange::new);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		ClientWeatherSystem system = ClientWeatherManager.getSystem((ClientLevel) player.level());
		if (system != null) {
			system.setCurrentEvent(event);
			system.setWeatherTimeline(durationTicks, remainingTicks, ageTicks);
		}
	}
}
