package com.machina.api.client.cinema.effect;

import java.util.Random;

import com.machina.api.network.c2s.C2SSpawnParticle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class ParticleEffect implements CameraEffect {

	private Vec3 pos;
	private Vec3 maxOffset;
	private float speed;
	private float chance;
	private ParticleOptions particle;

	public ParticleEffect(ParticleOptions particle, Vec3 pos, double offset, float speed, float chance) {
		this.particle = particle;
		this.pos = pos;
		this.speed = speed;
		this.chance = chance;
		this.maxOffset = new Vec3(offset, offset, offset);
	}

	@Override
	public void tickEffect(int tick) {
		if (new Random().nextFloat() < chance)
			PacketDistributor.sendToServer(new C2SSpawnParticle(particle, speed, 1, pos, maxOffset));
	}

}