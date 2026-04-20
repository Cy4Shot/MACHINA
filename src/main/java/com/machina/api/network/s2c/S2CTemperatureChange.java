package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;
import com.machina.weather.manager.ClientWeatherManager;
import com.machina.weather.system.ClientWeatherSystem;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record S2CTemperatureChange(float temperature, float roughMin, float roughMax)
		implements S2CMessage<S2CTemperatureChange> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CTemperatureChange> streamCodec() {
		return StreamCodec.composite(ByteBufCodecs.FLOAT, S2CTemperatureChange::temperature, ByteBufCodecs.FLOAT,
				S2CTemperatureChange::roughMin, ByteBufCodecs.FLOAT, S2CTemperatureChange::roughMax,
				S2CTemperatureChange::new);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		ClientWeatherSystem system = ClientWeatherManager.getSystem((ClientLevel) player.level());
		if (system != null) {
			system.setTemperatureData(temperature, roughMin, roughMax);
		}
	}
}
