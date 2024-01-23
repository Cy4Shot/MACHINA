package com.machina.api.client.planet;

import com.machina.api.starchart.obj.Orbit;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.Star;

import net.minecraft.world.phys.Vec3;

public record CelestialRenderInfo(String bg, String fg, double radius, Orbit orbit) {

	public static CelestialRenderInfo star(Star star) {
		return new CelestialRenderInfo("star_bg", "star_fg", star.radius(), Orbit.STAR);
	}

	public static CelestialRenderInfo planet(Planet planet) {
		return new CelestialRenderInfo("earth", "clouds", planet.radius() / 100000D, Orbit.from(planet));
	}

	public Vec3 getOrbitalCoords(double t) {
		return orbit.calculateOrbitalCoords(t);
	}
}