package com.machina.api.client.planet;

import com.machina.api.starchart.StarchartConst;
import com.machina.api.starchart.obj.Orbit;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.Star;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec3;

// TODO: Texture variation
public record CelestialRenderInfo(String bg, String fg, double radius, Orbit orbit, int width, int height) {

	public static CelestialRenderInfo from(Star star, GuiGraphics graphics) {
		return new CelestialRenderInfo("star_bg", "star_fg", star.radius() * StarchartConst.STELLAR_RADIUS_TO_M,
				Orbit.STAR, graphics.guiWidth(), graphics.guiHeight());
	}

	public static CelestialRenderInfo from(Planet planet, GuiGraphics graphics) {
		return new CelestialRenderInfo("earth", "clouds", planet.radius() * StarchartConst.KM_TO_M * 10, Orbit.from(planet),
				graphics.guiWidth(), graphics.guiHeight());
	}

	public Vec3 getOrbitalCoords(double t) {
		return orbit.calculateOrbitalCoords(t);
	}
}