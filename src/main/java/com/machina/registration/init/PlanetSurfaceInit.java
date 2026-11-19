package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.starchart.planet_biome.PlanetSurface;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PlanetSurfaceInit {
	public static final DeferredRegister<PlanetSurface> SURFACES = DeferredRegister.create(RegistryInit.SURFACE,
			Machina.MOD_ID);

	private static final Supplier<PlanetSurface> patches(int scale) {
		return () -> (blocks) -> {
			int size = blocks.size();
			return (x, y, z, noise) -> {
				double value = noise.getValue(x * scale, y * scale, z * scale);
				double normalized = (value + 1.0) / 2.0;
				int index = (int) (normalized * size);
				if (index >= size)
					index = size - 1;
				if (index < 0)
					index = 0;
				return blocks.get(index);
			};
		};
	}

	private static final Supplier<PlanetSurface> hybridBands(int scale, double basePortion, double decay) {
		return () -> (blocks) -> {
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
		};
	}

	private static final Supplier<PlanetSurface> cityPaths(int cellSize, double pathChance, double pathWidth) {
		return () -> (blocks) -> {
			final int n = blocks.size();
			final BlockState path = blocks.get(0);
			final BlockState pathAlt = n > 1 ? blocks.get(1) : path;
			return (x, y, z, noise) -> {
				// Cell-based winding paths using Worley-like approach
				int cellX = Math.floorDiv(x, cellSize);
				int cellZ = Math.floorDiv(z, cellSize);
				double localX = (x % cellSize + cellSize) % cellSize;
				double localZ = (z % cellSize + cellSize) % cellSize;

				// Get path direction for this cell from noise
				double cellNoise = noise.getValue(cellX * 50, 0, cellZ * 50);
				double cellNoise2 = noise.getValue(cellX * 50, 100, cellZ * 50);

				// Determine if this cell has a path and its direction
				boolean hasPath = cellNoise > -pathChance;

				if (hasPath) {
					// Path runs through the cell - calculate distance to path line
					double angle = cellNoise2 * Math.PI; // Random angle
					double pathCenterX = cellSize / 2.0 + cellNoise * cellSize * 0.3;
					double pathCenterZ = cellSize / 2.0 + cellNoise2 * cellSize * 0.3;

					// Distance from point to the path line through center
					double dx = localX - pathCenterX;
					double dz = localZ - pathCenterZ;

					// Perpendicular distance to path direction
					double cosA = Math.cos(angle);
					double sinA = Math.sin(angle);
					double distToPath = Math.abs(dx * sinA - dz * cosA);

					// Path width with some noise for roughness
					double edgeNoise = noise.getValue(x * 20, 50, z * 20) * 0.3;
					double effectiveWidth = pathWidth * cellSize * (0.15 + edgeNoise * 0.05);

					if (distToPath < effectiveWidth) {
						// On the path - use path block with some variation
						double pathVariation = noise.getValue(x * 10, 25, z * 10);
						return pathVariation > 0.3 ? pathAlt : path;
					}
				}

				// Not on path - use remaining blocks for base surface
				if (n > 2) {
					double value = (noise.getValue(x * 4, y * 4, z * 4) + 1.0) / 2.0;
					int index = 2 + (int) (value * (n - 2));
					if (index >= n) index = n - 1;
					return blocks.get(index);
				}
				return n > 2 ? blocks.get(2) : pathAlt;
			};
		};
	}

	private static final Supplier<PlanetSurface> craters(int scale, double rimWidth, double scatterChance) {
		return () -> (blocks) -> {
			final int n = blocks.size();
			final BlockState base = blocks.get(0);
			final int ringCount = Math.max(0, n - 1);
			return (x, y, z, noise) -> {
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
		};
	}

	//@formatter:off
	public static final Supplier<PlanetSurface> SIMPLE = SURFACES.register("simple", () -> (blocks) -> {
		final BlockState state = blocks.get(0);
		return (x, y, z, noise) -> state;
	});
	public static final Supplier<PlanetSurface> LARGE_PATCHES = SURFACES.register("large_patches", patches(1));
	public static final Supplier<PlanetSurface> MEDIUM_PATCHES = SURFACES.register("medium_patches", patches(4));
	public static final Supplier<PlanetSurface> SMALL_PATCHES = SURFACES.register("small_patches", patches(16));
	public static final Supplier<PlanetSurface> MEDIUM_EXP_DIST = SURFACES.register("medium_exp_dist", hybridBands(4, 0.467f, 0.6f));
	public static final Supplier<PlanetSurface> CRATERS_EXTRA = SURFACES.register("craters_extra", craters(2, 0.15, 0.05));
	public static final Supplier<PlanetSurface> CITY_PATHS = SURFACES.register("city_paths", cityPaths(20, 0.6, 1.2));
	public static final Supplier<PlanetSurface> CITY_PATHS_DENSE = SURFACES.register("city_paths_dense", cityPaths(16, 0.7, 1.5));
	//@formatter:on
}
