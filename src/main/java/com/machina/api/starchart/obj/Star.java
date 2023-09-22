package com.machina.api.starchart.obj;

import com.machina.api.starchart.StarchartConsts;
import com.machina.api.util.math.BetterRandom;

/**
 * Mass, 10^30 kg. Magnitude (no unit)</br>
 * Temperature, Centre, K</br>
 * Radius, Equatorial, m</br>
 * Luminosity, 10^25 W</br>
 * 
 * @author Cy4Shot
 */

public record Star(String name, float mass, float magnitude, float temperature) {

	public static final Star SUN = new Star("Sol", 1.989F, 4.83F, 15700); // , 695700000, 38.28F

	// TODO: Remove, this is for testing.
	public static void main(String[] args) {
		System.out.println(SUN.toString());
	}

	public static Star gen(BetterRandom rand) {
		float mass = rand.nextRange(0.08f, 150f);
		float mag = rand.nextRange(-10f, 20f);
		float t = rand.nextRange(2500, 50000);
//		float l = (float) (Math.pow(10, 0.4 * (4.85 - mag)) * StarchartConsts.SUN_LUMINOSITY);
//		float r = (float) Math.sqrt(l / (4D * Math.PI * StarchartConsts.STEFAN * t * t * t * t) * Math.pow(10, 25));
		return new Star(generateName(), mass, mag, t);
	}

	public static String generateName() {
		return "Star";
	}

	public String toString() {
		return name() + "[" + String.valueOf(magnitude) + ", " + String.valueOf(temperature) + ", "
				+ String.valueOf(radius()) + ", " + String.valueOf(luminosity()) + ", " + String.valueOf(mass) + ", "
				+ String.valueOf(gravity()) + "]";
	}

	public float luminosity() {
		double solarMass = StarchartConsts.SUN_MASS / Math.pow(10, 30);
		if (mass < 0.43 * solarMass) {
			return (float) (StarchartConsts.SUN_LUMINOSITY * 0.23 * Math.pow(mass / solarMass, 2.3));
		} else if (mass < 2 * solarMass) {
			return (float) (StarchartConsts.SUN_LUMINOSITY * Math.pow(mass / solarMass, 4));
		} else if (mass < 55 * solarMass) {
			return (float) (StarchartConsts.SUN_LUMINOSITY * 1.44 * Math.pow(mass / solarMass, 3.5));
		} else {
			return (float) (StarchartConsts.SUN_LUMINOSITY * 32000 * mass * solarMass);
		}
	}

	public float radius() {
		return (float) Math.sqrt(luminosity() * Math.pow(10, 25)
				/ (4D * Math.PI * StarchartConsts.STEFAN * temperature * temperature * temperature * temperature));
	}

	public float gravity() {
		return (float) (StarchartConsts.GRAVITATIONAL * mass() * Math.pow(10, 30) / (radius() * radius()));
	}
}
