package com.machina.client.explosion.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.machina.client.explosion.NuclearExplosion;
import com.machina.client.explosion.NuclearExplosionManager;
import com.machina.registration.init.SoundInit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class NuclearExplosionSounds {

	private static final Minecraft mc = Minecraft.getInstance();
	private static final float SPEED_OF_SOUND = 343.0f; // blocks per second

	// Track which sounds have been triggered for each explosion
	private static final Map<UUID, ExplosionSoundState> soundStates = new HashMap<>();

	private static class ExplosionSoundState {
		boolean snapPlayed = false;
		boolean rumblePlayed = false;
		boolean windPlayed = false;
		boolean distantBoomPlayed = false;
		float soundArrivalTime; // When sound reaches player (based on distance)
	}

	public static void tick() {
		if (mc.player == null || mc.level == null || mc.isPaused()) {
			return;
		}

		Vec3 playerPos = mc.player.position();

		// Clean up states for finished explosions
		soundStates.keySet().removeIf(id ->
			NuclearExplosionManager.getActiveExplosions().stream()
				.noneMatch(e -> getExplosionId(e).equals(id))
		);

		for (NuclearExplosion explosion : NuclearExplosionManager.getActiveExplosions()) {
			if (!explosion.getDimension().equals(mc.level.dimension())) {
				continue;
			}

			UUID id = getExplosionId(explosion);
			ExplosionSoundState state = soundStates.computeIfAbsent(id, k -> {
				ExplosionSoundState newState = new ExplosionSoundState();
				// Calculate when sound arrives based on distance
				double dist = playerPos.distanceTo(explosion.getPosition());
				newState.soundArrivalTime = (float) (dist / SPEED_OF_SOUND);

				// If joined in progress, skip initial sounds (snap, rumble start, wind start)
				if (explosion.isJoinedInProgress()) {
					newState.snapPlayed = true;
					newState.rumblePlayed = true;
					newState.windPlayed = true;
					newState.distantBoomPlayed = true;
				}
				return newState;
			});

			float time = explosion.getTime(); // Time in seconds since explosion
			double distance = playerPos.distanceTo(explosion.getPosition());
			float power = explosion.getPower();

			// Only play sounds after they've had time to travel
			float adjustedTime = time - state.soundArrivalTime;
			if (adjustedTime < 0) {
				continue; // Sound hasn't arrived yet
			}

			// Snap/crack - plays at all distances with varying volume and pitch
			if (!state.snapPlayed && adjustedTime >= 0) {
				float snapVolume = getVolumeForDistance(distance, 500 * power, 1.0f);
				// Lower pitch at distance (sounds more like thunder)
				float snapPitch = 1.0f - (float)(Math.min(distance, 400 * power) / (500 * power)) * 0.4f;
				playSound(SoundInit.NUKE_SNAP.get(), explosion.getPosition(), snapVolume, snapPitch);
				state.snapPlayed = true;
			}

			// Rumble - looping low rumble that fades out, plays at all distances
			if (!state.rumblePlayed && adjustedTime >= 0.1f) {
				float rumbleVolume = getVolumeForDistance(distance, 500 * power, 0.8f);
				mc.getSoundManager().play(new NukeRumbleSound(explosion, rumbleVolume));
				state.rumblePlayed = true;
			}

			// Wind/shockwave - looping, plays at all ranges, quieter for small explosions
			if (distance < 500 * power) {
				if (!state.windPlayed && adjustedTime >= 0) {
					float windBaseVolume = 0.3f * Math.min(power, 2.0f);
					float windVolume = getVolumeForDistance(distance, 400 * power, windBaseVolume);
					float windPitch = 0.9f + (float)(distance / (1000 * power)) * 0.2f;
					mc.getSoundManager().play(new NukeWindSound(explosion, windVolume, windPitch));
					state.windPlayed = true;
				}
			}

			// Far range sounds (> 200 blocks)
			if (distance >= 200 * power) {
				// Distant boom
				if (!state.distantBoomPlayed && adjustedTime >= 0) {
					playSound(SoundInit.NUKE_DISTANT_BOOM.get(), explosion.getPosition(),
						getVolumeForDistance(distance, 1000 * power, 0.5f),
						0.7f + (float)(distance / (2000 * power)) * 0.3f); // Lower pitch at extreme distance
					state.distantBoomPlayed = true;
				}
			}
		}
	}

	private static UUID getExplosionId(NuclearExplosion explosion) {
		// Create a unique ID based on position and start time
		Vec3 pos = explosion.getPosition();
		return UUID.nameUUIDFromBytes(
			String.format("%.2f,%.2f,%.2f,%d", pos.x, pos.y, pos.z, explosion.getStartTime()).getBytes()
		);
	}

	private static float getVolumeForDistance(double distance, float maxRange, float baseVolume) {
		if (distance >= maxRange) return 0f;
		float factor = 1.0f - (float)(distance / maxRange);
		return baseVolume * factor * factor; // Quadratic falloff
	}

	private static void playSound(SoundEvent sound, Vec3 pos, float volume, float pitch) {
		if (volume <= 0.01f) return;

		mc.getSoundManager().play(new SimpleSoundInstance(
			sound.getLocation(),
			SoundSource.AMBIENT,
			volume,
			pitch,
			SoundInstance.createUnseededRandom(),
			false,
			0,
			SoundInstance.Attenuation.NONE,
			pos.x, pos.y, pos.z,
			false
		));
	}

	public static void clear() {
		soundStates.clear();
	}
}
