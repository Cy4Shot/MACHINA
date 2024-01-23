package com.machina.api.client.planet;

import com.machina.api.starchart.StarchartConst;
import com.machina.api.starchart.obj.Orbit;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.Star;

import net.minecraft.world.phys.Vec3;

// TODO: Texture variation
public record CelestialRenderInfo(String bg, String fg, double radius, Orbit orbit) {

	public static CelestialRenderInfo from(Star star) {
		return new CelestialRenderInfo("star_bg", "star_fg", star.radius() * StarchartConst.STELLAR_RADIUS_TO_M,
				Orbit.STAR);
	}

	public static CelestialRenderInfo from(Planet planet) {
		return new CelestialRenderInfo("earth", "clouds", planet.radius() * StarchartConst.KM_TO_M, Orbit.from(planet));
	}

	public Vec3 getOrbitalCoords(double t) {
		return orbit.calculateOrbitalCoords(t);
	}
}