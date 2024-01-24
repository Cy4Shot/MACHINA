package com.machina.api.client.planet;

import java.awt.Color;
import java.util.function.BiConsumer;

import com.machina.api.starchart.StarchartConst;
import com.machina.api.starchart.obj.Orbit;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.Star;
import com.machina.client.particle.GUIParticles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;

// TODO: Texture variation
// TODO: Particle to match texture
public record CelestialRenderInfo(String bg, String fg, double radius, Orbit orbit, int width, int height,
		BiConsumer<Vec2, ScreenParticleHolder> particle) {

	public static CelestialRenderInfo from(Star star, GuiGraphics graphics) {
		return new CelestialRenderInfo("star_bg", "star_fg", star.radius() * StarchartConst.STELLAR_RADIUS_TO_AU,
				Orbit.STAR, graphics.guiWidth(), graphics.guiHeight(),
				(p, h) -> GUIParticles.planetGlow(p, h, Color.red));
	}

	public static CelestialRenderInfo from(Planet planet, GuiGraphics graphics) {
		return new CelestialRenderInfo("earth", "clouds", planet.radius() * StarchartConst.KM_TO_AU, Orbit.from(planet),
				graphics.guiWidth(), graphics.guiHeight(), (p, h) -> GUIParticles.planetGlow(p, h, Color.blue));
	}

	public Vec3 getOrbitalCoords(double t) {
		return orbit.calculateOrbitalCoords(t);
	}
}