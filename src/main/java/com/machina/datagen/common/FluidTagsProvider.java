package com.machina.datagen.common;

import com.machina.Machina;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.TagInit;

import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.common.data.ExistingFileHelper;

public class FluidTagsProvider extends net.minecraft.data.FluidTagsProvider {

	public FluidTagsProvider(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, Machina.MOD_ID, helper);
	}

	@Override
	protected void addTags() {
		tag(TagInit.Fluids.NOT_ACTUALLY_WATER)
				.add(FluidInit.FLUIDS.getEntries().stream().map(ro -> ro.get()).toArray(Fluid[]::new));
	}
}