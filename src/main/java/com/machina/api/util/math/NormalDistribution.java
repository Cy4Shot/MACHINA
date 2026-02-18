package com.machina.api.util.math;

import java.util.Random;

public class NormalDistribution {
	private final double mean;
	private final double standardDeviation;
	private final Random random;

	public NormalDistribution(double mean, double standardDeviation) {
		this.mean = mean;
		this.standardDeviation = standardDeviation;
		this.random = new Random();
	}

	public double sample() {
		double u1 = random.nextDouble();
		double u2 = random.nextDouble();
		double z0 = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
		return z0 * standardDeviation + mean;
	}
}