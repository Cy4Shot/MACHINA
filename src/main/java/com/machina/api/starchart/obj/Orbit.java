package com.machina.api.starchart.obj;

import java.util.function.Function;

import net.minecraft.world.phys.Vec3;

public class Orbit {

	public static final Orbit STAR = new Orbit(t -> Vec3.ZERO);

	public static Orbit from(Planet p) {
		return new Orbit(t -> p.calculateOrbitalCoordinates(t));
	}

	private Function<Double, Vec3> orbitalCoords;

	public Orbit(Function<Double, Vec3> fun) {
		this.orbitalCoords = fun;
	}

	public Vec3 calculateOrbitalCoords(double t) {
		return orbitalCoords.apply(t);
	}
}