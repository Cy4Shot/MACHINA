package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;
import com.machina.client.explosion.NuclearExplosionManager;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record S2CNuclearExplosion(double x, double y, double z, float power) implements S2CMessage<S2CNuclearExplosion> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CNuclearExplosion> streamCodec() {
		return StreamCodec.composite(
				ByteBufCodecs.DOUBLE, S2CNuclearExplosion::x,
				ByteBufCodecs.DOUBLE, S2CNuclearExplosion::y,
				ByteBufCodecs.DOUBLE, S2CNuclearExplosion::z,
				ByteBufCodecs.FLOAT, S2CNuclearExplosion::power,
				S2CNuclearExplosion::new);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		NuclearExplosionManager.addExplosion(new Vec3(x, y, z), power);
	}
}
