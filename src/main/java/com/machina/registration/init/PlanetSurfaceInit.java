package com.machina.registration.init;

import java.util.List;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.starchart.planet_biome.PlanetSurface;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PlanetSurfaceInit {
	public static final DeferredRegister<PlanetSurface> SURFACES = DeferredRegister.create(RegistryInit.SURFACE,
			Machina.MOD_ID);

	private static final Supplier<PlanetSurface> patches(int scale) {
		return () -> new PlanetSurface() {
			@Override
			public PlanetSurfaceGetter create(List<BlockState> blocks) {
				final BlockState state1 = blocks.get(0);
				final BlockState state2 = blocks.get(1);
				return (x, y, z, noise) -> noise.getValue(x * scale, y * scale, z * scale) < 0 ? state1 : state2;
			}
		};
	}

	public static final Supplier<PlanetSurface> SIMPLE = SURFACES.register("simple", () -> new PlanetSurface() {
		@Override
		public PlanetSurfaceGetter create(List<BlockState> blocks) {
			final BlockState state = blocks.get(0);
			return (x, y, z, noise) -> state;
		}
	});

	private static final Supplier<PlanetSurface> hybridBands(int scale, double basePortion, double decay) {
		return () -> new PlanetSurface() {
			@Override
			public PlanetSurfaceGetter create(List<BlockState> blocks) {
				final int n = blocks.size();

				return (x, y, z, noise) -> {
					double raw = noise.getValue(x * scale, y * scale, z * scale);
					double value = (raw + 1.0) / 2.0;

					// --- PATCH REGION (blocks 0 & 1) ---
					if (value < basePortion || n <= 2) {
						BlockState a = blocks.get(0);
						BlockState b = n > 1 ? blocks.get(1) : a;
						return raw < 0 ? a : b;
					}

					// --- BAND REGION (blocks 2+) ---
					double remaining = 1.0 - basePortion;
					double adjusted = (value - basePortion) / remaining;
					int bandCount = n - 2;
					double weight = 1.0;
					double totalWeight = 0.0;
					for (int i = 0; i < bandCount; i++) {
						totalWeight += weight;
						weight *= decay;
					}

					double cumulative = 0.0;
					weight = 1.0;
					for (int i = 0; i < bandCount; i++) {
						cumulative += weight / totalWeight;
						if (adjusted < cumulative || i == bandCount - 1) {
							return blocks.get(i + 2);
						}
						weight *= decay;
					}

					return blocks.get(0);
				};
			}
		};
	}

	private static final Supplier<PlanetSurface> craters(int scale, double rimWidth, double scatterChance) {
		return () -> new PlanetSurface() {
			@Override
			public PlanetSurfaceGetter create(List<BlockState> blocks) {
				final int n = blocks.size();
				final BlockState base = blocks.get(0);
				final BlockState scatter = blocks.get(n - 1);
				final int ringCount = Math.max(0, n - 2);
				return (x, y, z, noise) -> {
					// --- RANDOM SCATTER (independent of craters) ---
					double rand = noise.getValue(x * 9999, y * 9999, z * 9999);
					if (rand > 1.0 - scatterChance) {
						return scatter;
					}

					// --- VORONOI CRATER GENERATION ---
					double px = x * scale;
					double pz = z * scale;
					int cellX = (int) Math.floor(px);
					int cellZ = (int) Math.floor(pz);
					double minDist = Double.MAX_VALUE;
					double centerX = 0;
					double centerZ = 0;
					for (int dx = -1; dx <= 1; dx++) {
						for (int dz = -1; dz <= 1; dz++) {
							int cx = cellX + dx;
							int cz = cellZ + dz;
							double ox = noise.getValue(cx, 0, cz);
							double oz = noise.getValue(cx, 1, cz);
							double fx = cx + (ox + 1.0) * 0.5;
							double fz = cz + (oz + 1.0) * 0.5;
							double dxp = fx - px;
							double dzp = fz - pz;
							double dist = dxp * dxp + dzp * dzp;
							if (dist < minDist) {
								minDist = dist;
								centerX = fx;
								centerZ = fz;
							}
						}
					}

					double dxp = centerX - px;
					double dzp = centerZ - pz;
					double dist = Math.sqrt(dxp * dxp + dzp * dzp);
					double radius = 0.67; // lol 67
					if (dist > radius) {
						return base;
					}

					// --- CONCENTRIC RINGS ---
					if (ringCount <= 0) {
						return base;
					}
					double t = Math.pow(dist / radius, 1.5);
					int index = (int) (t * ringCount);
					index = Math.min(index, ringCount - 1);
					return blocks.get(index + 1);
				};
			}
		};
	}

	//@formatter:off
	public static final Supplier<PlanetSurface> LARGE_PATCHES = SURFACES.register("large_patches", patches(1));
	public static final Supplier<PlanetSurface> MEDIUM_PATCHES = SURFACES.register("medium_patches", patches(4));
	public static final Supplier<PlanetSurface> SMALL_PATCHES = SURFACES.register("small_patches", patches(16));
	public static final Supplier<PlanetSurface> MEDIUM_EXP_DIST = SURFACES.register("medium_exp_dist", hybridBands(4, 0.467f, 0.6f));
	public static final Supplier<PlanetSurface> CRATERS_EXTRA = SURFACES.register("craters_extra", craters(2, 0.15, 0.05));
	//@formatter:on
}
