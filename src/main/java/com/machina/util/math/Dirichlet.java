package com.machina.util.math;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

// Modified from: https://github.com/mimno/Mallet
public class Dirichlet {

	Alphabet dict;
	double magnitude = 1;
	double[] partition;

	Randoms random = null;

	public static final double EULER_MASCHERONI = -0.5772156649015328606065121;
	public static final double PI_SQUARED_OVER_SIX = Math.PI * Math.PI / 6;
	public static final double HALF_LOG_TWO_PI = Math.log(2 * Math.PI) / 2;

	public static final double DIGAMMA_COEF_1 = 1 / 12;
	public static final double DIGAMMA_COEF_2 = 1 / 120;
	public static final double DIGAMMA_COEF_3 = 1 / 252;
	public static final double DIGAMMA_COEF_4 = 1 / 240;
	public static final double DIGAMMA_COEF_5 = 1 / 132;
	public static final double DIGAMMA_COEF_6 = 691 / 32760;
	public static final double DIGAMMA_COEF_7 = 1 / 12;
	public static final double DIGAMMA_COEF_8 = 3617 / 8160;
	public static final double DIGAMMA_COEF_9 = 43867 / 14364;
	public static final double DIGAMMA_COEF_10 = 174611 / 6600;

	public static final double DIGAMMA_LARGE = 9.5;
	public static final double DIGAMMA_SMALL = .000001;

	public Dirichlet(double m, double[] p) {
		magnitude = m;
		partition = p;
	}

	public Dirichlet(double[] p) {
		magnitude = 0;
		partition = new double[p.length];

		for (int i = 0; i < p.length; i++) {
			magnitude += p[i];
		}

		for (int i = 0; i < p.length; i++) {
			partition[i] = p[i] / magnitude;
		}
	}

	public Dirichlet(double[] alphas, Alphabet dict) {
		this(alphas);
		if (dict != null && alphas.length != dict.size())
			throw new IllegalArgumentException("alphas and dict sizes do not match.");
		this.dict = dict;
		if (dict != null)
			dict.stopGrowth();
	}

	public Dirichlet(Alphabet dict) {
		this(dict, 1.0);
	}

	public Dirichlet(Alphabet dict, double alpha) {
		this(dict.size(), alpha);
		this.dict = dict;
		dict.stopGrowth();
	}

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

	private void initRandom() {
		if (random == null) {
			random = new Randoms();
		}
	}

	public double[] nextDistribution(long seed) {
		double distribution[] = new double[partition.length];
		initRandom();
		this.random.setSeed(seed);

		double sum = 0;
		for (int i = 0; i < distribution.length; i++) {
			distribution[i] = random.nextGamma(partition[i] * magnitude, 1);
			if (distribution[i] <= 0) {
				distribution[i] = 0.0001;
			}
			sum += distribution[i];
		}

		for (int i = 0; i < distribution.length; i++) {
			distribution[i] /= sum;
		}

		return distribution;
	}

	public static String distributionToString(double magnitude, double[] distribution) {
		StringBuffer output = new StringBuffer();
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(5);

		output.append(formatter.format(magnitude) + ":\t");

		for (int i = 0; i < distribution.length; i++) {
			output.append(formatter.format(distribution[i]) + "\t");
		}

		return output.toString();
	}

	public void toFile(String filename) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
		for (int i = 0; i < partition.length; i++) {
			out.println(magnitude * partition[i]);
		}
		out.flush();
		out.close();
	}

	public int[] drawObservation(int n) {
		initRandom();

		double[] distribution = nextDistribution(0);

		return drawObservation(n, distribution);
	}

	public int[] drawObservation(int n, double[] distribution) {
		initRandom();

		int[] histogram = new int[partition.length];

		Arrays.fill(histogram, 0);

		int count;

		if (n < 100) {
			count = random.nextPoisson();
		} else {

			count = (int) Math.round(random.nextGaussian(n, n));
		}

		for (int i = 0; i < count; i++) {
			histogram[random.nextDiscrete(distribution)]++;
		}

		return histogram;
	}

	public Object[] drawObservations(int d, int n) {
		Object[] observations = new Object[d];
		for (int i = 0; i < d; i++) {
			observations[i] = drawObservation(n);
		}
		return observations;
	}

	public static double logGammaDefinition(double z) {
		double result = EULER_MASCHERONI * z - Math.log(z);
		for (int k = 1; k < 10000000; k++) {
			result += (z / k) - Math.log(1 + (z / k));
		}
		return result;
	}

	public static double logGammaDifference(double z, int n) {
		double result = 0.0;
		for (int i = 0; i < n; i++) {
			result += Math.log(z + i);
		}
		return result;
	}

	public static double logGamma(double z) {
		return logGammaStirling(z);
	}

	public static double logGammaStirling(double z) {
		int shift = 0;
		while (z < 2) {
			z++;
			shift++;
		}

		double result = HALF_LOG_TWO_PI + (z - 0.5) * Math.log(z) - z + 1 / (12 * z) - 1 / (360 * z * z * z)
				+ 1 / (1260 * z * z * z * z * z);

		while (shift > 0) {
			shift--;
			z--;
			result -= Math.log(z);
		}

		return result;
	}

	public static double logGammaNemes(double z) {
		double result = HALF_LOG_TWO_PI - (Math.log(z) / 2) + z * (Math.log(z + (1 / (12 * z - (1 / (10 * z))))) - 1);
		return result;
	}

	public static double digamma(double z) {

		double psi = 0;

		if (z < DIGAMMA_SMALL) {
			psi = EULER_MASCHERONI - (1 / z);

			return psi;
		}

		while (z < DIGAMMA_LARGE) {
			psi -= 1 / z;
			z++;
		}

		double invZ = 1 / z;
		double invZSquared = invZ * invZ;

		psi += Math.log(z) - .5 * invZ
				- invZSquared * (DIGAMMA_COEF_1 - invZSquared
						* (DIGAMMA_COEF_2 - invZSquared * (DIGAMMA_COEF_3 - invZSquared * (DIGAMMA_COEF_4 - invZSquared
								* (DIGAMMA_COEF_5 - invZSquared * (DIGAMMA_COEF_6 - invZSquared * DIGAMMA_COEF_7))))));

		return psi;
	}

	public static double digammaDifference(double x, int n) {
		double sum = 0;
		for (int i = 0; i < n; i++) {
			sum += 1 / (x + i);
		}
		return sum;
	}

	public static double trigamma(double z) {
		int shift = 0;
		while (z < 2) {
			z++;
			shift++;
		}

		double oneOverZ = 1.0 / z;
		double oneOverZSquared = oneOverZ * oneOverZ;

		double result = oneOverZ + 0.5 * oneOverZSquared + 0.1666667 * oneOverZSquared * oneOverZ
				- 0.03333333 * oneOverZSquared * oneOverZSquared * oneOverZ
				+ 0.02380952 * oneOverZSquared * oneOverZSquared * oneOverZSquared * oneOverZ
				- 0.03333333 * oneOverZSquared * oneOverZSquared * oneOverZSquared * oneOverZSquared * oneOverZ;

		while (shift > 0) {
			shift--;
			z--;
			result += 1.0 / (z * z);
		}

		return result;
	}

	public static double learnSymmetricConcentration(int[] countHistogram, int[] observationLengths, int numDimensions,
			double currentValue) {
		double currentDigamma;

		int largestNonZeroCount = 0;
		int[] nonZeroLengthIndex = new int[observationLengths.length];

		for (int index = 0; index < countHistogram.length; index++) {
			if (countHistogram[index] > 0) {
				largestNonZeroCount = index;
			}
		}

		int denseIndex = 0;
		for (int index = 0; index < observationLengths.length; index++) {
			if (observationLengths[index] > 0) {
				nonZeroLengthIndex[denseIndex] = index;
				denseIndex++;
			}
		}

		int denseIndexSize = denseIndex;

		for (int iteration = 1; iteration <= 200; iteration++) {

			double currentParameter = currentValue / numDimensions;

			currentDigamma = 0;
			double numerator = 0;

			for (int index = 1; index <= largestNonZeroCount; index++) {
				currentDigamma += 1.0 / (currentParameter + index - 1);
				numerator += countHistogram[index] * currentDigamma;
			}

			currentDigamma = 0;
			double denominator = 0;
			int previousLength = 0;

			double cachedDigamma = digamma(currentValue);

			for (denseIndex = 0; denseIndex < denseIndexSize; denseIndex++) {
				int length = nonZeroLengthIndex[denseIndex];

				if (length - previousLength > 20) {

					currentDigamma = digamma(currentValue + length) - cachedDigamma;
				} else {

					for (int index = previousLength; index < length; index++) {
						currentDigamma += 1.0 / (currentValue + index);
					}
				}

				denominator += currentDigamma * observationLengths[length];
			}

			currentValue = currentParameter * numerator / denominator;

		}

		return currentValue;
	}

	public static void testSymmetricConcentration(int numDimensions, int numObservations, int observationMeanLength) {

		for (int exponent = -5; exponent < 4; exponent++) {
			double alpha = numDimensions * 1.0;

			Dirichlet prior = new Dirichlet(numDimensions, alpha / numDimensions);

			int[] countHistogram = new int[1000000];
			int[] observationLengths = new int[1000000];

			Object[] observations = prior.drawObservations(numObservations, observationMeanLength);

			Dirichlet optimizedDirichlet = new Dirichlet(numDimensions, 1.0);
			optimizedDirichlet.learnParametersWithHistogram(observations);

			System.out.println(optimizedDirichlet.magnitude);

			for (int i = 0; i < numObservations; i++) {
				int[] observation = (int[]) observations[i];

				int total = 0;
				for (int k = 0; k < numDimensions; k++) {
					if (observation[k] > 0) {
						total += observation[k];
						countHistogram[observation[k]]++;
					}
				}

				observationLengths[total]++;
			}

			double estimatedAlpha = learnSymmetricConcentration(countHistogram, observationLengths, numDimensions, 1.0);

			System.out.println(alpha + "\t" + estimatedAlpha + "\t" + Math.abs(alpha - estimatedAlpha));
		}

	}

	public static double learnParameters(double[] parameters, int[][] observations, int[] observationLengths) {

		return learnParameters(parameters, observations, observationLengths, 1.00001, 1.0, 200);
	}

	public static double learnParameters(double[] parameters, int[][] observations, int[] observationLengths,
			double shape, double scale, int numIterations) {
		int i, k;

		double parametersSum = 0;

		for (k = 0; k < parameters.length; k++) {
			parametersSum += parameters[k];
		}

		double oldParametersK;
		double currentDigamma;
		double denominator;

		int nonZeroLimit;
		int[] nonZeroLimits = new int[observations.length];
		Arrays.fill(nonZeroLimits, -1);

		int[] histogram;

		for (i = 0; i < observations.length; i++) {
			histogram = observations[i];

			for (k = 0; k < histogram.length; k++) {
				if (histogram[k] > 0) {
					nonZeroLimits[i] = k;

				}
			}

		}

		for (int iteration = 0; iteration < numIterations; iteration++) {

			denominator = 0;
			currentDigamma = 0;

			for (i = 1; i < observationLengths.length; i++) {
				currentDigamma += 1 / (parametersSum + i - 1);
				denominator += observationLengths[i] * currentDigamma;
			}

			denominator -= 1 / scale;

			parametersSum = 0;

			for (k = 0; k < parameters.length; k++) {

				nonZeroLimit = nonZeroLimits[k];

				oldParametersK = parameters[k];
				parameters[k] = 0;
				currentDigamma = 0;

				histogram = observations[k];

				for (i = 1; i <= nonZeroLimit; i++) {
					currentDigamma += 1 / (oldParametersK + i - 1);
					parameters[k] += histogram[i] * currentDigamma;
				}

				parameters[k] = oldParametersK * (parameters[k] + shape) / denominator;

				parametersSum += parameters[k];
			}
		}

		if (parametersSum < 0.0) {
			throw new RuntimeException("sum: " + parametersSum);
		}

		return parametersSum;
	}

	public long learnParametersWithHistogram(Object[] observations) {

		int maxLength = 0;
		int[] maxBinCounts = new int[partition.length];
		Arrays.fill(maxBinCounts, 0);

		for (int i = 0; i < observations.length; i++) {

			int length = 0;

			int[] observation = (int[]) observations[i];

			for (int bin = 0; bin < observation.length; bin++) {
				if (observation[bin] > maxBinCounts[bin]) {
					maxBinCounts[bin] = observation[bin];
				}
				length += observation[bin];
			}

			if (length > maxLength) {
				maxLength = length;
			}
		}

		int[][] binCountHistograms = new int[partition.length][];
		for (int bin = 0; bin < partition.length; bin++) {
			binCountHistograms[bin] = new int[maxBinCounts[bin] + 1];
			Arrays.fill(binCountHistograms[bin], 0);
		}

		int[] lengthHistogram = new int[maxLength + 1];
		Arrays.fill(lengthHistogram, 0);

		for (int i = 0; i < observations.length; i++) {
			int length = 0;
			int[] observation = (int[]) observations[i];
			for (int bin = 0; bin < observation.length; bin++) {
				binCountHistograms[bin][observation[bin]]++;
				length += observation[bin];
			}
			lengthHistogram[length]++;
		}

		return learnParametersWithHistogram(binCountHistograms, lengthHistogram);
	}

	public long learnParametersWithHistogram(int[][] binCountHistograms, int[] lengthHistogram) {

		long start = System.currentTimeMillis();

		double[] newParameters = new double[partition.length];

		double alphaK;
		double currentDigamma;
		double denominator;
		double parametersSum = 0.0;

		int i, k;

		for (k = 0; k < partition.length; k++) {
			newParameters[k] = magnitude * partition[k];
			parametersSum += newParameters[k];
		}

		for (int iteration = 0; iteration < 1000; iteration++) {

			denominator = 0;
			currentDigamma = 0;

			for (i = 1; i < lengthHistogram.length; i++) {
				currentDigamma += 1 / (parametersSum + i - 1);
				denominator += lengthHistogram[i] * currentDigamma;
			}

			assert (denominator > 0.0);
			assert (!Double.isNaN(denominator));

			parametersSum = 0.0;

			for (k = 0; k < partition.length; k++) {

				alphaK = newParameters[k];
				newParameters[k] = 0.0;
				currentDigamma = 0;

				int[] histogram = binCountHistograms[k];
				if (histogram.length <= 1) {
					newParameters[k] = 0.000001;
				} else {
					for (i = 1; i < histogram.length; i++) {
						currentDigamma += 1 / (alphaK + i - 1);
						newParameters[k] += histogram[i] * currentDigamma;
					}
				}

				if (!(newParameters[k] > 0.0)) {
					System.out.println("length of empty array: " + (new int[0]).length);

					for (i = 0; i < histogram.length; i++) {
						System.out.print(histogram[i] + " ");
					}
					System.out.println();
				}

				assert (newParameters[k] > 0.0);
				assert (!Double.isNaN(newParameters[k]));

				newParameters[k] *= alphaK / denominator;

				parametersSum += newParameters[k];
			}

		}

		for (k = 0; k < partition.length; k++) {
			partition[k] = newParameters[k] / parametersSum;
			magnitude = parametersSum;
		}

		return System.currentTimeMillis() - start;
	}

	public long learnParametersWithDigamma(Object[] observations) {

		int[][] binCounts = new int[partition.length][observations.length];

		int[] observationLengths = new int[observations.length];

		for (int i = 0; i < observations.length; i++) {
			int[] observation = (int[]) observations[i];
			for (int bin = 0; bin < partition.length; bin++) {
				binCounts[bin][i] = observation[bin];
				observationLengths[i] += observation[bin];
			}
		}

		return learnParametersWithDigamma(binCounts, observationLengths);
	}

	public long learnParametersWithDigamma(int[][] binCounts, int[] observationLengths) {

		long start = System.currentTimeMillis();

		double[] newParameters = new double[partition.length];

		double alphaK;
		double denominator;

		double newMagnitude;

		int i, k;

		for (int iteration = 0; iteration < 1000; iteration++) {
			newMagnitude = 0;

			denominator = 0;

			for (i = 0; i < observationLengths.length; i++) {
				denominator += digamma(magnitude + observationLengths[i]);
			}
			denominator -= observationLengths.length * digamma(magnitude);

			for (k = 0; k < partition.length; k++) {
				newParameters[k] = 0;

				int[] counts = binCounts[k];

				alphaK = magnitude * partition[k];

				double digammaAlphaK = digamma(alphaK);
				for (i = 0; i < counts.length; i++) {
					if (counts[i] == 0) {
						newParameters[k] += digammaAlphaK;
					} else {
						newParameters[k] += digamma(alphaK + counts[i]);
					}
				}
				newParameters[k] -= counts.length * digammaAlphaK;

				if (newParameters[k] <= 0) {
					newParameters[k] = 0.000001;
				} else {
					newParameters[k] *= alphaK / denominator;
				}

				if (newParameters[k] <= 0) {
					System.out.println(newParameters[k] + "\t" + alphaK + "\t" + denominator);
				}

				assert (newParameters[k] > 0);
				assert (!Double.isNaN(newParameters[k]));

				newMagnitude += newParameters[k];

			}

			magnitude = newMagnitude;
			for (k = 0; k < partition.length; k++) {
				partition[k] = newParameters[k] / magnitude;

			}

		}

		return System.currentTimeMillis() - start;
	}

	public long learnParametersWithMoments(Object[] observations) {
		long start = System.currentTimeMillis();

		int i, bin;

		int[] observationLengths = new int[observations.length];
		double[] variances = new double[partition.length];

		Arrays.fill(partition, 0.0);
		Arrays.fill(observationLengths, 0);
		Arrays.fill(variances, 0.0);

		for (i = 0; i < observations.length; i++) {
			int[] observation = (int[]) observations[i];

			for (bin = 0; bin < partition.length; bin++) {
				observationLengths[i] += observation[bin];
			}

			for (bin = 0; bin < partition.length; bin++) {
				partition[bin] += (double) observation[bin] / observationLengths[i];
			}
		}

		for (bin = 0; bin < partition.length; bin++) {
			partition[bin] /= observations.length;
		}

		double difference;
		for (i = 0; i < observations.length; i++) {
			int[] observation = (int[]) observations[i];

			for (bin = 0; bin < partition.length; bin++) {
				difference = ((double) observation[bin] / observationLengths[i]) - partition[bin];
				variances[bin] += difference * difference;
			}
		}

		for (bin = 0; bin < partition.length; bin++) {
			variances[bin] /= observations.length - 1;
		}

		double sum = 0.0;

		for (bin = 0; bin < partition.length; bin++) {
			if (partition[bin] == 0) {
				continue;
			}
			sum += Math.log((partition[bin] * (1 - partition[bin]) / variances[bin]) - 1);
		}

		magnitude = Math.exp(sum / (partition.length - 1));

		return System.currentTimeMillis() - start;
	}

	public long learnParametersWithLeaveOneOut(Object[] observations) {

		int[][] binCounts = new int[partition.length][observations.length];

		int[] observationLengths = new int[observations.length];

		for (int i = 0; i < observations.length; i++) {
			int[] observation = (int[]) observations[i];
			for (int bin = 0; bin < partition.length; bin++) {
				binCounts[bin][i] = observation[bin];
				observationLengths[i] += observation[bin];
			}
		}

		return learnParametersWithLeaveOneOut(binCounts, observationLengths);
	}

	public long learnParametersWithLeaveOneOut(int[][] binCounts, int[] observationLengths) {
		long start = System.currentTimeMillis();

		int i, bin;

		double[] newParameters = new double[partition.length];
		double[] binSums = new double[partition.length];
		double observationSum = 0.0;
		double parameterSum = 0.0;
		int[] counts;

		for (int iteration = 0; iteration < 1000; iteration++) {

			observationSum = 0.0;

			Arrays.fill(binSums, 0.0);

			for (i = 0; i < observationLengths.length; i++) {
				observationSum += (observationLengths[i] / (observationLengths[i] - 1 + magnitude));
			}

			for (bin = 0; bin < partition.length; bin++) {
				counts = binCounts[bin];
				for (i = 0; i < counts.length; i++) {
					if (counts[i] >= 2) {
						binSums[bin] += (counts[i] / (counts[i] - 1 + (magnitude * partition[bin])));
					}
				}
			}

			parameterSum = 0.0;
			for (bin = 0; bin < partition.length; bin++) {
				if (binSums[bin] == 0.0) {
					newParameters[bin] = 0.000001;
				} else {
					newParameters[bin] = (partition[bin] * magnitude * binSums[bin] / observationSum);
				}
				parameterSum += newParameters[bin];
			}

			for (bin = 0; bin < partition.length; bin++) {
				partition[bin] = newParameters[bin] / parameterSum;
			}
			magnitude = parameterSum;

		}

		return System.currentTimeMillis() - start;
	}

	public double absoluteDifference(Dirichlet other) {
		if (partition.length != other.partition.length) {
			throw new IllegalArgumentException("dirichlets must have the same dimension to be compared");
		}

		double residual = 0.0;

		for (int k = 0; k < partition.length; k++) {
			residual += Math.abs((partition[k] * magnitude) - (other.partition[k] * other.magnitude));
		}

		return residual;
	}

	public double squaredDifference(Dirichlet other) {
		if (partition.length != other.partition.length) {
			throw new IllegalArgumentException("dirichlets must have the same dimension to be compared");
		}

		double residual = 0.0;

		for (int k = 0; k < partition.length; k++) {
			residual += Math.pow((partition[k] * magnitude) - (other.partition[k] * other.magnitude), 2);
		}

		return residual;
	}

	public void checkBreakeven(double x) {
		long start, clock1, clock2;

		double digammaX = digamma(x);

		for (int n = 1; n < 100; n++) {
			start = System.currentTimeMillis();
			for (int i = 0; i < 1000000; i++) {
				digamma(x + n);
			}
			clock1 = System.currentTimeMillis() - start;

			start = System.currentTimeMillis();
			for (int i = 0; i < 1000000; i++) {
				digammaDifference(x, n);
			}
			clock2 = System.currentTimeMillis() - start;

			System.out.println(n + "\tdirect: " + clock1 + "\tindirect: " + clock2 + " (" + (clock1 - clock2) + ")");
			System.out.println("  " + (digamma(x + n) - digammaX) + " " + digammaDifference(x, n));

		}

	}

	public static String compare(double sum, int k, int n, int w) {

		Dirichlet uniformDirichlet, dirichlet;

		StringBuffer output = new StringBuffer();
		output.append(sum + "\t" + k + "\t" + n + "\t" + w + "\t");

		uniformDirichlet = new Dirichlet(k, sum / k);

		dirichlet = new Dirichlet(sum, uniformDirichlet.nextDistribution(0));

		Object[] observations = dirichlet.drawObservations(n, w);

		long time;

		Dirichlet estimatedDirichlet = new Dirichlet(k, sum / k);

		time = estimatedDirichlet.learnParametersWithDigamma(observations);
		output.append(time + "\t" + dirichlet.absoluteDifference(estimatedDirichlet) + "\t");

		estimatedDirichlet = new Dirichlet(k, sum / k);

		time = estimatedDirichlet.learnParametersWithHistogram(observations);
		output.append(time + "\t" + dirichlet.absoluteDifference(estimatedDirichlet) + "\t");

		estimatedDirichlet = new Dirichlet(k, sum / k);

		time = estimatedDirichlet.learnParametersWithMoments(observations);
		output.append(time + "\t" + dirichlet.absoluteDifference(estimatedDirichlet) + "\t");

		estimatedDirichlet = new Dirichlet(k, sum / k);

		time = estimatedDirichlet.learnParametersWithLeaveOneOut(observations);
		output.append(time + "\t" + dirichlet.absoluteDifference(estimatedDirichlet) + "\t");

		return output.toString();
	}

	public static double dirichletMultinomialLikelihoodRatio(HashMap<Integer, Integer> countsX,
			HashMap<Integer, Integer> countsY, double alpha, double alphaSum) {

		double logLikelihood = 0.0;

		int totalX = 0;
		int totalY = 0;

		int key, x, y;

		HashSet<Integer> distinctKeys = new HashSet<Integer>();
		distinctKeys.addAll(countsX.keySet());
		distinctKeys.addAll(countsY.keySet());

		Iterator<Integer> iterator = distinctKeys.iterator();
		while (iterator.hasNext()) {
			key = iterator.next();

			x = 0;
			if (countsX.containsKey(key)) {
				x = countsX.get(key);
			}

			y = 0;
			if (countsY.containsKey(key)) {
				y = countsY.get(key);
			}

			totalX += x;
			totalY += y;

			logLikelihood += logGamma(alpha) + logGamma(alpha + x + y) - logGamma(alpha + x) - logGamma(alpha + y);
		}

		logLikelihood += logGamma(alphaSum + totalX) + logGamma(alphaSum + totalY) - logGamma(alphaSum)
				- logGamma(alphaSum + totalX + totalY);

		return logLikelihood;
	}

	public static double dirichletMultinomialLikelihoodRatio(int[] countsX, int[] countsY, double alpha,
			double alphaSum) {

		if (countsX.length != countsY.length) {
			throw new IllegalArgumentException("both arrays must contain the same number of dimensions");
		}

		double logLikelihood = 0.0;
		double logGammaAlpha = logGamma(alpha);

		int totalX = 0;
		int totalY = 0;

		int x, y;

		for (int key = 0; key < countsX.length; key++) {
			x = countsX[key];
			y = countsY[key];

			totalX += x;
			totalY += y;

			logLikelihood += logGammaAlpha + logGamma(alpha + x + y) - logGamma(alpha + x) - logGamma(alpha + y);
		}

		logLikelihood += logGamma(alphaSum + totalX) + logGamma(alphaSum + totalY) - logGamma(alphaSum)
				- logGamma(alphaSum + totalX + totalY);

		return logLikelihood;
	}

	public double dirichletMultinomialLikelihoodRatio(int[] countsX, int[] countsY) {

		if (countsX.length != countsY.length || countsX.length != partition.length) {
			throw new IllegalArgumentException(
					"both arrays and the Dirichlet prior must contain the same number of dimensions");
		}

		double logLikelihood = 0.0;
		double alpha;

		int totalX = 0;
		int totalY = 0;

		int x, y;

		for (int key = 0; key < countsX.length; key++) {
			x = countsX[key];
			y = countsY[key];

			totalX += x;
			totalY += y;

			alpha = partition[key] * magnitude;
			logLikelihood += logGamma(alpha) + logGamma(alpha + x + y) - logGamma(alpha + x) - logGamma(alpha + y);
		}

		logLikelihood += logGamma(magnitude + totalX) + logGamma(magnitude + totalY) - logGamma(magnitude)
				- logGamma(magnitude + totalX + totalY);

		return logLikelihood;
	}

	public static double ewensLikelihoodRatio(int[] countsX, int[] countsY, double lambda) {

		if (countsX.length != countsY.length) {
			throw new IllegalArgumentException("both arrays must contain the same number of dimensions");
		}

		double logLikelihood = 0.0;

		int totalX = 0;
		int totalY = 0;
		int total = 0;

		int x, y;

		for (int key = 0; key < countsX.length; key++) {
			x = countsX[key];
			y = countsY[key];

			totalX += x;
			totalY += y;
			total += x + y;
		}

		int[] countHistogramX = new int[total + 1];
		int[] countHistogramY = new int[total + 1];
		int[] countHistogramBoth = new int[total + 1];

		for (int key = 0; key < countsX.length; key++) {
			x = countsX[key];
			y = countsY[key];

			countHistogramX[x]++;
			countHistogramX[y]++;
			countHistogramBoth[x + y]++;
		}

		for (int j = 1; j <= total; j++) {
			if (countHistogramX[j] == 0 && countHistogramY[j] == 0 && countHistogramBoth[j] == 0) {

				continue;
			}

			logLikelihood += (countHistogramBoth[j] - countHistogramX[j] - countHistogramY[j]) * Math.log(lambda / j);

			logLikelihood += logGamma(countHistogramX[j] + 1) + logGamma(countHistogramY[j] + 1)
					- logGamma(countHistogramBoth[j] + 1);

		}

		logLikelihood += logGamma(total + 1) - logGamma(totalX + 1) - logGamma(totalY + 1);

		logLikelihood += logGamma(lambda + totalX) + logGamma(lambda + totalY) - logGamma(lambda)
				- logGamma(lambda + totalX + totalY);

		return logLikelihood;
	}

	public static void runComparison() {
		int dimensions;
		int documents;
		int meanSize;

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("comparison")));

			dimensions = 10;
			for (int j = 0; j < 5; j++) {
				documents = 100;
				for (int k = 0; k < 5; k++) {
					meanSize = 100;
					for (int l = 0; l < 5; l++) {
						System.out.println(dimensions + "\t" + dimensions + "\t" + documents + "\t" + meanSize);

						for (int m = 0; m < 10; m++) {

							out.println(compare(dimensions, dimensions, documents, meanSize));
						}
						out.flush();
						meanSize *= 2;
					}
					documents *= 2;
				}
				dimensions *= 2;
			}

			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}

	public static void main(String[] args) {

		testSymmetricConcentration(1000, 100, 1000);

	}

	public Alphabet getAlphabet() {
		return dict;
	}

	public int size() {
		return partition.length;
	}

	public double alpha(int featureIndex) {
		return magnitude * partition[featureIndex];
	}

	public void print() {
		System.out.println("Dirichlet:");
		for (int j = 0; j < partition.length; j++)
			System.out.println(dict != null ? dict.lookupObject(j).toString() : j + "=" + magnitude * partition[j]);
	}

	protected double[] randomRawMultinomial(Randoms r) {
		double sum = 0;
		double[] pr = new double[this.partition.length];
		for (int i = 0; i < this.partition.length; i++) {

			pr[i] = r.nextGamma(magnitude * partition[i]);
			sum += pr[i];
		}
		for (int i = 0; i < this.partition.length; i++)
			pr[i] /= sum;
		return pr;
	}

	public Dirichlet randomDirichlet(Randoms r, double averageAlpha) {
		double[] pr = randomRawMultinomial(r);
		double alphaSum = pr.length * averageAlpha;

		for (int i = 0; i < pr.length; i++)
			pr[i] *= alphaSum;
		return new Dirichlet(pr, dict);
	}

	public double[] randomVector(Randoms r) {
		return randomRawMultinomial(r);
	}
}
