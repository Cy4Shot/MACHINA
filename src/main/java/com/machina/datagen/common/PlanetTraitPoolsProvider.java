package com.machina.datagen.common;

import com.machina.Machina;
import com.machina.datagen.trait_pools.TraitPool;
import com.machina.datagen.trait_pools.TraitPoolEntry;
import com.machina.datagen.trait_pools.TraitPoolsGenProvider;
import com.machina.registration.init.TraitInit;

import net.minecraft.data.DataGenerator;

public class PlanetTraitPoolsProvider extends TraitPoolsGenProvider {

	public PlanetTraitPoolsProvider(DataGenerator generator) {
		super(generator, Machina.MOD_ID);
	}

	@Override
	protected void addPools() {

		// @formatter:off
		addPool("geographical_traits", new TraitPool(3, 3).withEntries(
				new TraitPoolEntry(25, TraitInit.MOUNTAINOUS, TraitInit.HILLY, TraitInit.FLAT),
				new TraitPoolEntry(5,  TraitInit.CONTINENTAL, TraitInit.WATER_WORLD),
				new TraitPoolEntry(5,  TraitInit.FROZEN),
				new TraitPoolEntry(5,  TraitInit.ISLANDS)));
		// @formatter:on
	}

}
