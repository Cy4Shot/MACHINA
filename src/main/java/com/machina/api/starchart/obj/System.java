package com.machina.api.starchart.obj;

import java.util.List;
import java.util.Random;

public record System(String name, List<Star> stars, List<Planet> planets) {

	public static final System SOLAR_SYSTEM = new System("Sol", List.of(Star.SUN), List.of(Planet.MERCURY, Planet.VENUS,
			Planet.EARTH, Planet.MARS, Planet.JUPITER, Planet.SATURN, Planet.URANUS, Planet.NEPTUNE));

	public static System gen(Random rand) {
		return new System(generateName(), List.of(), List.of());
	}

	public System(String name, List<Star> stars, List<Planet> planets) {
		this.name = name;
		this.stars = stars;
		this.planets = planets;
	}

	public static String generateName() {
		return "Example";
	}
}