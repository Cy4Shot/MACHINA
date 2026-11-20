package com.machina.client.explosion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class NuclearExplosionManager {

	private static final List<NuclearExplosion> activeExplosions = new ArrayList<>();

	public static void addExplosion(BlockPos pos, float power) {
		activeExplosions.add(new NuclearExplosion(pos, power));
	}

	public static void addExplosion(Vec3 pos, float power) {
		activeExplosions.add(new NuclearExplosion(pos, power));
	}

	public static void tick() {
		Iterator<NuclearExplosion> it = activeExplosions.iterator();
		while (it.hasNext()) {
			NuclearExplosion explosion = it.next();
			if (explosion.isFinished()) {
				it.remove();
			}
		}
	}

	public static List<NuclearExplosion> getActiveExplosions() {
		return activeExplosions;
	}

	public static void clear() {
		activeExplosions.clear();
	}

	public static boolean hasActiveExplosions() {
		return !activeExplosions.isEmpty();
	}
}
