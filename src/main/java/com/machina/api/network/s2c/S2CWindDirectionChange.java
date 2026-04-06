package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;
import com.machina.weather.manager.ClientWeatherManager;
import com.machina.weather.system.ClientWeatherSystem;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record S2CWindDirectionChange(Vec2 direction) implements S2CMessage<S2CWindDirectionChange> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CWindDirectionChange> streamCodec() {
		return StreamCodec.composite(ByteBufCodecs.FLOAT, payload -> payload.direction.x, ByteBufCodecs.FLOAT,
				payload -> payload.direction.y, (x, y) -> new S2CWindDirectionChange(new Vec2(x, y)));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		ClientWeatherSystem system = ClientWeatherManager.getSystem((ClientLevel) player.level());
		if (system != null) {
			system.setWindDirection(direction);
		}
	}
}
