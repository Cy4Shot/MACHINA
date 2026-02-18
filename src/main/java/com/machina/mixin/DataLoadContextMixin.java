package com.machina.mixin;

import com.machina.Machina;
import com.machina.api.starchart.planet_biome.PlanetBiomeLoader;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings;
import com.machina.api.starchart.planet_type.PlanetTypeLoader;
import com.machina.world.biome.PlanetBiome;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.WorldLoader.DataLoadContext;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

@Mixin(DataLoadContext.class)
public abstract class DataLoadContextMixin {

    @Shadow
    protected abstract ResourceManager resources();

    // Highly dodgy code!
    @Inject(method = "Lnet/minecraft/server/WorldLoader$DataLoadContext;datapackWorldgen()Lnet/minecraft/core/RegistryAccess$Frozen;", at = @At("RETURN"), cancellable = true)
    private void init(CallbackInfoReturnable<RegistryAccess.Frozen> ci) {

        // Step 1: Load the planet biome settings
        ResourceManager man = resources();
        PlanetTypeLoader.INSTANCE.reload(man);
        PlanetBiomeLoader.INSTANCE.reload(man);

        // Step 2: Get the biome registry
        RegistryAccess.Frozen worldgen = ci.getReturnValue();
        Registry<Biome> biomes = worldgen.registryOrThrow(Registries.BIOME);

        // Step 3: Register the planet biomes if they are not
        for (Entry<ResourceLocation, PlanetBiomeSettings> e : PlanetBiomeLoader.INSTANCE.getEntrySet()) {
            ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, e.getKey());
            if (biomes.getOptional(key).isPresent()) {
                Machina.LOGGER.warn("Unable to register dimension {} -- already registered", e.getKey());
                continue;
            }
            if (biomes instanceof MappedRegistry<Biome> biomeReg) {
                biomeReg.unfreeze();
                biomeReg.register(key, new PlanetBiome(e.getValue()), RegistrationInfo.BUILT_IN);
            } else {
                throw new IllegalStateException(String
                        .format("Unable to register dimension %s -- dimension registry not writable", e.getKey()));
            }
        }

        // Step 4: Inject a custom registry access
        RegistryAccess custom = new RegistryAccess() {
            @SuppressWarnings("unchecked")
            @Override
            public <E> @NotNull Optional<Registry<E>> registry(ResourceKey<? extends Registry<? extends E>> key) {
                if (key.equals(Registries.BIOME))
                    return Optional.of((MappedRegistry<E>) biomes);
                return worldgen.registry(key);
            }

            @Override
            public @NotNull Stream<RegistryEntry<?>> registries() {
                return Stream.concat(worldgen.registries().filter(entry -> !entry.key().equals(Registries.BIOME)),
                        Stream.of(new RegistryEntry<>(Registries.BIOME, biomes)));
            }
        };
        ci.setReturnValue(custom.freeze());
    }
}
