package com.machina.world.gen;

import java.util.Random;

import com.machina.util.math.MathUtil;
import com.machina.util.math.OpenSimplex2F;
import com.machina.world.PlanetChunkGenerator;

import net.minecraft.util.math.vector.Vector3d;

public class PlanetTerrainGenerator {

	final long seed;
	OpenSimplex2F osf;
	PlanetChunkGenerator s;

	public PlanetTerrainGenerator(long seed, PlanetChunkGenerator settings) {
		this.seed = seed > Long.MAX_VALUE - 5 ? 0 : seed;
		this.osf = new OpenSimplex2F(seed);
		this.s = settings;
	}

	public double at(double x, double y, double z) {
		return noise(new Vector3d(x, y, z).scale(1 / s.surfscale), s.surfdetail, s.surfroughness, s.surfdistortion);
	}

	double noise(Vector3d co, double detail, double roughness, double distortion) {
		Vector3d p = new Vector3d(co.x, co.y, co.z);
		if (distortion != 0D) {
			p.add(safeSNoise(p.add(randomOffset(0))) * distortion, safeSNoise(p.add(randomOffset(1))) * distortion,
					safeSNoise(p.add(randomOffset(2))) * distortion);
		}
		return fractalNoise(p, detail, roughness);
	}

	Vector3d randomOffset(int s) {
		Random r = new Random(seed + s);
		return new Vector3d(100d + r.nextDouble() * 100d, 100d + r.nextDouble() * 100d, 100d + r.nextDouble() * 100d);
	}

	double safeSNoise(Vector3d p) {
		double f = osf.noise3_Classic(p.x, p.y, p.z);
		if (Double.isInfinite(f))
			return 0D;
		return f;
	}

	double fractalNoise(Vector3d p, double detail, double roughness) {
		double fscale = 1d;
		double amp = 1d;
		double mamp = 0d;
		double sum = 0d;
		double octaves = MathUtil.clamp(detail, 0d, 16d);
		int n = (int) octaves;
		for (int i = 0; i <= n; i++) {
			double t = safeSNoise(p.scale(fscale));
			sum += t * amp;
			mamp += amp;
			amp *= MathUtil.clamp(roughness, 0D, 1D);
			fscale *= 2;
		}
		double rmd = octaves - Math.floor(octaves);
		if (rmd != 0D) {
			double t = safeSNoise(p.scale(fscale));
			double sum2 = sum + t * amp;
			sum /= mamp;
			sum2 /= mamp + amp;
			return (1D - rmd) * sum + rmd * sum2;
		}
		return sum / mamp;
	}

}
