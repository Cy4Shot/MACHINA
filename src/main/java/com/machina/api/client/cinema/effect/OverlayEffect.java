package com.machina.api.client.cinema.effect;

import com.machina.api.client.cinema.effect.renderer.CinematicTextureOverlay;
import com.machina.api.util.MachinaRL;

public record OverlayEffect(float duration, String path, float opacity) implements CameraEffect {

    public OverlayEffect(float duration) {
        this(duration, "black", 1f);
    }
    
    public OverlayEffect(float duration, String path) {
        this(duration, path, 1f);
    }

    @Override
    public void tickEffect(int tick) {
        if (tick == 1) {
            CinematicTextureOverlay.rl = MachinaRL.create("textures/cinematic/" + path + ".png");
            CinematicTextureOverlay.render = true;
        }
        CinematicTextureOverlay.opacity = opacity;
    }
}