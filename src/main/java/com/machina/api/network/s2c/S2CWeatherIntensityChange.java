package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;
import com.machina.weather.manager.ClientWeatherManager;
import com.machina.weather.system.ClientWeatherSystem;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record S2CWeatherIntensityChange(float intensity) implements S2CMessage<S2CWeatherIntensityChange> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CWeatherIntensityChange> streamCodec() {
		return ByteBufCodecs.FLOAT.map(S2CWeatherIntensityChange::new, S2CWeatherIntensityChange::intensity);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		ClientWeatherSystem system = ClientWeatherManager.getSystem((ClientLevel) player.level());
		if (system != null) {
			system.setWeatherIntensity(Mth.clamp(intensity, 0.0F, 1.0F));
		}
	}
}
