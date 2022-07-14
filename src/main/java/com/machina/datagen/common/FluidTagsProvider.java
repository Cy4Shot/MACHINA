package com.machina.datagen.common;

import com.machina.Machina;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.TagInit;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class FluidTagsProvider extends net.minecraft.data.FluidTagsProvider {

	public FluidTagsProvider(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, Machina.MOD_ID, helper);
	}

	@Override
	protected void addTags() {
		tag(TagInit.Fluids.NOT_ACTUALLY_WATER).add(FluidInit.OXYGEN.get(), FluidInit.OXYGEN_FLOWING.get(),
				FluidInit.HYDROGEN.get(), FluidInit.HYDROGEN_FLOWING.get(), FluidInit.LIQUID_HYDROGEN.get(),
				FluidInit.LIQUID_HYDROGEN_FLOWING.get());
	}
}