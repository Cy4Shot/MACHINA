package com.machina.datagen.common;

import com.machina.Machina;
import com.machina.datagen.trait_pools.TraitPool;
import com.machina.datagen.trait_pools.TraitPoolEntry;
import com.machina.datagen.trait_pools.TraitPoolsGenProvider;
import com.machina.registration.init.TraitInit;

import net.minecraft.data.DataGenerator;

public class TraitPoolsProvider extends TraitPoolsGenProvider {

	public TraitPoolsProvider(DataGenerator generator) {
		super(generator, Machina.MOD_ID);
	}

	@Override
	protected void addPools() {

		// @formatter:off
		addPool("geographical_traits", new TraitPool(1, 1).withEntries(
				new TraitPoolEntry(1, TraitInit.NONE)));
		// @formatter:on
	}

}
