package com.machina.registration.init;

import java.util.List;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.starchart.planet_biome.PlanetSurface;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PlanetSurfaceInit {
    public static final DeferredRegister<PlanetSurface> SURFACES = DeferredRegister
            .create(RegistryInit.SURFACE_REGISTRY, Machina.MOD_ID);

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
    public static final Supplier<PlanetSurface> LARGE_PATCHES = SURFACES.register("large_patches", patches(1));
    public static final Supplier<PlanetSurface> MEDIUM_PATCHES = SURFACES.register("medium_patches", patches(4));
    public static final Supplier<PlanetSurface> SMALL_PATCHES = SURFACES.register("small_patches", patches(16));
}
