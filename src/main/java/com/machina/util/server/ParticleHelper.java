package com.machina.util.server;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ParticleHelper {
	public static void spawnParticle(World world, IParticleData type, BlockPos pos, double speed,
			@Nullable Vector3f offset) {
		Random r = new Random();
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 0.5D;
		double d2 = (double) pos.getZ() + 0.5D;
		if (offset != null) {
			d0 += offset.x();
			d1 += offset.y();
			d2 += offset.z();
		}
		double d3 = (r.nextDouble() - 0.5D) * speed;
		double d4 = (r.nextDouble() - 0.5D) * speed;
		double d5 = (r.nextDouble() - 0.5D) * speed;

		world.addParticle(type, d0, d1, d2, d3, d4, d5);
	}

	public static void spawnParticle(ServerWorld world, IParticleData type, Vector3d pos, int count, double speed,
			@Nullable Vector3d offset) {
		world.sendParticles(type, pos.x(), pos.y(), pos.z(), count, speed, offset.x(), offset.y(), offset.z());
	}
}
