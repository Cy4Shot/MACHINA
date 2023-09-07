package com.machina.api.client.cinema.effect;

import com.machina.api.client.cinema.effect.renderer.CinematicTextureOverlay;
import com.machina.api.util.MachinaRL;

public record OverlayEffect(float duration, String path, float opacity) implements CameraEffect {
	
	public OverlayEffect(float duration, String path) {
		this(duration, path, 1f);
	}
	
	@Override
	public void tickEffect(int tick) {
		if (tick == 1) {
			CinematicTextureOverlay.rl = new MachinaRL("textures/cinematic/" + path + ".png");
			CinematicTextureOverlay.render = true;
		} else if (tick == duration - 1) {
			CinematicTextureOverlay.render = false;
		}
		CinematicTextureOverlay.opacity = opacity;
	}
}