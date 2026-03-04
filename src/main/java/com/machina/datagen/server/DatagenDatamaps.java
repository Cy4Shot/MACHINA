package com.machina.datagen.server;

import java.util.concurrent.CompletableFuture;

import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FruitInit;
import com.machina.registration.init.FruitInit.Fruit;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public class DatagenDatamaps extends DataMapProvider {

	public DatagenDatamaps(PackOutput po, CompletableFuture<Provider> lookup) {
		super(po, lookup);
	}

	@Override
	protected void gather(HolderLookup.Provider provider) {
		DataMapProvider.Builder<Compostable, Item> builder = builder(NeoForgeDataMaps.COMPOSTABLES);
		compost(builder, provider, BlockInit.PURPLE_GLOWSHROOM, 0.65f);
		compost(builder, provider, BlockInit.PINK_GLOWSHROOM, 0.65f);
		compost(builder, provider, BlockInit.RED_GLOWSHROOM, 0.65f);
		compost(builder, provider, BlockInit.ORANGE_GLOWSHROOM, 0.65f);
		compost(builder, provider, BlockInit.YELLOW_GLOWSHROOM, 0.65f);
		compost(builder, provider, BlockInit.GREEN_GLOWSHROOM, 0.65f);
		compost(builder, provider, BlockInit.TURQUOISE_GLOWSHROOM, 0.65f);
		compost(builder, provider, BlockInit.BLUE_GLOWSHROOM, 0.65f);

		compost(builder, provider, BlockInit.SPRUCE_CUP, 0.65f);
		compost(builder, provider, BlockInit.DRAGON_PEONY, 0.65f);
		compost(builder, provider, BlockInit.SPRUCE_CUP, 0.65f);
		compost(builder, provider, BlockInit.ORPHEUM, 1.0f);

		compost(builder, provider, BlockInit.SPINDLESPROUT, 0.3f);
		compost(builder, provider, BlockInit.SMALL_FERN, 0.3f);
		compost(builder, provider, BlockInit.NEEDLEGRASS, 0.3f);
		compost(builder, provider, BlockInit.SPINDLEGRASS, 0.3f);
		compost(builder, provider, BlockInit.NEEDLETHATCH, 0.3f);
		compost(builder, provider, BlockInit.CLOVER, 0.3f);

		compost(builder, provider, BlockInit.TROPICAL_GRASS, 0.3f);
		compost(builder, provider, BlockInit.TWISTED_GRASS, 0.3f);
		compost(builder, provider, BlockInit.CONIFEROUS_GRASS, 0.3f);
		compost(builder, provider, BlockInit.SHORT_CONIFEROUS_GRASS, 0.3f);
		compost(builder, provider, BlockInit.WINDSWEPT_GRASS, 0.3f);
		compost(builder, provider, BlockInit.MYCELIAL_GRASS, 0.3f);

		compost(builder, provider, BlockInit.PURPLE_PETALS, 0.3f);
		compost(builder, provider, BlockInit.RED_PETALS, 0.3f);
		compost(builder, provider, BlockInit.ORANGE_PETALS, 0.3f);
		compost(builder, provider, BlockInit.YELLOW_PETALS, 0.3f);
		compost(builder, provider, BlockInit.GREEN_PETALS, 0.3f);
		compost(builder, provider, BlockInit.TURQUOISE_PETALS, 0.3f);
		compost(builder, provider, BlockInit.BLUE_PETALS, 0.3f);

		compost(builder, provider, BlockInit.PURPLE_GROUNDLILY, 0.3f);
		compost(builder, provider, BlockInit.PINK_GROUNDLILY, 0.3f);
		compost(builder, provider, BlockInit.RED_GROUNDLILY, 0.3f);
		compost(builder, provider, BlockInit.ORANGE_GROUNDLILY, 0.3f);
		compost(builder, provider, BlockInit.YELLOW_GROUNDLILY, 0.3f);
		compost(builder, provider, BlockInit.GREEN_GROUNDLILY, 0.3f);
		compost(builder, provider, BlockInit.TURQUOISE_GROUNDLILY, 0.3f);
		compost(builder, provider, BlockInit.BLUE_GROUNDLILY, 0.3f);

		compost(builder, provider, BlockInit.PURPLE_WATERLILY, 0.3f);
		compost(builder, provider, BlockInit.PINK_WATERLILY, 0.3f);
		compost(builder, provider, BlockInit.RED_WATERLILY, 0.3f);
		compost(builder, provider, BlockInit.ORANGE_WATERLILY, 0.3f);
		compost(builder, provider, BlockInit.YELLOW_WATERLILY, 0.3f);
		compost(builder, provider, BlockInit.GREEN_WATERLILY, 0.3f);
		compost(builder, provider, BlockInit.TURQUOISE_WATERLILY, 0.3f);
		compost(builder, provider, BlockInit.BLUE_WATERLILY, 0.3f);

		for (Fruit fruit : FruitInit.FRUITS) {
			compost(builder, fruit.item(), 1.0f);
		}
	}

	private void compost(DataMapProvider.Builder<Compostable, Item> builder, DeferredHolder<Item, ?> item,
			float chance) {
		builder.add(item.getDelegate(), new Compostable(chance), false);
	}

	private void compost(DataMapProvider.Builder<Compostable, Item> builder, HolderLookup.Provider provider,
			DeferredHolder<Block, ?> item, float chance) {
		builder.add(mapBlockHolderToItemHolder(item.getDelegate(), provider), new Compostable(chance), false);
	}

	public Holder<Item> mapBlockHolderToItemHolder(Holder<Block> blockHolder, HolderLookup.Provider provider) {
		return blockHolder.unwrapKey().flatMap(blockKey -> {
			ResourceLocation id = blockKey.location();
			ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
			HolderLookup.RegistryLookup<Item> itemRegistry = provider.lookupOrThrow(Registries.ITEM);
			return itemRegistry.get(itemKey);
		}).orElseThrow();
	}
}
