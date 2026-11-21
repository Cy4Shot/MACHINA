package com.machina.client.explosion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.machina.client.explosion.sound.NuclearExplosionSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NuclearExplosionManager {

	private static final List<NuclearExplosion> activeExplosions = new ArrayList<>();

	public static void addExplosion(BlockPos pos, float power, ResourceKey<Level> dimension) {
		activeExplosions.add(new NuclearExplosion(pos, power, dimension));
	}

	public static void addExplosion(Vec3 pos, float power, ResourceKey<Level> dimension) {
		activeExplosions.add(new NuclearExplosion(pos, power, dimension));
	}

	public static void addExplosion(Vec3 pos, float power, float initialProgress, ResourceKey<Level> dimension) {
		activeExplosions.add(new NuclearExplosion(pos, power, initialProgress, dimension));
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
		NuclearExplosionSounds.clear();
	}

	public static boolean hasActiveExplosions() {
		return !activeExplosions.isEmpty();
	}
}
