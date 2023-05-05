package com.machina.util.math;

import java.util.Random;

//Modified from: https://github.com/mimno/Mallet
@SuppressWarnings("serial")
public class Randoms extends Random {

	public static final double ONE_OVER_E = Math.exp(-1);

	public Randoms(int seed) {
		super(seed);
	}

	public Randoms() {
		super();
	}

	public synchronized double nextUniform() {
		long l = ((long) (next(26)) << 27) + next(27);
		return l / (double) (1L << 53);
	}

	public synchronized double nextGamma() {
		return nextGamma(1, 1, 0);
	}

	public synchronized double nextGamma(double alpha) {
		return nextGamma(alpha, 1, 0);
	}

	public synchronized double nextGamma(double alpha, double beta) {
		return nextGamma(alpha, beta, 0);
	}

	public synchronized double nextGamma(double alpha, double beta, double lambda) {
		double gamma = 0;
		if (alpha <= 0 || beta <= 0) {
			throw new IllegalArgumentException("alpha and beta must be strictly positive.");
		}
		if (alpha < 1) {
			double b, p;
			boolean flag = false;

			b = 1 + alpha * ONE_OVER_E;

			while (!flag) {
				p = b * nextUniform();
				if (p > 1) {
					gamma = -Math.log((b - p) / alpha);
					if (nextUniform() <= Math.pow(gamma, alpha - 1)) {
						flag = true;
					}
				} else {
					gamma = Math.pow(p, 1.0 / alpha);
					if (nextUniform() <= Math.exp(-gamma)) {
						flag = true;
					}
				}
			}
		} else if (alpha == 1) {

			gamma = -Math.log(nextUniform());

		} else {

			double b = alpha - 1;
			double c = 3 * alpha - 0.75;

			double u, v;
			double w, y, z;

			boolean accept = false;

			while (!accept) {
				u = nextUniform();
				v = nextUniform();

				w = u * (1 - u);
				y = Math.sqrt(c / w) * (u - 0.5);
				gamma = b + y;

				if (gamma >= 0.0) {
					z = 64 * w * w * w * v * v;

					accept = z <= 1.0 - ((2 * y * y) / gamma);

					if (!accept) {
						accept = (Math.log(z) <= 2 * (b * Math.log(gamma / b) - y));
					}
				}
			}

		}
		return beta * gamma + lambda;
	}
}
