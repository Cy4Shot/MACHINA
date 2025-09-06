package com.machina.api.util.block;

import com.machina.registration.init.BlockStateProviderInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import org.jetbrains.annotations.NotNull;

public class WeightedStateProviderProvider extends BlockStateProvider {
    public static final Codec<WeightedStateProviderProvider> CODEC = SimpleWeightedRandomList
            .wrappedCodec(BlockStateProvider.CODEC)
            .comapFlatMap(WeightedStateProviderProvider::create, instance -> instance.weightedList).fieldOf("entries")
            .codec();
    private final SimpleWeightedRandomList<BlockStateProvider> weightedList;

    private static DataResult<WeightedStateProviderProvider> create(SimpleWeightedRandomList<BlockStateProvider> list) {
        return list.isEmpty() ? DataResult.error(() -> "WeightedStateProviderProvider with no states")
                : DataResult.success(new WeightedStateProviderProvider(list));
    }

    private WeightedStateProviderProvider(SimpleWeightedRandomList<BlockStateProvider> list) {
        this.weightedList = list;
    }

    private WeightedStateProviderProvider(SimpleWeightedRandomList.Builder<BlockStateProvider> builder) {
        this(builder.build());
    }

    protected @NotNull BlockStateProviderType<?> type() {
        return BlockStateProviderInit.WEIGHTED_STATE_PROVIDER_PROVIDER.get();
    }

    public @NotNull BlockState getState(@NotNull RandomSource rand, @NotNull BlockPos pos) {
        return this.weightedList.getRandomValue(rand).orElseThrow(IllegalStateException::new).getState(rand, pos);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final SimpleWeightedRandomList.Builder<BlockStateProvider> builder;

        private Builder() {
            this.builder = SimpleWeightedRandomList.builder();
        }

        public Builder add(BlockState state, int weight) {
            this.builder.add(BlockStateProvider.simple(state), weight);
            return this;
        }

        public Builder addHorizontal(BlockState state, int weight) {
            this.builder.add(new HorizontalFacingBlockProvider(state), weight);
            return this;
        }

        public Builder add(BlockStateProvider provider, int weight) {
            this.builder.add(provider, weight);
            return this;
        }

        public WeightedStateProviderProvider build() {
            return new WeightedStateProviderProvider(this.builder);
        }
    }
}