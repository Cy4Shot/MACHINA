package com.machina.api.starchart.obj;

import java.util.function.Function;

import net.minecraft.world.phys.Vec3;

public record Orbit(Function<Double, Vec3> orbitalCoords, double a, double e) {

	public static final Orbit STAR = new Orbit(t -> Vec3.ZERO, 0, 0);

	public static Orbit from(Planet p) {
		return new Orbit(t -> p.calculateOrbitalCoordinates(t), p.a(), p.e());
	}

	public Vec3 calculateOrbitalCoords(double t) {
		return orbitalCoords.apply(t);
	}
}