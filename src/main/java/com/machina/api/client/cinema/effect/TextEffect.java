package com.machina.api.client.cinema.effect;

import com.machina.api.client.cinema.effect.renderer.CinematicTextOverlay;

import net.minecraft.network.chat.Component;

public record TextEffect(int start, int stop, int fade, int subdelay, String title, String sub, boolean fadein,
		boolean fadeout) implements CameraEffect {

	public TextEffect(int start, int stop, int fade, int subdelay, String title, String sub) {
		this(start, stop, fade, subdelay, title, sub, true, true);
	}

	@Override
	public void tickEffect(int tick) {
		if (tick > start + 1 && tick < stop - 1) {
			CinematicTextOverlay.title = Component.literal(title);
			CinematicTextOverlay.subtitle = Component.literal(sub);
			CinematicTextOverlay.render = true;

			if (tick < start) {
				CinematicTextOverlay.titleOpacity = 0f;
			} else if (tick < fade + start) {
				CinematicTextOverlay.titleOpacity = fadein ? (float) (tick - start) / (float) fade : 1f;
			} else if (tick > stop - fade) {
				CinematicTextOverlay.titleOpacity = fadeout ? (float) (stop - tick) / (float) fade : 1f;
			} else {
				CinematicTextOverlay.titleOpacity = 1f;
			}

			if (tick < subdelay + start) {
				CinematicTextOverlay.subOpacity = 0f;
			} else if (tick < fade + subdelay + start) {
				CinematicTextOverlay.subOpacity = fadein ? (float) (tick - subdelay - start) / (float) fade : 1f;
			} else if (tick > stop - fade) {
				CinematicTextOverlay.subOpacity = fadeout ? (float) (stop - tick) / (float) fade : 1f;
			} else {
				CinematicTextOverlay.subOpacity = 1f;
			}
		} else {
			CinematicTextOverlay.render = false;
		}
	}
}