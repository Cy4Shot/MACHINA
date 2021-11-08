package com.cy4.machina.datagen.common;

import static com.cy4.machina.init.PlanetTraitInit.*;

import com.cy4.machina.Machina;
import com.cy4.machina.api.datagen.trait_pools.TraitPool;
import com.cy4.machina.api.datagen.trait_pools.TraitPoolEntry;
import com.cy4.machina.api.datagen.trait_pools.TraitPoolsGenProvider;
import com.cy4.machina.init.PlanetTraitInit;

import net.minecraft.data.DataGenerator;

public class PlanetTraitPoolsProvider extends TraitPoolsGenProvider {

	public PlanetTraitPoolsProvider(DataGenerator generator) {
		super(generator, Machina.MOD_ID);
	}

	@Override
	protected void addPools() {
		addPool("geographical_traits", new TraitPool(3, 5).withEntries(
				new TraitPoolEntry(15, PlanetTraitInit.CONTINENTALL, PlanetTraitInit.WATER_WORLD,
						PlanetTraitInit.LANDMMASS),
				new TraitPoolEntry(15, PlanetTraitInit.MOUNTAINOUS, PlanetTraitInit.HILLY, PlanetTraitInit.FLAT),
				new TraitPoolEntry(15, ORE_BARREN, ORE_RICH), new TraitPoolEntry(5, CANYONS, FIORDS, RAVINES, LAKES),
				new TraitPoolEntry(5, VOLCANIC, FROZEN), new TraitPoolEntry(2, LAYERED)));
	}

}
