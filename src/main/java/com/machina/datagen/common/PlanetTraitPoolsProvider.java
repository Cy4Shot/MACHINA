package com.machina.datagen.common;

import com.machina.Machina;
import com.machina.datagen.trait_pools.TraitPool;
import com.machina.datagen.trait_pools.TraitPoolEntry;
import com.machina.datagen.trait_pools.TraitPoolsGenProvider;
import com.machina.registration.init.PlanetTraitInit;

import net.minecraft.data.DataGenerator;

public class PlanetTraitPoolsProvider extends TraitPoolsGenProvider {

	public PlanetTraitPoolsProvider(DataGenerator generator) {
		super(generator, Machina.MOD_ID);
	}

	@Override
	protected void addPools() {

		// @formatter:off
		addPool("geographical_traits", new TraitPool(3, 3).withEntries(
				new TraitPoolEntry(25, PlanetTraitInit.MOUNTAINOUS, PlanetTraitInit.HILLY, PlanetTraitInit.FLAT),
				new TraitPoolEntry(5,  PlanetTraitInit.CONTINENTAL, PlanetTraitInit.WATER_WORLD),
				new TraitPoolEntry(5,  PlanetTraitInit.FROZEN),
				new TraitPoolEntry(5,  PlanetTraitInit.ISLANDS)));
		// @formatter:on
	}

}
