package com.machina.client.explosion.sound;

import com.machina.client.explosion.NuclearExplosion;
import com.machina.client.explosion.NuclearExplosionManager;
import com.machina.registration.init.SoundInit;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class NukeRumbleSound extends AbstractTickableSoundInstance {

	private final NuclearExplosion explosion;
	private final float maxVolume;
	private final float fadeStartProgress;
	private final float fadeEndProgress;

	public NukeRumbleSound(NuclearExplosion explosion, float volume) {
		super(SoundInit.NUKE_RUMBLE.get(), SoundSource.AMBIENT, explosion.random());
		this.explosion = explosion;
		this.maxVolume = volume;
		this.fadeStartProgress = 0.15f;  // Start fading at 15%
		this.fadeEndProgress = 0.5f;     // Fully faded by 50%

		Vec3 pos = explosion.getPosition();
		this.x = pos.x;
		this.y = pos.y;
		this.z = pos.z;
		this.looping = true;
		this.volume = volume;
		this.pitch = 0.8f;
		this.attenuation = Attenuation.NONE;
	}

	@Override
	public void tick() {
		// Stop if explosion is no longer active
		if (!NuclearExplosionManager.getActiveExplosions().contains(explosion)) {
			this.stop();
			return;
		}

		float progress = explosion.getProgress();

		// Stop when explosion is done or faded
		if (explosion.isFinished() || progress >= fadeEndProgress) {
			this.stop();
			return;
		}

		// Fade out volume based on progress
		if (progress > fadeStartProgress) {
			float fadeProgress = (progress - fadeStartProgress) / (fadeEndProgress - fadeStartProgress);
			this.volume = maxVolume * (1.0f - fadeProgress * fadeProgress); // Quadratic fade
		} else {
			this.volume = maxVolume;
		}
	}

	@Override
	public boolean canStartSilent() {
		return true;
	}
}
