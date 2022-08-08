package com.machina.util.math;

// Modified from: https://github.com/mimno/Mallet
public class Dirichlet {

	double magnitude = 1;
	double[] partition;
	Randoms random = null;

	public Dirichlet(int size) {
		this(size, 1.0);
	}

	public Dirichlet(int size, double alpha) {
		magnitude = size * alpha;

		partition = new double[size];

		partition[0] = 1.0 / size;
		for (int i = 1; i < size; i++) {
			partition[i] = partition[0];
		}
	}

	public double[] nextDistribution(int seed) {
		double distribution[] = new double[partition.length];
		if (random == null)
			random = new Randoms(seed);

		double sum = 0;
		for (int i = 0; i < distribution.length; i++) {
			distribution[i] = random.nextGamma(partition[i] * magnitude, 1);
			if (distribution[i] <= 0)
				distribution[i] = 0.0001;
			sum += distribution[i];
		}

		for (int i = 0; i < distribution.length; i++) {
			distribution[i] /= sum;
		}

		return distribution;
	}
}
