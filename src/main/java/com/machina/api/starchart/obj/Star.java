package com.machina.api.starchart.obj;

import java.util.Random;

/**
 * Magnitude (no unit)</br>
 * Temperature, Centre, K</br>
 * Radius, Equatorial, m</br>
 * Luminosity, 10^25 W</br>
 * 
 * @author Cy4Shot
 */

public record Star(String name, float magnitude, float temperature, float radius, float luminosity) {

	public static final Star SUN = new Star("Sol", 4.83F, 15700000, 695700000, 38.28F);

	public static Star gen(Random rand) {
		return new Star("", 0, 0, 0, 0);
	}
}
