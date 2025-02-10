package com.machina.registration.init;

import com.machina.Machina;
import com.machina.api.util.block.HorizontalFacingBlockProvider;
import com.machina.api.util.block.WeightedStateProviderProvider;
import com.mojang.serialization.Codec;

import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockStateProviderInit {
	public static final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDERS = DeferredRegister
			.create(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, Machina.MOD_ID);

	public static final RegistryObject<BlockStateProviderType<WeightedStateProviderProvider>> WEIGHTED_STATE_PROVIDER_PROVIDER = register(
			"weighted_state_provider_provider", WeightedStateProviderProvider.CODEC);

	public static final RegistryObject<BlockStateProviderType<HorizontalFacingBlockProvider>> HORIZONTAL_FACING_BLOCK_PROVIDER = register(
			"horizontal_facing_block_provider", HorizontalFacingBlockProvider.CODEC);

	private static <P extends BlockStateProvider> RegistryObject<BlockStateProviderType<P>> register(String name,
			Codec<P> codec) {
		return BLOCK_STATE_PROVIDERS.register(name, () -> new BlockStateProviderType<>(codec));
	}
}
