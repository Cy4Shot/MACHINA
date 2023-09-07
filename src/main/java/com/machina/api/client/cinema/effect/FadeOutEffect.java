package com.machina.api.client.cinema.effect;

import com.machina.api.client.cinema.effect.renderer.CinematicTextureOverlay;
import com.machina.api.util.MachinaRL;

public record FadeOutEffect(int totalDuration, int fadeDuration) implements CameraEffect {

	@Override
	public void tickEffect(int tick) {
		if (tick == 1 + totalDuration - fadeDuration) {
			CinematicTextureOverlay.rl = new MachinaRL("textures/cinematic/black.png");
			CinematicTextureOverlay.render = true;
		} else if (tick == totalDuration - 1) {
			CinematicTextureOverlay.render = false;
		}
		CinematicTextureOverlay.opacity = Math.max(0f,
				(float) (tick - totalDuration + fadeDuration) / (float) (fadeDuration));
	}
}