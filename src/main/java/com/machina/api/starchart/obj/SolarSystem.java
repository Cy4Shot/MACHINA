package com.machina.api.starchart.obj;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.machina.api.util.StringUtils;

import net.minecraftforge.fluids.FluidStack;

public record SolarSystem(String name, List<Star> stars, List<Planet> planets) {

	public static final SolarSystem SOLAR_SYSTEM = new SolarSystem("Sol", List.of(Star.SUN), List.of(Planet.MERCURY,
			Planet.VENUS, Planet.EARTH, Planet.MARS, Planet.JUPITER, Planet.SATURN, Planet.URANUS, Planet.NEPTUNE));

	public static SolarSystem gen(Random rand) {
		return new SolarSystem(generateName(), List.of(), List.of());
	}

	public SolarSystem(String name, List<Star> stars, List<Planet> planets) {
		this.name = name;
		this.stars = stars;
		this.planets = planets;
	}

	public static String generateName() {
		return "Example";
	}

	public void debug() {
		StringUtils.printlnUtf8(name());
		StringUtils.printlnUtf8(StringUtils.TREE_F + StringUtils.TREE_H + " Stars:");
		for (int i = 0; i < stars().size(); i++) {
			boolean last = i == stars().size() - 1;
			Star star = stars().get(i);
			String o = StringUtils.TREE_V + "    " + (last ? " " : StringUtils.TREE_V);
			StringUtils.printlnUtf8(StringUtils.TREE_V + "  " + (last ? StringUtils.TREE_L : StringUtils.TREE_F)
					+ StringUtils.TREE_H + " " + star.name());
			StringUtils.printlnUtf8(o + StringUtils.TREE_F + StringUtils.TREE_H + " Magnitude: " + star.magnitude());
			StringUtils
					.printlnUtf8(o + StringUtils.TREE_F + StringUtils.TREE_H + " Temperature: " + star.temperature());
			StringUtils.printlnUtf8(o + StringUtils.TREE_F + StringUtils.TREE_H + " Radius: " + star.radius());
			StringUtils.printlnUtf8(o + StringUtils.TREE_L + StringUtils.TREE_H + " Luminosity: " + star.luminosity());
		}
		StringUtils.printlnUtf8(StringUtils.TREE_L + StringUtils.TREE_H + " Planets:");
		for (int i = 0; i < planets().size(); i++) {
			boolean last = i == planets().size() - 1;
			Planet planet = planets().get(i);
			String o = "   " + (last ? " " : StringUtils.TREE_V) + "  ";
			StringUtils.printlnUtf8("   " + (last ? StringUtils.TREE_L : StringUtils.TREE_F) + StringUtils.TREE_H + " "
					+ planet.name());
			StringUtils.printlnUtf8(o + StringUtils.TREE_F + StringUtils.TREE_H + " Mass: " + planet.mass());
			StringUtils.printlnUtf8(o + StringUtils.TREE_F + StringUtils.TREE_H + " Radius: " + planet.radius());
			StringUtils.printlnUtf8(o + StringUtils.TREE_F + StringUtils.TREE_H + " Orbital: " + planet.orbital());
			StringUtils.printlnUtf8(
					o + StringUtils.TREE_F + StringUtils.TREE_H + " Orbital Speed: " + planet.orbitalSpeed());
			StringUtils.printlnUtf8(
					o + StringUtils.TREE_F + StringUtils.TREE_H + " Eccentricity: " + planet.eccentricity());
			StringUtils.printlnUtf8(o + StringUtils.TREE_F + StringUtils.TREE_H + " Gravity: " + planet.gravity());
			StringUtils
					.printlnUtf8(o + StringUtils.TREE_F + StringUtils.TREE_H + " Temperature: " + planet.temperature());
			StringUtils.printlnUtf8(o + StringUtils.TREE_F + StringUtils.TREE_H + " Atmospheric Pressure: "
					+ planet.atmosphericPressure());
			StringUtils.printlnUtf8(o + StringUtils.TREE_F + StringUtils.TREE_H + " Atmosphere:");
			int max = 0;
			for (int j = 0; j < planet.atmosphericComposition().size(); j++) {
				int s = planet.atmosphericComposition().get(j).getKey().getDisplayName().getString().length();
				if (s > max)
					max = s;
			}
			int spaces = (int) Math.ceil((double) (max) / 5) * 5 + 1;
			for (int j = 0; j < planet.atmosphericComposition().size(); j++) {
				boolean last2 = j == planet.atmosphericComposition().size() - 1;
				String o2 = o + StringUtils.TREE_V + "  " + (last2 ? StringUtils.TREE_L : StringUtils.TREE_F)
						+ StringUtils.TREE_H + " ";
				Map.Entry<FluidStack, Float> entry = planet.atmosphericComposition().get(j);
				String name = entry.getKey().getDisplayName().getString();
				StringUtils.printlnUtf8(
						o2 + name + " ".repeat(spaces - name.length()) + String.valueOf(entry.getValue() * 100) + "%");
			}
			StringUtils.printlnUtf8(o + StringUtils.TREE_L + StringUtils.TREE_H + " Moons:");
			for (int j = 0; j < planet.moons().size(); j++) {
				boolean last2 = j == planet.moons().size() - 1;
				Moon moon = planet.moons().get(j);
				String o2 = o + "   " + (last2 ? " " : StringUtils.TREE_V) + "  ";
				StringUtils.printlnUtf8(o + "   " + (last2 ? StringUtils.TREE_L : StringUtils.TREE_F)
						+ StringUtils.TREE_H + " " + moon.name());
				StringUtils.printlnUtf8(o2 + StringUtils.TREE_F + StringUtils.TREE_H + " Mass: " + moon.mass());
				StringUtils.printlnUtf8(o2 + StringUtils.TREE_F + StringUtils.TREE_H + " Radius: " + moon.radius());
				StringUtils.printlnUtf8(o2 + StringUtils.TREE_F + StringUtils.TREE_H + " Orbital: " + moon.orbital());
				StringUtils.printlnUtf8(
						o2 + StringUtils.TREE_F + StringUtils.TREE_H + " Orbital Speed: " + moon.orbitalSpeed());
				StringUtils.printlnUtf8(
						o2 + StringUtils.TREE_F + StringUtils.TREE_H + " Eccentricity: " + moon.eccentricity());
				StringUtils.printlnUtf8(o2 + StringUtils.TREE_F + StringUtils.TREE_H + " Gravity: " + moon.gravity());
				StringUtils.printlnUtf8(
						o2 + StringUtils.TREE_F + StringUtils.TREE_H + " Temperature: " + moon.temperature());
				if (moon.atmosphericComposition().size() != 0) {
					StringUtils.printlnUtf8(o2 + StringUtils.TREE_F + StringUtils.TREE_H + " Atmospheric Pressure: "
							+ moon.atmosphericPressure());
					StringUtils.printlnUtf8(o2 + StringUtils.TREE_L + StringUtils.TREE_H + " Atmospherere:");
					int max2 = 0;
					for (int k = 0; k < moon.atmosphericComposition().size(); k++) {
						int s = moon.atmosphericComposition().get(k).getKey().getDisplayName().getString().length();
						if (s > max2)
							max2 = s;
					}
					int spaces2 = (int) Math.ceil((double) (max2) / 5) * 5 + 1;
					for (int k = 0; k < moon.atmosphericComposition().size(); k++) {
						boolean last3 = k == moon.atmosphericComposition().size() - 1;
						Map.Entry<FluidStack, Float> entry = moon.atmosphericComposition().get(k);
						String name = entry.getKey().getDisplayName().getString();
						StringUtils.printlnUtf8(o2 + "   " + (last3 ? StringUtils.TREE_L : StringUtils.TREE_F)
								+ StringUtils.TREE_H + " " + name + " ".repeat(spaces2 - name.length())
								+ String.valueOf(entry.getValue() * 100) + "%");
					}
				} else {
					StringUtils.printlnUtf8(o2 + StringUtils.TREE_L + StringUtils.TREE_H + " Atmospheric Pressure: "
							+ moon.atmosphericPressure());
				}
			}
		}
	}
}