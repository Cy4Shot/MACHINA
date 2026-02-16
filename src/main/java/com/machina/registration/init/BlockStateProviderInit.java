package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.util.block.HorizontalFacingBlockProvider;
import com.machina.api.util.block.WeightedStateProviderProvider;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockStateProviderInit {
    public static final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDERS = DeferredRegister
            .create(Registries.BLOCK_STATE_PROVIDER_TYPE, Machina.MOD_ID);

    public static final Supplier<BlockStateProviderType<WeightedStateProviderProvider>> WEIGHTED_STATE_PROVIDER_PROVIDER = register(
            "weighted_state_provider_provider", WeightedStateProviderProvider.CODEC);

    public static final Supplier<BlockStateProviderType<HorizontalFacingBlockProvider>> HORIZONTAL_FACING_BLOCK_PROVIDER = register(
            "horizontal_facing_block_provider", HorizontalFacingBlockProvider.CODEC);

    private static <P extends BlockStateProvider> Supplier<BlockStateProviderType<P>> register(String name,
            MapCodec<P> codec) {
        return BLOCK_STATE_PROVIDERS.register(name, () -> new BlockStateProviderType<>(codec));
    }
}
