package com.machina.api.client.cinema.effect;

import com.machina.api.client.cinema.effect.renderer.CinematicTextureOverlay;
import com.machina.api.util.MachinaRL;

public record FadeInEffect(int fadeDuration) implements CameraEffect {

	@Override
	public void tickEffect(int tick) {
		if (tick == 1) {
			CinematicTextureOverlay.rl = new MachinaRL("textures/cinematic/black.png");
			CinematicTextureOverlay.render = true;
		} else if (tick == fadeDuration - 1) {
			CinematicTextureOverlay.render = false;
		}
		CinematicTextureOverlay.opacity = 1 - Math.max(0f, (float) (tick) / (float) (fadeDuration));
	}
}