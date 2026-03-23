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

						// reuse original patch logic
						return raw < 0 ? a : b;
					}

					// --- BAND REGION (blocks 2+) ---
					double remaining = 1.0 - basePortion;
					double adjusted = (value - basePortion) / remaining;

					int bandCount = n - 2;

					// compute total weight
					double weight = 1.0;
					double totalWeight = 0.0;
					for (int i = 0; i < bandCount; i++) {
						totalWeight += weight;
						weight *= decay;
					}

					// pick band
					double cumulative = 0.0;
					weight = 1.0;

					for (int i = 0; i < bandCount; i++) {
						cumulative += weight / totalWeight;

						if (adjusted < cumulative || i == bandCount - 1) {
							return blocks.get(i + 2);
						}

						weight *= decay;
					}

					return blocks.get(0); // fallback
				};
			}
		};
	}

	//@formatter:off
	public static final Supplier<PlanetSurface> LARGE_PATCHES = SURFACES.register("large_patches", patches(1));
	public static final Supplier<PlanetSurface> MEDIUM_PATCHES = SURFACES.register("medium_patches", patches(4));
	public static final Supplier<PlanetSurface> SMALL_PATCHES = SURFACES.register("small_patches", patches(16));
	public static final Supplier<PlanetSurface> MEDIUM_EXP_DIST = SURFACES.register("medium_exp_dist", hybridBands(4, 0.467f, 0.6f));
	//@formatter:on
}
