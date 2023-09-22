package com.machina.api.util.math;

import java.util.Random;

public class BetterRandom extends Random {
	private static final long serialVersionUID = -3493409504014707317L;

	public int nextExpRange(int min, int max, double lambda) {
		double randomValue = -Math.log(1 - nextDouble()) / lambda;
		int randomInteger = (int) Math.floor(randomValue) + min;
		if (randomInteger < min) {
			randomInteger = min;
		} else if (randomInteger > max) {
			randomInteger = max;
		}
		return randomInteger;
	}

	public int nextRange(int min, int max) {
		return nextInt(max - min + 1) + min;
	}
	
	public float nextRange(float min, float max) {
		return nextFloat(max - min + 1) + min;
	}
}
