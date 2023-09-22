package com.machina.api.starchart.obj;

import com.machina.api.starchart.StarchartConsts;
import com.machina.api.util.math.BetterRandom;

/**
 * Mass, 10^30 kg.</br>
 * Magnitude (no unit)</br>
 * Temperature, Centre, K</br>
 * Temperature, Surface, K</br>
 * Radius, Equatorial, m</br>
 * Luminosity, 10^25 W</br>
 * Gravity, m/s^2</br>
 * 
 * @author Cy4Shot
 */

public record Star(String name, double mass, double magnitude, double temperature, double surftemp, double luminosity,
		double radius, double gravity) {

	public static final Star SUN = new Star("Sol", 1.989D, 4.83D, 15700, 5800, 38.28D, 696342000, 274);

	public Star(String name, double mass, double magnitude, double temperature, double surftemp) {
		this(name, mass, magnitude, temperature, surftemp, luminosity(mass), radius(luminosity(mass), surftemp),
				gravity(mass, radius(luminosity(mass), surftemp)));
	}

	public static Star gen(BetterRandom rand) {
		double mass = rand.nextRange(0.08f, 150f);
		double mag = rand.nextRange(-10f, 20f);
		double t = rand.nextRange(2500, 50000);
		double st = 5740D * Math.pow(mass * Math.pow(10, 30), 0.54);
		return new Star(generateName(), mass, mag, t, st);
	}

	public static String generateName() {
		return "Star";
	}

	public String toString() {
		return name() + "[" + String.valueOf(magnitude) + ", " + String.valueOf(temperature) + ", "
				+ String.valueOf(radius()) + ", " + String.valueOf(luminosity()) + ", " + String.valueOf(mass) + ", "
				+ String.valueOf(gravity()) + "]";
	}

	public static double luminosity(double mass) {
		double solarMass = StarchartConsts.SUN_MASS / Math.pow(10, 30);
		if (mass < 0.43 * solarMass) {
			return StarchartConsts.SUN_LUMINOSITY * 0.23 * Math.pow(mass / solarMass, 2.3);
		} else if (mass < 2 * solarMass) {
			return StarchartConsts.SUN_LUMINOSITY * Math.pow(mass / solarMass, 4);
		} else if (mass < 55 * solarMass) {
			return StarchartConsts.SUN_LUMINOSITY * 1.44 * Math.pow(mass / solarMass, 3.5);
		} else {
			return StarchartConsts.SUN_LUMINOSITY * 32000 * mass * solarMass;
		}
	}

	public static double radius(double luminosity, double temperature) {
		return Math.sqrt(luminosity * Math.pow(10, 25)
				/ (4D * Math.PI * StarchartConsts.STEFAN * temperature * temperature * temperature * temperature));
	}

	public static double gravity(double mass, double radius) {
		return StarchartConsts.GRAVITATIONAL * mass * Math.pow(10, 30) / (radius * radius);
	}
}