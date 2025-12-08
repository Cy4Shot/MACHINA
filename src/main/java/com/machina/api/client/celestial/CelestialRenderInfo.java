package com.machina.api.client.celestial;

import com.machina.api.starchart.obj.Celestial;
import com.machina.api.starchart.obj.Orbit;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec3;

// TODO: Texture variation
// TODO: Particle to match texture
public record CelestialRenderInfo(int id, Celestial celestial, double radius, Orbit orbit, int width, int height) {
    
    public static CelestialRenderInfo from(int id, Celestial celestial, GuiGraphics graphics) {
        return new CelestialRenderInfo(id, celestial, celestial.radiusAU(), celestial.orbit(), graphics.guiWidth(), graphics.guiHeight());
    }

    public Vec3 getOrbitalCoords(double t) {
        return orbit.calculateOrbitalCoords(t);
    }
}