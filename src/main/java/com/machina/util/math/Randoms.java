package com.machina.util.math;

import java.util.BitSet;

//Modified from: https://github.com/mimno/Mallet
@SuppressWarnings("serial")
public class Randoms extends java.util.Random {

	public static final double ONE_OVER_E = Math.exp(-1);

	public Randoms(int seed) {
		super(seed);
	}

	public Randoms() {
		super();
	}

	public synchronized int nextPoisson(double lambda) {
		int v = -1;
		double l = Math.exp(-lambda), p;
		p = 1.0;
		while (p >= l) {
			p *= nextUniform();
			v++;
		}
		return v;
	}

	public synchronized int nextPoisson() {
		return nextPoisson(1);
	}

	public synchronized boolean nextBoolean() {
		return (next(32) & 1 << 15) != 0;
	}

	public synchronized boolean nextBoolean(double p) {
		double u = nextUniform();
		if (u < p)
			return true;
		return false;
	}

	public synchronized BitSet nextBitSet(int size, double p) {
		BitSet bs = new BitSet(size);
		for (int i = 0; i < size; i++)
			if (nextBoolean(p)) {
				bs.set(i);
			}
		return bs;
	}

	public synchronized double nextUniform() {
		long l = ((long) (next(26)) << 27) + next(27);
		return l / (double) (1L << 53);
	}

	public synchronized double nextUniform(double a, double b) {
		return a + (b - a) * nextUniform();
	}

	public synchronized int nextDiscrete(double[] a) {
		double b = 0, r = nextUniform();
		for (int i = 0; i < a.length; i++) {
			b += a[i];
			if (b > r) {
				return i;
			}
		}
		return a.length - 1;
	}

	public synchronized int nextDiscrete(double[] a, double sum) {
		double b = 0, r = nextUniform() * sum;
		for (int i = 0; i < a.length; i++) {
			b += a[i];
			if (b > r) {
				return i;
			}
		}
		return a.length - 1;
	}

	private double nextGaussian;
	private boolean haveNextGaussian = false;

	public synchronized double nextGaussian() {
		if (!haveNextGaussian) {
			double v1 = nextUniform(), v2 = nextUniform();
			double x1, x2;
			x1 = Math.sqrt(-2 * Math.log(v1)) * Math.cos(2 * Math.PI * v2);
			x2 = Math.sqrt(-2 * Math.log(v1)) * Math.sin(2 * Math.PI * v2);
			nextGaussian = x2;
			haveNextGaussian = true;
			return x1;
		} else {
			haveNextGaussian = false;
			return nextGaussian;
		}
	}

	public synchronized double nextGaussian(double m, double s2) {
		return nextGaussian() * Math.sqrt(s2) + m;
	}

	public synchronized double nextGamma() {
		return nextGamma(1, 1, 0);
	}

	public synchronized double nextGamma(double alpha) {
		return nextGamma(alpha, 1, 0);
	}

	public synchronized double oldNextGamma(int ia) {
		int j;
		double am, e, s, v1, v2, x, y;

		assert (ia >= 1);
		if (ia < 6) {
			x = 1.0;
			for (j = 1; j <= ia; j++)
				x *= nextUniform();
			x = -Math.log(x);
		} else {
			do {
				do {
					do {
						v1 = 2.0 * nextUniform() - 1.0;
						v2 = 2.0 * nextUniform() - 1.0;
					} while (v1 * v1 + v2 * v2 > 1.0);
					y = v2 / v1;
					am = ia - 1;
					s = Math.sqrt(2.0 * am + 1.0);
					x = s * y + am;
				} while (x <= 0.0);
				e = (1.0 + y * y) * Math.exp(am * Math.log(x / am) - s * y);
			} while (nextUniform() > e);
		}
		return x;
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

	public synchronized double nextExp() {
		return nextGamma(1, 1, 0);
	}

	public synchronized double nextExp(double beta) {
		return nextGamma(1, beta, 0);
	}

	public synchronized double nextExp(double beta, double lambda) {
		return nextGamma(1, beta, lambda);
	}

	public synchronized double nextChiSq() {
		return nextGamma(0.5, 2, 0);
	}

	public synchronized double nextChiSq(int df) {
		return nextGamma(0.5 * (double) df, 2, 0);
	}

	public synchronized double nextChiSq(int df, double lambda) {
		return nextGamma(0.5 * (double) df, 2, lambda);
	}

	public synchronized double nextBeta(double alpha, double beta) {
		if (alpha <= 0 || beta <= 0) {
			throw new IllegalArgumentException("alpha and beta must be strictly positive.");
		}
		if (alpha == 1 && beta == 1) {
			return nextUniform();
		} else if (alpha >= 1 && beta >= 1) {
			double A = alpha - 1, B = beta - 1, C = A + B, L = C * Math.log(C), mu = A / C, sigma = 0.5 / Math.sqrt(C);
			double y = nextGaussian(), x = sigma * y + mu;
			while (x < 0 || x > 1) {
				y = nextGaussian();
				x = sigma * y + mu;
			}
			double u = nextUniform();
			while (Math.log(u) >= A * Math.log(x / A) + B * Math.log((1 - x) / B) + L + 0.5 * y * y) {
				y = nextGaussian();
				x = sigma * y + mu;
				while (x < 0 || x > 1) {
					y = nextGaussian();
					x = sigma * y + mu;
				}
				u = nextUniform();
			}
			return x;
		} else {
			double v1 = Math.pow(nextUniform(), 1 / alpha), v2 = Math.pow(nextUniform(), 1 / beta);
			while (v1 + v2 > 1) {
				v1 = Math.pow(nextUniform(), 1 / alpha);
				v2 = Math.pow(nextUniform(), 1 / beta);
			}
			return v1 / (v1 + v2);
		}
	}

	@Deprecated

	public java.util.Random asJavaRandom() {
		return new java.util.Random() {
			protected int next(int bits) {
				return Randoms.this.next(bits);
			}
		};
	}

	public static void main(String[] args) {

		Randoms r = new Randoms();
		final int resolution = 60;
		int[] histogram1 = new int[resolution];
		int[] histogram2 = new int[resolution];
		int scale = 10;

		for (int i = 0; i < 10000; i++) {
			double x = 4;
			int index1 = ((int) (r.nextGamma(x) / scale * resolution)) % resolution;
			int index2 = ((int) (r.oldNextGamma((int) x) / scale * resolution)) % resolution;
			histogram1[index1]++;
			histogram2[index2]++;
		}

		for (int i = 0; i < resolution; i++) {
			for (int y = 0; y < histogram1[i] / scale; y++)
				System.out.print("*");
			System.out.print("\n");
			for (int y = 0; y < histogram2[i] / scale; y++)
				System.out.print("-");
			System.out.print("\n");
		}
	}

}
