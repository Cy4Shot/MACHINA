package com.machina.client.cinema.effect;

import java.util.Random;

import com.machina.network.MachinaNetwork;
import com.machina.network.c2s.C2SSpawnParticle;

import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.vector.Vector3d;

public class ParticleEffect extends CameraEffect {

	private Vector3d pos;
	private Vector3d maxOffset;
	private float speed;
	private float chance;
	private IParticleData particle;

	public ParticleEffect(IParticleData particle, Vector3d pos, double offset, float speed, float chance) {
		this.particle = particle;
		this.pos = pos;
		this.speed = speed;
		this.chance = chance;
		this.maxOffset = new Vector3d(offset, offset, offset);
	}

	@Override
	public void tickEffect(int tick) {
		if (new Random().nextFloat() < chance)
			MachinaNetwork.CHANNEL.sendToServer(new C2SSpawnParticle(particle, speed, 1, pos, maxOffset));
	}

}
