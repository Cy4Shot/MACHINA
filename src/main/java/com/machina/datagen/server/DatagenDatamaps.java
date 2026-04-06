package com.machina.datagen.server;

import java.util.concurrent.CompletableFuture;

import com.machina.registration.init.BlockInit;
import com.machina.registration.init.DataMapsInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
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
import net.minecraft.world.level.material.Fluid;
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
		DataMapProvider.Builder<Compostable, Item> compost = builder(NeoForgeDataMaps.COMPOSTABLES);
		compost(compost, provider, BlockInit.PURPLE_GLOWSHROOM, 0.65f);
		compost(compost, provider, BlockInit.PINK_GLOWSHROOM, 0.65f);
		compost(compost, provider, BlockInit.RED_GLOWSHROOM, 0.65f);
		compost(compost, provider, BlockInit.ORANGE_GLOWSHROOM, 0.65f);
		compost(compost, provider, BlockInit.YELLOW_GLOWSHROOM, 0.65f);
		compost(compost, provider, BlockInit.GREEN_GLOWSHROOM, 0.65f);
		compost(compost, provider, BlockInit.TURQUOISE_GLOWSHROOM, 0.65f);
		compost(compost, provider, BlockInit.BLUE_GLOWSHROOM, 0.65f);

		compost(compost, provider, BlockInit.SPRUCE_CUP, 0.65f);
		compost(compost, provider, BlockInit.DRAGON_PEONY, 0.65f);
		compost(compost, provider, BlockInit.SPRUCE_CUP, 0.65f);
		compost(compost, provider, BlockInit.ORPHEUM, 1.0f);

		compost(compost, provider, BlockInit.SPINDLESPROUT, 0.3f);
		compost(compost, provider, BlockInit.SMALL_FERN, 0.3f);
		compost(compost, provider, BlockInit.NEEDLEGRASS, 0.3f);
		compost(compost, provider, BlockInit.SPINDLEGRASS, 0.3f);
		compost(compost, provider, BlockInit.NEEDLETHATCH, 0.3f);
		compost(compost, provider, BlockInit.CLOVER, 0.3f);

		compost(compost, provider, BlockInit.TROPICAL_GRASS, 0.3f);
		compost(compost, provider, BlockInit.TWISTED_GRASS, 0.3f);
		compost(compost, provider, BlockInit.CONIFEROUS_GRASS, 0.3f);
		compost(compost, provider, BlockInit.SHORT_CONIFEROUS_GRASS, 0.3f);
		compost(compost, provider, BlockInit.WINDSWEPT_GRASS, 0.3f);
		compost(compost, provider, BlockInit.MYCELIAL_GRASS, 0.3f);
		compost(compost, provider, BlockInit.FERROUS_GRASS, 0.3f);
		compost(compost, provider, BlockInit.MOONGRASS, 0.3f);

		compost(compost, provider, BlockInit.PURPLE_PETALS, 0.3f);
		compost(compost, provider, BlockInit.RED_PETALS, 0.3f);
		compost(compost, provider, BlockInit.ORANGE_PETALS, 0.3f);
		compost(compost, provider, BlockInit.YELLOW_PETALS, 0.3f);
		compost(compost, provider, BlockInit.GREEN_PETALS, 0.3f);
		compost(compost, provider, BlockInit.TURQUOISE_PETALS, 0.3f);
		compost(compost, provider, BlockInit.BLUE_PETALS, 0.3f);

		compost(compost, provider, BlockInit.PURPLE_GROUNDLILY, 0.3f);
		compost(compost, provider, BlockInit.PINK_GROUNDLILY, 0.3f);
		compost(compost, provider, BlockInit.RED_GROUNDLILY, 0.3f);
		compost(compost, provider, BlockInit.ORANGE_GROUNDLILY, 0.3f);
		compost(compost, provider, BlockInit.YELLOW_GROUNDLILY, 0.3f);
		compost(compost, provider, BlockInit.GREEN_GROUNDLILY, 0.3f);
		compost(compost, provider, BlockInit.TURQUOISE_GROUNDLILY, 0.3f);
		compost(compost, provider, BlockInit.BLUE_GROUNDLILY, 0.3f);

		compost(compost, provider, BlockInit.PURPLE_WATERLILY, 0.3f);
		compost(compost, provider, BlockInit.PINK_WATERLILY, 0.3f);
		compost(compost, provider, BlockInit.RED_WATERLILY, 0.3f);
		compost(compost, provider, BlockInit.ORANGE_WATERLILY, 0.3f);
		compost(compost, provider, BlockInit.YELLOW_WATERLILY, 0.3f);
		compost(compost, provider, BlockInit.GREEN_WATERLILY, 0.3f);
		compost(compost, provider, BlockInit.TURQUOISE_WATERLILY, 0.3f);
		compost(compost, provider, BlockInit.BLUE_WATERLILY, 0.3f);

		for (Fruit fruit : FruitInit.FRUITS) {
			compost(compost, fruit.item(), 1.0f);
		}

		DataMapProvider.Builder<Integer, Fluid> chemburn = builder(DataMapsInit.CHEMICAL_BURNABLE);
		chemburn(chemburn, FluidInit.HYDROGEN, 100);
		chemburn(chemburn, FluidInit.METHANE, 98);
		chemburn(chemburn, FluidInit.ETHANE, 95);
		chemburn(chemburn, FluidInit.ETHYLENE, 90);
		chemburn(chemburn, FluidInit.AMMONIA, 80);
		chemburn(chemburn, FluidInit.CARBON_MONOXIDE, 70);
		chemburn(chemburn, FluidInit.FORMALDEHYDE, 60);
		chemburn(chemburn, FluidInit.METHANOL, 60);
		chemburn(chemburn, FluidInit.ETHANOL, 67);
		chemburn(chemburn, FluidInit.TOLUENE, 70);
		chemburn(chemburn, FluidInit.BENZENE, 70);
		chemburn(chemburn, FluidInit.NITROMETHANE, 60);
		chemburn(chemburn, FluidInit.ACETALDEHYDE, 50);
		chemburn(chemburn, FluidInit.BENZYLAMINE, 50);
		chemburn(chemburn, FluidInit.ACETIC_ACID, 40);
		chemburn(chemburn, FluidInit.BENZYL_CHLORIDE, 30);
	}

	@SuppressWarnings("deprecation")
	private void chemburn(DataMapProvider.Builder<Integer, Fluid> builder, FluidObject fluid, int burnRate) {
		builder.add(fluid.fluid().builtInRegistryHolder(), burnRate, false);
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
