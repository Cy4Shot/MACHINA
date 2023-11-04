package com.machina.api.starchart.obj;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public record SolarSystem(long seed, String name, Star star, List<Planet> planets) {

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
	}

//	public static final SolarSystem SOLAR_SYSTEM = new SolarSystem(-1L, "Sol", Star.SUN, List.of(Planet.MERCURY,
//			Planet.VENUS, Planet.EARTH, Planet.MARS, Planet.JUPITER, Planet.SATURN, Planet.URANUS, Planet.NEPTUNE));
}